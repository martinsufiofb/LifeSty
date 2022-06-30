package com.example.lifestyle;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Exercise.class);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(History.class);
        ParseObject.registerSubclass(Pushups.class);
        ParseObject.registerSubclass(Situps.class);
        ParseObject.registerSubclass(Squats.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("ZV1oqmS1nwMiS9S8gw325hWMljYaH1vMtITOi4QD")
                .clientKey("GID8FQZjuoZ9TJT2j6Y5w9q9WtPr7EfehDqKpsug")
                .server("https://parseapi.back4app.com")
                .build());
    }
}
