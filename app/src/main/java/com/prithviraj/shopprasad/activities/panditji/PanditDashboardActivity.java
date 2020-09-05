package com.prithviraj.shopprasad.activities.panditji;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.prithviraj.shopprasad.R;
import com.prithviraj.shopprasad.activities.ChangePassword;
import com.prithviraj.shopprasad.activities.LoginActivity;
import com.prithviraj.shopprasad.activities.customer.CartActivity;
import com.prithviraj.shopprasad.activities.customer.ChanegPasswordActivity;
import com.prithviraj.shopprasad.activities.customer.CustomerDashboard;
import com.prithviraj.shopprasad.activities.customer.MyOrdersActivity;
import com.prithviraj.shopprasad.activities.customer.MyProfileActivity;
import com.prithviraj.shopprasad.activities.customer.PanditActivity;
import com.prithviraj.shopprasad.activities.customer.PoojaActivity;
import com.prithviraj.shopprasad.activities.customer.ProductsActivity;

public class PanditDashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ImageView cart, hamburgerMenu;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Menu menu;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_pandit_drawer_layout);


        init();
        onClickListeners();
    }

    void init(){

        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);

        hamburgerMenu = findViewById(R.id.imageView);
        cart = findViewById(R.id.cart);
    }

    void onClickListeners(){

        navigationView.setNavigationItemSelectedListener(this);

        hamburgerMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }

    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_profile) {
            // Handle the camera action
            Intent in = new Intent(PanditDashboardActivity.this, PanditProfileActivity.class);
            startActivity(in);
        } else if (id == R.id.nav_bookings) {
            Intent in = new Intent(PanditDashboardActivity.this, MyBookingActivity.class);
            startActivity(in);
        } else if (id == R.id.nav_logout) {
            Intent in = new Intent(PanditDashboardActivity.this, LoginActivity.class);
            startActivity(in);
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}