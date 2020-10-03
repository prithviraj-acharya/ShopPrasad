package com.prithviraj.shopprasad.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.prithviraj.shopprasad.R;
import com.prithviraj.shopprasad.dataModelClasses.AddressDataModel;
import com.prithviraj.shopprasad.utils.CommonClass;
import com.squareup.picasso.Picasso;


public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.AddressViewHolder> {


    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.address_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, final int position) {

        AddressDataModel addressDataModel = CommonClass.GLOBAL_LIST_CLASS.addressList.get(position);

        holder.customerName.setText(addressDataModel.getFullName());
        holder.customerAddress.setText(addressDataModel.getHouseNumber());
        holder.customerAddressDetails.setText(String.format("%s, %s, %s", addressDataModel.getArea(), addressDataModel.getCity(), addressDataModel.getPinCode()));

        holder.customerPhoneNumber.setText(String.format("Phone number: %s", addressDataModel.getPhone()));

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonClass.GLOBAL_VARIABLE_CLASS.addressButtonInterfaces.passPosition(position,false);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonClass.GLOBAL_VARIABLE_CLASS.addressButtonInterfaces.passPosition(position,true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return CommonClass.GLOBAL_LIST_CLASS.addressList.size();
    }


    static class AddressViewHolder extends RecyclerView.ViewHolder {

        private TextView customerName, customerAddress, customerAddressDetails, customerPhoneNumber;
        private Button editButton,deleteButton;


        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customerName);
            customerAddress = itemView.findViewById(R.id.customerAddress);
            customerAddressDetails = itemView.findViewById(R.id.customerAddressDetails);
            customerPhoneNumber = itemView.findViewById(R.id.customerPhoneNumber);

            editButton = itemView.findViewById(R.id.editButton);

            deleteButton = itemView.findViewById(R.id.deleteButton);


        }
    }
}
