package com.example.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentplanner.Constant;
import com.example.Models.Posts;
import com.example.studentplanner.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AccountPostAdapter extends RecyclerView.Adapter<AccountPostAdapter.AccountPostHolder> {
    private Context context;
    private ArrayList<Posts> arrayList;

    public AccountPostAdapter(Context context, ArrayList<Posts> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public AccountPostAdapter.AccountPostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_account_post,parent,false);
        return new AccountPostHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountPostAdapter.AccountPostHolder holder, int position) {
        Picasso.get().load(arrayList.get(position).getPhoto()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class AccountPostHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;

        public AccountPostHolder(View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.imgAccountPost);
        }
    }
}
