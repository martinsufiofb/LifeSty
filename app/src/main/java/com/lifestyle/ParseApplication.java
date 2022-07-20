package com.lifestyle;

import android.app.Application;

import com.lifestyle.model.Exercise;
import com.lifestyle.model.History;
import com.lifestyle.model.Pushups;
import com.lifestyle.model.Situps;
import com.lifestyle.model.Squats;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Exercise.class);
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
