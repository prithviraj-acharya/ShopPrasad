package com.prithviraj.shopprasad.activities.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
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
import com.prithviraj.shopprasad.adapters.PoojaPanditOrderHistoryAdapter;
import com.prithviraj.shopprasad.dataModelClasses.PoojaPanditBookingDataModel;
import com.prithviraj.shopprasad.utils.CommonClass;
import com.prithviraj.shopprasad.utils.SharedPreference;
import com.prithviraj.shopprasad.utils.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PanditBookingHistoryActivity extends AppCompatActivity {

    ImageView backIcon;
    Button shopNow;

    ConstraintLayout noItemsConst;

    SharedPreference sharedPreference;

    RecyclerView recyclerView;
    PoojaPanditOrderHistoryAdapter poojaPanditOrderHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pandit_booking_history);

        init();
        onClickListeners();
        getPanditOrderHistory();
    }

    private void init() {
        sharedPreference = new SharedPreference(PanditBookingHistoryActivity.this);

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

    public void getPanditOrderHistory() {

        CommonClass.GLOBAL_LIST_CLASS.panditHistoryList.clear();

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + sharedPreference.getUserToken());

        final ProgressDialog dialog = ProgressDialog.show(PanditBookingHistoryActivity.this, "",
                "Loading. Please wait...", true);

        new VolleyServiceCall(Request.Method.GET, Url.GET_PANDIT_HISTORY, header, null, null, PanditBookingHistoryActivity.this) {
            @Override
            public void onResponse(String s) {

                Log.d("zxcv", s);

                dialog.cancel();

                try {
                    JSONArray jsonArray = new JSONObject(s).getJSONArray("data");

                    for(int i=0;i<jsonArray.length();i++){

                        PoojaPanditBookingDataModel dataModel = new PoojaPanditBookingDataModel();

                        JSONObject poojaObject = jsonArray.getJSONObject(i);

                        dataModel.setName(poojaObject.getJSONObject("puja").getString("name"));
                        dataModel.setStatus(poojaObject.getString("status"));
                        dataModel.setPrice(poojaObject.getString("price"));
                        dataModel.setDate(poojaObject.getString("created_at"));

                        if(poojaObject.getString("address_id").equalsIgnoreCase("null")){
                            dataModel.setAddress("Not Added");
                        }
                        else {
                            dataModel.setAddress(poojaObject.getJSONObject("address").getString("full_name")+"\n"+poojaObject.getJSONObject("address").getString("house_number")+", "+
                                    poojaObject.getJSONObject("address").getString("area")+", "+poojaObject.getJSONObject("address").getString("city"));
                        }



                        CommonClass.GLOBAL_LIST_CLASS.panditHistoryList.add(dataModel);

                    }

                    poojaPanditOrderHistoryAdapter = new PoojaPanditOrderHistoryAdapter(CommonClass.GLOBAL_LIST_CLASS.panditHistoryList);
                    recyclerView.setAdapter(poojaPanditOrderHistoryAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(VolleyError error, String errorMessage) {
                dialog.cancel();
                Log.d("zxcv ", errorMessage);
                ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, PanditBookingHistoryActivity.this) {
                    @Override
                    public void setAction(boolean action) {
                        if (action)
                            getPanditOrderHistory();
                    }
                };
                apiErrorAction.createDialog();
            }
        }.start();
    }
}