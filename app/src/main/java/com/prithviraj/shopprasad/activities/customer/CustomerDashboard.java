package com.prithviraj.shopprasad.activities.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.prithviraj.shopprasad.R;
import com.prithviraj.shopprasad.activities.LoginActivity;

public class CustomerDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ConstraintLayout pooja, pandit, product, uploadOrder;
    ImageView cart, hamburgerMenu;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Menu menu;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_drawer_layout);

        init();
        onClickListeners();
    }

    void init(){
        pooja = findViewById(R.id.constraintLayout4);
        pandit = findViewById(R.id.constraintLayout5);
        product = findViewById(R.id.constraintLayout6);
        uploadOrder = findViewById(R.id.constraintLayout61);

        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);

        hamburgerMenu = findViewById(R.id.imageView);
        cart = findViewById(R.id.cart);
    }

    void onClickListeners(){

        pooja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(CustomerDashboard.this,PoojaActivity.class);
                startActivity(in);
            }
        });

        pandit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(CustomerDashboard.this,PanditActivity.class);
                startActivity(in);
            }
        });

        product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(CustomerDashboard.this,ProductsActivity.class);
                startActivity(in);
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(CustomerDashboard.this,CartActivity.class);
                startActivity(in);
            }
        });

        uploadOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(CustomerDashboard.this,UploadProductActivity.class);
                startActivity(in);
            }
        });

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
            Intent in = new Intent(CustomerDashboard.this, MyProfileActivity.class);
            startActivity(in);
        } else if (id == R.id.nav_orders) {
            Intent in = new Intent(CustomerDashboard.this, MyOrdersActivity.class);
            startActivity(in);
        }  else if (id == R.id.nav_logout) {
            Intent in = new Intent(CustomerDashboard.this, LoginActivity.class);
            startActivity(in);
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}