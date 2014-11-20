package com.example.cindywang.wellnesssurvey.ListView;

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
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class QList extends Activity {
    // Declare Variables
    List<ParseObject> questionEntries;
    List<String> listHeader;

    ProgressDialog mProgressDialog;

    ListView listView;
    ArrayList<questionListItem> questionListItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from listview_main.xml
        setContentView(R.layout.activity_qlist);
        Parse.initialize(this, "7y3hW9Gibahq2gCpaTd5TfTH3xlrSao2PCleXr9E", "qVUTlfmiQatz6EvU1gqUBsDqzgO3ajcAGhbXpj4Q");
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
            // Put data into child list
            questionListItems = new ArrayList<questionListItem>();
            for (ParseObject quesEntry : questionEntries) {
                String type = (String) quesEntry.get("type");
                String aspect = (String) quesEntry.get("aspect");
                Date end = (Date) quesEntry.get("endDate");

                questionListItem item = new questionListItem();
                item.setqAspect(aspect);
                item.setqCategory(type);
                //item.setqExpire();
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
                    Intent i = new Intent(QList.this,
                            SingleItemView.class);
                    String aspect = questionListItems.get(position).getqAspect();
                    String question = null;
                    for (ParseObject qEntry:questionEntries) {
                        if (qEntry.get("aspect").equals(aspect)){
                            question = (String) qEntry.get("Question");
                            break;
                        }
                    }
                    // Pass data "name" followed by the position
                    i.putExtra("question", question);
                    // Open SingleItemView.java Activity
                    startActivity(i);
                }
            });

        }


    }

}