package com.example.cindywang.wellnesssurvey;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseUser;

import java.text.ParseException;


public class Login extends Activity {

    FragmentManager fm = getFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Parse.initialize(this, "7y3hW9Gibahq2gCpaTd5TfTH3xlrSao2PCleXr9E", "qVUTlfmiQatz6EvU1gqUBsDqzgO3ajcAGhbXpj4Q");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
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

    public void logIn(){
        EditText usernameView = (EditText) findViewById(R.id.username);
        EditText passwordView = (EditText) findViewById(R.id.password);
        String username = usernameView.getText().toString();
        String password = passwordView.getText().toString();
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, com.parse.ParseException e) {
                if (user != null) {
                    // Hooray! The user is logged in.
                    Intent i = new Intent(Login.this, QList.class);

                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                    DFragment dFragment = new DFragment();
                    dFragment.show(fm, "Login Error");
                }
            }
        });

    }

    public void signUp (View view){
        Intent intent = new Intent(this, Signup.class);
        startActivity(intent);
    }
}
