package com.example.lifestyle.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lifestyle.Exercise;
import com.example.lifestyle.ExercisesAdapter;
import com.example.lifestyle.R;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String TAG = "HomeFragment";
    public static RecyclerView rvExercises;
    private ExercisesAdapter adapter;
    private List<Exercise> allExercises;
    private static final String[] CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA};
    private static final int CAMERA_REQUEST_CODE = 10;
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvExercises = view.findViewById(R.id.rvExercises);
        allExercises = new ArrayList<>();
        adapter = new ExercisesAdapter(getContext(), allExercises);
        rvExercises.setAdapter(adapter);
        rvExercises.setLayoutManager(new LinearLayoutManager(getContext()));
        queryExercises();
        if (!(hasCameraPermission())) {
            Log.i("CAMERA", "ENTERED");
            requestPermission();
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(
                getActivity(),
                CAMERA_PERMISSION,
                CAMERA_REQUEST_CODE
        );
    }

    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(
                getContext(),
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private void queryExercises() {
        ParseQuery<Exercise> query = ParseQuery.getQuery(Exercise.class);
        query.findInBackground(new FindCallback<Exercise>() {
            @Override
            public void done(List<Exercise> exercises, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue getting exercises", e);
                    return;
                }
                for (Exercise exercise : exercises) {
                    Log.i(TAG, "Exercises: " + exercise.getTitle());
                }
                allExercises.addAll(exercises);
                adapter.notifyDataSetChanged();
            }
        });
    }
}