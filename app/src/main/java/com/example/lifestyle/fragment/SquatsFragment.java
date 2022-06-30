package com.example.lifestyle.fragment;

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

import com.example.lifestyle.History;
import com.example.lifestyle.R;
import com.example.lifestyle.Squats;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class SquatsFragment extends Fragment {
    public TextView squatsCount;
    public Button squatsButton;
    public Button squatsDoneButton;
    public int squatsNo = 0;
    private static final String TAG = "SquatsFragments";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public SquatsFragment() {
        // Required empty public constructor
    }

    public static SquatsFragment newInstance(String param1, String param2) {
        SquatsFragment fragment = new SquatsFragment();
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
        return inflater.inflate(R.layout.fragment_squats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        squatsCount = view.findViewById(R.id.squatsCount);
        squatsButton = view.findViewById(R.id.squatsButton);
        squatsDoneButton = view.findViewById(R.id.squatsDoneButton);
        squatsNo = Integer.parseInt(squatsCount.getText().toString());
        squatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                squatsNo++;
                squatsCount.setText(String.valueOf(squatsNo));
            }
        });

        squatsDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                String count = String.valueOf(squatsNo);
                saveHistory(currentUser, count, "Squats");
                saveSquats(currentUser,count);
            }
        });
    }

    private void saveSquats(ParseUser currentUser, String count) {
        Squats squats = new Squats();
        squats.setUser(currentUser);
        squats.setCount(count);
        squats.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null){
                    Log.e(TAG, "Error While Saving squats", e);
                }
                Log.i(TAG, "Squats save was successfully");
            }
        });
    }

    public void saveHistory(ParseUser currentUser, String squatsno, String name){
        History history = new History();
        history.setUser(currentUser);
        history.setCount(squatsno);
        history.setNameOfExercise(name);
        history.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null){
                    Log.e(TAG, "Error While Saving squats history", e);
                }
                Log.i(TAG, "Squats history save was successfully");
                squatsNo =0;
                squatsCount.setText("0");
            }
        });
    }
}