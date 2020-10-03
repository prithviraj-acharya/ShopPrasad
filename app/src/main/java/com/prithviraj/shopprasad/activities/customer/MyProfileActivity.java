package com.prithviraj.shopprasad.activities.customer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.opensooq.supernova.gligar.GligarPicker;
import com.prithviraj.myvollyimplementation.DataModelClasses.DataPart;
import com.prithviraj.myvollyimplementation.UtilityClasses.ApiErrorAction;
import com.prithviraj.myvollyimplementation.VolleyServiceCall;
import com.prithviraj.shopprasad.R;
import com.prithviraj.shopprasad.utils.CircleTransform;
import com.prithviraj.shopprasad.utils.ErrorDialog;
import com.prithviraj.shopprasad.utils.SharedPreference;
import com.prithviraj.shopprasad.utils.Url;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MyProfileActivity extends AppCompatActivity {

    ImageView backIcon, profile_image, add_icon;
    SharedPreference sharedPreference;
    EditText edtName;
    TextView edtPhone;
    Button updateButton;

    GligarPicker imagePicker;
    byte profileImageArray[] = null;
    long length;
    String path1 = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        init();
        onClickListeners();
        getProfile();
    }

    void init() {
        backIcon = findViewById(R.id.imageView);
        sharedPreference = new SharedPreference(MyProfileActivity.this);

        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        updateButton = findViewById(R.id.button4);

        profile_image = findViewById(R.id.profile_image);
        add_icon = findViewById(R.id.add_icon);

        imagePicker = new GligarPicker();
        imagePicker.limit(1);
        imagePicker.requestCode(1001);
        imagePicker.withActivity(MyProfileActivity.this);
    }

    void onClickListeners() {
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtName.getText().toString().trim().equalsIgnoreCase("")) {
                    new ErrorDialog("Please enter a valid name.", MyProfileActivity.this).showDialog();
                } else {
                    updateProfile();
                }

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

    public void getProfile() {


        final ProgressDialog dialog = ProgressDialog.show(MyProfileActivity.this, "",
                "Loading. Please wait...", true);

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + sharedPreference.getUserToken());

        Log.d("zxcv ", "Bearer " + sharedPreference.getUserToken());

        new VolleyServiceCall(Request.Method.GET, Url.PROFILE, header, null, null, MyProfileActivity.this) {
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
                ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, MyProfileActivity.this) {
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

        final ProgressDialog dialog = ProgressDialog.show(MyProfileActivity.this, "",
                "Loading. Please wait...", true);

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + sharedPreference.getUserToken());

        Map<String, String> param = new HashMap<>();
        param.put("full_name", edtName.getText().toString().trim());
        param.put("phone", edtPhone.getText().toString().trim());

        new VolleyServiceCall(Request.Method.POST, Url.PROFILE_UPDATE, header, param, null, MyProfileActivity.this) {

            @Override
            public void onResponse(String s) {
                dialog.cancel();

                Log.d("zxcv", s);
                try {
                    new ErrorDialog(new JSONObject(s).getString("message"), MyProfileActivity.this).showDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(VolleyError error, String errorMessage) {
                dialog.cancel();
                Log.d("zxcv ", errorMessage);
                ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, MyProfileActivity.this) {
                    @Override
                    public void setAction(boolean action) {
                        if (action)
                            updateProfile();
                    }
                };
                apiErrorAction.createDialog();
            }
        }.start();

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

        Map<String, DataPart> part = new HashMap<>();

        if (profileImageArray != null)
            part.put("profile_image", new DataPart("profile_image", profileImageArray));


        Map<String, String> param = new HashMap<>();

        Log.d("zxcv: ", param.toString());

        final ProgressDialog dialog = ProgressDialog.show(MyProfileActivity.this, "",
                "Loading. Please wait...", true);

        new VolleyServiceCall(Request.Method.POST, Url.PROFILE_IMAGE_UPDATE, header, param, part, MyProfileActivity.this) {
            @Override
            public void onResponse(String s) {
                dialog.cancel();

                Log.d("zxcv", s);

                try {
                    JSONObject jsonObject = new JSONObject(s);


                    new ErrorDialog(jsonObject.getString("message"), MyProfileActivity.this).showDialog();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(VolleyError error, String errorMessage) {
                dialog.cancel();
                Log.d("zxcv ", errorMessage);
                ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, MyProfileActivity.this) {
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
}