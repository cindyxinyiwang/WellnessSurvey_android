package com.example.cindywang.wellnesssurvey.answerList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cindywang.wellnesssurvey.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class answerList extends Activity {
    List<ParseObject> questionEntries;

    TextView prompt;
    ListView questionListView;

    ArrayList<answerListItem> answerListItems;
    String promptStr;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_llist);
        //get data from previous activity
        Intent i = getIntent();
        Bundle data = i.getExtras();
        promptStr = data.getString("prompt");
        type = data.getString("typeName");
        //display question prompt
        prompt = (TextView) findViewById(R.id.prompt);
        prompt.setText(promptStr);

        new GetRemoteData().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_answer_llist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //async data task
    private class GetRemoteData extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //query SurveyQuestion and populate answerListItems
            //query the survey question for this particular type
            questionEntries = new ArrayList<ParseObject>();
            answerListItems = new ArrayList<answerListItem>();
            ParseQuery<ParseObject> questQuery = new ParseQuery<ParseObject>("SurveyQuestion");
            questQuery.whereEqualTo("type", type);
            try {
                questionEntries = questQuery.find();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            for (ParseObject obj : questionEntries) {
                String question, answerType;
                question = obj.getString("Question");
                answerType = obj.getString("answerType");
                answerListItem item = new answerListItem(question, answerType);
                //get jsonarray and convert it to list
                List<String> list = new ArrayList<String>();
                JSONArray jsonArray = obj.getJSONArray("config");
                if (jsonArray != null){
                    int len = jsonArray.length();
                    for (int i=0; i<len; i++){
                        try {
                            list.add(jsonArray.get(i).toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                item.setConfig(list);
                answerListItems.add(item);
            }
         return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            questionListView = (ListView)findViewById(R.id.answerListView);
            questionListView.setAdapter(new answerListViewAdapter(answerList.this, answerListItems));
            super.onPostExecute(aVoid);
        }
    }

    //method for button to submit the answer
    public void submitAnswer(View view){
            for (answerListItem item:answerListItems) {
                if (item.getAnswerNum()>=0)
                    Log.println(4, "VALUETEST",item.getAnswerNum()+"");
                else if (item.getAnswer()!=null)
                    Log.println(4, "VALUETEST",item.getAnswer());
            }
    }
}
