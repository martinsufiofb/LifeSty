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
import com.example.lifestyle.R;
import com.example.lifestyle.Situps;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class SitupsFragment extends Fragment {
    public TextView situpCount;
    public Button situpButton;
    public Button situpDoneButton;
    public int situpNo = 0;
    private static final String TAG = "SitupsFragments";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public SitupsFragment() {
        // Required empty public constructor
    }

    public static SitupsFragment newInstance(String param1, String param2) {
        SitupsFragment fragment = new SitupsFragment();
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
        return inflater.inflate(R.layout.fragment_situps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        situpCount = view.findViewById(R.id.situpCount);
        situpButton = view.findViewById(R.id.situpButton);
        situpDoneButton = view.findViewById(R.id.situpDoneButton);
        situpNo = Integer.parseInt(situpCount.getText().toString());
        situpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int exerciseCliked = 2;
                Intent intent = new Intent(getContext(), CameraActivity.class);
                intent.putExtra("exerciseClicked", exerciseCliked);
                startActivity(intent);
            }
        });

        situpDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                String count = String.valueOf(situpNo);
                saveHistory(currentUser, count, "Sit Ups");
                saveSitups(currentUser,count);
            }
        });
    }

    private void saveSitups(ParseUser currentUser, String count) {
        Situps situps = new Situps();
        situps.setUser(currentUser);
        situps.setCount(count);
        situps.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null){
                    Log.e(TAG, "Error While Saving sit ups", e);
                }
                Log.i(TAG, "Sit ups save was successfully");
            }
        });
    }

    public void saveHistory(ParseUser currentUser, String situpno, String name){
        History history = new History();
        history.setUser(currentUser);
        history.setCount(situpno);
        history.setNameOfExercise(name);
        history.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null){
                    Log.e(TAG, "Error While Saving sit ups history", e);
                }
                Log.i(TAG, "Sit up history save was successfully");
                situpNo =0;
                situpCount.setText("0");
            }
        });
    }
}