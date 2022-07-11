package com.example.lifestyle;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("User")
public class User extends ParseObject {
    public static final  String KEY_TITLE = "username";

    public String getUsername(){
        return getString(KEY_TITLE);
    }
}
