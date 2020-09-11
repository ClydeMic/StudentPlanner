package com.example.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.studentplanner.CommentActivity;
import com.example.studentplanner.Constant;
import com.example.studentplanner.EditPostActivity;
import com.example.studentplanner.HomeActivity;
import com.example.Models.Posts;
import com.example.studentplanner.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsHolder> {

    private Context context;
    private ArrayList<Posts> list;
    private ArrayList<Posts> listAll;
    private SharedPreferences preferences;

    public PostsAdapter(Context context, ArrayList<Posts> list) {
        this.context = context;
        this.list = list;
        this.listAll = new ArrayList<>(list);
        preferences = context.getApplicationContext().getSharedPreferences("user",Context.MODE_PRIVATE);

    }

    @NonNull
    @Override
    public PostsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_post,parent,false);
       return new PostsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsHolder holder, int position) {
        Posts post = list.get(position);
        Picasso.get().load(Constant.URL+"storage/profiles/"+post.getPhoto()).into(holder.imgProfile);
        Picasso.get().load(Constant.URL+"storage/posts/"+post.getUser().getUserName());
        holder.txtName.setText(post.getUser().getUserName());
        holder.txtComments.setText("View all "+post.getComments());
        holder.txtLikes.setText(post.getLike()+"Attendees");
        holder.txtDate.setText(post.getDate());
        holder.txtDesc.setText(post.getDesc());

        holder.btnLike.setImageResource(
                post.isSelflike()?R.drawable.ic_confirm:R.drawable.ic_check
        );

        holder.btnLike.setOnClickListener(view->{
            holder.btnLike.setImageResource(
                    post.isSelflike()?R.drawable.ic_check:R.drawable.ic_confirm
            );

            StringRequest request = new StringRequest(Request.Method.POST,Constant.LIKE_POST,response -> {

                Posts myPost = list.get(position);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("success")) {
                              myPost.setSelflike((!post.isSelflike()));
                              myPost.setLike(myPost.isSelflike()?post.getLike() + 1:post.getLike()-1);
                              list.set(position,myPost);
                              notifyItemChanged(position);
                              notifyDataSetChanged();


                    }else{
                        holder.btnLike.setImageResource(
                                post.isSelflike()?R.drawable.ic_confirm:R.drawable.ic_check
                        );
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }

            },error -> {
                  error.printStackTrace();
            }){
                //add tokens


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    String token = preferences.getString("token" , "");
                    HashMap <String,String> map = new HashMap<>();
                    map.put("Authorization","Bearer " + token);
                    return map;
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    HashMap <String,String> map = new HashMap<>();
                    map.put("id",post.getId() + "");
                    return  map;
                }
            };

            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(request);

        });

        if (post.getUser().getId()==preferences.getInt("id",0)){
            holder.btnPostOption.setVisibility(View.VISIBLE);
        }else {
            holder.btnPostOption.setVisibility(View.GONE);
        }

        holder.btnPostOption.setOnClickListener(view->{
            PopupMenu popupMenu = new PopupMenu(context,holder.btnPostOption);
            popupMenu.inflate(R.menu.menu_post_options);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.item_edit:{
                               Intent i = new Intent(((HomeActivity)context), EditPostActivity.class);
                               i.putExtra("position",position);
                               i.putExtra("text",post.getDesc());
                               context.startActivity(i);
                               return true;

                        }
                        case R.id.item_delete: {
                           deletePost(post.getId(),position);
                           return true;
                        }

                    }
                    return false;
                }
            });
            popupMenu.show();
        });

        holder.txtComments.setOnClickListener(view->{
            Intent i = new Intent(((HomeActivity)context),CommentActivity.class);
            i.putExtra("postId",post.getId());
            i.putExtra("postPosition",position);
            context.startActivity(i);

        });

        holder.btnComment.setOnClickListener(view->{
            Intent i = new Intent(((HomeActivity)context), CommentActivity.class);
            i.putExtra("postId",post.getId());
            i.putExtra("postPosition",position);
            context.startActivity(i);

        });
    }

    private void deletePost(int postId,int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm");
        builder.setMessage("Delete posts");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                StringRequest request = new StringRequest(Request.Method.POST,Constant.DELETE_POST,response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getBoolean("success")) {
                            list.remove(position);
                            notifyItemRemoved(position);
                            notifyDataSetChanged();
                            listAll.clear();
                            listAll.addAll(list);
                        }
                    }catch (JSONException e) {
                         e.printStackTrace();
                    }
                    },error -> {

                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        String token = preferences.getString("token","");
                        HashMap <String,String> map = new HashMap<>();
                        map.put("Authorization","Bearer " + token);
                        return map;
                    }

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap <String,String> map = new HashMap<>();
                        map.put("id",postId + "");
                        return  map;
                    }
                };

                RequestQueue queue = Volley.newRequestQueue(context);
                queue.add(request);
            }
        });
        builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Posts> filteredList = new ArrayList<>();
            if(constraint.toString().isEmpty()){
                filteredList.addAll(listAll);
            }else{
                for(Posts post : listAll){
                    if(post.getDesc().toLowerCase().contains(constraint.toString().toLowerCase()) || post.getUser().getUserName().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filteredList.add(post);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return  results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
           list.clear();
           list.addAll((Collection<? extends Posts>)results.values);
           notifyDataSetChanged();
        }
    };

    public  Filter getFilter(){
        return filter;
    }
    class PostsHolder extends RecyclerView.ViewHolder{

        private TextView txtName,txtDate,txtDesc,txtLikes, txtComments;
        private CircleImageView imgProfile;
        private ImageView imgPost;
        private ImageButton btnPostOption,btnLike,btnComment;

        public PostsHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtPostName);
            txtDate = itemView.findViewById(R.id.txtPostDate);
            txtDesc = itemView.findViewById(R.id.txtPostDesc);
            txtLikes = itemView.findViewById(R.id.txtPostLikes);
            txtComments = itemView.findViewById(R.id.txtPostComments);
            imgProfile =itemView.findViewById(R.id.imgPostProfile);
            imgPost =itemView.findViewById(R.id.imgPosPhoto);
            btnPostOption=itemView.findViewById(R.id.btnPostOption);
            btnLike=itemView.findViewById(R.id.btnPostLike);
            btnComment=itemView.findViewById(R.id.btnPostComment);

            btnPostOption.setVisibility(View.GONE);
        }
    }
}
