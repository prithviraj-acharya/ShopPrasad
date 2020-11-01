package com.prithviraj.shopprasad.activities.customer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.prithviraj.myvollyimplementation.UtilityClasses.ApiErrorAction;
import com.prithviraj.myvollyimplementation.VolleyServiceCall;
import com.prithviraj.shopprasad.R;
import com.prithviraj.shopprasad.adapters.AddressListAdapter;
import com.prithviraj.shopprasad.adapters.PoojaListForCustomerAdapter;
import com.prithviraj.shopprasad.dataModelClasses.AddressDataModel;
import com.prithviraj.shopprasad.interfaces.AddressButtonInterfaces;
import com.prithviraj.shopprasad.utils.CommonClass;
import com.prithviraj.shopprasad.utils.SharedPreference;
import com.prithviraj.shopprasad.utils.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyAddressActivity extends AppCompatActivity {


    SharedPreference sharedPreference;

    Button addNewAddress;
    ImageView backIcon;
    RecyclerView recyclerView;
    AddressListAdapter addressListAdapter;
    TextView noAddressAdded;

    boolean isSelectAddress;

    public static Activity myAddressActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);

        myAddressActivity = this;

        isSelectAddress = getIntent().getBooleanExtra("isSelectAddress",false);

        init();
        onClickListeners();
        getAddresses();

        CommonClass.GLOBAL_VARIABLE_CLASS.addressButtonInterfaces = new AddressButtonInterfaces() {
            @Override
            public void passPosition(final int position, boolean isDelete) {
                if(isDelete){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MyAddressActivity.this);
                    builder1.setMessage("Are you sure you want to delete this address?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    removeAddress(CommonClass.GLOBAL_LIST_CLASS.addressList.get(position).getAddressId());
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }else {
                    Intent in = new Intent(MyAddressActivity.this, AddNewAddresses.class);
                    in.putExtra("isEditing", true);
                    in.putExtra("position", position);
                    startActivity(in);
                }
            }
        };
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getAddresses();
    }

    void init(){


        sharedPreference = new SharedPreference(MyAddressActivity.this);
        addNewAddress = findViewById(R.id.addNewAddress);

        backIcon = findViewById(R.id.imageView);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(MyAddressActivity.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        addressListAdapter = new AddressListAdapter(isSelectAddress);
        recyclerView.setAdapter(addressListAdapter);

        noAddressAdded = findViewById(R.id.noAddressAdded);

    }

    void onClickListeners(){

        addNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MyAddressActivity.this, AddNewAddresses.class);
                startActivity(in);
            }
        });

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void getAddresses() {

        CommonClass.GLOBAL_LIST_CLASS.addressList.clear();
        noAddressAdded.setVisibility(View.INVISIBLE);

        final ProgressDialog dialog = ProgressDialog.show(MyAddressActivity.this, "",
                "Loading. Please wait...", true);

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer "+sharedPreference.getUserToken());

        new VolleyServiceCall(Request.Method.GET, Url.MY_ADDRESS_LIST, header, null, null, MyAddressActivity.this) {
            @Override
            public void onResponse(String s) {
                // getDoctors(departmentId);

                dialog.cancel();

                Log.d("zxcv", s);

                try {
                    JSONArray array = new JSONObject(s).getJSONArray("data");

                    if(array.length()<1)
                        noAddressAdded.setVisibility(View.VISIBLE);



                    for(int i = 0;i<array.length();i++){
                        JSONObject jsonObject = array.getJSONObject(i);

                        AddressDataModel addressDataModel = new AddressDataModel();

                        addressDataModel.setSelected(i == 0);
                        addressDataModel.setAddressId(jsonObject.getInt("id"));
                        addressDataModel.setFullName(jsonObject.getString("full_name"));
                        addressDataModel.setHouseNumber(jsonObject.getString("house_number"));
                        addressDataModel.setPinCode(jsonObject.getString("pincode"));
                        addressDataModel.setArea(jsonObject.getString("area"));
                        addressDataModel.setPhone(jsonObject.getString("phone"));
                        addressDataModel.setLandmark(jsonObject.getString("landmark"));
                        addressDataModel.setCity(jsonObject.getString("city"));
                        addressDataModel.setStateId(jsonObject.getInt("state_id"));

                        CommonClass.GLOBAL_LIST_CLASS.addressList.add(addressDataModel);
                    }

                    addressListAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(VolleyError error, String errorMessage) {
                dialog.cancel();
                Log.d("zxcv ", errorMessage);
                ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, MyAddressActivity.this) {
                    @Override
                    public void setAction(boolean action) {
                        if (action)
                            getAddresses();
                    }
                };
                apiErrorAction.createDialog();
            }
        }.start();
    }

    public void removeAddress(final int addressId) {

        final ProgressDialog dialog = ProgressDialog.show(MyAddressActivity.this, "",
                "Loading. Please wait...", true);

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer "+sharedPreference.getUserToken());

        Log.d("zxcv",Url.REMOVE_ADDRESS+addressId+"/remove");

        new VolleyServiceCall(Request.Method.GET, Url.REMOVE_ADDRESS+addressId+"/remove", header, null, null, MyAddressActivity.this) {
            @Override
            public void onResponse(String s) {
                // getDoctors(departmentId);

                dialog.cancel();

                Log.d("zxcv", s);

                getAddresses();

            }

            @Override
            public void onError(VolleyError error, String errorMessage) {
                dialog.cancel();
                Log.d("zxcv ", errorMessage);
                ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, MyAddressActivity.this) {
                    @Override
                    public void setAction(boolean action) {
                        if (action)
                            removeAddress(addressId);
                    }
                };
                apiErrorAction.createDialog();
            }
        }.start();
    }
}