package com.prithviraj.shopprasad.activities.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.prithviraj.myvollyimplementation.UtilityClasses.ApiErrorAction;
import com.prithviraj.myvollyimplementation.VolleyServiceCall;
import com.prithviraj.shopprasad.R;
import com.prithviraj.shopprasad.adapters.PoojaListForCustomerAdapter;
import com.prithviraj.shopprasad.adapters.ProductListForCustomerAdapter;
import com.prithviraj.shopprasad.dataModelClasses.PoojaDataModel;
import com.prithviraj.shopprasad.utils.CommonClass;
import com.prithviraj.shopprasad.utils.SharedPreference;
import com.prithviraj.shopprasad.utils.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProductsActivity extends AppCompatActivity {

    ImageView backIcon;
    SharedPreference sharedPreference;

    RecyclerView recyclerView;
    ProductListForCustomerAdapter productListForCustomerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        init();
        onClickListeners();
        getProductApi();
    }

    void init(){
        backIcon = findViewById(R.id.imageView);
        sharedPreference = new SharedPreference(ProductsActivity.this);

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(ProductsActivity.this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        productListForCustomerAdapter = new ProductListForCustomerAdapter();
        recyclerView.setAdapter(productListForCustomerAdapter);
    }

    void onClickListeners(){
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void getProductApi() {

        CommonClass.GLOBAL_LIST_CLASS.productList.clear();

        final ProgressDialog dialog = ProgressDialog.show(ProductsActivity.this, "",
                "Loading. Please wait...", true);

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer "+sharedPreference.getUserToken());

        new VolleyServiceCall(Request.Method.GET, Url.PRODUCT_LIST, header, null, null, ProductsActivity.this) {
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

                        CommonClass.GLOBAL_LIST_CLASS.productList.add(poojaDataModel);
                    }

                    productListForCustomerAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(VolleyError error, String errorMessage) {
                dialog.cancel();
                Log.d("zxcv ", errorMessage);
                ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, ProductsActivity.this) {
                    @Override
                    public void setAction(boolean action) {
                        if (action)
                            getProductApi();
                    }
                };
                apiErrorAction.createDialog();
            }
        }.start();

    }
}