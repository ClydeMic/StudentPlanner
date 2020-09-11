package com.example.studentplanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import HomeFragment;
import com.example.Models.Posts;
import com.example.Models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {
    private Button btnPost;
    private ImageView imgPost;
    private EditText txtDesc;
    private Bitmap bitmap = null;
    private  static  final int GALLERY_CHANGE_POST = 1;
    private ProgressDialog dialog;
    private SharedPreferences preferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        init();

    }

    private void init(){
        preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        btnPost = findViewById(R.id.btnAddPost);
        imgPost = findViewById(R.id.imgAddPost);
        txtDesc = findViewById(R.id.txtDescAddPost);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        imgPost.setImageURI(getIntent().getData());
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),getIntent().getData());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void post(){
        dialog.setMessage("Posting");
        dialog.show();

        StringRequest request = new StringRequest(Request.Method.POST,Constant.ADD_POST,response -> {

            try {
                JSONObject object = new JSONObject(response);
                if(object.getBoolean("success")){
                    JSONObject postObject = object.getJSONObject("post");
                    JSONObject userObject = postObject.getJSONObject("user");

                    User user = new User();
                    user.setId(userObject.getInt("id"));
                    user.setUserName(userObject.getString("name")+ " " + userObject.getString("lastname") );
                    user.setPhoto(userObject.getString("photo"));

                    Posts post = new Posts();
                    post.setUser(user);
                    post.setId(postObject.getInt("id"));
                    post.setSelflike(false);
                    post.setPhoto(postObject.getString("photo"));
                    post.setDesc(postObject.getString("desc"));
                    post.setComments(0);
                    post.setLike(0);
                    post.setDate(postObject.getString("created_at"));

                    com.example.studentplanner.HomeFragment.arrayList.add(0,post);
                    com.example.studentplanner.HomeFragment.recyclerView.getAdapter().notifyItemInserted(0);
                    com.example.studentplanner.HomeFragment.recyclerView.getAdapter().notifyDataSetChanged();
                    Toast.makeText(this,"Posted",Toast.LENGTH_SHORT).show();
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialog.dismiss();
        },error -> {
            error.printStackTrace();
            dialog.dismiss();

        }){
            //add token to header

           public Map <String,String> getHeaders() throws AuthFailureError{
               String token = preferences.getString("token"," ");
               HashMap<String,String> map = new HashMap<>();
               map.put("Authentication","Bearer " + token);
               return map;
           }

           //add params
           protected  Map <String,String> getParams() throws AuthFailureError {
               HashMap<String,String> map = new HashMap<>();
               map.put("desc",txtDesc.getText().toString().trim());
               map.put("photo",bitmapToString(bitmap));
               return map;
           }
        };

        RequestQueue queue = Volley.newRequestQueue(AddPostActivity.this);
        queue.add(request);
    }

    private String bitmapToString(Bitmap bitmap){
        if(bitmap!=null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            byte [] array = byteArrayOutputStream.toByteArray();
            return android.util.Base64.encodeToString(array, Base64.DEFAULT);
        }

        return "";
    }
    public void cancelPost(View view) {
        super.onBackPressed();

    }

    public void changePhoto(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALLERY_CHANGE_POST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_CHANGE_POST && resultCode==RESULT_OK) {
            Uri imgUri = data.getData();
            imgPost.setImageURI(imgUri);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imgUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            btnPost.setOnClickListener(view->{
               if(!txtDesc.getText().toString().isEmpty()){
                   post();
               }else{
                   Toast.makeText(this,"Post description is required",Toast.LENGTH_SHORT).show();
               }
            });
        }


    }
}
