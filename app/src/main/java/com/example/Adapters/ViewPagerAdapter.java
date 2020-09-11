package com.example.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.studentplanner.R;

public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater inflater;

    public ViewPagerAdapter(Context context) {
        this.context = context;
    }

    private int images []={
            R.drawable.timetablee,
            R.drawable.journal,
            R.drawable.events,
    };

    private String titles[]={
      "Planning & Co-ordination",
            "Journaling",
            "Friends & Events"
    };

    private String desc[]={
            "Plan all Your Activities",
            "write and share your daily experiences",
            "Be Up-to date with all the latest Campus Events"
    };

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(LinearLayout)object;
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container,int position){
        inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View v=inflater.inflate(R.layout.view_pager,container,false);

        //INITIATE VIEWS
        ImageView imageView=v.findViewById(R.id.imgViewPager);
        TextView txtTitle=v.findViewById(R.id.txtTitleViewPager);
        TextView txtDesc=v.findViewById(R.id.txtDescViewPager);

        imageView.setImageResource(images[position]);
        txtTitle.setText(titles[position]);
        txtDesc.setText(desc[position]);

        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
