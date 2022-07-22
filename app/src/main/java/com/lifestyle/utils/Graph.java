package com.lifestyle.utils;

import static com.lifestyle.fragment.ProfileFragment.lineChart;

import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.lifestyle.fragment.ProfileFragment;
import com.lifestyle.model.History;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Graph {

    private static final String TAG = "Graph" ;
    static int numberOfDays = 7;
    public static HashMap<String, String> unDuplicatedPushUps;
    public static HashMap<String, String> unDuplicatedSitUps;
    public static HashMap<String, String> unDuplicatedSquats;

    public static void createGraph(int days) {
        float[] yPushupsDataL = new float[days];
        float[] ySitupsDataL = new float[days];
        float[] ySquatsDataL = new float[days];
        String[] xAxisL = Graph.setXAxis(days);
        for (int i = 0; i < days; i++) {
            if (unDuplicatedPushUps.containsKey(xAxisL[i])) {
                yPushupsDataL[i] = Float.valueOf(unDuplicatedPushUps.get(xAxisL[i]));
            } else {
                yPushupsDataL[i] = 0;
            }
            if (unDuplicatedSitUps.containsKey(xAxisL[i])) {
                ySitupsDataL[i] = Float.valueOf(unDuplicatedSitUps.get(xAxisL[i]));
            } else {
                ySitupsDataL[i] = 0;
            }
            if (unDuplicatedSquats.containsKey(xAxisL[i])) {
                ySquatsDataL[i] = Float.valueOf(unDuplicatedSquats.get(xAxisL[i]));
            } else {
                ySquatsDataL[i] = 0;
            }
        }

        ArrayList<Entry> yEntrys = new ArrayList<>();
        ArrayList<com.github.mikephil.charting.data.Entry> yEntrys2 = new ArrayList<>();
        ArrayList<com.github.mikephil.charting.data.Entry> yEntrys3 = new ArrayList<>();

        final ArrayList<String> xEntrys = new ArrayList<>();

        for (int i = 0; i < xAxisL.length; i++) {
            yEntrys.add(new com.github.mikephil.charting.data.Entry(i, yPushupsDataL[i]));
            yEntrys2.add(new com.github.mikephil.charting.data.Entry(i, ySitupsDataL[i]));
            yEntrys3.add(new com.github.mikephil.charting.data.Entry(i, ySquatsDataL[i]));
        }

        for (int i = 1; i < xAxisL.length; i++) {
            xEntrys.add(xAxisL[i]);
        }

        LineDataSet dataSet2 = new LineDataSet(yEntrys2, "Sit ups");
        dataSet2.setColor(Color.parseColor("#50C878"));
        dataSet2.setCircleColor(Color.parseColor("#50C878"));
        dataSet2.setLineWidth(1f);
        dataSet2.setCircleRadius(5f);
        dataSet2.setDrawCircleHole(true);
        dataSet2.setDrawValues(true);

        LineDataSet dataSet = new LineDataSet(yEntrys, "Push ups");
        dataSet.setColor(Color.parseColor("#b30000"));
        dataSet.setCircleColor(Color.parseColor("#b30000"));
        dataSet.setLineWidth(1f);
        dataSet.setCircleRadius(5f);
        dataSet.setDrawCircleHole(true);
        dataSet.setDrawValues(true);

        LineDataSet dataSet3 = new LineDataSet(yEntrys3, "Squats");
        dataSet3.setColor(Color.parseColor("#0000FF"));
        dataSet3.setCircleColor(Color.parseColor("#0000FF"));
        dataSet3.setLineWidth(1f);
        dataSet3.setCircleRadius(5f);
        dataSet3.setDrawCircleHole(true);
        dataSet3.setDrawValues(true);


        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        if (xAxisL.length > 7) {
            dataSet.setDrawCircleHole(false);
            dataSet.setDrawValues(false);
            dataSet.setDrawCircles(false);
            dataSet2.setDrawCircleHole(false);
            dataSet2.setDrawValues(false);
            dataSet2.setDrawCircles(false);
            dataSet3.setDrawCircleHole(false);
            dataSet3.setDrawValues(false);
            dataSet3.setDrawCircles(false);
            xAxis.setDrawLabels(false);
        }
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return xAxisL[(int) value];
            }
        });

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setDrawAxisLine(true);
        rightAxis.setEnabled(true);

        LineData pieData = new LineData(dataSet, dataSet2, dataSet3);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawAxisLine(false);
        lineChart.getAxisRight().setDrawLabels(false);
        lineChart.setData(pieData);
        lineChart.animateXY(3000,3000);
        lineChart.invalidate();
    }

    public static void queryForDataPoints(Date newDate) {
        ParseQuery<History> query = ParseQuery.getQuery(History.class);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.whereGreaterThan("createdAt", newDate);
        query.addAscendingOrder("createdAt");
        query.findInBackground(new FindCallback<History>() {
            @Override
            public void done(List<History> history, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue getting exercises", e);
                    return;
                }
                List<History> pushUps = new ArrayList<>();
                List<History> sitUps = new ArrayList<>();
                List<History> squats = new ArrayList<>();
                HashMap<String, List> hm = new HashMap<>();
                hm.put("Push Ups", pushUps);
                hm.put("Squats", squats);
                hm.put("Sit Ups", sitUps);
                for (int i = 0; i < history.size(); i++) {
                    hm.get(history.get(i).getNameOfExercise()).add(history.get(i));
                }
                unDuplicatedPushUps = Graph.addDuplicateDate(pushUps);
                unDuplicatedSitUps = Graph.addDuplicateDate(sitUps);
                unDuplicatedSquats = Graph.addDuplicateDate(squats);
                createGraph(numberOfDays);
            }
        });
    }

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