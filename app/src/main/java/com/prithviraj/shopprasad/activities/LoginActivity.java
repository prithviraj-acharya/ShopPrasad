package com.prithviraj.shopprasad.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.prithviraj.myvollyimplementation.UtilityClasses.ApiErrorAction;
import com.prithviraj.myvollyimplementation.VolleyServiceCall;
import com.prithviraj.shopprasad.R;
import com.prithviraj.shopprasad.activities.customer.CustomerDashboard;
import com.prithviraj.shopprasad.activities.customer.RegistrationActivity;
import com.prithviraj.shopprasad.utils.ErrorDialog;
import com.prithviraj.shopprasad.utils.Url;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    TextView noAccount;
    Button signIn;

    EditText email,password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        setOnClickListeners();
    }

    private void setOnClickListeners() {

        noAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);

            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginApi();
            }
        });
    }

    private void init() {
        noAccount = findViewById(R.id.textView18);
        signIn = findViewById(R.id.button7);
        email = findViewById(R.id.editTextTextPersonName2);
        password = findViewById(R.id.editTextTextPersonName);



    }

    public void loginApi() {


        if (email.getText().toString().trim().equals("") || email.getText().toString().trim().length()<10 ) {

            new ErrorDialog("Please fill up the phone number to continue.", LoginActivity.this).showDialog();
        } else {

            Map<String, String> param = new HashMap<>();


            param.put("phone", email.getText().toString().trim());

            Log.d("zxcv: ", param.toString());

            final ProgressDialog dialog = ProgressDialog.show(LoginActivity.this, "",
                    "Loading. Please wait...", true);

            new VolleyServiceCall(Request.Method.POST, Url.LOGIN_URL, null, param, null, LoginActivity.this) {
                @Override
                public void onResponse(String s) {
                    dialog.cancel();

                    Log.d("zxcv", s);

                    try {
                        JSONObject jsonObject = new JSONObject(s);

                        if(jsonObject.getBoolean("success")){

                            Intent in = new Intent(LoginActivity.this, OtpActivity.class);
                            in.putExtra("userId",jsonObject.getJSONObject("data").getInt("id"));
                            startActivity(in);
                        }else {

                            new ErrorDialog(jsonObject.getString("msg"), LoginActivity.this).showDialog();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onError(VolleyError error, String errorMessage) {
                    dialog.cancel();
                    Log.d("zxcv ", errorMessage);
                    ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, LoginActivity.this) {
                        @Override
                        public void setAction(boolean action) {
                            if (action)
                                loginApi();
                        }
                    };
                    apiErrorAction.createDialog();
                }
            }.start();
        }
    }


}
