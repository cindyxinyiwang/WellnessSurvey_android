package com.example.cindywang.wellnesssurvey;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class QList extends Activity {
    // Declare Variables
    ExpandableListView expandlistview;
    List<ParseObject> category;
    List<ParseObject> questionEntries;
    List<String> listHeader;
    HashMap<String, List<String>> listChild;
    ProgressDialog mProgressDialog;
    ExpandQListAdapter expandAdapter;

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
            // Locate the class table named "Category" in Parse.com
            ParseQuery<ParseObject> cateQuery = new ParseQuery<ParseObject>(
                    "Category");
            cateQuery.orderByDescending("createdAt");
            category = new ArrayList<ParseObject>();
            try {
                category = cateQuery.find();
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            // Put data into header list
            listHeader = new ArrayList<String>();
            for (ParseObject cate : category) {
                listHeader.add((String) cate.get("type"));
            }

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
            listChild = new HashMap<String, List<String>>();
            for (ParseObject quesEntry : questionEntries) {
                String type = (String) quesEntry.get("type");
                String aspect = (String) quesEntry.get("aspect");
                if (listChild.containsKey(type)) {
                    listChild.get(type).add(aspect);
                } else {
                    List<String> newValues = new ArrayList<String>();
                    newValues.add(aspect);
                    listChild.put(type, newValues);
                }
            }

            /*
            listHeader = new ArrayList<String>();
            listHeader.add("Header1");
            List<String> vals = new ArrayList<String>();
            vals.add("child");
            listChild = new HashMap<String, List<String>>();
            listChild.put("Header1", vals);
            */
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Locate the listview in listview_main.xml
            expandlistview = (ExpandableListView) findViewById(R.id.expandableListView);

            // Close the progressdialog
            mProgressDialog.dismiss();
            // Pass the results into an ArrayAdapter
            expandAdapter = new ExpandQListAdapter(QList.this,
                    listHeader, listChild);

            // Binds the Adapter to the ListView
            expandlistview.setAdapter(expandAdapter);
            // Capture button clicks on ListView items
            expandlistview.setOnChildClickListener(myListItemClicked);

        }
        private ExpandableListView.OnChildClickListener myListItemClicked = new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
                // Send single item click data to SingleItemView Class
                Intent i = new Intent(QList.this,
                        SingleItemView.class);
                String aspect = (listChild.get(listHeader.get(groupPosition))).get(childPosition);
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
                return false;
            }
        };

    }

}