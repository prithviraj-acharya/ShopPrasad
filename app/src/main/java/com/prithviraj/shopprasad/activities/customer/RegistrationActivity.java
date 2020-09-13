package com.prithviraj.shopprasad.activities.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.prithviraj.myvollyimplementation.UtilityClasses.ApiErrorAction;
import com.prithviraj.myvollyimplementation.VolleyServiceCall;
import com.prithviraj.shopprasad.R;
import com.prithviraj.shopprasad.activities.LoginActivity;
import com.prithviraj.shopprasad.activities.OtpActivity;
import com.prithviraj.shopprasad.activities.panditji.PanditDashboardActivity;
import com.prithviraj.shopprasad.utils.ErrorDialog;
import com.prithviraj.shopprasad.utils.SharedPreference;
import com.prithviraj.shopprasad.utils.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView alreadyHaveAccnt;
    Button registerButton;
    EditText phoneNumber;
    SharedPreference sharedPreference;

    String[] country = {"Please Select a user type", "Normal User", "Pandit"};
    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        init();
        onClickListeners();
    }

    void init() {
        alreadyHaveAccnt = findViewById(R.id.textView18);
        registerButton = findViewById(R.id.button7);

        sharedPreference = new SharedPreference(RegistrationActivity.this);

        phoneNumber = findViewById(R.id.editTextTextPersonName2);

        Spinner spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);
    }

    void onClickListeners() {
        alreadyHaveAccnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrationApi();
            }
        });
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        // Toast.makeText(getApplicationContext(),country[position] , Toast.LENGTH_LONG).show();
        type = position + 1;
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public void registrationApi() {


        if (phoneNumber.getText().toString().trim().equals("") || phoneNumber.getText().toString().trim().length() < 10) {

            new ErrorDialog("Please fill up a valid phone number.", RegistrationActivity.this).showDialog();
        } else if (type < 2) {
            new ErrorDialog("Please select a user type.", RegistrationActivity.this).showDialog();
        } else {

            Map<String, String> param = new HashMap<>();


            param.put("phone", phoneNumber.getText().toString().trim());
            param.put("type", String.valueOf(type));

            Log.d("zxcv: ", param.toString());

            final ProgressDialog dialog = ProgressDialog.show(RegistrationActivity.this, "",
                    "Loading. Please wait...", true);

            new VolleyServiceCall(Request.Method.POST, Url.REGISTRATION_URL, null, param, null, RegistrationActivity.this) {
                @Override
                public void onResponse(String s) {
                    dialog.cancel();

                    Log.d("zxcv", s);

                    try {
                        JSONObject jsonObject = new JSONObject(s);

                        if (jsonObject.getBoolean("success")) {

                            Intent in = new Intent(RegistrationActivity.this, OtpActivity.class);
                            in.putExtra("userId", jsonObject.getJSONObject("data").getInt("id"));
                            startActivity(in);
                        } else {

                            new ErrorDialog(jsonObject.getJSONArray("msg").getString(0), RegistrationActivity.this).showDialog();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onError(VolleyError error, String errorMessage) {
                    dialog.cancel();
                    Log.d("zxcv ", errorMessage);
                    ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, RegistrationActivity.this) {
                        @Override
                        public void setAction(boolean action) {
                            if (action)
                                registrationApi();
                        }
                    };
                    apiErrorAction.createDialog();
                }
            }.start();
        }
    }
}