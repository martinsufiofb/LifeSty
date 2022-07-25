package com.lifestyle.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import java.util.Map.Entry;

import android.view.MenuItem;
import android.view.View;

import com.example.lifestyle.R;
import com.lifestyle.fragment.FriendsFragment;
import com.lifestyle.fragment.HomeFragment;
import com.lifestyle.fragment.ProfileFragment;
import com.lifestyle.fragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private static final int NUM_PAGES = 3;
    boolean onHomePage = false;
    private ViewPager2 viewPager2;
    private FragmentStateAdapter pageAdapter;
    HashMap<Integer, Integer> positionOfPages = new HashMap<>();
    HashMap<Integer, Fragment> fragments = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        positionOfPages.put(0, R.id.action_home);
        positionOfPages.put(1, R.id.action_search);
        positionOfPages.put(2, R.id.action_profile);
        fragments.put(0, new HomeFragment());
        fragments.put(1, new SearchFragment());
        fragments.put(2, new ProfileFragment());
        viewPager2 = findViewById(R.id.VP);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        pageAdapter = new ScreenSlidePageAdapter(this);
        viewPager2.setAdapter(pageAdapter);
        viewPager2.setPageTransformer(new DepthPageTransformer());
        setBottomNavigation();
        setViewPagerListener();
    }

    private void setViewPagerListener() {
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                int selectedPage = positionOfPages.get(position);
                bottomNavigationView.setSelectedItemId(selectedPage);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    private void setBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                for (Entry<Integer, Integer> entry : positionOfPages.entrySet()) {
                    if (entry.getValue() == item.getItemId()) {
                        viewPager2.setCurrentItem(entry.getKey());
                        return true;
                    }
                }
                return true;
            }
        });
    }

    private class ScreenSlidePageAdapter extends FragmentStateAdapter {
        public ScreenSlidePageAdapter(MainActivity mainActivity) {
            super(mainActivity);
        }

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


    @RequiresApi(21)
    public class DepthPageTransformer implements ViewPager2.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) {
                view.setAlpha(0f);

            } else if (position <= 0) {
                view.setAlpha(1f);
                view.setTranslationX(0f);
                view.setTranslationZ(0f);
                view.setScaleX(1f);
                view.setScaleY(1f);

            } else if (position <= 1) {
                view.setAlpha(1 - position);

                view.setTranslationX(pageWidth * -position);
                view.setTranslationZ(-1f);

                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else {
                view.setAlpha(0f);
            }
        }
    }
}