package com.example.cindywang.wellnesssurvey;

/**
 * Created by cindywang on 11/2/14.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.parse.ParseObject;

public class SingleItemView extends Activity {
    // Declare Variables
    TextView txtname;
    Bundle question;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from singleitemview.xml
        setContentView(R.layout.singleitemview);

        //define variables
        String questionText = "";

        // Retrieve data from MainActivity on item click event
        Intent i = getIntent();

        // Get the question entry parse object
        question = i.getExtras();
        questionText = question.getString("Question");

        // Locate the TextView in singleitemview.xml
        txtname = (TextView) findViewById(R.id.name);

        // Load the text into the TextView
        txtname.setText(questionText);

    }
}