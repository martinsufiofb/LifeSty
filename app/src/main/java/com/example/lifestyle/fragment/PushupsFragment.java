package com.example.lifestyle.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.lifestyle.CameraActivity;
import com.example.lifestyle.History;
import com.example.lifestyle.Pushups;
import com.example.lifestyle.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class PushupsFragment extends Fragment {
    public TextView pushupCount;
    public Button pushupButton;
    public Button pushupDoneButton;
    public int pushupNo = 0;
    private static final String TAG = "PushupsFragments";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public PushupsFragment() {
        // Required empty public constructor
    }

    public static PushupsFragment newInstance(String param1, String param2) {
        PushupsFragment fragment = new PushupsFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pushups, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pushupCount = view.findViewById(R.id.pushupCount);
        pushupButton = view.findViewById(R.id.pushupButton);
        pushupDoneButton = view.findViewById(R.id.pushupDoneButton);
        pushupNo = Integer.parseInt(pushupCount.getText().toString());
        pushupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int exerciseCliked = 1;
                Intent intent = new Intent(getContext(), CameraActivity.class);
                intent.putExtra("exerciseClicked", exerciseCliked);
                startActivity(intent);
            }
        });

        pushupDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                String count = String.valueOf(pushupNo);
                saveHistory(currentUser, count, "Push Ups");
                savePush(currentUser, count);
            }
        });
    }

    private void savePush(ParseUser currentUser, String count) {
        Pushups pushups = new Pushups();
        pushups.setUser(currentUser);
        pushups.setCount(count);
        pushups.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null){
                    Log.e(TAG, "Error While Saving push ups", e);
                }
                Log.i(TAG, "Push ups save was successfully");
            }
        });
    }

    public void saveHistory(ParseUser currentUser, String pushupno, String name){
        History history = new History();
        history.setUser(currentUser);
        history.setCount(pushupno);
        history.setNameOfExercise(name);
        history.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null){
                    Log.e(TAG, "Error While Saving push ups history", e);
                }
                Log.i(TAG, "Push up history save was successfully");
                pushupNo =0;
                pushupCount.setText("0");
            }
        });
    }
}