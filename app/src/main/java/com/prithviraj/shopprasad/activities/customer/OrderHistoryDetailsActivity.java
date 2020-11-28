package com.prithviraj.shopprasad.activities.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.prithviraj.shopprasad.R;
import com.prithviraj.shopprasad.adapters.OrderHistoryProductItemAdapter;
import com.prithviraj.shopprasad.dataModelClasses.OrderHitoryDataModel;
import com.prithviraj.shopprasad.utils.CommonClass;

public class OrderHistoryDetailsActivity extends AppCompatActivity {

    OrderHitoryDataModel orderHitoryDataModel;
    TextView userName, userNumber, userAddress, totalItemsInCart;
    ImageView backIcon;

    RecyclerView recyclerView;
    OrderHistoryProductItemAdapter orderHistoryProductItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_details);

        orderHitoryDataModel = CommonClass.GLOBAL_LIST_CLASS.orderHistoryList.get(getIntent().getIntExtra("position",0));

        init();
        onClickListeners();
    }

    void init(){
        backIcon = findViewById(R.id.backIcon);

        userName = findViewById(R.id.userName);
        userNumber = findViewById(R.id.userNumber);
        userAddress = findViewById(R.id.userAddress);



        totalItemsInCart = findViewById(R.id.totalItemsInCart);

        userName.setText("Name: "+orderHitoryDataModel.getName());
        userNumber.setText("Phone Number: "+orderHitoryDataModel.getPhone());
        userAddress.setText("Address: "+orderHitoryDataModel.getAddress());

        totalItemsInCart.setText(String.format("%d Total items: ₹%s", orderHitoryDataModel.getItemsOrdered().size(), orderHitoryDataModel.getAmountPaid()));

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(OrderHistoryDetailsActivity.this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        orderHistoryProductItemAdapter = new OrderHistoryProductItemAdapter(orderHitoryDataModel.getItemsOrdered());
        recyclerView.setAdapter(orderHistoryProductItemAdapter);

    }

    void onClickListeners(){
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}