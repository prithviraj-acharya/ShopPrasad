package com.prithviraj.shopprasad.activities.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.prithviraj.myvollyimplementation.UtilityClasses.ApiErrorAction;
import com.prithviraj.myvollyimplementation.VolleyServiceCall;
import com.prithviraj.shopprasad.R;
import com.prithviraj.shopprasad.adapters.CartAdapter;
import com.prithviraj.shopprasad.dataModelClasses.CartDataModel;
import com.prithviraj.shopprasad.interfaces.DecreaseItemQuantityFromCart;
import com.prithviraj.shopprasad.interfaces.IncreaseItemQuantityFromCart;
import com.prithviraj.shopprasad.utils.CommonClass;
import com.prithviraj.shopprasad.utils.SharedPreference;
import com.prithviraj.shopprasad.utils.Url;

import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    ImageView backIcon;
    Button shopNow;
    TextView totalItemsInCart;

    ConstraintLayout noItemsConst, checkoutConst;

    SharedPreference sharedPreference;

    RecyclerView recyclerView;
    CartAdapter cartAdapter;

    String totalItems;

    public static Activity cartActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartActivity = this;

        init();
        onClickListeners();

        CommonClass.GLOBAL_VARIABLE_CLASS.increaseItemQuantityFromCart = new IncreaseItemQuantityFromCart() {
            @Override
            public void setPosition(int position) {
                CartDataModel cartItem =  CommonClass.GLOBAL_LIST_CLASS.cartList.get(position);
                cartItem.setQuantity(cartItem.getQuantity()+1);
                cartAdapter.notifyDataSetChanged();

                totalItems = findTotalItemAndPrice();
                totalItemsInCart.setText(totalItems);

                //if(cartItem.getQuantity()==1){
                //    addToCart(productId,0,product.getQuantity());
                //}else if(product.getQuantity()>1){
                updateItemsToCart(cartItem.getCartItemId(),cartItem.getQuantity());
                //}
            }
        };

        CommonClass.GLOBAL_VARIABLE_CLASS.decreaseItemQuantityFromCart = new DecreaseItemQuantityFromCart() {
            @Override
            public void setPosition(int position) {
                CartDataModel cartItem =  CommonClass.GLOBAL_LIST_CLASS.cartList.get(position);
                cartItem.setQuantity(cartItem.getQuantity()-1);


                if(cartItem.getQuantity()==0){
                    CommonClass.GLOBAL_LIST_CLASS.cartList.remove(position);
                    removeItemsFromCart(cartItem.getCartItemId());

                }else if(cartItem.getQuantity()>1){
                    updateItemsToCart(cartItem.getCartItemId(),cartItem.getQuantity());
                }

                cartAdapter.notifyDataSetChanged();

                totalItems = findTotalItemAndPrice();
                totalItemsInCart.setText(totalItems);

                if(CommonClass.GLOBAL_LIST_CLASS.cartList.size()>0){
                    noItemsConst.setVisibility(View.GONE);
                    checkoutConst.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);

                }else {
                    noItemsConst.setVisibility(View.VISIBLE);
                    checkoutConst.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                }

            }
        };
    }

    private void init() {
        sharedPreference = new SharedPreference(CartActivity.this);

        backIcon = findViewById(R.id.backIcon);
        shopNow = findViewById(R.id.button);
        noItemsConst = findViewById(R.id.noItemsConst);
        totalItemsInCart = findViewById(R.id.totalItemsInCart);

        checkoutConst = findViewById(R.id.constraintLayout4);

        totalItems = findTotalItemAndPrice();
        totalItemsInCart.setText(totalItems);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        cartAdapter = new CartAdapter();
        recyclerView.setAdapter(cartAdapter);

        if(CommonClass.GLOBAL_LIST_CLASS.cartList.size()>0){
            noItemsConst.setVisibility(View.GONE);
            checkoutConst.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);

        }else {
            noItemsConst.setVisibility(View.VISIBLE);
            checkoutConst.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    private void onClickListeners() {
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        shopNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        checkoutConst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(CartActivity.this, CheckoutActivity.class);
                in.putExtra("totalItems",totalItems);
                startActivity(in);
            }
        });
    }

    private String findTotalItemAndPrice(){

        int price = 0;

        for(int i = 0; i<CommonClass.GLOBAL_LIST_CLASS.cartList.size();i++){
            CartDataModel cartDataModel = CommonClass.GLOBAL_LIST_CLASS.cartList.get(i);

            if(cartDataModel.getDiscount()>0){

                price+=(cartDataModel.getDiscount()*cartDataModel.getQuantity());
            }
            else {
                price+=(cartDataModel.getPrice()*cartDataModel.getQuantity());
            }
        }
        return CommonClass.GLOBAL_LIST_CLASS.cartList.size()+" Total Items: ₹ "+price;
    }

    public void updateItemsToCart(final int cartItemId, final int quantity){

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + sharedPreference.getUserToken());

        Map<String, String> param = new HashMap<>();
        param.put("id",String.valueOf(cartItemId));
        param.put("quantity",String.valueOf(quantity));

        new VolleyServiceCall(Request.Method.POST, Url.UPDATE_ITEMS_TO_CART, header, param, null, CartActivity.this) {
            @Override
            public void onResponse(String s) {


                Log.d("zxcv", s);
//
            }

            @Override
            public void onError(VolleyError error, String errorMessage) {
                //  dialog.cancel();
                Log.d("zxcv ", errorMessage);
                ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, CartActivity.this) {
                    @Override
                    public void setAction(boolean action) {
                        if (action)
                            updateItemsToCart(cartItemId, quantity);
                    }
                };
                apiErrorAction.createDialog();
            }
        }.start();

    }

    public void removeItemsFromCart(final int cartItemId){

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + sharedPreference.getUserToken());


        new VolleyServiceCall(Request.Method.GET, Url.REMOVE_ITEMS_FROM_CART+cartItemId+"/remove", header, null, null, CartActivity.this) {
            @Override
            public void onResponse(String s) {


                Log.d("zxcv", s);
//
            }

            @Override
            public void onError(VolleyError error, String errorMessage) {
                //  dialog.cancel();
                Log.d("zxcv ", errorMessage);
                ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, CartActivity.this) {
                    @Override
                    public void setAction(boolean action) {
                        if (action)
                            removeItemsFromCart(cartItemId);
                    }
                };
                apiErrorAction.createDialog();
            }
        }.start();

    }
}