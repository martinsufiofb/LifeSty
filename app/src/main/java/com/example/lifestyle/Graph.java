package com.example.lifestyle;


import android.util.Log;

import com.bumptech.glide.disklrucache.DiskLruCache;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Graph {
    public static String[] setXAxis(String day) {
        HashMap<String, Integer> hm = new HashMap<>();
        List<String> days = new ArrayList<>();
        hm.put("Sun", 1);
        hm.put("Mon", 2);
        hm.put("Tue", 3);
        hm.put("Wed", 4);
        hm.put("Thu", 5);
        hm.put("Fri", 6);
        hm.put("Sat", 7);
        days.add(0,"Sun");
        days.add(1,"Mon");
        days.add(2,"Tue");
        days.add(3,"Wed");
        days.add(4,"Thu");
        days.add(5,"Fri");
        days.add(6,"Sat");
        String[] orderedDays = new String[7];
        int j = hm.get(day);
        int z = 0;
        for(int i = j; i<7+j; i++){
            orderedDays[z] = days.get(i%7);
            z++;
        }
        return orderedDays;
    }

    public static HashMap<String, String> addDuplicateDate(List<History> exercise){
        HashMap<String, String> hm = new HashMap<>();
        for(int i = 0; i<exercise.size();i++){
            String day = exercise.get(i).getCreatedAt().toString().substring(0,3);
            if(hm.containsKey(day)){
                hm.put(day, Integer.toString(Integer.parseInt(hm.get(day))+Integer.parseInt(exercise.get(i).getCount())));
            }else{
                hm.put(day, exercise.get(i).getCount());
            }
        }
        return hm;
    }
}