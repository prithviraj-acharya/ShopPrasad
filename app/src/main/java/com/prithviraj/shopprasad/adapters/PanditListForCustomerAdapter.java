package com.prithviraj.shopprasad.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.prithviraj.shopprasad.R;
import com.prithviraj.shopprasad.utils.CommonClass;
import com.squareup.picasso.Picasso;

public class PanditListForCustomerAdapter extends RecyclerView.Adapter<PanditListForCustomerAdapter.PanditListViewHolder> {


    @NonNull
    @Override
    public PanditListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PanditListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.pandit_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PanditListViewHolder holder, final int position) {

        holder.panditName.setText(CommonClass.GLOBAL_LIST_CLASS.panditList.get(position).getPujaName().equalsIgnoreCase("null")?"Not yet added":CommonClass.GLOBAL_LIST_CLASS.panditList.get(position).getPujaName());

            if(!CommonClass.GLOBAL_LIST_CLASS.panditList.get(position).getPujaImage().equalsIgnoreCase("null"))
            Picasso.get()
                    .load(CommonClass.GLOBAL_LIST_CLASS.panditList.get(position).getPujaImage())
                    .into(holder.panditImage);


    }

    @Override
    public int getItemCount() {
        return CommonClass.GLOBAL_LIST_CLASS.panditList.size();
    }

    static class PanditListViewHolder extends RecyclerView.ViewHolder {

        private TextView panditName;
        private ImageView panditImage;
        private CardView productCard;


        public PanditListViewHolder(@NonNull View itemView) {
            super(itemView);

            panditName = itemView.findViewById(R.id.panditName);
            panditImage = itemView.findViewById(R.id.panditImage);
        }
    }
}
