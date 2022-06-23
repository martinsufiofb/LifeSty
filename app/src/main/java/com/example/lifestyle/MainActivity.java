package com.example.lifestyle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.lifestyle.fragment.FriendsFragment;
import com.example.lifestyle.fragment.HomeFragment;
import com.example.lifestyle.fragment.ProfileFragment;
import com.example.lifestyle.fragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigationView;
    int currentFragment = 0;
    int newFragment;
    private static final int NUM_PAGES=4;
    private ViewPager2 viewPager2;
    private FragmentStateAdapter pageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager2 = findViewById(R.id.VP);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        pageAdapter = new ScreenSlidePageAdapter(this);
        viewPager2.setAdapter(pageAdapter);
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
                if(item.getItemId()==R.id.action_home){
                    viewPager2.setCurrentItem(0);
                }
                if(item.getItemId()==R.id.action_search){
                    viewPager2.setCurrentItem(1);
                }
                if(item.getItemId()==R.id.action_friends){
                    viewPager2.setCurrentItem(2);
                }
                if(item.getItemId()==R.id.action_profile){
                    viewPager2.setCurrentItem(3);
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




}