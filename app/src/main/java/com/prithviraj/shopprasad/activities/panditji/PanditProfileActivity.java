package com.prithviraj.shopprasad.activities.panditji;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.opensooq.supernova.gligar.GligarPicker;
import com.prithviraj.myvollyimplementation.DataModelClasses.DataPart;
import com.prithviraj.myvollyimplementation.UtilityClasses.ApiErrorAction;
import com.prithviraj.myvollyimplementation.VolleyServiceCall;
import com.prithviraj.shopprasad.R;
import com.prithviraj.shopprasad.activities.customer.MyAddressActivity;
import com.prithviraj.shopprasad.activities.customer.MyProfileActivity;
import com.prithviraj.shopprasad.activities.customer.PoojaActivity;
import com.prithviraj.shopprasad.adapters.AddressListAdapter;
import com.prithviraj.shopprasad.adapters.PanditProfilePujaListAdapter;
import com.prithviraj.shopprasad.dataModelClasses.PanditProfilePujaDataModel;
import com.prithviraj.shopprasad.dataModelClasses.PoojaDataModel;
import com.prithviraj.shopprasad.interfaces.AddPanditProfilePujaInterfaces;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PanditProfileActivity extends AppCompatActivity implements  AdapterView.OnItemSelectedListener {

    ImageView  backIcon, profile_image, add_icon, addNewPuja;
    TextView pujaPrice;
    EditText edtName;
    TextView edtPhone;
    Button updateButton;

    SharedPreference sharedPreference;

    RecyclerView recyclerView;
    PanditProfilePujaListAdapter adapter;

    Spinner spin;

    ArrayList<Integer> pujaIds = new ArrayList<>();
    int pujaID;
    String pujaName;

    GligarPicker imagePicker;
    byte profileImageArray[] = null;
    long length;
    String path1 = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pandit_profile);

        init();
        onClickListeners();
        getPujaList();
        getProfile();

        CommonClass.GLOBAL_VARIABLE_CLASS.addPanditProfilePujaInterfaces = new AddPanditProfilePujaInterfaces() {
            @Override
            public void removePuja(int position) {
                CommonClass.GLOBAL_LIST_CLASS.panditPujaList.remove(position);
                adapter.notifyDataSetChanged();
            }
        };
    }

    void init(){

        sharedPreference = new SharedPreference(PanditProfileActivity.this);

        backIcon = findViewById(R.id.imageView);
        addNewPuja = findViewById(R.id.imageView3);
        pujaPrice = findViewById(R.id.edtName5);
        updateButton = findViewById(R.id.button4);

        profile_image = findViewById(R.id.profile_image);
        add_icon = findViewById(R.id.add_icon);

        imagePicker = new GligarPicker();
        imagePicker.limit(1);
        imagePicker.requestCode(1001);
        imagePicker.withActivity(PanditProfileActivity.this);

        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);

        spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(PanditProfileActivity.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new PanditProfilePujaListAdapter(PanditProfileActivity.this);
        recyclerView.setAdapter(adapter);
    }

    void onClickListeners(){
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addNewPuja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PanditProfilePujaDataModel panditProfilePujaDataModel = new PanditProfilePujaDataModel();
                panditProfilePujaDataModel.setPujaName(pujaName);
                panditProfilePujaDataModel.setPujaId(pujaID);
                panditProfilePujaDataModel.setAddedPujaPrice(pujaPrice.getText().toString());

                CommonClass.GLOBAL_LIST_CLASS.panditPujaList.add(panditProfilePujaDataModel);

                adapter.notifyDataSetChanged();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePicker.show();
            }
        });

        add_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePicker.show();
            }
        });
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        pujaID = pujaIds.get(position);
        pujaName = CommonClass.GLOBAL_LIST_CLASS.staticPujaList.get(position);

        // Toast.makeText(getApplicationContext(),country[position] , Toast.LENGTH_LONG).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
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

                Picasso.get()
                        .load(new File(pathsList[0]))
                        .transform(new CircleTransform())
                        .into(profile_image);


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

        uploadProfileImage();


    }

    public void uploadProfileImage() {


        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + sharedPreference.getUserToken());

        Log.d("token", "Bearer "+sharedPreference.getUserToken());

        Map<String, DataPart> part = new HashMap<>();

        if (profileImageArray != null)
            part.put("profile_image", new DataPart("profile_image", profileImageArray));


        Map<String, String> param = new HashMap<>();

        Log.d("zxcv: ", param.toString());

        final ProgressDialog dialog = ProgressDialog.show(PanditProfileActivity.this, "",
                "Loading. Please wait...", true);

        new VolleyServiceCall(Request.Method.POST, Url.PANDIT_PROFILE_IMAGE_UPDATE, header, param, part, PanditProfileActivity.this) {
            @Override
            public void onResponse(String s) {
                dialog.cancel();

                Log.d("zxcv", s);

                try {
                    JSONObject jsonObject = new JSONObject(s);


                    new ErrorDialog(jsonObject.getString("message"), PanditProfileActivity.this).showDialog();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(VolleyError error, String errorMessage) {
                dialog.cancel();
                Log.d("zxcv ", errorMessage);
                ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, PanditProfileActivity.this) {
                    @Override
                    public void setAction(boolean action) {
                        if (action)
                            uploadProfileImage();
                    }
                };
                apiErrorAction.createDialog();
            }
        }.start();


    }

    public void getPujaList() {

        CommonClass.GLOBAL_LIST_CLASS.staticPujaList.clear();

        final ProgressDialog dialog = ProgressDialog.show(PanditProfileActivity.this, "",
                "Loading. Please wait...", true);

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + sharedPreference.getUserToken());

        new VolleyServiceCall(Request.Method.GET, Url.PUJA_LIST, header, null, null, PanditProfileActivity.this) {
            @Override
            public void onResponse(String s) {
                dialog.cancel();

                Log.d("zxcv", s);

                try {
                    JSONArray pujaArray = new JSONObject(s).getJSONArray("data");

                    pujaIds.add(0);
                    CommonClass.GLOBAL_LIST_CLASS.staticPujaList.add("Add puja");

                    for (int i = 0; i < pujaArray.length(); i++) {

                        pujaIds.add(pujaArray.getJSONObject(i).getInt("id"));
                        CommonClass.GLOBAL_LIST_CLASS.staticPujaList.add(pujaArray.getJSONObject(i).getString("name"));
                    }

                    ArrayAdapter aa = new ArrayAdapter(PanditProfileActivity.this,android.R.layout.simple_spinner_item, CommonClass.GLOBAL_LIST_CLASS.staticPujaList);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //Setting the ArrayAdapter data on the Spinner
                    spin.setAdapter(aa);




                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(VolleyError error, String errorMessage) {
                dialog.cancel();
                Log.d("zxcv ", errorMessage);
                ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, PanditProfileActivity.this) {
                    @Override
                    public void setAction(boolean action) {
                        if (action)
                            getPujaList();
                    }
                };
                apiErrorAction.createDialog();
            }
        }.start();

    }

    public void getProfile() {


        final ProgressDialog dialog = ProgressDialog.show(PanditProfileActivity.this, "",
                "Loading. Please wait...", true);

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + sharedPreference.getUserToken());

        Log.d("zxcv ", "Bearer " + sharedPreference.getUserToken());

        new VolleyServiceCall(Request.Method.GET, Url.PANDIT_PROFILE, header, null, null, PanditProfileActivity.this) {
            @Override
            public void onResponse(String s) {
                dialog.cancel();

                Log.d("zxcv", s);
                try {
                    edtName.setText(new JSONObject(s).getJSONObject("data").getString("full_name").equalsIgnoreCase("null") ? "" : new JSONObject(s).getJSONObject("data").getString("full_name"));
                    edtName.setSelection(edtName.getText().length());
                    edtPhone.setText(new JSONObject(s).getJSONObject("data").getString("phone").equalsIgnoreCase("null") ? "Not added yet" : new JSONObject(s).getJSONObject("data").getString("phone"));

                    if (!new JSONObject(s).getJSONObject("data").getString("profile_image").equals("null")) {
                        Picasso.get()
                                .load(new JSONObject(s).getJSONObject("data").getString("profile_image"))
                                .transform(new CircleTransform())
                                .into(profile_image);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(VolleyError error, String errorMessage) {
                dialog.cancel();
                Log.d("zxcv ", errorMessage);
                ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, PanditProfileActivity.this) {
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

    public void updateProfile() {

        final ProgressDialog dialog = ProgressDialog.show(PanditProfileActivity.this, "",
                "Loading. Please wait...", true);

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + sharedPreference.getUserToken());

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("full_name", edtName.getText().toString().trim());
            jsonObject.put("phone", edtPhone.getText().toString().trim());

            JSONArray productsArray = new JSONArray();

            for (int i = 0; i < CommonClass.GLOBAL_LIST_CLASS.panditPujaList.size(); i++) {

                PanditProfilePujaDataModel panditProfilePujaDataModel = CommonClass.GLOBAL_LIST_CLASS.panditPujaList.get(i);
                JSONObject ob = new JSONObject();

                ob.put("puja_id", panditProfilePujaDataModel.getPujaId());
                ob.put("price", panditProfilePujaDataModel.getAddedPujaPrice());

                productsArray.put(ob);

            }

            jsonObject.put("puja", productsArray);



            new VolleyServiceCall(Request.Method.POST, Url.PANDIT_PROFILE_UPDATE, header, jsonObject, PanditProfileActivity.this) {

                @Override
                public void onResponse(String s) {
                    dialog.cancel();

                    Log.d("zxcv", s);
                    try {
                        new ErrorDialog(new JSONObject(s).getString("message"), PanditProfileActivity.this).showDialog();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onError(VolleyError error, String errorMessage) {
                    dialog.cancel();
                    Log.d("zxcv ", errorMessage);
                    ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, PanditProfileActivity.this) {
                        @Override
                        public void setAction(boolean action) {
                            if (action)
                                updateProfile();
                        }
                    };
                    apiErrorAction.createDialog();
                }
            }.start();


        } catch (JSONException e) {
            e.printStackTrace();
        }




    }
}