package com.example.lifestyle;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;

import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import android.view.MenuItem;

import android.view.View;


import com.example.lifestyle.fragment.FriendsFragment;
import com.example.lifestyle.fragment.HomeFragment;
import com.example.lifestyle.fragment.ProfileFragment;
import com.example.lifestyle.fragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private static final int NUM_PAGES=4;
    private ViewPager2 viewPager2;
    private FragmentStateAdapter pageAdapter;
    boolean home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        home =false;
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
                if(position==0){
                    bottomNavigationView.setSelectedItemId(R.id.action_home);
                }
                if(position==1){
                    bottomNavigationView.setSelectedItemId(R.id.action_search);
                }
                if(position==2){
                    bottomNavigationView.setSelectedItemId(R.id.action_friends);
                }
                if(position==3){
                    bottomNavigationView.setSelectedItemId(R.id.action_profile);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                super.onPageScrollStateChanged(state);
            }
        });
    }


    private void setBottomNavigation() {

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId()==R.id.action_home && home!=true ){
                    viewPager2.setCurrentItem(0);
                    home = true;
                    return true;
                }
                if(item.getItemId()==R.id.action_search){
                    viewPager2.setCurrentItem(1);
                    home = false;
                }
                if(item.getItemId()==R.id.action_friends){
                    viewPager2.setCurrentItem(2);
                    home = false;
                }
                if(item.getItemId()==R.id.action_profile){
                    viewPager2.setCurrentItem(3);
                    home = false;
                }
                if(home==true){
                    HomeFragment.rvExercises.smoothScrollToPosition(0);

                }
                return true;
            }


        });
    }


    private class ScreenSlidePageAdapter extends FragmentStateAdapter {
        public ScreenSlidePageAdapter(MainActivity mainActivity) {super(mainActivity);}


        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Fragment fragment;
            switch (position) {
                case 0:
                    fragment = new HomeFragment();
                    break;
                case 1:
                    fragment = new SearchFragment();
                    break;
                case 2:
                    fragment = new FriendsFragment();
                    break;
                case 3:
                    fragment = new ProfileFragment();
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


    private class ZoomOutPageTransformer implements ViewPager2.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }
        }
    }
}