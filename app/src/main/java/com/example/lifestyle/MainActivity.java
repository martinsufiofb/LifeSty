package com.example.lifestyle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    int currentFragment = 0;
    int newFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.side_navigation);


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_home:
                        fragment = new HomeFragment();
                        newFragment = 0;
                        break;
                    case R.id.action_search:
                        fragment = new SearchFragment();
                        newFragment = 1;
                        break;
                    case R.id.action_friends:
                        fragment = new FriendsFragment();
                        newFragment = 2;
                        break;
                    case R.id.action_profile:
                        fragment = new ProfileFragment();
                        newFragment = 3;
                        break;
                    default:
                        fragment = new ProfileFragment();
                        break;
                }
                if(newFragment>currentFragment){
                    //slide right
                    fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_left_enter, R.anim.slide_left_exit, R.anim.slide_right_enter, R.anim.slide_right_exit).replace(R.id.flContainer, fragment).commit();
                    currentFragment = newFragment;

                }else if (currentFragment>newFragment){
                    //slide left
                    fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_right_enter, R.anim.slide_right_exit, R.anim.slide_left_enter, R.anim.slide_left_exit).replace(R.id.flContainer, fragment).commit();
                    currentFragment = newFragment;
                }else{
                    fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                }
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.action_home);



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }


        return true;
    }



}