package com.example.lifestyle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.util.Map.Entry;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.lifestyle.fragment.FriendsFragment;
import com.example.lifestyle.fragment.HomeFragment;
import com.example.lifestyle.fragment.ProfileFragment;
import com.example.lifestyle.fragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private static final int NUM_PAGES = 4;
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
        positionOfPages.put(2, R.id.action_friends);
        positionOfPages.put(3, R.id.action_profile);
        fragments.put(0, new HomeFragment());
        fragments.put(1, new SearchFragment());
        fragments.put(2, new FriendsFragment());
        fragments.put(3, new ProfileFragment());
        viewPager2 = findViewById(R.id.VP);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        pageAdapter = new ScreenSlidePageAdapter(this);
        viewPager2.setAdapter(pageAdapter);
        viewPager2.setPageTransformer(new ZoomOutPageTransformer());
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

    private class ZoomOutPageTransformer implements ViewPager2.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();
            if (position < -1) {
                view.setAlpha(0f);
            } else if (position <= 1) {
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
                view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
            } else {
                view.setAlpha(0f);
            }
        }
    }
}