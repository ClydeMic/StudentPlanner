package com.example.studentplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    private Fragment fragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        View navHost = findViewById(R.id.fragment);
        NavHostFragment navHostFragment  = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        fragmentManager = getSupportFragmentManager();
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.getNavController());


//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                Fragment selectedFragment = null;
//                int id = item.getItemId();
//                switch (id){
//                    case R.id.dashboardFragment:
//                        fragment = new DashboardFragment();
//                        break;
//                    case R.id.timetableFragment:
//                        fragment = new TimetableFragment();
//                        break;
//                    case R.id.journalFragment:
//                        fragment = new JournalFragment();
//                        break;
//                    case R.id.eventsFragment:
//                        fragment = new EventsFragment();
//                        break;
//                }
//
//                final FragmentTransaction transaction = fragmentManager.beginTransaction();
//                transaction.replace(R.id.fragment);
//                return false;
//
//            }
//        });
    }
}
