package com.prithviraj.shopprasad.activities.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.prithviraj.myvollyimplementation.UtilityClasses.ApiErrorAction;
import com.prithviraj.myvollyimplementation.VolleyServiceCall;
import com.prithviraj.shopprasad.R;
import com.prithviraj.shopprasad.adapters.AddressListAdapter;
import com.prithviraj.shopprasad.adapters.AddressListAdapterForCheckout;
import com.prithviraj.shopprasad.adapters.FeaturedProductListForCustomerAdapter;
import com.prithviraj.shopprasad.dataModelClasses.AddressDataModel;
import com.prithviraj.shopprasad.dataModelClasses.CartDataModel;
import com.prithviraj.shopprasad.interfaces.SelectAddressButtonInterfaces;
import com.prithviraj.shopprasad.interfaces.SelectAddressForCheckout;
import com.prithviraj.shopprasad.utils.CommonClass;
import com.prithviraj.shopprasad.utils.ErrorDialog;
import com.prithviraj.shopprasad.utils.SharedPreference;
import com.prithviraj.shopprasad.utils.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CheckoutActivity extends AppCompatActivity {

    TextView totalItems;
    SharedPreference sharedPreference;
    //TextView userName, userNumber, userAddress, userPinCode;
    int addressId=-1;

    Button placeOrderButton, addNew;
    ImageView backIcon;

    String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    int GOOGLE_PAY_REQUEST_CODE = 123;

    CheckBox googlePayCheckBox, cashOnDeliveryCheckBox;

    RecyclerView recyclerView;
    AddressListAdapterForCheckout addressListAdapterForCheckout;

    int positionOfAddress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        init();
        onClickListeners();
        getAddresses(positionOfAddress);



        CommonClass.GLOBAL_VARIABLE_CLASS.selectAddressForCheckout = new SelectAddressForCheckout() {
            @Override
            public void addressPosition(int position) {

                addressId = CommonClass.GLOBAL_LIST_CLASS.addressList.get(position).getAddressId();
                positionOfAddress = position;
                getAddresses(positionOfAddress);

            }
        };
    }

    void init(){

        sharedPreference = new SharedPreference(CheckoutActivity.this);

        totalItems = findViewById(R.id.totalItems);
        totalItems.setText(getIntent().getStringExtra("totalItems"));

        backIcon = findViewById(R.id.backIcon);
        addNew = findViewById(R.id.button6);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(CheckoutActivity.this, RecyclerView.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        addressListAdapterForCheckout= new AddressListAdapterForCheckout();
        recyclerView.setAdapter(addressListAdapterForCheckout);

        placeOrderButton = findViewById(R.id.button);

        googlePayCheckBox = findViewById(R.id.googlePayCheckBox);
        cashOnDeliveryCheckBox = findViewById(R.id.cashOnDeliveryCheckBox);
    }

    void onClickListeners() {
        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(addressId==-1){
                    new ErrorDialog("Please select at-least one address.", CheckoutActivity.this).showDialog();
                }else {
                    if (!googlePayCheckBox.isChecked() && !cashOnDeliveryCheckBox.isChecked()) {
                        new ErrorDialog("Please select at-least one payment method.", CheckoutActivity.this).showDialog();
                    } else if (cashOnDeliveryCheckBox.isChecked()) {
                        placeOrderApi(true, "");
                    } else {

                        if (isPackageInstalled(GOOGLE_PAY_PACKAGE_NAME, getPackageManager())) {

                            paymentGooglePay(findTotalItemAndPrice());
                        } else {
                            new ErrorDialog("Please install Google Pay.", CheckoutActivity.this).showDialog();

                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.nbu.paisa.user"));
                            startActivity(browserIntent);
                        }

                    }
                }
            }
        });

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        googlePayCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    cashOnDeliveryCheckBox.setChecked(false);
            }
        });

        cashOnDeliveryCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    googlePayCheckBox.setChecked(false);
            }
        });

//        addAddressButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(CheckoutActivity.this,MyAddressActivity.class);
//                intent.putExtra("isSelectAddress",true);
//                startActivity(intent);
//            }
//        });

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CheckoutActivity.this,MyAddressActivity.class);
                intent.putExtra("isSelectAddress",false);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getAddresses(positionOfAddress);
    }

    public void getAddresses(final int position) {

        CommonClass.GLOBAL_LIST_CLASS.addressList.clear();


        final ProgressDialog dialog = ProgressDialog.show(CheckoutActivity.this, "",
                "Loading. Please wait...", true);

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer "+sharedPreference.getUserToken());

        new VolleyServiceCall(Request.Method.GET, Url.MY_ADDRESS_LIST, header, null, null, CheckoutActivity.this) {
            @Override
            public void onResponse(String s) {
                // getDoctors(departmentId);

                dialog.cancel();

                Log.d("zxcv", s);

                try {
                    JSONArray array = new JSONObject(s).getJSONArray("data");

                    if(array.length()<1){
                        addNew.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                    }else {
                        addNew.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                    for(int i = 0;i<array.length();i++){
                        JSONObject jsonObject = array.getJSONObject(i);

                        AddressDataModel addressDataModel = new AddressDataModel();

                        addressDataModel.setSelected(i == position);
                        addressDataModel.setAddressId(jsonObject.getInt("id"));
                        addressId = jsonObject.getInt("id");
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

                    addressListAdapterForCheckout.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(VolleyError error, String errorMessage) {
                dialog.cancel();
                Log.d("zxcv ", errorMessage);
                ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, CheckoutActivity.this) {
                    @Override
                    public void setAction(boolean action) {
                        if (action)
                            getAddresses(positionOfAddress);
                    }
                };
                apiErrorAction.createDialog();
            }
        }.start();
    }

    private int findTotalItemAndPrice(){

        int price = 0;

        for(int i = 0; i< CommonClass.GLOBAL_LIST_CLASS.cartList.size(); i++){
            CartDataModel cartDataModel = CommonClass.GLOBAL_LIST_CLASS.cartList.get(i);

            if(cartDataModel.getDiscount()>0){
                int discountPercentage = cartDataModel.getDiscount();
                int discountedPrice = (int)(cartDataModel.getPrice()*((float) discountPercentage /100.0f));
                price+=((cartDataModel.getPrice()-discountedPrice)*cartDataModel.getQuantity());
            }
            else {
                price+=(cartDataModel.getPrice()*cartDataModel.getQuantity());
            }
        }
        return price;
    }

    void placeOrderApi(final boolean cashOnDelivery, final String transactionId) {

        final ProgressDialog dialog = ProgressDialog.show(CheckoutActivity.this, "",
                "Loading. Please wait...", true);

        try {

            Map<String, String> header = new HashMap<>();
            header.put("Authorization", "Bearer " + sharedPreference.getUserToken());


            JSONObject orderOb = new JSONObject();

            orderOb.put("address_id", addressId);
//            orderOb.put("payment_type", cashOnDelivery?"Cash On Delivery": "Google Pay");
//            orderOb.put("transaction_id", transactionId);

            JSONArray productsArray = new JSONArray();

            for (int i = 0; i < CommonClass.GLOBAL_LIST_CLASS.cartList.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                CartDataModel cartDataModel = CommonClass.GLOBAL_LIST_CLASS.cartList.get(i);

                jsonObject.put("product_name", cartDataModel.getProductName());
                jsonObject.put("product_id", cartDataModel.getProductId());
                jsonObject.put("product_image", cartDataModel.getProductImage());
                jsonObject.put("quantity", cartDataModel.getQuantity());

                if(cartDataModel.getDiscount()>0){
                    int discountPercentage = cartDataModel.getDiscount();
                    int discountedPrice = (int)(cartDataModel.getPrice()*((float) discountPercentage /100.0f));
                    jsonObject.put("product_price", cartDataModel.getDiscount());
                }
                else {
                    jsonObject.put("product_price", cartDataModel.getPrice());
                }

                productsArray.put(jsonObject);
            }

            orderOb.put("product", productsArray);

            Log.d("zxcv", String.valueOf(orderOb));


            new VolleyServiceCall(Request.Method.POST, Url.ORDER_STORE, header, orderOb, CheckoutActivity.this) {
                @Override
                public void onResponse(String s) {
                    dialog.cancel();

                    Log.d("zxcv", s);

                    try {
                        JSONObject jsonObject = new JSONObject(s);

                        if(jsonObject.getBoolean("success")){
                            CommonClass.GLOBAL_LIST_CLASS.cartList.clear();
                            Intent intent = new Intent(CheckoutActivity.this,OrderCompleteActivity.class);
                            startActivity(intent);
                            CartActivity.cartActivity.finish();
                            finish();
                        }else{

                            new ErrorDialog("Oops! Something Went wrong, please try again", CheckoutActivity.this).showDialog();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onError(VolleyError error, String errorMessage) {
                    dialog.cancel();
                    Log.d("zxcv ", errorMessage);
                    ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, CheckoutActivity.this) {
                        @Override
                        public void setAction(boolean action) {
                            if (action)
                                placeOrderApi(cashOnDelivery, transactionId);
                        }
                    };
                    apiErrorAction.createDialog();
                }
            }.start();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void paymentGooglePay(int price) {
        Uri uri =
                new Uri.Builder()
                        .scheme("upi")
                        .authority("pay")
                        .appendQueryParameter("pa", "rajnandanreddragon@okicici")
                        .appendQueryParameter("pn", "ShopPrasad")
                        //.appendQueryParameter("mc", "your-merchant-code")
                        .appendQueryParameter("tr", giveRandomString())
                        .appendQueryParameter("tn", "Payment for you order in shopPraasad")
                        .appendQueryParameter("am", String.valueOf(price))
                        .appendQueryParameter("cu", "INR")
                        .appendQueryParameter("url", "http://shopPraasad.in/")
                        .build();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
        startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_PAY_REQUEST_CODE) {
            // Process based on the data in response.
            Log.d("result", data.getStringExtra("Status"));
            Log.d("result", data.getStringExtra("response"));

            if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                if (data != null) {
                    String trxt = data.getStringExtra("response");
                    Log.e("UPI", "onActivityResult: " + trxt);
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add(trxt);
                    upiPaymentDataOperation(dataList);
                } else {
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
            } else {
                //when user simply back without payment
                Log.e("UPI", "onActivityResult: " + "Return data is null");
                ArrayList<String> dataList = new ArrayList<>();
                dataList.add("nothing");
                upiPaymentDataOperation(dataList);
            }
        }
    }


    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(CheckoutActivity.this)) {
            String str = data.get(0);
            Log.e("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String transactionId = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    } else if(equalStr[0].toLowerCase().equals("txnId".toLowerCase())){
                        transactionId = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(CheckoutActivity.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "payment successfull: "+approvalRefNo+" "+transactionId);
                placeOrderApi(false, transactionId);
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(CheckoutActivity.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "Cancelled by user: "+approvalRefNo);

            }
            else {
                Toast.makeText(CheckoutActivity.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "failed payment: "+approvalRefNo);

            }
        } else {
            Log.e("UPI", "Internet issue: ");

            Toast.makeText(CheckoutActivity.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    String giveRandomString() {
        byte[] array = new byte[12]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));

        return generatedString;
    }

    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

}