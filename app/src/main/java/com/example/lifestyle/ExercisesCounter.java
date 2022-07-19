package com.example.lifestyle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.lifestyle.fragment.PushupsFragment;
import com.example.lifestyle.fragment.SitupsFragment;
import com.example.lifestyle.fragment.SquatsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

public class ExercisesCounter extends AppCompatActivity {
    private BottomNavigationView exercisesCounterBottomNavigationView;
    private static final int NUM_PAGES=3;
    private ViewPager2 viewPager2;
    private FragmentStateAdapter pageAdapter;
    HashMap<Integer, Integer> positionOfPages = new HashMap<>();
    HashMap<Integer, Fragment> fragments = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises_counter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        int page = intent.getIntExtra("page",0);
        viewPager2 = findViewById(R.id.VP2);
        exercisesCounterBottomNavigationView = findViewById(R.id.bottom_navigation2);
        pageAdapter = new ScreenSlidePageAdapter(this);
        viewPager2.setAdapter(pageAdapter);
        setBottomNavigation();
        setViewPagerListener();
        positionOfPages.put(0, R.id.action_push_ups);
        positionOfPages.put(1, R.id.action_sit_ups);
        positionOfPages.put(2, R.id.action_squats);
        fragments.put(0, new PushupsFragment());
        fragments.put(1, new SitupsFragment());
        fragments.put(2, new SquatsFragment());
        viewPager2.setCurrentItem(page,false);
    }

    private void setViewPagerListener() {
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                int selectedPage = positionOfPages.get(position);
                exercisesCounterBottomNavigationView.setSelectedItemId(selectedPage);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void setBottomNavigation() {
        exercisesCounterBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                for(Map.Entry<Integer, Integer> entry: positionOfPages.entrySet()) {
                    if(entry.getValue() == item.getItemId()) {
                        viewPager2.setCurrentItem(entry.getKey());
                        return true;
                    }
                }
                return true;
            }
        });
    }

    private class ScreenSlidePageAdapter extends FragmentStateAdapter {
        public ScreenSlidePageAdapter(ExercisesCounter exercisesCounter) {super(exercisesCounter);}

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }
}