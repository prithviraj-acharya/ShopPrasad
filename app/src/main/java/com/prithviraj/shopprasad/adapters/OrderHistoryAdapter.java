package com.prithviraj.shopprasad.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prithviraj.shopprasad.R;
import com.prithviraj.shopprasad.dataModelClasses.OrderHitoryDataModel;
import com.prithviraj.shopprasad.utils.CommonClass;


public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder> {


    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderHistoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, final int position) {

        OrderHitoryDataModel orderHitoryDataModel = CommonClass.GLOBAL_LIST_CLASS.orderHistoryList.get(position);

        holder.orderId.setText(orderHitoryDataModel.getOrderId());
        holder.orderStatus.setText(orderHitoryDataModel.getStatus());
        holder.paymentType.setText(orderHitoryDataModel.getPaymentType());
        holder.otp.setVisibility(View.GONE);
        holder.otp.setText(String.valueOf(orderHitoryDataModel.getOtp()));
        holder.amountPaid.setText(String.format("₹ %s", orderHitoryDataModel.getAmountPaid()));

        holder.viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonClass.GLOBAL_VARIABLE_CLASS.orderHistoryClickInterface.setPosition(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return CommonClass.GLOBAL_LIST_CLASS.orderHistoryList.size();
    }


    static class OrderHistoryViewHolder extends RecyclerView.ViewHolder {

        private TextView orderId, orderStatus, paymentType, otp, amountPaid;
        private Button viewDetails;

        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            paymentType = itemView.findViewById(R.id.paymentType);
            otp = itemView.findViewById(R.id.otp);
            amountPaid = itemView.findViewById(R.id.amountPaid);
            viewDetails = itemView.findViewById(R.id.button8);

        }
    }
}
