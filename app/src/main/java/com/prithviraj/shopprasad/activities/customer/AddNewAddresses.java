package com.prithviraj.shopprasad.activities.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.prithviraj.myvollyimplementation.UtilityClasses.ApiErrorAction;
import com.prithviraj.myvollyimplementation.VolleyServiceCall;
import com.prithviraj.shopprasad.R;
import com.prithviraj.shopprasad.dataModelClasses.AddressDataModel;
import com.prithviraj.shopprasad.utils.CommonClass;
import com.prithviraj.shopprasad.utils.ErrorDialog;
import com.prithviraj.shopprasad.utils.SharedPreference;
import com.prithviraj.shopprasad.utils.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddNewAddresses extends AppCompatActivity implements  AdapterView.OnItemSelectedListener {

    Spinner spin;
    EditText edtName,edtPin,edtAddress,edtArea,phone,landmark,city;
    Button addNewAddress;
    ImageView backIcon;
    AddressDataModel addressDataModel;

    SharedPreference sharedPreference;
    ArrayList<Integer> stateIds = new ArrayList<>();

    boolean isEditing;

    int stateId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_addresses);

        isEditing = getIntent().getBooleanExtra("isEditing", false);

        init();
        onClickListeners();
        getStates();

    }

    void init(){
        sharedPreference = new SharedPreference(AddNewAddresses.this);

        spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);

        edtName = findViewById(R.id.edtName);
        edtPin = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        edtArea = findViewById(R.id.timeOfBirth);
        phone = findViewById(R.id.phone);
        landmark = findViewById(R.id.landmark);
        city = findViewById(R.id.city);



        if(isEditing){
            addressDataModel = CommonClass.GLOBAL_LIST_CLASS.addressList.get(getIntent().getIntExtra("position",0));

            edtName.setText(addressDataModel.getFullName());
            edtPin.setText(addressDataModel.getPinCode());
            edtAddress.setText(addressDataModel.getHouseNumber());
            edtArea.setText(addressDataModel.getArea());
            phone.setText(addressDataModel.getPhone());
            landmark.setText(addressDataModel.getLandmark());
            city.setText(addressDataModel.getCity());
        }

        addNewAddress = findViewById(R.id.button4);
        backIcon = findViewById(R.id.imageView);
    }

    void onClickListeners(){

        addNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadNewAddress();
            }
        });

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        stateId = stateIds.get(position);

        // Toast.makeText(getApplicationContext(),country[position] , Toast.LENGTH_LONG).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    public void uploadNewAddress() {


        if (edtName.getText().toString().trim().equals("")) {
            new ErrorDialog("Please enter a valid name.", AddNewAddresses.this).showDialog();
        } else if (edtPin.getText().toString().trim().equals("") || edtPin.getText().toString().trim().length() < 6) {
            new ErrorDialog("Please enter a valid pin number.", AddNewAddresses.this).showDialog();
        } else if (edtAddress.getText().toString().trim().equals("")) {
            new ErrorDialog("Please enter a valid house number.", AddNewAddresses.this).showDialog();
        } else if (edtArea.getText().toString().trim().equals("")) {
            new ErrorDialog("Please enter a valid Area.", AddNewAddresses.this).showDialog();
        }else if (phone.getText().toString().trim().equals("") || edtPin.getText().toString().trim().length() < 6) {
            new ErrorDialog("Please enter a valid Phone number.", AddNewAddresses.this).showDialog();
        }else if (landmark.getText().toString().trim().equals("")) {
            new ErrorDialog("Please enter a valid landmark.", AddNewAddresses.this).showDialog();
        } else if (city.getText().toString().trim().equals("")) {
            new ErrorDialog("Please enter a valid city.", AddNewAddresses.this).showDialog();
        }else if (stateId == 0) {
            new ErrorDialog("Please enter a valid state.", AddNewAddresses.this).showDialog();
        } else {

            Map<String, String> header = new HashMap<>();
            header.put("Authorization", "Bearer " + sharedPreference.getUserToken());

            Map<String, String> param = new HashMap<>();
            param.put("full_name", edtName.getText().toString().trim());
            param.put("pincode", edtPin.getText().toString().trim());
            param.put("house_number", edtAddress.getText().toString().trim());
            param.put("area", edtArea.getText().toString().trim());
            param.put("phone", phone.getText().toString().trim());
            param.put("landmark", landmark.getText().toString().trim());
            param.put("city", city.getText().toString().trim());
            param.put("state_id", String.valueOf(stateId));

            if(isEditing)
                param.put("id", String.valueOf(addressDataModel.getAddressId()));

            Log.d("zxcv: ", param.toString());

            final ProgressDialog dialog = ProgressDialog.show(AddNewAddresses.this, "",
                    "Loading. Please wait...", true);

            new VolleyServiceCall(Request.Method.POST, isEditing?Url.UPDATE_ADDRESS:Url.ADD_NEW_ADDRESS, header, param, null, AddNewAddresses.this) {
                @Override
                public void onResponse(String s) {
                    dialog.cancel();

                    Log.d("zxcv", s);

                    try {
                        JSONObject jsonObject = new JSONObject(s);

                        if (jsonObject.getBoolean("success")) {
                            new ErrorDialog(jsonObject.getString("message"), AddNewAddresses.this).showDialog();


                        } else {

                            new ErrorDialog(jsonObject.getJSONArray("msg").getString(0), AddNewAddresses.this).showDialog();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onError(VolleyError error, String errorMessage) {
                    dialog.cancel();
                    Log.d("zxcv ", errorMessage);
                    ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, AddNewAddresses.this) {
                        @Override
                        public void setAction(boolean action) {
                            if (action)
                                uploadNewAddress();
                        }
                    };
                    apiErrorAction.createDialog();
                }
            }.start();
        }
    }

    public void getStates() {

        final ProgressDialog dialog = ProgressDialog.show(AddNewAddresses.this, "",
                "Loading. Please wait...", true);

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer "+sharedPreference.getUserToken());

        new VolleyServiceCall(Request.Method.GET, Url.STATES, header, null, null, AddNewAddresses.this) {
            @Override
            public void onResponse(String s) {
                // getDoctors(departmentId);

                dialog.cancel();

                Log.d("zxcv", s);
                ArrayList<String> departments = new ArrayList<>();
                try {
                    JSONArray array = new JSONObject(s).getJSONArray("data");

                    stateIds.add(0);
                    departments.add("Please select a state");

                    for(int i = 0;i<array.length();i++){
                        JSONObject jsonObject = array.getJSONObject(i);
                        stateIds.add(jsonObject.getInt("id"));
                        departments.add(jsonObject.getString("name"));
                    }
                    ArrayAdapter aa = new ArrayAdapter(AddNewAddresses.this,android.R.layout.simple_spinner_item,departments);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //Setting the ArrayAdapter data on the Spinner
                    spin.setAdapter(aa);

                    if(isEditing)
                    spin.setSelection(stateIds.indexOf(addressDataModel.getStateId()));

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(VolleyError error, String errorMessage) {
                dialog.cancel();
                Log.d("zxcv ", errorMessage);
                ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, AddNewAddresses.this) {
                    @Override
                    public void setAction(boolean action) {
                        if (action)
                            getStates();
                    }
                };
                apiErrorAction.createDialog();
            }
        }.start();
    }
}