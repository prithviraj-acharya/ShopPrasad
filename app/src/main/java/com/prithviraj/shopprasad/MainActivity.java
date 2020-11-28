package com.prithviraj.shopprasad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.prithviraj.shopprasad.activities.LoginActivity;
import com.prithviraj.shopprasad.activities.customer.CustomerDashboard;
import com.prithviraj.shopprasad.activities.panditji.PanditDashboardActivity;
import com.prithviraj.shopprasad.utils.SharedPreference;


public class MainActivity extends AppCompatActivity {

    SharedPreference sharedPreference;
    TextView versionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        versionName = findViewById(R.id.versionName);
//        versionName.setText(String.format("Version code: %s",VERSION_NAMES));

        sharedPreference = new SharedPreference(MainActivity.this);



        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


                if(sharedPreference.getUserToken().equalsIgnoreCase("")) {
                    Intent in = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(in);
                    finish();
                }else {
                    if(sharedPreference.getUserType().equalsIgnoreCase("2")){
                        Intent in = new Intent(MainActivity.this, CustomerDashboard.class);
                        startActivity(in);
                        finish();
                    }else {
                        Intent in = new Intent(MainActivity.this, PanditDashboardActivity.class);
                        startActivity(in);
                        finish();
                    }
                }


            }
        },3000);
    }
}
