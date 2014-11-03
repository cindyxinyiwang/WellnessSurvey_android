package com.example.cindywang.wellnesssurvey;

/**
 * Created by cindywang on 11/2/14.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SingleItemView extends Activity {
    // Declare Variables
    TextView txtname;
    String question;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from singleitemview.xml
        setContentView(R.layout.singleitemview);

        // Retrieve data from MainActivity on item click event
        Intent i = getIntent();

        // Get the name
        question = i.getStringExtra("question");

        // Locate the TextView in singleitemview.xml
        txtname = (TextView) findViewById(R.id.name);

        // Load the text into the TextView
        txtname.setText(question);

    }
}