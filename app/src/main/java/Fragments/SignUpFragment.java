package Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.studentplanner.AuthActivity;
import com.example.studentplanner.Constant;
import com.example.studentplanner.HomeActivity;
import com.example.studentplanner.R;
import com.example.studentplanner.UserInfoActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpFragment extends Fragment {

    private View view;
    private TextInputEditText txtEmail, txtPassword, txtConfirm ;
    private TextInputLayout layoutEmail, layoutPassword, layoutConfirm;
    private TextView txtSignIn;
    Button btnSignUp;
    private ProgressDialog dialog;

    public SignUpFragment(){}
    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.layout_sign_up, container, false);
        init();

        return view;
    }

    private void init() {
        layoutPassword = view.findViewById(R.id.txtLayoutPasswordSignUp);
        layoutEmail = view.findViewById(R.id.txtLayoutEmailSignUp);
        layoutConfirm = view.findViewById(R.id.txtLayoutConfirmSignUp);
        txtPassword = view.findViewById(R.id.txtPasswordSignUp);
        txtConfirm = view.findViewById(R.id.txtConfirmSignUp);
        txtSignIn = view.findViewById(R.id.txtSignIn);
        txtEmail = view.findViewById(R.id.txtEmailSignUp);
        btnSignUp = view.findViewById(R.id.btnSignUp);
        dialog=new ProgressDialog(getContext());
        dialog.setCancelable(false);


        txtSignIn.setOnClickListener(v -> {
            //change fragments
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameAuthContainer, new SignInFragment()).commit();



        });
        btnSignUp.setOnClickListener(v -> {
            //validate fields first
            if (validate()) {
                register();

            }

        });

        txtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!txtEmail.getText().toString().isEmpty()){
                    layoutEmail.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txtPassword.getText().toString().length()>7){
                    layoutPassword.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(txtConfirm.getText().toString().equals(txtPassword.getText().toString())){
                    layoutConfirm.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private boolean validate() {
        if (txtEmail.getText().toString().isEmpty()) {
            layoutEmail.setErrorEnabled(true);
            layoutEmail.setError("Email is required");
            return false;
        }

        if (txtPassword.getText().toString().length() < 8) {
            layoutPassword.setErrorEnabled(true);
            layoutPassword.setError("Password should be at least 8 characters");
            return false;
        }
        if (!txtConfirm.getText().toString().equals(txtPassword.getText().toString())) {
            layoutConfirm.setErrorEnabled(true);
            layoutConfirm.setError("Password does not match");
            return false;
        }
        return true;
    }

    private void register (){

        dialog.setMessage("Registering...");
        dialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, Constant.REGISTER, response->{
//WE GET A RESPONSE IF CONNECTION IS SUCCESSFUL
            try{
                JSONObject object =new JSONObject(response);
                if(object.getBoolean("success")){
                    JSONObject user=object.getJSONObject("user");
                    //MAKE SHARED PREFERENCES USER
                    SharedPreferences userPref = getActivity().getApplicationContext().getSharedPreferences("user",getContext().MODE_PRIVATE);
                    SharedPreferences.Editor editor =userPref.edit();
                    editor.putString("token",object.getString("token"));
                    editor.putString("name",user.getString("name"));
                    editor.putString("campus",user.getString("campus"));
                    editor.putString("photo",user.getString("photo"));
                    editor.putString("course",user.getString("course"));
                    editor.putString("YOS",user.getString("YOS"));
                    editor.putString("interests",user.getString("interests"));
                    editor.putBoolean("isLoggedIn",true);
                    editor.apply();

                    //IF SUCCESS

                    startActivity(new Intent(((AuthActivity)getContext()), UserInfoActivity.class));
                    ((AuthActivity) getContext()).finish();


                    Toast.makeText(getContext(),"Successfully Registered!!",Toast.LENGTH_SHORT).show();




                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
dialog.dismiss();
        },error->{
            //WE GET AN ERROR IF CONNECTION IS NOT SUCCESSFUL
            error.printStackTrace();
            dialog.dismiss();
        }){
            //ADD PARAMETERS
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map=new HashMap<>();
                map.put("email",txtEmail.getText().toString().trim());
                map.put("password",txtPassword.getText().toString());
                return map;
            }


        };
        //THIS REQUEST TO REQUEST QUEUE
        RequestQueue queue= Volley.newRequestQueue(getContext());
        queue.add(request);
    }

}

