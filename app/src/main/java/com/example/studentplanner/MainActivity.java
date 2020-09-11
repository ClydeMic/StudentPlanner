package com.example.studentplanner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //PAUSE THE APP FOR 1.5 SECS THEN ANYTHING IN THE RUN METHOD WILL RUN
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                SharedPreferences userPref = getApplication().getSharedPreferences("user",Context.MODE_PRIVATE);
                boolean isLoggedIn= userPref.getBoolean("isLoggedIn",false);

                if(isLoggedIn){
                    startActivity(new Intent(MainActivity.this,HomeActivity.class));
                    finish();
                }

             else{
                    isFirstTime();

                }
            }
        }, 1500);
    }

    private void isFirstTime() {
        //CHECK IF THE APP IS RUNNING FOR THE VERY FIRST TIME
        //WE NEED TO SAVE A VALUE TO SHARED PREFERENCES
        SharedPreferences preferences=getApplication().getSharedPreferences("onBoard", Context.MODE_PRIVATE);
        boolean isFirstTime=preferences.getBoolean("isFirstTime",true);
        //DEFAULT VALUE IS TRUE
        if(isFirstTime){
            //IF ITS TRUE THEN ITS THE FIRST TIME AND WE WILL CHANGE IT TO FALSE
            SharedPreferences.Editor editor=preferences.edit();
            editor.putBoolean("isFirstTime",false);
            editor.apply();

            //START ONBOARD ACTIVITY
            startActivity(new Intent(MainActivity.this,OnboardActivity.class));
            finish();
        }
        else{
            //START AUTH ACTIVITY
            startActivity(new Intent(MainActivity.this,AuthActivity.class));
            finish();
        }
    }
}