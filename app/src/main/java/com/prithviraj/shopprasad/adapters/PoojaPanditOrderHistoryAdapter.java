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
import com.prithviraj.shopprasad.dataModelClasses.PoojaPanditBookingDataModel;
import com.prithviraj.shopprasad.utils.CommonClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class PoojaPanditOrderHistoryAdapter extends RecyclerView.Adapter<PoojaPanditOrderHistoryAdapter.OrderHistoryViewHolder> {

    ArrayList<PoojaPanditBookingDataModel> list;

    public PoojaPanditOrderHistoryAdapter(ArrayList<PoojaPanditBookingDataModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderHistoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.pooja_pandit_history_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, final int position) {

        PoojaPanditBookingDataModel dataModel = list.get(position);

        holder.name.setText(dataModel.getName());
        holder.orderStatus.setText(dataModel.getStatus());
        holder.price.setText("₹ "+dataModel.getPrice());

        Date parsed = null;
        String outputDate = "";
        String inputFormat = "yyyy-MM-dd HH:mm:ss";
        String outputFormat = "dd MMM, yyyy";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(dataModel.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        outputDate = df_output.format(parsed);

        holder.date.setText(outputDate);
        holder.address.setText(dataModel.getAddress());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    static class OrderHistoryViewHolder extends RecyclerView.ViewHolder {

        private TextView name, orderStatus, price, date, address;

        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.orderId);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            price = itemView.findViewById(R.id.paymentType);
            date = itemView.findViewById(R.id.otp);
            address = itemView.findViewById(R.id.amountPaid);

        }
    }
}
