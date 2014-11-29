package com.example.cindywang.wellnesssurvey;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cindywang.wellnesssurvey.ListView.QList;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.ArrayList;


public class multiSelect_answer extends Activity {

    TextView questionView;
    ListView listView;
    Bundle question;
    JSONArray answer;

    //values associated with question
    String questionId = "";
    int questionIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_select_answer);

        // Retrieve data from MainActivity on item click event
        Intent i = getIntent();

        //define variables
        String questionText = "";
        String[] config = null;

        // Get the name
        question = i.getExtras();
        questionText = question.getString("Question");
        config = question.getStringArray("config");
        questionId = question.getString("questionId");
        questionIndex = question.getInt("questionIndex");

        //set question view
        questionView = (TextView) findViewById(R.id.question);
        questionView.setText(questionText);

        //set list view
        listView = (ListView) findViewById(R.id.multiSelectList);
        //set the adapter for single selection
        listView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice,
                android.R.id.text1, config));

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.multi_select_answer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void submitAnswer(View view) {
        SparseBooleanArray checked = listView.getCheckedItemPositions();
        answer = new JSONArray();

        for (int i = 0; i < checked.size(); i++){
            if (checked.valueAt(i) == true) {
                answer.put((String) listView.getItemAtPosition(checked.keyAt(i)));
            }
        }

        if (answer.length() < 1) {
            //no answer is selected

            return;
        }

        openAlert(view);


    }

    private void openAlert(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(multiSelect_answer.this);
        alertDialogBuilder.setTitle(this.getTitle()+ " decision");
        alertDialogBuilder.setMessage("Are you sure?");
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {


                //query the question in quesntionEntries
                ParseQuery<ParseObject> questionQuery = ParseQuery.getQuery("SurveyQuestion");
                questionQuery.getInBackground(questionId,new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {
                        if (e == null){
                            //object is retrieved
                            // save answer and go back


                            ParseObject answerInProgress = new ParseObject("AnswerInProgress");
                            answerInProgress.put("questionId",questionId);
                            answerInProgress.put("user", ParseUser.getCurrentUser());
                            answerInProgress.put("answerArray",answer);
                            answerInProgress.put("question",parseObject);
                            answerInProgress.saveInBackground();

                        } else {
                            System.out.println("Error retrieving question entry!");
                        }
                    }
                });

                Intent positveActivity = new Intent(getApplicationContext(),
                        QList.class);
                positveActivity.putExtra("deleteIndex",questionId);
                startActivity(positveActivity);
            }
        });
        // set negative button: No message
        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                // cancel the alert box and put a Toast to the user
                dialog.cancel();
                Toast.makeText(getApplicationContext(), "You chose a negative answer",
                        Toast.LENGTH_LONG).show();
            }
        });
        /*
        // set neutral button: Exit the app message
        alertDialogBuilder.setNeutralButton("Exit the app",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                // exit the app and go to the HOME
                slider_answer.this.finish();
            }
        });
        */
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show alert
        alertDialog.show();
    }
}
