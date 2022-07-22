package com.lifestyle.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lifestyle.activities.CameraActivity;
import com.lifestyle.model.History;
import com.lifestyle.model.Pushups;
import com.example.lifestyle.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class PushupsFragment extends Fragment {
    public TextView pushupsCount;
    public Button pushupsStartCounterButton;
    public Button pushupsSaveButton;
    private static final String TAG = "PushupsFragments";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 10) {
                        Intent intent = result.getData();
                        if (intent != null) {
                            int noOfPushups = intent.getIntExtra("result", 0);
                            if (noOfPushups != 0) {
                                pushupsCount.setText(String.valueOf(noOfPushups));
                            }
                        }
                    }
                }
            }
    );

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
        pushupsCount = view.findViewById(R.id.pushupCount);
        pushupsStartCounterButton = view.findViewById(R.id.pushupButton);
        pushupsSaveButton = view.findViewById(R.id.pushupDoneButton);
        pushupsStartCounterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int exerciseClicked = 1;
                String count = pushupsCount.getText().toString();
                Intent intent = new Intent(getContext(), CameraActivity.class);
                intent.putExtra("exerciseClicked", exerciseClicked);
                intent.putExtra("numberOfPushups", count);
                activityLauncher.launch(intent);
            }
        });

        pushupsSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                String userId = currentUser.getObjectId();
                String count = pushupsCount.getText().toString();
                if (!count.equals("0")) {
                    saveHistory(currentUser, count, "Push Ups", userId);
                    savePush(currentUser, count);
                }
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
                if (e != null) {
                    Log.e(TAG, "Error While Saving push ups", e);
                }
            }
        });
        ParseUser user = ParseUser.getCurrentUser();
        int totalPushupsCount = user.getInt("pushUps");
        totalPushupsCount += Integer.parseInt(pushupsCount.getText().toString());
        user.put("pushUps", totalPushupsCount);
        user.saveInBackground();
    }

    public void saveHistory(ParseUser currentUser, String pushupno, String name, String userId) {
        History history = new History();
        history.setUser(currentUser);
        history.setCount(pushupno);
        history.setNameOfExercise(name);
        history.setUserId(userId);
        history.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error While Saving push ups history", e);
                }
                Log.i(TAG, "Push up history save was successfully");
                pushupsCount.setText("0");
            }
        });
    }
}