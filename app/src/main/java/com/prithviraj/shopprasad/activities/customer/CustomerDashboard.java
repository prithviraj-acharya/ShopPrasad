package com.prithviraj.shopprasad.activities.customer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.material.navigation.NavigationView;
import com.opensooq.supernova.gligar.GligarPicker;
import com.prithviraj.myvollyimplementation.DataModelClasses.DataPart;
import com.prithviraj.myvollyimplementation.UtilityClasses.ApiErrorAction;
import com.prithviraj.myvollyimplementation.VolleyServiceCall;
import com.prithviraj.shopprasad.R;
import com.prithviraj.shopprasad.activities.LoginActivity;
import com.prithviraj.shopprasad.dataModelClasses.CartDataModel;
import com.prithviraj.shopprasad.utils.CircleTransform;
import com.prithviraj.shopprasad.utils.CommonClass;
import com.prithviraj.shopprasad.utils.ErrorDialog;
import com.prithviraj.shopprasad.utils.SharedPreference;
import com.prithviraj.shopprasad.utils.Url;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CustomerDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ConstraintLayout pooja, pandit, product, uploadOrder, astrology, poojaSamagriList;
    ImageView cart, hamburgerMenu, orderHistory;
    SharedPreference sharedPreference;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    GligarPicker imagePicker;
    Toolbar toolbar;
    Menu menu;
    TextView navUsername;
    ImageView navUserImage;

    byte profileImageArray[] = null;
    long length;
    String path1 = "";

    ConstraintLayout addToCartDot;
    TextView itemNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_drawer_layout);

        init();
        onClickListeners();
        getProfile();
        GetCart();
    }

    void init() {
        pooja = findViewById(R.id.constraintLayout4);
        pandit = findViewById(R.id.constraintLayout5);
        product = findViewById(R.id.constraintLayout6);
        uploadOrder = findViewById(R.id.constraintLayout61);
        astrology = findViewById(R.id.constraintLayout51);
        poojaSamagriList = findViewById(R.id.constraintLayout41);


        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);

        View headerView = navigationView.getHeaderView(0);
        navUsername = (TextView) headerView.findViewById(R.id.textView11);
        navUserImage = headerView.findViewById(R.id.imageView2);

        hamburgerMenu = findViewById(R.id.imageView);
        cart = findViewById(R.id.cart);
        orderHistory = findViewById(R.id.orderHistory);

        addToCartDot = findViewById(R.id.addToCartDot);
        itemNumber = findViewById(R.id.itemNumber);


        imagePicker = new GligarPicker();
        imagePicker.limit(1);
        imagePicker.requestCode(1001);
        imagePicker.withActivity(CustomerDashboard.this);

        sharedPreference = new SharedPreference(CustomerDashboard.this);
    }

    void onClickListeners() {

        pooja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(CustomerDashboard.this, PoojaActivity.class);
                in.putExtra("isPuja", true);
                startActivity(in);
            }
        });

        pandit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(CustomerDashboard.this, PoojaActivity.class);
                in.putExtra("isPuja", false);
                startActivity(in);
            }
        });

        product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(CustomerDashboard.this, ProductsActivity.class);
                startActivity(in);
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(CustomerDashboard.this, CartActivity.class);
                startActivity(in);
            }
        });

        astrology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(CustomerDashboard.this, AstrologyActivity.class);
                startActivity(in);
            }
        });

        poojaSamagriList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(CustomerDashboard.this, PoojaSamagriActivity.class);
                startActivity(in);
            }
        });
        uploadOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePicker.show();
            }
        });

        navigationView.setNavigationItemSelectedListener(this);

        hamburgerMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        orderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(CustomerDashboard.this, OrderHistoryActivity.class);
                startActivity(in);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getProfile();
        GetCart();
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
        } else if (id == R.id.nav_address) {
            Intent in = new Intent(CustomerDashboard.this, MyAddressActivity.class);
            in.putExtra("isSelectAddress",false);
            startActivity(in);
        } else if (id == R.id.nav_logout) {
            Intent in = new Intent(CustomerDashboard.this, LoginActivity.class);
            startActivity(in);
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == 1001) {

            if (data != null) {
                Uri imageUri = data.getData();
                // String filepath = RealPathFetching.getFilePath(EditProfileActivity.this, imageUri);

                String pathsList[] = data.getExtras().getStringArray(GligarPicker.IMAGES_RESULT);
                Log.d("zxcv", "onActivityResult: " + pathsList[0]);
                getByteArray(new File(pathsList[0]));

            }

        }
    }

    public void getByteArray(File imageFile) {

        path1 = imageFile.getPath();
        length = imageFile.length() / 1024;
        BitmapFactory.Options bmOptions2 = new BitmapFactory.Options();
        Bitmap bitmap2 = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bmOptions2);
        ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
        if (length <= 800) {
            bitmap2.compress(Bitmap.CompressFormat.JPEG, 70, stream2);
        } else if (length <= 1200) {
            bitmap2.compress(Bitmap.CompressFormat.JPEG, 50, stream2);
        } else if (length <= 2000) {
            bitmap2.compress(Bitmap.CompressFormat.JPEG, 40, stream2);
        } else if (length <= 2600) {
            bitmap2.compress(Bitmap.CompressFormat.JPEG, 30, stream2);
        } else if (length <= 3200) {
            bitmap2.compress(Bitmap.CompressFormat.JPEG, 15, stream2);
        } else {
            bitmap2.compress(Bitmap.CompressFormat.JPEG, 8, stream2);
        }

        profileImageArray = stream2.toByteArray();

        Log.d("zxcv", "getByteArray: " + profileImageArray);

        uploadOrder();


    }

    public void uploadOrder() {


        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + sharedPreference.getUserToken());

        Map<String, DataPart> part = new HashMap<>();

        if (profileImageArray != null)
            part.put("image", new DataPart("image", profileImageArray));


        Map<String, String> param = new HashMap<>();

        Log.d("zxcv: ", param.toString());

        final ProgressDialog dialog = ProgressDialog.show(CustomerDashboard.this, "",
                "Loading. Please wait...", true);

        new VolleyServiceCall(Request.Method.POST, Url.UPLOAD_ORDER, header, param, part, CustomerDashboard.this) {
            @Override
            public void onResponse(String s) {
                dialog.cancel();

                Log.d("zxcv", s);

                try {
                    JSONObject jsonObject = new JSONObject(s);


                    new ErrorDialog(jsonObject.getString("msg"), CustomerDashboard.this).showDialog();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(VolleyError error, String errorMessage) {
                dialog.cancel();
                Log.d("zxcv ", errorMessage);
                ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, CustomerDashboard.this) {
                    @Override
                    public void setAction(boolean action) {
                        if (action)
                            uploadOrder();
                    }
                };
                apiErrorAction.createDialog();
            }
        }.start();


    }

    public void getProfile() {


        final ProgressDialog dialog = ProgressDialog.show(CustomerDashboard.this, "",
                "Loading. Please wait...", true);

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + sharedPreference.getUserToken());

        Log.d("zxcv ", "Bearer " + sharedPreference.getUserToken());

        new VolleyServiceCall(Request.Method.GET, Url.PROFILE, header, null, null, CustomerDashboard.this) {
            @Override
            public void onResponse(String s) {
                dialog.cancel();

                Log.d("zxcv", s);

                try {
                    navUsername.setText(new JSONObject(s).getJSONObject("data").getString("full_name").equalsIgnoreCase("null") ? "Not added yet" : new JSONObject(s).getJSONObject("data").getString("full_name"));

                    if (!new JSONObject(s).getJSONObject("data").getString("profile_image").equals("null")) {
                        Picasso.get()
                                .load(new JSONObject(s).getJSONObject("data").getString("profile_image"))
                                .transform(new CircleTransform())
                                .into(navUserImage);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(VolleyError error, String errorMessage) {
                dialog.cancel();
                Log.d("zxcv ", errorMessage);
                ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, CustomerDashboard.this) {
                    @Override
                    public void setAction(boolean action) {
                        if (action)
                            getProfile();
                    }
                };
                apiErrorAction.createDialog();
            }
        }.start();

    }

    public void GetCart() {

        CommonClass.GLOBAL_LIST_CLASS.cartList.clear();

        final ProgressDialog dialog = ProgressDialog.show(CustomerDashboard.this, "",
                "Loading. Please wait...", true);

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + sharedPreference.getUserToken());

        new VolleyServiceCall(Request.Method.GET, Url.MY_CART, header, null, null, CustomerDashboard.this) {
            @Override
            public void onResponse(String s) {
                dialog.cancel();

                Log.d("zxcv", s);

                try {
                    JSONArray array = new JSONObject(s).getJSONArray("data");

                    if (array.length() > 0) {
                        addToCartDot.setVisibility(View.VISIBLE);
                        itemNumber.setText(String.valueOf(array.length()));

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject cartObject = array.getJSONObject(i);

                            CartDataModel cartDataModel = new CartDataModel();

                            cartDataModel.setCartItemId(cartObject.getInt("id"));
                            cartDataModel.setUserId(cartObject.getInt("user_id"));
                            cartDataModel.setProductId(cartObject.getInt("product_id"));
                            cartDataModel.setQuantity(cartObject.getInt("quantity"));
                            cartDataModel.setPrice(cartObject.getInt("price"));
                            cartDataModel.setDiscount(cartObject.getString("offer_price").equalsIgnoreCase("null")?0:cartObject.getInt("offer_price"));
                            cartDataModel.setProductName(cartObject.getString("name"));
                            cartDataModel.setProductImage(cartObject.getString("image"));
                            cartDataModel.setProductDesc(cartObject.getString("description"));

                            CommonClass.GLOBAL_LIST_CLASS.cartList.add(cartDataModel);

                        }


                    } else {
                        addToCartDot.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(VolleyError error, String errorMessage) {
                dialog.cancel();
                Log.d("zxcv ", errorMessage);
                ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, CustomerDashboard.this) {
                    @Override
                    public void setAction(boolean action) {
                        if (action)
                            GetCart();
                    }
                };
                apiErrorAction.createDialog();
            }
        }.start();

    }
}