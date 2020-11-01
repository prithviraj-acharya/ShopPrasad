package com.prithviraj.shopprasad.activities.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.prithviraj.myvollyimplementation.UtilityClasses.ApiErrorAction;
import com.prithviraj.myvollyimplementation.VolleyServiceCall;
import com.prithviraj.shopprasad.R;
import com.prithviraj.shopprasad.adapters.PoojaListForCustomerAdapter;
import com.prithviraj.shopprasad.dataModelClasses.PoojaDataModel;
import com.prithviraj.shopprasad.interfaces.ClickForProductDetails;
import com.prithviraj.shopprasad.interfaces.ClickPoojaList;
import com.prithviraj.shopprasad.utils.CommonClass;
import com.prithviraj.shopprasad.utils.ErrorDialog;
import com.prithviraj.shopprasad.utils.SharedPreference;
import com.prithviraj.shopprasad.utils.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PoojaActivity extends AppCompatActivity {

    ImageView backIcon;
    SharedPreference sharedPreference;

    RecyclerView recyclerView;
    PoojaListForCustomerAdapter poojaListForCustomerAdapter;

    private String dayOfWeek;
    private int poojaId;
    private String poojaPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pooja);


        init();
        onClickListeners();
        getPujaList();

        CommonClass.GLOBAL_VARIABLE_CLASS.clickPoojaList = new ClickPoojaList() {
            @Override
            public void passPoojaProduct(int position) {

                poojaId = CommonClass.GLOBAL_LIST_CLASS.poojaList.get(position).getId();
                poojaPrice = CommonClass.GLOBAL_LIST_CLASS.poojaList.get(position).getPujaPrice();

                // Get Current Date
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                Calendar minDate = Calendar.getInstance();
                minDate.set(mYear, mMonth,mDay);

                Calendar maxDate = Calendar.getInstance();
                maxDate.set(mYear, mMonth+1,mDay+1);

                DatePickerDialog datePickerDialog = new DatePickerDialog(PoojaActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {


                                SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
                                Date date = new Date(year, monthOfYear, dayOfMonth-1);
                                dayOfWeek = simpledateformat.format(date);

                                bookPooja(year + "-" + ((monthOfYear + 1)>9?(monthOfYear + 1):"0"+(monthOfYear + 1)) + "-" + (dayOfMonth>9?dayOfMonth:"0"+dayOfMonth));

                                Log.d("zxcv", "onDateSet: "+dayOfWeek);
                            }
                        }, mYear, mMonth, mDay);

                datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
                datePickerDialog.show();
            }
        };

        CommonClass.GLOBAL_VARIABLE_CLASS.clickForProductDetails = new ClickForProductDetails() {
            @Override
            public void passPoojaPosition(int position) {

                Intent in = new Intent(PoojaActivity.this, PoojaDetailsActivity.class);
                in.putExtra("position", position);
                in.putExtra("isPuja",  getIntent().getBooleanExtra("isPuja",true));

                startActivity(in);
            }
        };

    }

    void init() {

        sharedPreference = new SharedPreference(PoojaActivity.this);

        backIcon = findViewById(R.id.imageView);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(PoojaActivity.this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        poojaListForCustomerAdapter = new PoojaListForCustomerAdapter();
        recyclerView.setAdapter(poojaListForCustomerAdapter);


    }

    void onClickListeners() {
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void getPujaList() {

        CommonClass.GLOBAL_LIST_CLASS.poojaList.clear();

        final ProgressDialog dialog = ProgressDialog.show(PoojaActivity.this, "",
                "Loading. Please wait...", true);

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + sharedPreference.getUserToken());

        new VolleyServiceCall(Request.Method.GET, Url.PUJA_LIST, header, null, null, PoojaActivity.this) {
            @Override
            public void onResponse(String s) {
                dialog.cancel();

                Log.d("zxcv", s);

                try {
                    JSONArray pujaArray = new JSONObject(s).getJSONArray("data");

                    for (int i = 0; i < pujaArray.length(); i++) {
                        PoojaDataModel poojaDataModel = new PoojaDataModel();

                        poojaDataModel.setPujaName(pujaArray.getJSONObject(i).getString("name"));
                        poojaDataModel.setPujaPrice(pujaArray.getJSONObject(i).getString("price"));
                        poojaDataModel.setPujaImage(pujaArray.getJSONObject(i).getString("image"));
                        poojaDataModel.setPujaDesc(pujaArray.getJSONObject(i).getString("description"));
                        poojaDataModel.setId(pujaArray.getJSONObject(i).getInt("id"));

                        CommonClass.GLOBAL_LIST_CLASS.poojaList.add(poojaDataModel);
                    }

                    poojaListForCustomerAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(VolleyError error, String errorMessage) {
                dialog.cancel();
                Log.d("zxcv ", errorMessage);
                ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, PoojaActivity.this) {
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

    public void bookPooja(final String date) {


            Map<String, String> header = new HashMap<>();
            header.put("Authorization", "Bearer " + sharedPreference.getUserToken());

            Map<String, String> param = new HashMap<>();
            param.put("puja_id", String.valueOf(poojaId));
            param.put("date", date);
            param.put("price", poojaPrice);


            Log.d("zxcv: ", param.toString());

            final ProgressDialog dialog = ProgressDialog.show(PoojaActivity.this, "",
                    "Loading. Please wait...", true);

            String url;
            if(getIntent().getBooleanExtra("isPuja",true))
                url =  Url.BOOK_POOJA;
            else
                url = Url.BOOK_PANDIT;

            new VolleyServiceCall(Request.Method.POST, url, header, param, null, PoojaActivity.this) {
                @Override
                public void onResponse(String s) {
                    dialog.cancel();

                    Log.d("zxcv", s);
                    try {
                        new ErrorDialog(new JSONObject(s).getString("msg"), PoojaActivity.this).showDialog();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onError(VolleyError error, String errorMessage) {
                    dialog.cancel();
                    Log.d("zxcv ", errorMessage);
                    ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, PoojaActivity.this) {
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