package com.example.lifestyle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.lifestyle.fragment.FriendsFragment;
import com.example.lifestyle.fragment.HomeFragment;
import com.example.lifestyle.fragment.ProfileFragment;
import com.example.lifestyle.fragment.PushupsFragment;
import com.example.lifestyle.fragment.SearchFragment;
import com.example.lifestyle.fragment.SitupsFragment;
import com.example.lifestyle.fragment.SquatsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ExercisesCounter extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView2;
    private static final int NUM_PAGES=3;
    private ViewPager2 viewPager2;
    private FragmentStateAdapter pageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises_counter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        int page = intent.getIntExtra("page",0);

        viewPager2 = findViewById(R.id.VP2);
        bottomNavigationView2 = findViewById(R.id.bottom_navigation2);
        pageAdapter = new ScreenSlidePageAdapter(this);
        viewPager2.setAdapter(pageAdapter);
        setBottomNavigation();
        setViewPagerListener();
        viewPager2.setCurrentItem(page);

    }




    private void setViewPagerListener() {
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    bottomNavigationView2.setSelectedItemId(R.id.action_push_ups);
                }
                if(position==1){
                    bottomNavigationView2.setSelectedItemId(R.id.action_sit_ups);
                }
                if(position==2){
                    bottomNavigationView2.setSelectedItemId(R.id.action_squats);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                super.onPageScrollStateChanged(state);
            }
        });
    }


    private void setBottomNavigation() {

        bottomNavigationView2.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId()==R.id.action_push_ups ){
                    viewPager2.setCurrentItem(0);
                }
                if(item.getItemId()==R.id.action_sit_ups){
                    viewPager2.setCurrentItem(1);
                }
                if(item.getItemId()==R.id.action_squats){
                    viewPager2.setCurrentItem(2);

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
            Fragment fragment;
            switch (position) {
                case 0:
                    fragment = new PushupsFragment();
                    break;
                case 1:
                    fragment = new SitupsFragment();
                    break;
                case 2:
                    fragment = new SquatsFragment();
                    break;
                default:
                    return null;
            }
            return fragment;

        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }


}