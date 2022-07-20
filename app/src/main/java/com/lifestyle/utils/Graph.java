package com.lifestyle.utils;

import com.lifestyle.model.History;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Graph {
    public static String[] setXAxis(int noOfDays) {
        String[] orderedDays = new String[noOfDays];
        for(int i = 0; i< noOfDays; i++){
            Date currentTime = Calendar.getInstance().getTime();
            Calendar day7ago = Calendar.getInstance();
            day7ago.setTime(currentTime);
            day7ago.add(Calendar.DAY_OF_YEAR, -(noOfDays -i-1));
            Date newDate = day7ago.getTime();
            String day = newDate.toString().substring(0,10);
            orderedDays[i] = day;
        }
        return orderedDays;
    }

    public static HashMap<String, String> addDuplicateDate(List<History> exercise) {
        HashMap<String, String> hm = new HashMap<>();
        for (int i = 0; i < exercise.size(); i++) {
            String day = exercise.get(i).getCreatedAt().toString().substring(0, 10);
            if (hm.containsKey(day)) {
                hm.put(day, Integer.toString(Integer.parseInt(hm.get(day)) + Integer.parseInt(exercise.get(i).getCount())));
            } else {
                hm.put(day, exercise.get(i).getCount());
            }
        }
        return hm;
    }
}