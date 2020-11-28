package com.prithviraj.shopprasad.activities.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.prithviraj.myvollyimplementation.UtilityClasses.ApiErrorAction;
import com.prithviraj.myvollyimplementation.VolleyServiceCall;
import com.prithviraj.shopprasad.R;
import com.prithviraj.shopprasad.adapters.OrderHistoryAdapter;
import com.prithviraj.shopprasad.dataModelClasses.OrderHitoryDataModel;
import com.prithviraj.shopprasad.dataModelClasses.ProductDataModel;
import com.prithviraj.shopprasad.interfaces.OrderHistoryClickInterface;
import com.prithviraj.shopprasad.utils.CommonClass;
import com.prithviraj.shopprasad.utils.SharedPreference;
import com.prithviraj.shopprasad.utils.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderHistoryActivity extends AppCompatActivity {

    ImageView backIcon;
    Button shopNow;

    ConstraintLayout noItemsConst;

    SharedPreference sharedPreference;

    RecyclerView recyclerView;
    OrderHistoryAdapter orderHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        init();
        onClickListeners();
        getOrderHistory();

        CommonClass.GLOBAL_VARIABLE_CLASS.orderHistoryClickInterface = new OrderHistoryClickInterface() {
            @Override
            public void setPosition(int position) {
                Intent in = new Intent(OrderHistoryActivity.this, OrderHistoryDetailsActivity.class);
                in.putExtra("position", position);
                startActivity(in);
            }
        };
    }

    private void init() {
        sharedPreference = new SharedPreference(OrderHistoryActivity.this);

        backIcon = findViewById(R.id.backIcon);
        shopNow = findViewById(R.id.button);
        noItemsConst = findViewById(R.id.noItemsConst);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        //recyclerView.setLayoutManager(new LinearLayoutManager(OrderHistoryActivity.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        orderHistoryAdapter = new OrderHistoryAdapter();
        recyclerView.setAdapter(orderHistoryAdapter);

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

    }

    public void getOrderHistory() {

        CommonClass.GLOBAL_LIST_CLASS.orderHistoryList.clear();

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + sharedPreference.getUserToken());

        final ProgressDialog dialog = ProgressDialog.show(OrderHistoryActivity.this, "",
                "Loading. Please wait...", true);

        new VolleyServiceCall(Request.Method.GET, Url.MY_ORDERS, header, null, null, OrderHistoryActivity.this) {
            @Override
            public void onResponse(String s) {

                Log.d("zxcv", s);

                dialog.cancel();

                try {
                    JSONArray orderHistoryArray = new JSONObject(s).getJSONArray("data");

                    if(orderHistoryArray.length()>0){

                        noItemsConst.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                        for(int i = 0; i<orderHistoryArray.length();i++)
                        {
                            JSONObject orderHistoryObject = orderHistoryArray.getJSONObject(i);

                            OrderHitoryDataModel orderHitoryDataModel = new OrderHitoryDataModel();
                            orderHitoryDataModel.setOrderId(orderHistoryObject.getString("order_id"));
                            orderHitoryDataModel.setStatus(orderHistoryObject.getString("status"));
                            orderHitoryDataModel.setPaymentType(orderHistoryObject.getString("payment_type"));
                            orderHitoryDataModel.setOrderDate(orderHistoryObject.getString("created_at"));
//                            orderHitoryDataModel.setOtp(orderHistoryObject.getInt("otp"));

                            orderHitoryDataModel.setName(orderHistoryObject.getJSONObject("address").getString("full_name"));
                            orderHitoryDataModel.setAddress(orderHistoryObject.getJSONObject("address").getString("house_number")+", "+orderHistoryObject.getJSONObject("address").getString("area"));
                            orderHitoryDataModel.setPhone(orderHistoryObject.getJSONObject("address").getString("phone"));

                            JSONArray productsArray = orderHistoryObject.getJSONArray("order_product");

                            ArrayList<ProductDataModel> products = new ArrayList<>();

                            int amountPaid = 0;

                            for(int j =0; j<productsArray.length();j++){
                                JSONObject productsObject = productsArray.getJSONObject(j);

                                int in = Integer.parseInt(productsObject.getString("product_price"));
                                amountPaid = amountPaid + (in * productsObject.getInt("quantity"));

                                ProductDataModel productDataModel = new ProductDataModel();

                                productDataModel.setProductId(productsObject.getInt("product_id"));
                                productDataModel.setProductName(productsObject.getString("product_name"));
                                productDataModel.setPrice(Integer.parseInt(productsObject.getString("product_price")));
                                productDataModel.setProductImage(productsObject.getString("product_image"));
                                productDataModel.setQuantity(productsObject.getInt("quantity"));

                                products.add(productDataModel);

                            }

                            orderHitoryDataModel.setAmountPaid(String.valueOf(amountPaid));
                            orderHitoryDataModel.setItemsOrdered(products);



                            CommonClass.GLOBAL_LIST_CLASS.orderHistoryList.add(orderHitoryDataModel);
                        }

                        orderHistoryAdapter.notifyDataSetChanged();


                    }else {
                        noItemsConst.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(VolleyError error, String errorMessage) {
                dialog.cancel();
                Log.d("zxcv ", errorMessage);
                ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, OrderHistoryActivity.this) {
                    @Override
                    public void setAction(boolean action) {
                        if (action)
                            getOrderHistory();
                    }
                };
                apiErrorAction.createDialog();
            }
        }.start();
    }
}