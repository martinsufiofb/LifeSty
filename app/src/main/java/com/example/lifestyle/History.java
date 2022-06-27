package com.example.lifestyle;


import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("History")
public class History extends ParseObject {

    public static final String KEY_COUNT = "count";
    public static final String KEY_NAMEOFEXERCISE = "nameOfExercise";
    public static final String KEY_USER = "user";


    public String getCount(){
        return getString(KEY_COUNT);
    }
    public String getNameOfExercise(){
        return getString(KEY_NAMEOFEXERCISE);
    }
    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void  setCount(String count){
        put(KEY_COUNT, count);
    }
    public void  setNameOfExercise(String nameofexercise){put(KEY_NAMEOFEXERCISE, nameofexercise);}
    public void setUser(ParseUser user){put(KEY_USER, user);}

}
