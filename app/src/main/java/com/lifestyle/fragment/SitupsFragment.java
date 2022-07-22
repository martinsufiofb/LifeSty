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
import com.example.lifestyle.R;
import com.lifestyle.model.Situps;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class SitupsFragment extends Fragment {
    public TextView situpsCount;
    public Button situpsStartCounterButton;
    public Button situpsSaveButton;
    private static final String TAG = "SitupsFragments";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 11) {
                        Intent intent = result.getData();
                        if (intent != null) {
                            int noOfSitups = intent.getIntExtra("situpsResult", 0);
                            if (noOfSitups != 0) {
                                situpsCount.setText(String.valueOf(noOfSitups));
                            }
                        }
                    }
                }
            }
    );

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
        situpsCount = view.findViewById(R.id.situpCount);
        situpsStartCounterButton = view.findViewById(R.id.situpButton);
        situpsSaveButton = view.findViewById(R.id.situpDoneButton);
        situpsStartCounterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int exerciseClicked = 2;
                String count = situpsCount.getText().toString();
                Intent intent = new Intent(getContext(), CameraActivity.class);
                intent.putExtra("exerciseClicked", exerciseClicked);
                intent.putExtra("numberOfPushups", count);
                activityLauncher.launch(intent);
            }
        });

        situpsSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                String userId = currentUser.getObjectId();
                String count = situpsCount.getText().toString();
                if (!count.equals("0")) {
                    saveHistory(currentUser, count, "Sit Ups", userId);
                    saveSitups(currentUser, count);
                }
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
                if (e != null) {
                    Log.e(TAG, "Error While Saving sit ups", e);
                }
            }
        });
        ParseUser user = ParseUser.getCurrentUser();
        int totalSitUps = user.getInt("sitUps");
        totalSitUps += Integer.parseInt(situpsCount.getText().toString());
        user.put("sitUps", totalSitUps);
        user.saveInBackground();
    }

    public void saveHistory(ParseUser currentUser, String situpno, String name, String userId) {
        History history = new History();
        history.setUser(currentUser);
        history.setCount(situpno);
        history.setNameOfExercise(name);
        history.setUserId(userId);
        history.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error While Saving sit ups history", e);
                }
                situpsCount.setText("0");
            }
        });
    }
}