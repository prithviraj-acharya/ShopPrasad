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
import com.prithviraj.shopprasad.adapters.PanditListForCustomerAdapter;
import com.prithviraj.shopprasad.adapters.PoojaListForCustomerAdapter;
import com.prithviraj.shopprasad.dataModelClasses.PoojaDataModel;
import com.prithviraj.shopprasad.utils.CommonClass;
import com.prithviraj.shopprasad.utils.SharedPreference;
import com.prithviraj.shopprasad.utils.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PanditActivity extends AppCompatActivity {

    ImageView backIcon;
    SharedPreference sharedPreference;

    RecyclerView recyclerView;
    PanditListForCustomerAdapter panditListForCustomerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pandit);

        init();
        onClickListeners();
        getPanditList();
    }

    void init(){
        sharedPreference = new SharedPreference(PanditActivity.this);
        backIcon = findViewById(R.id.imageView);

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(PanditActivity.this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        panditListForCustomerAdapter = new PanditListForCustomerAdapter();
        recyclerView.setAdapter(panditListForCustomerAdapter);

    }

    void onClickListeners(){
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void getPanditList() {

        CommonClass.GLOBAL_LIST_CLASS.panditList.clear();

        final ProgressDialog dialog = ProgressDialog.show(PanditActivity.this, "",
                "Loading. Please wait...", true);

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer "+sharedPreference.getUserToken());

        new VolleyServiceCall(Request.Method.GET, Url.PANDIT_LIST, header, null, null, PanditActivity.this) {
            @Override
            public void onResponse(String s) {
                dialog.cancel();

                Log.d("zxcv", s);

                try {
                    JSONArray pujaArray = new JSONObject(s).getJSONArray("data");

                    for(int i = 0;i<pujaArray.length();i++){
                        PoojaDataModel poojaDataModel = new PoojaDataModel();

                        poojaDataModel.setPujaName(pujaArray.getJSONObject(i).getString("full_name"));
                        poojaDataModel.setPujaImage(pujaArray.getJSONObject(i).getString("profile_image"));
                        poojaDataModel.setId(pujaArray.getJSONObject(i).getInt("id"));

                        CommonClass.GLOBAL_LIST_CLASS.panditList.add(poojaDataModel);
                    }

                    panditListForCustomerAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(VolleyError error, String errorMessage) {
                dialog.cancel();
                Log.d("zxcv ", errorMessage);
                ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, PanditActivity.this) {
                    @Override
                    public void setAction(boolean action) {
                        if (action)
                            getPanditList();
                    }
                };
                apiErrorAction.createDialog();
            }
        }.start();

    }
}