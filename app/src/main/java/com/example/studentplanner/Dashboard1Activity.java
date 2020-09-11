package com.example.studentplanner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Dashboard1Activity extends AppCompatActivity {
    TextView tvSkip, tvNext;
    ImageView backArrow;

    @RequiresApi(api = Build.VERSION_CODES.M)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard1);

        //Buttons yet to be implemented
//        backArrow=(ImageView) findViewById(R.id.iv_barrow);
//        tvSkip=(TextView) findViewById(R.id.tv_skip);
//        tvNext=(TextView) findViewById(R.id.tv_next);



        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(Dashboard1Activity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}
