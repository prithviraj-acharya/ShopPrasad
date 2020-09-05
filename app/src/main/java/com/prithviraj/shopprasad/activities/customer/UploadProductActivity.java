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
import android.widget.ImageView;

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

public class UploadProductActivity extends AppCompatActivity {

    SharedPreference sharedPreference;
    ImageView backIcon,addIcon,userPhoto;
    GligarPicker imagePicker;
    Button uploadOrder;

    byte profileImageArray[]=null;
    long length;
    String path1 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_product);

        init();
        onClickListeners();
    }

    void init(){

        backIcon = findViewById(R.id.imageView);
        addIcon = findViewById(R.id.add_icon);
        userPhoto = findViewById(R.id.userPhoto);
        uploadOrder = findViewById(R.id.button5);

        imagePicker = new GligarPicker();
        imagePicker.limit(1);
        imagePicker.requestCode(1001);
        imagePicker.withActivity(UploadProductActivity.this);
    }

    void onClickListeners(){
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePicker.show();

            }
        });

        uploadOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadOrder();
            }
        });

        sharedPreference = new SharedPreference(UploadProductActivity.this);
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
                        .into(userPhoto);

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

        Log.d("zxcv", "getByteArray: "+profileImageArray);


    }

    public void uploadOrder() {


        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer "+sharedPreference.getUserToken());

        Map<String, DataPart> part = new HashMap<>();

        if(profileImageArray!=null)
            part.put("image", new DataPart("image", profileImageArray));


        Map<String, String> param = new HashMap<>();

        Log.d("zxcv: ", param.toString());

        final ProgressDialog dialog = ProgressDialog.show(UploadProductActivity.this, "",
                "Loading. Please wait...", true);

        new VolleyServiceCall(Request.Method.POST, Url.UPLOAD_ORDER, header, param, part, UploadProductActivity.this) {
            @Override
            public void onResponse(String s) {
                dialog.cancel();

                Log.d("zxcv", s);

                try {
                    JSONObject jsonObject = new JSONObject(s);


                    new ErrorDialog(jsonObject.getString("message"), UploadProductActivity.this).showDialog();


                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

            @Override
            public void onError(VolleyError error, String errorMessage) {
                dialog.cancel();
                Log.d("zxcv ", errorMessage);
                ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, UploadProductActivity.this) {
                    @Override
                    public void setAction(boolean action) {
                        if (action)
                            uploadOrder();
                    }
                };
                apiErrorAction.createDialog();
            }
        }.start();


    }

}