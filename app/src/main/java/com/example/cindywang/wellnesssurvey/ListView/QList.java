package com.example.cindywang.wellnesssurvey.ListView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.cindywang.wellnesssurvey.R;
import com.example.cindywang.wellnesssurvey.answerList.answerList;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class QList extends Activity {
    // Declare Variables
    List<ParseObject> typeEntries;

    ProgressDialog mProgressDialog;

    ListView listView;
    ArrayList<questionListItem> typeListItems;
    //check if we need to delete the index of the question
    String deleteId = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from listview_main.xml
        setContentView(R.layout.activity_qlist);
        Parse.initialize(this, "7y3hW9Gibahq2gCpaTd5TfTH3xlrSao2PCleXr9E", "qVUTlfmiQatz6EvU1gqUBsDqzgO3ajcAGhbXpj4Q");
        //get deleteIndex
        Intent i = getIntent();
        deleteId = i.getStringExtra("deleteIndex");
        // Execute RemoteDataTask AsyncTask
        new RemoteDataTask().execute();
    }

    // RemoteDataTask AsyncTask
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(QList.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Retrieving data");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            // Locate the class table named Type
            typeEntries = new ArrayList<ParseObject>();
            ParseQuery<ParseObject> questQuery = new ParseQuery<ParseObject>("Type");
            try {
                typeEntries = questQuery.find();
            } catch (ParseException e){
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            //query the currentAnswer
            ParseQuery<ParseObject> answerInProgress = ParseQuery.getQuery("AnswerInProgress");
            answerInProgress.whereEqualTo("user", ParseUser.getCurrentUser());
            // Put data into child list
            typeListItems = new ArrayList<questionListItem>();
            // set counter for questionIndex
            //TODO: pos is the index of the question in already modified list

            for (ParseObject typeEntry : typeEntries) {
                answerInProgress.whereEqualTo("questionId", typeEntry.getObjectId());
                try {
                    if (answerInProgress.count() > 0)
                        //TODO: Make entry unclickable
                        ;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String type = (String) typeEntry.get("typeName");
                String prompt = (String) typeEntry.get("prompt");
                //get jsonarray and convert it to list
                List<String> time = new ArrayList<String>();
                JSONArray jsonArray = typeEntry.getJSONArray("issueTime");
                if (jsonArray!= null){
                    int len = jsonArray.length();
                    for (int i=0; i<len; i++){
                        try {
                            time.add(jsonArray.get(i).toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                
                questionListItem item = new questionListItem(type, prompt, time);
                //check if current time is within issue time range
                //if yes, set index of current time in the time list
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);
                int timeIndexCounter = 0;
                if (time.size() == 0){
                    //entry stays open all day
                    item.setClickable(true);

                } else {
                    for (String tString : time) {
                        try {
                            if (submitted (timeIndexCounter, typeEntry)){
                                //if question issued in this time period has already been submitted, do nothing

                            } else {
                                String[] hourMinute = tString.split(":");
                                int timeDiff = (mHour * 60 + mMinute) - (Integer.parseInt(hourMinute[0]) * 60 + Integer.parseInt(hourMinute[1]));
                                if (timeDiff >= 0 && timeDiff <= 90) {
                                    item.setClickable(true);
                                    item.setTimeIndex(timeIndexCounter);
                                    break;
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        timeIndexCounter++;
                    }
                }
                //add item to list
                typeListItems.add(item);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Locate the listview in listview_main.xml
            listView = (ListView) findViewById(R.id.ListView);

            // Close the progressdialog
            mProgressDialog.dismiss();


            // Binds the Adapter to the ListView
            listView.setAdapter(new listViewBaseAdapter(QList.this, typeListItems));
            // Capture button clicks on ListView items
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    questionListItem currentItem = typeListItems.get(position);
                    //only clickable if the current item is within issue time
                    if (currentItem.getClickable()) {
                        // Send single item click data to SingleItemView Class
                        Intent i;

                        String type = currentItem.getqType();
                        String prompt = currentItem.getqPrompt();
                        int issueTimeIndex = currentItem.getTimeIndex();

                        Bundle bundle = new Bundle();
                        bundle.putString("typeName", type);
                        bundle.putString("prompt", prompt);
                        bundle.putInt("issueTimeIndex", issueTimeIndex);

                        i = new Intent(QList.this, answerList.class);
                        i.putExtras(bundle);
                        // Open SingleItemView.java Activity
                        startActivity(i);
                    }
                }
            });

        }

        //check if the current user has submitted answers to the question of a particular type
        boolean submitted(int timeIndex, ParseObject type) throws ParseException {
            ParseQuery<ParseObject> currentAnswerQuery = ParseQuery.getQuery("answeredToday");
            currentAnswerQuery.whereEqualTo("type", type);
            currentAnswerQuery.whereEqualTo("user", ParseUser.getCurrentUser());
            currentAnswerQuery.whereEqualTo("issueIndex", timeIndex);

            int entryCount = 0;

            entryCount = currentAnswerQuery.count();
            if (entryCount > 0) return true;
            else return false;
        }


    }

}