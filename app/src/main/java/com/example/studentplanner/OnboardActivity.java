package com.example.studentplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.Adapters.ViewPagerAdapter;

public class OnboardActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private Button btnLeft,btnRight;
    private ViewPagerAdapter adapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboard);
        init();


    }

    private void init() {
        viewPager=findViewById(R.id.view_pager);
        btnLeft=findViewById(R.id.btnLeft);
        btnRight=findViewById(R.id.btnRight);
        dotsLayout=findViewById(R.id.dotsLayout);
        adapter=new ViewPagerAdapter(this);
        addDots(0);
        viewPager.addOnPageChangeListener(listener);  //THIS LISTENER IS CREATED
        viewPager.setAdapter(adapter);

        btnRight.setOnClickListener(v->{
            //IF BUTTON TEXT IS NEXT WE GO TO THE NEXT VIEW PAGER PAGE
            if(btnRight.getText().toString().equals("NEXT")){
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
            }
            else{
                //ELSE IF ITS FINISHED WE OPEN AUTH ACTIVITY
                startActivity(new Intent(OnboardActivity.this,AuthActivity.class));
                finish();
            }
        });
btnLeft.setOnClickListener(v->{
    //IF BUTTON SKIP IS CLICKED THEN WE GO TO PAGE 3
    viewPager.setCurrentItem(viewPager.getCurrentItem()+2);
        }
);


    }
    //METHOD TO CREATE DOTS FROM HTML CODE
    private void addDots(int position){
        dotsLayout.removeAllViews();
        dots=new TextView[3];
        for(int i=0;i<dots.length;i++){
            dots[i]=new TextView(this);
            //EXACT HTML CODE THAT CREATES THE DOTS
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.colorLightGrey));
            dotsLayout.addView(dots[i]);
        }
        //CHANGING THE SELECTED DOTS COLOR
        if(dots.length>0){
            dots[position].setTextColor(getResources().getColor(R.color.colorGrey));
        }



    }

    private ViewPager.OnPageChangeListener listener =new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            //CHANGE THE TEXT OF THE NEXT BUTTON TO FINISH ONCE WE REACH PAGE THREE
            //HIDE SKIP BUTTON IF WE ARE NOT ON PAGE ONE
if(position==0){
    btnLeft.setVisibility(View.VISIBLE);
    btnLeft.setEnabled(true);
 btnRight.setText("NEXT");
}
else if(position==1){
    btnLeft.setVisibility(View.GONE);
    btnLeft.setEnabled(false);
    btnRight.setText("NEXT");
}
else{
    btnLeft.setVisibility(View.GONE);
    btnLeft.setEnabled(false);
    btnRight.setText("FINISH");
}
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


}
