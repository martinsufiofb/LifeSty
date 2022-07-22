package com.lifestyle.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.lifestyle.R;
import com.lifestyle.fragment.PushupsCounterFragment;
import com.lifestyle.fragment.SitupsCounterFragment;
import com.lifestyle.fragment.SquatCounterFragment;

import java.util.HashMap;

public class CameraActivity extends AppCompatActivity {
    final FragmentManager fragmentManager = getSupportFragmentManager();
    HashMap<Integer, Fragment> fragments = new HashMap<>();
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        fragments.put(1, new PushupsCounterFragment());
        fragments.put(2, new SitupsCounterFragment());
        fragments.put(3, new SquatCounterFragment());

        Intent intent = getIntent();
        int exerciseChosen = intent.getIntExtra("exerciseClicked", 0);
        fragment = fragments.get(exerciseChosen);
        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();

    }
}