package com.prithviraj.shopprasad.activities.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.prithviraj.myvollyimplementation.UtilityClasses.ApiErrorAction;
import com.prithviraj.myvollyimplementation.VolleyServiceCall;
import com.prithviraj.shopprasad.R;
import com.prithviraj.shopprasad.dataModelClasses.CartDataModel;
import com.prithviraj.shopprasad.utils.CircleTransform;
import com.prithviraj.shopprasad.utils.CommonClass;
import com.prithviraj.shopprasad.utils.SharedPreference;
import com.prithviraj.shopprasad.utils.Url;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProductDetails extends AppCompatActivity {

    SharedPreference sharedPreference;

    ImageView productImage, backIcon;
    TextView productName, productDesc, originalPrice, discountPrice;

    ConstraintLayout addItem, removeItem;
    TextView productQuantity;

    ConstraintLayout addToCartDot;
    TextView itemNumber;

    int quantity = 0;

    ImageView cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        init();
        onClickListeners();
        getProductDetails();
        getCart();
    }

    void init() {

        sharedPreference = new SharedPreference(ProductDetails.this);

        backIcon = findViewById(R.id.backIcon);

        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        productDesc = findViewById(R.id.productDesc);
        originalPrice = findViewById(R.id.originalPrice);
        discountPrice = findViewById(R.id.discountPrice);

        addItem = findViewById(R.id.addItem);
        removeItem = findViewById(R.id.removeItem);
        productQuantity = findViewById(R.id.productQuantity);

        addToCartDot = findViewById(R.id.addToCartDot);
        itemNumber = findViewById(R.id.itemNumber);

        cart = findViewById(R.id.cart);
    }

    void onClickListeners() {
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                quantity = getQuantity(getIntent().getIntExtra("productId", 0));

                if (quantity > 0) {
                    updateItemsToCart(getCartId(getIntent().getIntExtra("productId", 0)), getQuantity(getIntent().getIntExtra("productId", 0)) + 1);
                } else {
                    addToCart(getIntent().getIntExtra("productId", 0), 1);
                }

            }
        });

        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity = getQuantity(getIntent().getIntExtra("productId", 0));

                if (quantity == 1) {
                    removeItemsFromCart(getCartId(getIntent().getIntExtra("productId", 0)));
                } else {
                    updateItemsToCart(getCartId(getIntent().getIntExtra("productId", 0)), getQuantity(getIntent().getIntExtra("productId", 0)) - 1);
                }


            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ProductDetails.this, CartActivity.class);
                startActivity(in);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getCart();
    }


    public void getProductDetails() {


        final ProgressDialog dialog = ProgressDialog.show(ProductDetails.this, "",
                "Loading. Please wait...", true);

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + sharedPreference.getUserToken());

        Log.d("zxcv ", "Bearer " + sharedPreference.getUserToken());

        new VolleyServiceCall(Request.Method.GET, Url.PRODUCT_DETAILS + getIntent().getIntExtra("productId", 0) + "/detail", header, null, null, ProductDetails.this) {
            @Override
            public void onResponse(String s) {
                dialog.cancel();

                Log.d("zxcv", s);

                try {
                    JSONObject productObject = new JSONObject(s).getJSONObject("data");

                    productName.setText(productObject.getString("name"));
                    productDesc.setText(productObject.getString("description").replaceAll("\\<.*?\\>", ""));

                    if (!productObject.getString("image").equalsIgnoreCase("null"))
                        Picasso.get()
                                .load(productObject.getString("image"))
                                .into(productImage);


                    if (productObject.getString("percentage_off").equalsIgnoreCase("null")) {
                        originalPrice.setVisibility(View.INVISIBLE);
                        discountPrice.setText(String.format("₹ %s", productObject.getString("price")));
                    } else {
                        //holder.percentageOff.setText(String.format("%s %% OFF", CommonClass.GLOBAL_LIST_CLASS.productList.get(position).getPercentageOff()));
                        originalPrice.setText(String.format("₹ %s", productObject.getString("offer_price")));
                        originalPrice.setPaintFlags(originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                        discountPrice.setText(String.format("₹ %s", productObject.getString("offer_price")));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(VolleyError error, String errorMessage) {
                dialog.cancel();
                Log.d("zxcv ", errorMessage);
                ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, ProductDetails.this) {
                    @Override
                    public void setAction(boolean action) {
                        if (action)
                            getProductDetails();
                    }
                };
                apiErrorAction.createDialog();
            }
        }.start();

    }

    int getQuantity(int productId) {

        for (int i = 0; i < CommonClass.GLOBAL_LIST_CLASS.cartList.size(); i++) {

            CartDataModel cartDataModel = CommonClass.GLOBAL_LIST_CLASS.cartList.get(i);

            if (productId == cartDataModel.getProductId()) {
                return cartDataModel.getQuantity();
            }
        }

        return 0;
    }

    int getCartId(int productId) {

        for (int i = 0; i < CommonClass.GLOBAL_LIST_CLASS.cartList.size(); i++) {

            CartDataModel cartDataModel = CommonClass.GLOBAL_LIST_CLASS.cartList.get(i);

            if (productId == cartDataModel.getProductId()) {
                return cartDataModel.getCartItemId();
            }
        }

        return 0;
    }

    public void getCart() {

        CommonClass.GLOBAL_LIST_CLASS.cartList.clear();

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + sharedPreference.getUserToken());

        new VolleyServiceCall(Request.Method.GET, Url.MY_CART, header, null, null, ProductDetails.this) {
            @Override
            public void onResponse(String s) {

                Log.d("zxcv", s);

                try {
                    JSONArray array = new JSONObject(s).getJSONArray("data");

                    if (array.length() > 0) {
                        addToCartDot.setVisibility(View.VISIBLE);
                        itemNumber.setText(String.valueOf(array.length()));

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject cartObject = array.getJSONObject(i);

                            CartDataModel cartDataModel = new CartDataModel();

                            cartDataModel.setCartItemId(cartObject.getInt("id"));
                            cartDataModel.setUserId(cartObject.getInt("user_id"));
                            cartDataModel.setProductId(cartObject.getInt("product_id"));
                            cartDataModel.setQuantity(cartObject.getInt("quantity"));
                            cartDataModel.setPrice(cartObject.getInt("price"));
                            cartDataModel.setDiscount(cartObject.getString("offer_price").equalsIgnoreCase("null") ? 0 : cartObject.getInt("offer_price"));
                            cartDataModel.setProductName(cartObject.getString("name"));
                            cartDataModel.setProductImage(cartObject.getString("image"));
                            cartDataModel.setProductDesc(cartObject.getString("description"));

                            CommonClass.GLOBAL_LIST_CLASS.cartList.add(cartDataModel);

                        }

                    } else {
                        addToCartDot.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                quantity = getQuantity(getIntent().getIntExtra("productId", 0));

                if (quantity > 0) {
                    productQuantity.setText("X" + quantity);
                    removeItem.setVisibility(View.VISIBLE);
                } else {
                    productQuantity.setText("Add");
                    removeItem.setVisibility(View.GONE);
                }

            }

            @Override
            public void onError(VolleyError error, String errorMessage) {
                Log.d("zxcv ", errorMessage);
                ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, ProductDetails.this) {
                    @Override
                    public void setAction(boolean action) {
                        if (action)
                            getCart();
                    }
                };
                apiErrorAction.createDialog();
            }
        }.start();

    }

    public void addToCart(final int productId, final int quantity) {


        Log.d("zxcv", "addToCart: " + productId);
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + sharedPreference.getUserToken());

        Map<String, String> param = new HashMap<>();
        param.put("product_id", String.valueOf(productId));
        param.put("quantity", String.valueOf(quantity));


        new VolleyServiceCall(Request.Method.POST, Url.ADD_TO_CART, header, param, null, ProductDetails.this) {
            @Override
            public void onResponse(String s) {

                Log.d("zxcv", s);
                getCart();
//
            }

            @Override
            public void onError(VolleyError error, String errorMessage) {
                //  dialog.cancel();
                Log.d("zxcv ", errorMessage);
                ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, ProductDetails.this) {
                    @Override
                    public void setAction(boolean action) {
                        if (action)
                            addToCart(productId, quantity);
                    }
                };
                apiErrorAction.createDialog();
            }
        }.start();

    }

    public void updateItemsToCart(final int cartItemId, final int quantity) {

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + sharedPreference.getUserToken());

        Map<String, String> param = new HashMap<>();
        param.put("id", String.valueOf(cartItemId));
        param.put("quantity", String.valueOf(quantity));

        new VolleyServiceCall(Request.Method.POST, Url.UPDATE_ITEMS_TO_CART, header, param, null, ProductDetails.this) {
            @Override
            public void onResponse(String s) {
                Log.d("zxcv", s);

                getCart();
            }

            @Override
            public void onError(VolleyError error, String errorMessage) {
                //  dialog.cancel();
                Log.d("zxcv ", errorMessage);
                ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, ProductDetails.this) {
                    @Override
                    public void setAction(boolean action) {
                        if (action)
                            updateItemsToCart(cartItemId, quantity);
                    }
                };
                apiErrorAction.createDialog();
            }
        }.start();

    }

    public void removeItemsFromCart(final int cartItemId) {

        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Bearer " + sharedPreference.getUserToken());


        new VolleyServiceCall(Request.Method.GET, Url.REMOVE_ITEMS_FROM_CART + cartItemId + "/remove", header, null, null, ProductDetails.this) {
            @Override
            public void onResponse(String s) {

                getCart();

                Log.d("zxcv", s);
//
            }

            @Override
            public void onError(VolleyError error, String errorMessage) {
                //  dialog.cancel();
                Log.d("zxcv ", errorMessage);
                ApiErrorAction apiErrorAction = new ApiErrorAction(error, errorMessage, ProductDetails.this) {
                    @Override
                    public void setAction(boolean action) {
                        if (action)
                            removeItemsFromCart(cartItemId);
                    }
                };
                apiErrorAction.createDialog();
            }
        }.start();

    }
}