package com.example.cindywang.wellnesssurvey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;


public class slider_answer extends Activity {

    TextView questionView;
    TextView lowView;
    TextView highView;
    Bundle question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider_answer);
        // Retrieve data from MainActivity on item click event
        Intent i = getIntent();

        //define variables
        String questionText = "";
        String low = "";
        String high = "";
        String[] config = null;

        // Get the name
        question = i.getExtras();
        questionText = question.getString("Question");
        config = question.getStringArray("config");
        low = config[0];
        high = config[1];

        // Locate the Views in slider_answer.xml
        questionView = (TextView) findViewById(R.id.question);
        lowView = (TextView) findViewById(R.id.lowBound);
        highView = (TextView) findViewById(R.id.highBound);

        // Load the text into the TextView
        questionView.setText(questionText);
        lowView.setText(low);
        highView.setText(high);
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
}
