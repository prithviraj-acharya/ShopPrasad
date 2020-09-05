package com.prithviraj.shopprasad.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.prithviraj.shopprasad.R;

public class ChangePassword extends AppCompatActivity {

    ImageView backIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        init();
        onClickListeners();
    }

    void init(){
        backIcon = findViewById(R.id.imageView);

    }

    void onClickListeners(){
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}