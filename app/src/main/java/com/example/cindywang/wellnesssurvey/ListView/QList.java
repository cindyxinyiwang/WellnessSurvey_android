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

import java.util.ArrayList;
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
/*
                String expire = "";
                Date end = (Date) typeEntry.get("endDate");
                Date now = new Date();
                long hour = (end.getTime() - now.getTime())/ (1000*3600);
                if (hour < 1){
                    long minute = (end.getTime() - now.getTime())/ (1000*60);
                    expire = "Expire in " + minute + "minutes";
                } else {
                    expire = "Expire in " + hour + "hours";
                } */
                questionListItem item = new questionListItem();
                item.setqType(type);
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
                    // Send single item click data to SingleItemView Class
                    Intent i;

                    String type = typeListItems.get(position).getqType();
                    //objects that need to be retrieved from Parse
                    String prompt = null;
                    /*
                    String question = null;
                    List<String> config = new ArrayList<String>();
                    String[] configArray = null;
                    String questionId = null;*/

                    for (ParseObject tEntry : typeEntries) {
                        if (tEntry.get("typeName").equals(type)) {
                            prompt = tEntry.getString("prompt");
                            /*
                            question = tEntry.getString("Question");
                            answerType = tEntry.getString("answerType");
                            //put arrays in config
                            config = (List<String>) tEntry.get("config");
                            configArray = new String[config.size()];
                            config.toArray(configArray);

                            questionId = tEntry.getObjectId();*/

                            break;
                        }
                    }

                    Bundle bundle = new Bundle();
                    bundle.putString("typeName", type);
                    bundle.putString("prompt", prompt);
                    /*
                    bundle.putStringArray("config", configArray);
                    bundle.putInt("questionIndex", position);
                    //start activity based on answerType
                    if (answerType.equals("rate")) {
                        i = new Intent(QList.this, slider_answer.class);
                    } else if (answerType.equals("singleSelect")) {
                        i = new Intent(QList.this, singleSelect_answer.class);
                    } else if (answerType.equals("multiSelect")){
                        i = new Intent(QList.this, multiSelect_answer.class);
                    } else {
                        i = new Intent(QList.this,
                                SingleItemView.class);
                    }
                    // Pass data "name" followed by the position
                    //serialize question entry, a little bit worse performance
                    i.putExtras(bundle); */

                    i = new Intent(QList.this, answerList.class);
                    i.putExtras(bundle);
                    // Open SingleItemView.java Activity
                    startActivity(i);
                }
            });

        }


    }

}