package com.example.cindywang.wellnesssurvey;

/**
 * Created by cindywang on 11/2/14.
 */
import com.parse.Parse;
import com.parse.ParseACL;

import com.parse.ParseUser;

import android.app.Application;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Add your initialization code here
        Parse.initialize(this, "7y3hW9Gibahq2gCpaTd5TfTH3xlrSao2PCleXr9E", "qVUTlfmiQatz6EvU1gqUBsDqzgO3ajcAGhbXpj4Q");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
    }

}
