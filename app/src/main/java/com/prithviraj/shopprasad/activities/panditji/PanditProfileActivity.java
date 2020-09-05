package com.prithviraj.shopprasad.activities.panditji;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.prithviraj.shopprasad.R;

public class PanditProfileActivity extends AppCompatActivity {

    ImageView backIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pandit_profile);

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