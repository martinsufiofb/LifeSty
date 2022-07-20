package com.example.lifestyle.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lifestyle.utils.Graph;
import com.example.lifestyle.model.History;
import com.example.lifestyle.adapters.HistoryAdapter;
import com.example.lifestyle.LoginActivity;
import com.example.lifestyle.model.Pushups;
import com.example.lifestyle.R;
import com.example.lifestyle.model.Situps;
import com.example.lifestyle.model.Squats;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private static String TAG = "ProfileFragment";
    CardView logout;
    CardView clearData;
    CardView deleteAccount;
    String currentUserUsername;
    TextView profileUsername;
    TextView secondCircle;
    TextView firstCircle;
    TextView thirdCircle;
    TextView emptyRecyclerView;
    TextView secondCircleName;
    TextView firstCircleName;
    TextView thirdCircleName;
    CircleImageView profileImage;
    String defaultImage;
    Switch switch7days;
    Switch switch30days;
    Switch switch365days;
    int pushupsTotal;
    int situpsTotal;
    int squatsTotal;
    private RecyclerView rvHistory;
    private HistoryAdapter adapter;
    private List<History> historyList;
    private List<Pushups> pushupsList;
    private List<Situps> situpsList;
    private List<Squats> squatsList;
    LineChart lineChart;
    int numberOfDays = 7;
    public HashMap<String, String> unDuplicatedPushUps;
    public HashMap<String, String> unDuplicatedSitUps;
    public HashMap<String, String> unDuplicatedSquats;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentUserUsername = ParseUser.getCurrentUser().getUsername();
        profileUsername = view.findViewById(R.id.tvProfileUsername);
        firstCircle = view.findViewById(R.id.tv1);
        secondCircle = view.findViewById(R.id.tv2);
        thirdCircle = view.findViewById(R.id.tv3);
        clearData = view.findViewById(R.id.clearData);
        deleteAccount = view.findViewById(R.id.deleteAccount);
        emptyRecyclerView = view.findViewById(R.id.tvEmptyRecyclerView);
        firstCircleName = view.findViewById(R.id.tvName1);
        secondCircleName = view.findViewById(R.id.tvName2);
        thirdCircleName = view.findViewById(R.id.tvName3);
        profileImage = view.findViewById(R.id.ivprofilepic);
        profileUsername.setText(currentUserUsername);
        logout = view.findViewById(R.id.LLlogout);
        lineChart = view.findViewById(R.id.lineChart);
        switch7days = view.findViewById(R.id.switch7days);
        switch30days = view.findViewById(R.id.switch30days);
        switch365days = view.findViewById(R.id.switch365days);
        defaultImage = getProfileUrl(ParseUser.getCurrentUser().getObjectId());

        Glide.with(this)
                .load(defaultImage)
                .into(profileImage);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        clearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser user = ParseUser.getCurrentUser();
                deleteHistoryData(user);
                deletePushupsData(user);
                deleteSitupsData(user);
                deleteSquatsData(user);
                user.put("pushUps", 0);
                user.put("sitUps", 0);
                user.put("squats", 0);
                user.saveInBackground();
                lineChart.clear();
            }
        });

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser user = ParseUser.getCurrentUser();
                deleteHistoryData(user);
                deletePushupsData(user);
                deleteSitupsData(user);
                deleteSquatsData(user);
                deleteUser(user);
            }
        });

        switch7days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!switch7days.isChecked()) {
                    switch7days.toggle();
                } else {
                    if (switch365days.isChecked()) {
                        switch365days.toggle();
                    }
                    if (switch30days.isChecked()) {
                        switch30days.toggle();
                    }
                    lineChart.clear();
                    createGraph(7);
                }
            }
        });

        switch30days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!switch30days.isChecked()) {
                    switch30days.toggle();
                } else {
                    if (switch365days.isChecked()) {
                        switch365days.toggle();
                    }
                    if (switch7days.isChecked()) {
                        switch7days.toggle();
                    }
                    lineChart.clear();
                    createGraph(30);
                }
            }
        });

        switch365days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!switch365days.isChecked()) {
                    switch365days.toggle();
                } else {
                    if (switch30days.isChecked()) {
                        switch30days.toggle();
                    }
                    if (switch7days.isChecked()) {
                        switch7days.toggle();
                    }
                    lineChart.clear();
                    createGraph(365);
                }
            }
        });

        rvHistory = view.findViewById(R.id.rvHistory);
        historyList = new ArrayList<>();
        pushupsList = new ArrayList<>();
        situpsList = new ArrayList<>();
        squatsList = new ArrayList<>();
        adapter = new HistoryAdapter(getContext(), historyList);
        rvHistory.setAdapter(adapter);
        rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));

        queryHistory();
        queryPushups();
        querySitups();
        querySquats();

        Date currentTime = Calendar.getInstance().getTime();
        Calendar day7ago = Calendar.getInstance();
        day7ago.setTime(currentTime);
        day7ago.add(Calendar.DAY_OF_YEAR, -365);
        Date newDate = day7ago.getTime();
        queryForDataPoints(newDate);
    }

    private void createGraph(int days) {
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

        ArrayList<com.github.mikephil.charting.data.Entry> yEntrys = new ArrayList<>();
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
        lineChart.animateXY(3000, 3000);
        lineChart.invalidate();
    }

    private void queryForDataPoints(Date newDate) {
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

    private void deleteUser(ParseUser user) {
        user.deleteInBackground();
        logout();
    }

    private void deleteSquatsData(ParseUser user) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Squats");
        query.whereEqualTo("user", user);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                ParseObject.deleteAllInBackground(objects);
                squatsList.clear();
                squatsTotal = 0;
                isDone(secondCircle, firstCircle, thirdCircle,
                        secondCircleName, firstCircleName, thirdCircleName,
                        pushupsTotal, situpsTotal, squatsTotal);
            }
        });
    }

    private void deleteSitupsData(ParseUser user) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Situps");
        query.whereEqualTo("user", user);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                ParseObject.deleteAllInBackground(objects);
                situpsList.clear();
                situpsTotal = 0;
                isDone(secondCircle, firstCircle, thirdCircle,
                        secondCircleName, firstCircleName, thirdCircleName,
                        pushupsTotal, situpsTotal, squatsTotal);
            }
        });
    }

    private void deletePushupsData(ParseUser user) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Pushups");
        query.whereEqualTo("user", user);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                ParseObject.deleteAllInBackground(objects);
                pushupsList.clear();
                pushupsTotal = 0;
                isDone(secondCircle, firstCircle, thirdCircle,
                        secondCircleName, firstCircleName, thirdCircleName,
                        pushupsTotal, situpsTotal, squatsTotal);
            }
        });
    }

    private void deleteHistoryData(ParseUser user) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("History");
        query.whereEqualTo("user", user);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                ParseObject.deleteAllInBackground(objects);
                historyList.clear();
                adapter.notifyDataSetChanged();
                emptyRecyclerView.setVisibility(View.VISIBLE);
            }
        });

    }

    private void querySquats() {
        ParseQuery<Squats> query = ParseQuery.getQuery(Squats.class);
        query.include(Squats.KEY_USER);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Squats>() {
            @Override
            public void done(List<Squats> squats, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue getting squats", e);
                    return;
                }
                squatsList.addAll(squats);
                for (int i = 0; i < squatsList.size(); i++) {
                    squatsTotal += Integer.parseInt(squatsList.get(i).getCount());
                }
                isDone(secondCircle, firstCircle, thirdCircle,
                        secondCircleName, firstCircleName, thirdCircleName,
                        pushupsTotal, situpsTotal, squatsTotal);
            }
        });
    }

    private void querySitups() {
        ParseQuery<Situps> query = ParseQuery.getQuery(Situps.class);
        query.include(Situps.KEY_USER);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Situps>() {
            @Override
            public void done(List<Situps> situps, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue getting situps", e);
                    return;
                }
                situpsList.addAll(situps);
                for (int i = 0; i < situpsList.size(); i++) {
                    situpsTotal += Integer.parseInt(situpsList.get(i).getCount());
                }
                isDone(secondCircle, firstCircle, thirdCircle,
                        secondCircleName, firstCircleName, thirdCircleName,
                        pushupsTotal, situpsTotal, squatsTotal);
            }
        });
    }

    private void queryPushups() {
        ParseQuery<Pushups> query = ParseQuery.getQuery(Pushups.class);
        query.include(Pushups.KEY_USER);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Pushups>() {
            @Override
            public void done(List<Pushups> pushups, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue getting pushups", e);
                    return;
                }
                pushupsList.addAll(pushups);
                for (int i = 0; i < pushupsList.size(); i++) {
                    pushupsTotal += Integer.parseInt(pushupsList.get(i).getCount());
                }
                isDone(secondCircle, firstCircle, thirdCircle,
                        secondCircleName, firstCircleName, thirdCircleName,
                        pushupsTotal, situpsTotal, squatsTotal);
            }
        });
    }

    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm) {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(hm.entrySet());
        Collections.sort(list, Comparator.comparing(Entry::getValue));
        HashMap<String, Integer> temp = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public static void isDone(TextView secondCircle, TextView firstCircle, TextView thirdCircle,
                              TextView secondCircleName, TextView firstCircleName, TextView thirdCircleName,
                              int pushupsTotal, int situpsTotal, int squatsTotal) {
        HashMap<String, Integer> hm = new HashMap<>();
        List<TextView> l1 = new ArrayList<>();
        List<TextView> l2 = new ArrayList<>();
        l1.add(0, thirdCircle);
        l1.add(1, secondCircle);
        l1.add(2, firstCircle);
        l2.add(0, thirdCircleName);
        l2.add(1, secondCircleName);
        l2.add(2, firstCircleName);
        hm.put("Push ups", pushupsTotal);
        hm.put("Sit ups", situpsTotal);
        hm.put("Squats", squatsTotal);
        int i = 0;
        Map<String, Integer> hmSorted = sortByValue(hm);
        for (Map.Entry<String, Integer> en : hmSorted.entrySet()) {
            l1.get(i).setText(String.valueOf(en.getValue()));
            l2.get(i).setText(String.valueOf(en.getKey()));
            i++;
        }
    }

    private void queryHistory() {
        ParseQuery<History> query = ParseQuery.getQuery(History.class);
        query.include(History.KEY_USER);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<History>() {
            @Override
            public void done(List<History> history, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue getting exercises", e);
                    return;
                }
                historyList.addAll(history);
                adapter.notifyDataSetChanged();
                if (historyList.size() == 0) {
                    emptyRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    emptyRecyclerView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void logout() {
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error signing out", e);
                    Toast.makeText(getContext(), "Error signing out", Toast.LENGTH_SHORT).show();
                    return;
                }
                goToLoginActivity();
            }
        });
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private static String getProfileUrl(final String userId) {
        String hex = "";
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final byte[] hash = digest.digest(userId.getBytes());
            final BigInteger bigInt = new BigInteger(hash);
            hex = bigInt.abs().toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "https://www.gravatar.com/avatar/" + hex + "?d=identicon";
    }
}