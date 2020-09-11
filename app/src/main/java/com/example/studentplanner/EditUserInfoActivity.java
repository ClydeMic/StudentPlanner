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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditUserInfoActivity extends AppCompatActivity {

    private TextInputLayout layoutName, layoutLastName;
    private TextInputEditText txtName, txtLastName;
    private TextView txtSelectPhoto;
    private Button btnSaveEdit;
    private CircleImageView circleImageView;
    private static final int GALLERY_CHANGE_PROFILE = 1;
    private Bitmap bitmap = null;
    private SharedPreferences userPref;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        init();
    }

    private void init(){
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        layoutLastName = findViewById(R.id.txtEditLayoutLastNameUserInfo);
        layoutName = findViewById(R.id.txtEditLayoutNameUserInfo);
        txtLastName = findViewById(R.id.txtEditLastNameUserInfo);
        txtLastName = findViewById(R.id.txtEditNameUserInfo);
        txtSelectPhoto = findViewById(R.id.txtEditSelectPhoto);
        btnSaveEdit= findViewById(R.id.btnEditSaveContinue);
        circleImageView = findViewById(R.id.imgEditUserInfo);

        Picasso.get().load(getIntent().getStringExtra("imgUrl")).into(circleImageView);
        txtName.setText(userPref.getString("name",""));
        txtName.setText(userPref.getString("lastname",""));

        txtSelectPhoto.setOnClickListener(view -> {
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("images/*");
            startActivityForResult(i,GALLERY_CHANGE_PROFILE );
        });

        btnSaveEdit.setOnClickListener(view -> {
            //validate fields
            if (validate()) {
                updateProfile();
            }
        });

    }

    private  void updateProfile(){
        dialog.setMessage("Updating");
        dialog.show();

        StringRequest request = new StringRequest(Request.Method.POST,Constant.SAVE_USER_INFO,response -> {

            try {
                JSONObject jsonObject = new JSONObject(response);
                if(jsonObject.getBoolean("success")){
                    SharedPreferences.Editor editor = userPref.edit();
                    editor.putString("name",txtName.getText().toString().trim());
                    editor.putString("lastname",txtLastName.getText().toString().trim());
                    editor.apply();
                    Toast.makeText(this,"Profile Updated",Toast.LENGTH_SHORT).show();
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialog.dismiss();
        },error->{
            error.printStackTrace();
            dialog.dismiss();
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = userPref.getString("token","");
                HashMap<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer" + token);
                return  map;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("name",txtName.getText().toString().trim());
                map.put("lastname",txtLastName.getText().toString().trim());
                map.put("photo",bitmapToString(bitmap));
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(EditUserInfoActivity.this);
        queue.add(request);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CHANGE_PROFILE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            circleImageView.setImageURI(uri);

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private boolean validate(){

        if(txtName.getText().toString().isEmpty()){
            layoutName.setErrorEnabled(true);
            layoutName.setError("Name is Required");
            return false;
        }
        if(txtLastName.getText().toString().isEmpty()){
            layoutLastName.setErrorEnabled(true);
            layoutLastName.setError("LastName is Required");
            return false;
        }
        return true;

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
}
