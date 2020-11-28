package com.prithviraj.shopprasad.activities.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.prithviraj.myvollyimplementation.UtilityClasses.ApiErrorAction;
import com.prithviraj.myvollyimplementation.VolleyServiceCall;
import com.prithviraj.shopprasad.R;
import com.prithviraj.shopprasad.dataModelClasses.PoojaDataModel;
import com.prithviraj.shopprasad.utils.CommonClass;
import com.prithviraj.shopprasad.utils.ErrorDialog;
import com.prithviraj.shopprasad.utils.SharedPreference;
import com.prithviraj.shopprasad.utils.Url;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PoojaDetailsActivity extends AppCompatActivity {

    SharedPreference sharedPreference;
    PoojaDataModel poojaDataModel;

    ImageView productImage, backIcon;
    TextView productName, productDesc, originalPrice, discountPrice, showMoreText;
    Button bookPuja;

    private String dayOfWeek;

    boolean showMoreBoolean = false;

    public static Activity PoojaDetailsActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pooja_details);

        PoojaDetailsActivity = this;

        init();
        onClickListeners();
    }


    void init() {

        sharedPreference = new SharedPreference(PoojaDetailsActivity.this);

        backIcon = findViewById(R.id.backIcon);

        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        productDesc = findViewById(R.id.productDesc);
        originalPrice = findViewById(R.id.originalPrice);
        discountPrice = findViewById(R.id.discountPrice);
        showMoreText = findViewById(R.id.textView6);
        bookPuja = findViewById(R.id.button7);


        poojaDataModel = CommonClass.GLOBAL_LIST_CLASS.poojaList.get(getIntent().getIntExtra("position",0));


        productName.setText(poojaDataModel.getPujaName());

        if (poojaDataModel.getPujaDesc().length() > 200) {
            showMoreText.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                productDesc.setText(Html.fromHtml(poojaDataModel.getPujaDesc().substring(0, 200) + "...", Html.FROM_HTML_MODE_COMPACT));
            } else {
                productDesc.setText(Html.fromHtml(poojaDataModel.getPujaDesc().substring(0, 200) + "..."));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                productDesc.setText(Html.fromHtml(poojaDataModel.getPujaDesc(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                productDesc.setText(Html.fromHtml(poojaDataModel.getPujaDesc()));
            }
        }

        //  productDesc.setText(productObject.getString("description").replaceAll("\\<.*?\\>", ""));

        if (!poojaDataModel.getPujaImage().equalsIgnoreCase("null"))
            Picasso.get()
                    .load(poojaDataModel.getPujaImage())
                    .into(productImage);


        originalPrice.setVisibility(View.INVISIBLE);
        discountPrice.setText(String.format("₹ %s", poojaDataModel.getPujaPrice()));




    }

    void onClickListeners() {
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        bookPuja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CommonClass.GLOBAL_VARIABLE_CLASS.poojaId = CommonClass.GLOBAL_LIST_CLASS.poojaList.get(getIntent().getIntExtra("position",0)).getId();
                CommonClass.GLOBAL_VARIABLE_CLASS.poojaPrice = CommonClass.GLOBAL_LIST_CLASS.poojaList.get(getIntent().getIntExtra("position",0)).getPujaPrice();

                Intent in = new Intent(PoojaDetailsActivity.this, MyAddressActivity.class);
                in.putExtra("isSelectAddress", true);
                startActivity(in);
//
//                // Get Current Date
//                final Calendar c = Calendar.getInstance();
//                int mYear = c.get(Calendar.YEAR);
//                int mMonth = c.get(Calendar.MONTH);
//                int mDay = c.get(Calendar.DAY_OF_MONTH);
//
//                Calendar minDate = Calendar.getInstance();
//                minDate.set(mYear, mMonth,mDay);
//
//                Calendar maxDate = Calendar.getInstance();
//                maxDate.set(mYear, mMonth+1,mDay+1);
//
//                DatePickerDialog datePickerDialog = new DatePickerDialog(PoojaDetailsActivity.this,
//                        new DatePickerDialog.OnDateSetListener() {
//
//                            @Override
//                            public void onDateSet(DatePicker view, int year,
//                                                  int monthOfYear, int dayOfMonth) {
//
//
//                                SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
//                                Date date = new Date(year, monthOfYear, dayOfMonth-1);
//                                dayOfWeek = simpledateformat.format(date);
//
//                                bookPooja(year + "-" + ((monthOfYear + 1)>9?(monthOfYear + 1):"0"+(monthOfYear + 1)) + "-" + (dayOfMonth>9?dayOfMonth:"0"+dayOfMonth));
//
//                                Log.d("zxcv", "onDateSet: "+dayOfWeek);
//                            }
//                        }, mYear, mMonth, mDay);
//
//                datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
//                datePickerDialog.show();
            }
        });


        showMoreText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!showMoreBoolean){
                    showMoreBoolean = true;
                    showMoreText.setText("Show less");

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            productDesc.setText(Html.fromHtml(poojaDataModel.getPujaDesc(), Html.FROM_HTML_MODE_COMPACT));
                        } else {
                            productDesc.setText(Html.fromHtml(poojaDataModel.getPujaDesc()));
                        }

                }else {
                    showMoreBoolean = false;
                    showMoreText.setText("Show more");

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            productDesc.setText(Html.fromHtml(poojaDataModel.getPujaDesc().substring(0, 200) + "...", Html.FROM_HTML_MODE_COMPACT));
                        } else {
                            productDesc.setText(Html.fromHtml(poojaDataModel.getPujaDesc().substring(0, 200) + "..."));
                        }

                }

            }
        });
    }

    public void bookPooja(final String date) {


        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + sharedPreference.getUserToken());

        Map<String, String> param = new HashMap<>();
        param.put("puja_id", String.valueOf(CommonClass.GLOBAL_VARIABLE_CLASS.poojaId));
        param.put("date", date);
        param.put("price", CommonClass.GLOBAL_VARIABLE_CLASS.poojaPrice);
        param.put("address_id", "9");


        Log.d("zxcv: ", param.toString());

        final ProgressDialog dialog = ProgressDialog.show(PoojaDetailsActivity.this, "",
                "Loading. Please wait...", true);

        String url;
        if(getIntent().getBooleanExtra("isPuja",true))
            url =  Url.BOOK_POOJA;
        else
            url = Url.BOOK_PANDIT;

        new VolleyServiceCall(Request.Method.POST, url, header, param, null, PoojaDetailsActivity.this) {
            @Override
            public void onResponse(String s) {
                dialog.cancel();

                Log.d("zxcv", s);
                try {
                    new ErrorDialog(new JSONObject(s).getString("msg"), PoojaDetailsActivity.this).showDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(VolleyError error, String errorMessage) {
                dialog.cancel();
                Log.d("zxcv ", errorMessage);
                ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, PoojaDetailsActivity.this) {
                    @Override
                    public void setAction(boolean action) {
                        if (action)
                            bookPooja(date);
                    }
                };
                apiErrorAction.createDialog();
            }
        }.start();

    }
}