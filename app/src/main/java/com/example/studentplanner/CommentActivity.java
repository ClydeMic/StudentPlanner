package com.example.studentplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Adapters.CommentsAdapter;
import com.example.studentplanner.HomeFragment;
import com.example.Models.Comment;
import com.example.Models.Posts;
import com.example.Models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Comment> list;
    private CommentsAdapter adapter;
    private int postId = 0 ;
    public static int postPosition = 0;
    private SharedPreferences preferences;
    private EditText txtAddComment;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        init();
    }

    private void init() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        postPosition = getIntent().getIntExtra("postPosition",-1);
        preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        recyclerView = findViewById(R.id.recyclerComments);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        txtAddComment = findViewById(R.id.txtAddComment);
        postId = getIntent().getIntExtra("postId",0);
        
        getComments();

    }

    private void getComments() {
        list = new ArrayList<>();

        StringRequest request = new StringRequest(Request.Method.POST,Constant.COMMENTS,response -> {

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getBoolean("success")) {
                    JSONArray comments = new JSONArray(jsonObject.getString("comments"));
                    for(int i = 0; i < comments.length(); i++){
                         JSONObject comment = comments.getJSONObject(i);
                        JSONObject user = comment.getJSONObject("user");

                        User myUser = new User();
                        myUser.setId(user.getInt("id"));
                        myUser.setPhoto(Constant.URL+ "storage/profiles/" + user.getString("photo"));
                        myUser.setUserName(user.getString("name") + " " +user.getString("lastname"));

                        Comment myComment = new Comment();
                        myComment.setId(comment.getInt("id"));
                        myComment.setUser(myUser);
                        myComment.setDate(comment.getString("created_at"));
                        myComment.setComment(comment.getString("comment"));
                        list.add(myComment);

                    }
                }

                adapter = new CommentsAdapter(this,list);
                recyclerView.setAdapter(adapter);

            }catch (JSONException e) {
                e.printStackTrace();
            }

        },error -> {
            error.printStackTrace();
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = preferences.getString("token","");
                HashMap<String,String> map = new HashMap<>();
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

        RequestQueue queue = Volley.newRequestQueue(CommentActivity.this);
        queue.add(request);
    }

    public void goBack(View view) {
        super.onBackPressed();
    }


    public void addComment(View view) {
        String commentText = txtAddComment.getText().toString();
        progressDialog.setMessage("Adding comment");
        progressDialog.show();

        if (commentText.length()>0){
            StringRequest request = new StringRequest(Request.Method.POST,Constant.CREATE_COMMENT,response -> {

                try {
                    JSONObject object = new JSONObject(response);
                    if(object.getBoolean("success")){
                        JSONObject comment = object.getJSONObject("comment");
                        JSONObject user = comment.getJSONObject("user");

                        Comment comment1 = new Comment();
                        User user1 = new User();
                        user1.setId(user.getInt("id"));
                        user1.setUserName(user.getString("name")+" " + user.getString("lastname"));
                        user1.setPhoto(Constant.URL+ "storage/profiles/" + user.getString("photo"));
                        comment1.setUser(user1);
                        user1.setId(comment.getInt("id"));
                        comment1.setDate(comment.getString("created_at"));
                        comment1.setComment(comment.getString("comment"));

                        Posts post = HomeFragment.arrayList.get(postPosition);
                        post.setComments(post.getComments()+1);
                        HomeFragment.recyclerView.getAdapter().notifyDataSetChanged();

                        list.add(comment1);
                        recyclerView.getAdapter().notifyDataSetChanged();
                        txtAddComment.setText(" ");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            },error->{
               error.printStackTrace();
               progressDialog.dismiss();
            }){
                //add token to header

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    String token = preferences.getString("token","");
                    HashMap<String,String> map = new HashMap<>();
                    map.put("Authorization","Bearer " + token);
                    return map;
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap <String,String> map = new HashMap<>();
                    map.put("id",postId + "");
                    map.put("comment",commentText);
                    return map;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(CommentActivity.this);
            queue.add(request);
        }
    }
}
