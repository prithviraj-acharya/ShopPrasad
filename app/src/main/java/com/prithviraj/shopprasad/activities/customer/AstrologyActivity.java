package com.prithviraj.shopprasad.activities.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.prithviraj.myvollyimplementation.UtilityClasses.ApiErrorAction;
import com.prithviraj.myvollyimplementation.VolleyServiceCall;
import com.prithviraj.shopprasad.R;
import com.prithviraj.shopprasad.activities.OtpActivity;
import com.prithviraj.shopprasad.utils.ErrorDialog;
import com.prithviraj.shopprasad.utils.SharedPreference;
import com.prithviraj.shopprasad.utils.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AstrologyActivity extends AppCompatActivity {

    ImageView backIcon;
    EditText timeOfBirth, dateOfBirth, edtName, edtPhone, edtAddress;
    SharedPreference sharedPreference;
    Button uploadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_astrology);


        init();
        onClickListeners();
    }

    void init() {
        sharedPreference = new SharedPreference(AstrologyActivity.this);

        backIcon = findViewById(R.id.imageView);
        timeOfBirth = findViewById(R.id.timeOfBirth);
        dateOfBirth = findViewById(R.id.dateOfBirth);
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);

        uploadButton = findViewById(R.id.button4);


    }

    void onClickListeners() {

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        timeOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(AstrologyActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                timeOfBirth.setText(String.format("%d:%d", hourOfDay, minute));
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(AstrologyActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {


                                dateOfBirth.setText(dayOfMonth + " / " + (monthOfYear + 1) + " / "
                                        + year);

                            }
                        }, mYear, mMonth, mDay);

                datePickerDialog.show();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadAstrologyForm();
            }
        });
    }

    public void uploadAstrologyForm() {


        if (edtName.getText().toString().trim().equals("")) {
            new ErrorDialog("Please enter a valid name.", AstrologyActivity.this).showDialog();
        } else if (edtPhone.getText().toString().trim().equals("") || edtPhone.getText().toString().trim().length() < 10) {
            new ErrorDialog("Please enter a valid number.", AstrologyActivity.this).showDialog();
        } else if (edtAddress.getText().toString().trim().equals("")) {
            new ErrorDialog("Please enter a valid place of birth.", AstrologyActivity.this).showDialog();
        } else if (timeOfBirth.getText().toString().trim().equals("")) {
            new ErrorDialog("Please enter a valid time of birth.", AstrologyActivity.this).showDialog();
        } else if (dateOfBirth.getText().toString().trim().equals("")) {
            new ErrorDialog("Please enter a valid date of birth.", AstrologyActivity.this).showDialog();
        } else {

            Map<String, String> header = new HashMap<>();
            header.put("Authorization", "Bearer " + sharedPreference.getUserToken());

            Map<String, String> param = new HashMap<>();
            param.put("full_name", edtName.getText().toString().trim());
            param.put("date", dateOfBirth.getText().toString().trim());
            param.put("phone", edtPhone.getText().toString().trim());
            param.put("time", timeOfBirth.getText().toString().trim());
            param.put("place", edtAddress.getText().toString().trim());


            Log.d("zxcv: ", param.toString());

            final ProgressDialog dialog = ProgressDialog.show(AstrologyActivity.this, "",
                    "Loading. Please wait...", true);

            new VolleyServiceCall(Request.Method.POST, Url.ASTROLOGY_STORE, header, param, null, AstrologyActivity.this) {
                @Override
                public void onResponse(String s) {
                    dialog.cancel();

                    Log.d("zxcv", s);

                    try {
                        JSONObject jsonObject = new JSONObject(s);

                        if (jsonObject.getBoolean("success")) {
                            new ErrorDialog(jsonObject.getString("msg"), AstrologyActivity.this).showDialog();
                            edtName.setText("");
                            edtAddress.setText("");
                            edtPhone.setText("");
                            dateOfBirth.setText("");
                            timeOfBirth.setText("");

                        } else {

                            new ErrorDialog(jsonObject.getJSONArray("msg").getString(0), AstrologyActivity.this).showDialog();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onError(VolleyError error, String errorMessage) {
                    dialog.cancel();
                    Log.d("zxcv ", errorMessage);
                    ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, AstrologyActivity.this) {
                        @Override
                        public void setAction(boolean action) {
                            if (action)
                                uploadAstrologyForm();
                        }
                    };
                    apiErrorAction.createDialog();
                }
            }.start();
        }
    }


}