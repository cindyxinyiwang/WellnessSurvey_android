package com.example.cindywang.wellnesssurvey.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
import com.example.cindywang.wellnesssurvey.SingleItemView;
import com.example.cindywang.wellnesssurvey.multiSelect_answer;
import com.example.cindywang.wellnesssurvey.singleSelect_answer;
import com.example.cindywang.wellnesssurvey.slider_answer;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class QList extends Activity {
    // Declare Variables
    List<ParseObject> questionEntries;

    ProgressDialog mProgressDialog;

    ListView listView;
    ArrayList<questionListItem> questionListItems;
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

            // Locate the class table named SurveyQuestion
            questionEntries = new ArrayList<ParseObject>();
            ParseQuery<ParseObject> questQuery = new ParseQuery<ParseObject>("SurveyQuestion");
            try {
                questionEntries = questQuery.find();
            } catch (ParseException e){
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            //query the currentAnswer
            ParseQuery<ParseObject> answerInProgress = ParseQuery.getQuery("AnswerInProgress");
            answerInProgress.whereEqualTo("user", ParseUser.getCurrentUser());
            // Put data into child list
            questionListItems = new ArrayList<questionListItem>();
            // set counter for questionIndex
            //TODO: pos is the index of the question in already modified list

            for (ParseObject quesEntry : questionEntries) {
                if (quesEntry.getObjectId().equals(deleteId))
                    continue;
                answerInProgress.whereEqualTo("questionId",quesEntry.getObjectId());
                try {
                    if (answerInProgress.count() > 0)
                        continue;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String type = (String) quesEntry.get("type");
                String aspect = (String) quesEntry.get("aspect");

                String expire = "";
                Date end = (Date) quesEntry.get("endDate");
                Date now = new Date();
                long hour = (end.getTime() - now.getTime())/ (1000*3600);
                if (hour < 1){
                    long minute = (end.getTime() - now.getTime())/ (1000*60);
                    expire = "Expire in " + minute + "minutes";
                } else {
                    expire = "Expire in " + hour + "hours";
                }
                questionListItem item = new questionListItem();
                item.setqAspect(aspect);
                item.setqCategory(type);
                item.setqExpire(expire);
                questionListItems.add(item);
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
            listView.setAdapter(new listViewBaseAdapter(QList.this, questionListItems));
            // Capture button clicks on ListView items
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    // Send single item click data to SingleItemView Class
                    Intent i;
                    String aspect = questionListItems.get(position).getqAspect();
                    //objects that need to be retrieved from Parse
                    String answerType = null;
                    String question = null;
                    List<String> config = new ArrayList<String>();
                    String[] configArray = null;
                    String questionId = null;

                    for (ParseObject qEntry : questionEntries) {
                        if (qEntry.get("aspect").equals(aspect)) {
                            question = qEntry.getString("Question");
                            answerType = qEntry.getString("answerType");
                            //put arrays in config
                            config = (List<String>) qEntry.get("config");
                            configArray = new String[config.size()];
                            config.toArray(configArray);

                            questionId = qEntry.getObjectId();

                            break;
                        }
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("Question", question);
                    bundle.putString("questionId", questionId);
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
                    i.putExtras(bundle);
                    // Open SingleItemView.java Activity
                    startActivity(i);
                }
            });

        }


    }

}