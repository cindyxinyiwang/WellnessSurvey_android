package com.example.cindywang.wellnesssurvey;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cindywang.wellnesssurvey.ListView.QList;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class slider_answer extends Activity {

    TextView questionView;
    TextView lowView;
    TextView highView;
    Bundle question;
    SeekBar seekBar;
    int seekbarAnswer = 0;

    //values associated with question
    String questionId = "";
    ParseObject questionPointer;
    int questionIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider_answer);
        // Retrieve data from MainActivity on item click event
        Intent i = getIntent();

        //define variables
        String questionText = "";
        //String low = "";
        String high = "";
        String[] config = null;

        // Get the name
        question = i.getExtras();
        questionText = question.getString("Question");
        config = question.getStringArray("config");
        questionId = question.getString("questionId");
        questionIndex = question.getInt("questionIndex");

        final String low = config[0];
        high = config[1];

        // Locate the Views in slider_answer.xml
        questionView = (TextView) findViewById(R.id.question);
        lowView = (TextView) findViewById(R.id.lowBound);
        highView = (TextView) findViewById(R.id.highBound);

        // Load the text into the TextView
        questionView.setText(questionText);
        lowView.setText(low);
        highView.setText(high);

        //set seekBar activity and value
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        //seekBar.setMax(Integer.valueOf(high) - Integer.valueOf(low));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekbarAnswer = progress + Integer.valueOf(low);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.slider_answer, menu);

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

    //submit answer button activity
    public void submitAnswer(View view) {

        openAlert(view);

    }

    private void openAlert(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(slider_answer.this);
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
                            answerInProgress.put("answer",seekbarAnswer+"");
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
