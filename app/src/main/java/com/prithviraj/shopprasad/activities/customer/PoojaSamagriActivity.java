package com.prithviraj.shopprasad.activities.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.prithviraj.myvollyimplementation.UtilityClasses.ApiErrorAction;
import com.prithviraj.myvollyimplementation.VolleyServiceCall;
import com.prithviraj.shopprasad.R;
import com.prithviraj.shopprasad.adapters.ProductSamagriListForCustomerAdapter;
import com.prithviraj.shopprasad.dataModelClasses.CartDataModel;
import com.prithviraj.shopprasad.dataModelClasses.PoojaDataModel;
import com.prithviraj.shopprasad.interfaces.AddToCartPoojaSamagriList;
import com.prithviraj.shopprasad.interfaces.AddToCartProductList;
import com.prithviraj.shopprasad.interfaces.ClickPoojaSamagriList;
import com.prithviraj.shopprasad.interfaces.ClickProductList;
import com.prithviraj.shopprasad.utils.CommonClass;
import com.prithviraj.shopprasad.utils.SharedPreference;
import com.prithviraj.shopprasad.utils.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PoojaSamagriActivity extends AppCompatActivity {

    ImageView backIcon;
    SharedPreference sharedPreference;
    ImageView cart;

    RecyclerView recyclerView;
    ProductSamagriListForCustomerAdapter productSamagriListForCustomerAdapter;

    ConstraintLayout addToCartDot;
    TextView itemNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pooja_samagri);

        init();
        onClickListeners();
        getPujaSamagriList();
        getCart();

        CommonClass.GLOBAL_VARIABLE_CLASS.clickPoojaSamagriList = new ClickPoojaSamagriList() {
            @Override
            public void passProductId(int productId) {
                Intent intent = new Intent(PoojaSamagriActivity.this,ProductDetails.class);
                intent.putExtra("productId",productId);
                startActivity(intent);
            }
        };

        CommonClass.GLOBAL_VARIABLE_CLASS.addToCartPoojaSamagriList = new AddToCartPoojaSamagriList() {
            @Override
            public void passPosition(int position) {
                PoojaDataModel poojaDataModel = CommonClass.GLOBAL_LIST_CLASS.poojaaSamagriList.get(position);
                addToCart(poojaDataModel.getId(),getQuantity(poojaDataModel.getId())+1);
            }
        };
    }

    void init(){

        sharedPreference = new SharedPreference(PoojaSamagriActivity.this);

        cart = findViewById(R.id.cart);

        backIcon = findViewById(R.id.imageView);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(PoojaSamagriActivity.this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        productSamagriListForCustomerAdapter = new ProductSamagriListForCustomerAdapter();
        recyclerView.setAdapter(productSamagriListForCustomerAdapter);

        addToCartDot = findViewById(R.id.addToCartDot);
        itemNumber = findViewById(R.id.itemNumber);

    }

    void onClickListeners(){
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(PoojaSamagriActivity.this, CartActivity.class);
                startActivity(in);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getCart();
    }


    public void getPujaSamagriList() {

        CommonClass.GLOBAL_LIST_CLASS.poojaaSamagriList.clear();

        final ProgressDialog dialog = ProgressDialog.show(PoojaSamagriActivity.this, "",
                "Loading. Please wait...", true);

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer "+sharedPreference.getUserToken());

        new VolleyServiceCall(Request.Method.GET, Url.SAMAGRI_LIST, header, null, null, PoojaSamagriActivity.this) {
            @Override
            public void onResponse(String s) {
                dialog.cancel();

                Log.d("zxcv", s);

                try {
                    JSONArray pujaArray = new JSONObject(s).getJSONArray("data");

                    for(int i = 0;i<pujaArray.length();i++){
                        PoojaDataModel poojaDataModel = new PoojaDataModel();

                        poojaDataModel.setPujaName(pujaArray.getJSONObject(i).getString("name"));
                        poojaDataModel.setPujaPrice(pujaArray.getJSONObject(i).getString("price"));
                        poojaDataModel.setPujaImage(pujaArray.getJSONObject(i).getString("image"));
                        poojaDataModel.setId(pujaArray.getJSONObject(i).getInt("id"));
                        poojaDataModel.setPercentageOff(pujaArray.getJSONObject(i).getString("percentage_off"));
                        poojaDataModel.setOfferPrice(pujaArray.getJSONObject(i).getString("offer_price"));

                        CommonClass.GLOBAL_LIST_CLASS.poojaaSamagriList.add(poojaDataModel);
                    }

                    productSamagriListForCustomerAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(VolleyError error, String errorMessage) {
                dialog.cancel();
                Log.d("zxcv ", errorMessage);
                ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, PoojaSamagriActivity.this) {
                    @Override
                    public void setAction(boolean action) {
                        if (action)
                            getPujaSamagriList();
                    }
                };
                apiErrorAction.createDialog();
            }
        }.start();

    }

    public void addToCart(final int productId, final int quantity){

        final ProgressDialog dialog = ProgressDialog.show(PoojaSamagriActivity.this, "",
                "Loading. Please wait...", true);


        Log.d("zxcv", "addToCart: "+productId);
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer "+sharedPreference.getUserToken());

        Map<String, String> param = new HashMap<>();
        param.put("product_id",String.valueOf(productId));
        param.put("quantity",String.valueOf(quantity));


        new VolleyServiceCall(Request.Method.POST, Url.ADD_TO_CART, header, param, null, PoojaSamagriActivity.this) {
            @Override
            public void onResponse(String s) {
                dialog.cancel();

                Log.d("zxcv", s);
                getCart();
//
            }

            @Override
            public void onError(VolleyError error, String errorMessage) {
                  dialog.cancel();
                Log.d("zxcv ", errorMessage);
                ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, PoojaSamagriActivity.this) {
                    @Override
                    public void setAction(boolean action) {
                        if (action)
                            addToCart(productId,quantity);
                    }
                };
                apiErrorAction.createDialog();
            }
        }.start();

    }

    public void getCart() {

        CommonClass.GLOBAL_LIST_CLASS.cartList.clear();

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + sharedPreference.getUserToken());

        new VolleyServiceCall(Request.Method.GET, Url.MY_CART, header, null, null, PoojaSamagriActivity.this) {
            @Override
            public void onResponse(String s) {

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
                Log.d("zxcv ", errorMessage);
                ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, PoojaSamagriActivity.this) {
                    @Override
                    public void setAction(boolean action) {
                        if (action)
                            getCart();
                    }
                };
                apiErrorAction.createDialog();
            }
        }.start();

    }

    int getQuantity(int productId){

        for(int i = 0; i<CommonClass.GLOBAL_LIST_CLASS.cartList.size();i++){

            CartDataModel cartDataModel = CommonClass.GLOBAL_LIST_CLASS.cartList.get(i);

            if(productId==cartDataModel.getProductId()){
                return cartDataModel.getQuantity();
            }
        }

        return 0;
    }

}