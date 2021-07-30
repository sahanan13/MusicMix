package com.codepath.musicmix;

import android.app.Application;

import com.codepath.musicmix.models.Playlist;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();

        // Register your parse models

        ParseObject.registerSubclass(Playlist.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("X1yrOT63fuxupDkdQ4yYLVC57P7hOGjL3oltuQQ4")
                .clientKey("BRbx2kNSnuI373lAqz0Z61IvvSf2DSdpLtFlbqy4")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
