package com.prithviraj.shopprasad.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prithviraj.shopprasad.R;
import com.prithviraj.shopprasad.activities.customer.AddNewAddresses;
import com.prithviraj.shopprasad.dataModelClasses.AddressDataModel;
import com.prithviraj.shopprasad.dataModelClasses.PanditProfilePujaDataModel;
import com.prithviraj.shopprasad.utils.CommonClass;


public class PanditProfilePujaListAdapter extends RecyclerView.Adapter<PanditProfilePujaListAdapter.PanditProfilePujaViewHolder> {

    Context context;

    public PanditProfilePujaListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public PanditProfilePujaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PanditProfilePujaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.new_puja_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PanditProfilePujaViewHolder holder, final int position) {

        PanditProfilePujaDataModel ppDataModel = CommonClass.GLOBAL_LIST_CLASS.panditPujaList.get(position);

        holder.pujaName.setText(ppDataModel.getPujaName());
        holder.pujaPrice.setText(ppDataModel.getAddedPujaPrice());

        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonClass.GLOBAL_VARIABLE_CLASS.addPanditProfilePujaInterfaces.removePuja(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return CommonClass.GLOBAL_LIST_CLASS.panditPujaList.size();
    }


    static class PanditProfilePujaViewHolder extends RecyclerView.ViewHolder {

        private TextView pujaPrice, pujaName;
        private ImageView addButton;


        public PanditProfilePujaViewHolder(@NonNull View itemView) {
            super(itemView);
            pujaPrice = itemView.findViewById(R.id.edtName5);
            addButton = itemView.findViewById(R.id.imageView3);
            pujaName = itemView.findViewById(R.id.spinner);

        }
    }
}
