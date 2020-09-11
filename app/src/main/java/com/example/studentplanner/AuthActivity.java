package com.example.studentplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import Fragments.SignInFragment;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth2);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameAuthContainer,new SignInFragment()).commit();
    }
}