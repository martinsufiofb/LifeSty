package com.example.lifestyle;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("ZV1oqmS1nwMiS9S8gw325hWMljYaH1vMtITOi4QD")
                .clientKey("GID8FQZjuoZ9TJT2j6Y5w9q9WtPr7EfehDqKpsug")
                .server("https://parseapi.back4app.com")
                .build());
    }
}
