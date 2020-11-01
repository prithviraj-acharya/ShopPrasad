package com.prithviraj.shopprasad.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prithviraj.shopprasad.R;
import com.prithviraj.shopprasad.dataModelClasses.AddressDataModel;
import com.prithviraj.shopprasad.utils.CommonClass;


public class AddressListAdapterForCheckout extends RecyclerView.Adapter<AddressListAdapterForCheckout.AddressViewHolder> {


    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.address_item_layout_for_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, final int position) {

        AddressDataModel addressDataModel = CommonClass.GLOBAL_LIST_CLASS.addressList.get(position);

        holder.customerName.setText(addressDataModel.getFullName());
        holder.customerAddress.setText(addressDataModel.getHouseNumber());
        holder.customerAddressDetails.setText(String.format("%s, %s, %s", addressDataModel.getArea(), addressDataModel.getCity(), addressDataModel.getPinCode()));

        holder.customerPhoneNumber.setText(String.format("Phone number: %s", addressDataModel.getPhone()));

        holder.checkBox.setChecked(addressDataModel.isSelected());

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonClass.GLOBAL_VARIABLE_CLASS.selectAddressForCheckout.addressPosition(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return CommonClass.GLOBAL_LIST_CLASS.addressList.size();
    }


    static class AddressViewHolder extends RecyclerView.ViewHolder {

        private TextView customerName, customerAddress, customerAddressDetails, customerPhoneNumber;
        private CheckBox checkBox;


        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customerName);
            customerAddress = itemView.findViewById(R.id.customerAddress);
            customerAddressDetails = itemView.findViewById(R.id.customerAddressDetails);
            customerPhoneNumber = itemView.findViewById(R.id.customerPhoneNumber);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
