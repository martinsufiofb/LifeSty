package com.example.lifestyle;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("Exercises")
public class Exercise extends ParseObject {
    public static final  String KEY_TITLE = "nameOfExercise";
    public static final  String KEY_DESCRIPTION = "description";
    public static final  String KEY_IMAGE = "picture";

    public String getTitle(){
        return getString(KEY_TITLE);
    }
    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }
    public ParseFile getImage(){
        return getParseFile(KEY_IMAGE);
    }
}
