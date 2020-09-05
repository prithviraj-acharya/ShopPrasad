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

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.prithviraj.myvollyimplementation.UtilityClasses.ApiErrorAction;
import com.prithviraj.myvollyimplementation.VolleyServiceCall;
import com.prithviraj.shopprasad.R;
import com.prithviraj.shopprasad.activities.customer.CustomerDashboard;
import com.prithviraj.shopprasad.activities.customer.RegistrationActivity;
import com.prithviraj.shopprasad.activities.panditji.PanditDashboardActivity;
import com.prithviraj.shopprasad.utils.ErrorDialog;
import com.prithviraj.shopprasad.utils.SharedPreference;
import com.prithviraj.shopprasad.utils.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OtpActivity extends AppCompatActivity {

    Button signIn;
    EditText otp;
    TextView resendOtp;
    SharedPreference sharedPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);


        init();
        setOnClickListeners();
    }

    private void setOnClickListeners() {

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOtp();
            }
        });

        resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendOtp();
            }
        });
    }

    private void init() {

        sharedPreference = new SharedPreference(OtpActivity.this);

        signIn = findViewById(R.id.button7);
        otp = findViewById(R.id.editTextTextPersonName2);
        resendOtp = findViewById(R.id.textView22);

    }

    public void verifyOtp() {


        if (otp.getText().toString().trim().equals("") || otp.getText().toString().trim().length()<4 ) {

            new ErrorDialog("Please enter a valid OTP to continue.", OtpActivity.this).showDialog();
        } else {

            Map<String, String> param = new HashMap<>();


            param.put("otp", otp.getText().toString().trim());
            param.put("user_id", String.valueOf(getIntent().getExtras().getInt("userId",0)));

            Log.d("zxcv: ", param.toString());

            final ProgressDialog dialog = ProgressDialog.show(OtpActivity.this, "",
                    "Loading. Please wait...", true);

            new VolleyServiceCall(Request.Method.POST, Url.VERIFY_OTP, null, param, null, OtpActivity.this) {
                @Override
                public void onResponse(String s) {
                    dialog.cancel();

                    Log.d("zxcv", s);

                    try {
                        JSONObject jsonObject = new JSONObject(s);

                        if(jsonObject.getBoolean("success")){

                            sharedPreference.setUserToken(jsonObject.getString("token"));
                            sharedPreference.setUserType(jsonObject.getJSONObject("data").getString("type"));

                            if(jsonObject.getJSONObject("data").getString("type").equalsIgnoreCase("2")){

                                Intent intent = new Intent(OtpActivity.this, CustomerDashboard.class);
                                startActivity(intent);
                            } else{
                                Intent intent = new Intent(OtpActivity.this, PanditDashboardActivity.class);
                                startActivity(intent);
                            }


                        }else {

                            new ErrorDialog(jsonObject.getString("msg"), OtpActivity.this).showDialog();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onError(VolleyError error, String errorMessage) {
                    dialog.cancel();
                    Log.d("zxcv ", errorMessage);
                    ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, OtpActivity.this) {
                        @Override
                        public void setAction(boolean action) {
                            if (action)
                                verifyOtp();
                        }
                    };
                    apiErrorAction.createDialog();
                }
            }.start();
        }
    }

    public void resendOtp() {

            Map<String, String> param = new HashMap<>();


            param.put("user_id", String.valueOf(getIntent().getExtras().getInt("userId",0)));

            Log.d("zxcv: ", param.toString());

            final ProgressDialog dialog = ProgressDialog.show(OtpActivity.this, "",
                    "Loading. Please wait...", true);

            new VolleyServiceCall(Request.Method.POST, Url.RESEND_OTP, null, param, null, OtpActivity.this) {
                @Override
                public void onResponse(String s) {
                    dialog.cancel();

                    Log.d("zxcv", s);

                    try {
                        JSONObject jsonObject = new JSONObject(s);

                        new ErrorDialog(jsonObject.getString("msg"), OtpActivity.this).showDialog();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onError(VolleyError error, String errorMessage) {
                    dialog.cancel();
                    Log.d("zxcv ", errorMessage);
                    ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, OtpActivity.this) {
                        @Override
                        public void setAction(boolean action) {
                            if (action)
                                resendOtp();
                        }
                    };
                    apiErrorAction.createDialog();
                }
            }.start();

    }

}