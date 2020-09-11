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
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoActivity extends AppCompatActivity {
    private static final String TAG = "info";
    private TextInputLayout layoutName, layoutCourse,layoutCampus, layoutYOS,layoutInterests;
    private TextInputEditText txtName, txtCourse,txtCampus, txtYOS,txtInterests;
    private TextView txtSelectPhoto;
    private Button btnContinue;
    private CircleImageView circleImageView;
    private static final int GALLERY_ADD_PROFILE=1;
    private Bitmap bitmap =null;
    private SharedPreferences userPref;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        init();

    }

    private void init() {

        dialog=new ProgressDialog(this);
        dialog.setCancelable(false);

        userPref=getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        layoutName=findViewById(R.id.txtLayoutNameUserInfo);
        layoutCourse=findViewById(R.id.txtLayoutCourseUserInfo);
        layoutCampus=findViewById(R.id.txtLayoutCampusUserInfo);
        layoutYOS=findViewById(R.id.txtLayoutYOSUserInfo);
        layoutInterests=findViewById(R.id.txtLayoutInterestsUserInfo);
        txtName=findViewById(R.id.txtNameUserInfo);
        txtCourse=findViewById(R.id.txtCourseUserInfo);
        txtCampus=findViewById(R.id.txtCampusUserInfo);
        txtYOS=findViewById(R.id.txtYOSUserInfo);
        txtInterests=findViewById(R.id.txtInterestsUserInfo);
        txtSelectPhoto=findViewById(R.id.txtSelectPhoto);
        btnContinue=findViewById(R.id.btnContinue);
        circleImageView=findViewById(R.id.imgUserInfo);

        //PICK A PHOTO FROM GALLERY

        txtSelectPhoto.setOnClickListener(v->{
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            startActivityForResult(i,GALLERY_ADD_PROFILE);
        });

btnContinue.setOnClickListener(v->{
    //VALIDATE THE FIELDS
    if(validate()){
saveUserInfo();
    }
});


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_ADD_PROFILE && resultCode==RESULT_OK){
            Uri imgUri=data.getData();
            circleImageView.setImageURI(imgUri);

            try {
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),imgUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }



    private boolean validate(){
        if (txtName.getText().toString().isEmpty()) {
            layoutName.setErrorEnabled(true);
            layoutName.setError("Name is required");
            return false;
        }

        if (txtCourse.getText().toString().isEmpty()) {
            layoutCourse.setErrorEnabled(true);
            layoutCourse.setError("Student Course is required");
            return false;
        }

        if (txtCampus.getText().toString().isEmpty()) {
            layoutCampus.setErrorEnabled(true);
            layoutCampus.setError("Student Campus is required");
            return false;
        }
        if (txtYOS.getText().toString().isEmpty()) {
            layoutYOS.setErrorEnabled(true);
            layoutYOS.setError("Enter your year of study");
            return false;
        }

        if (txtInterests.getText().toString().isEmpty()) {
            layoutInterests.setErrorEnabled(true);
            layoutInterests.setError("Give at least one of your interests i.e Rugby");
            return false;
        }

        return true;
    }

    public void saveUserInfo() {

        dialog.setMessage("Saving...");
        dialog.show();
        String name = txtName.getText().toString().trim();
        String course = txtCourse.getText().toString().trim();
        String campus = txtCampus.getText().toString().trim();
        String yos = txtYOS.getText().toString().trim();
        String interests = txtInterests.getText().toString().trim();

        StringRequest request = new StringRequest(Request.Method.POST, Constant.SAVE_USER_INFO, response -> {

            try {
                Log.e(TAG, "saveUserInfo: "+response );
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")) {
                    SharedPreferences.Editor editor = userPref.edit();
                    editor.putString("photo", object.getString("photo"));
                    editor.apply();
                    startActivity(new Intent(UserInfoActivity.this, HomeActivity.class));
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "saveUserInfo2: "+response );
            }


        }, error -> {
            error.printStackTrace();
            dialog.dismiss();
            Log.e(TAG, "saveUserInfo: "+error );
        }) {

            //ADD TOKEN TO HEADERS
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = userPref.getString("token", "");
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Bearer " + token);
                return map;






            }

            //ADD PARAMS


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("name", name);
                map.put("course", course);
                map.put("campus", campus);
                map.put("yos", yos);
                map.put("interests", interests);
                map.put("photo", bitmapToString(bitmap));
                return map;
            }

        };
RequestQueue queue =Volley.newRequestQueue(UserInfoActivity.this);
queue.add(request);
    }


            private String bitmapToString(Bitmap bitmap) {
                if(bitmap!=null){
                    ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                    byte[]array=byteArrayOutputStream.toByteArray();
                    return Base64.encodeToString(array, Base64.DEFAULT);
                }

                return "";
            }
        }







