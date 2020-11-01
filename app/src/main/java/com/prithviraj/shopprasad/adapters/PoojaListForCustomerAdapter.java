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

public class PoojaListForCustomerAdapter extends RecyclerView.Adapter<PoojaListForCustomerAdapter.PoojaListViewHolder> {


    @NonNull
    @Override
    public PoojaListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PoojaListForCustomerAdapter.PoojaListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.pooja_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PoojaListViewHolder holder, final int position) {

        holder.poojaName.setText(CommonClass.GLOBAL_LIST_CLASS.poojaList.get(position).getPujaName());

        if(!CommonClass.GLOBAL_LIST_CLASS.poojaList.get(position).getPujaImage().equalsIgnoreCase("null"))
        Picasso.get()
                .load(CommonClass.GLOBAL_LIST_CLASS.poojaList.get(position).getPujaImage())
                .into(holder.poojaImage);
        holder.poojaPrice.setText(String.format("₹ %s", CommonClass.GLOBAL_LIST_CLASS.poojaList.get(position).getPujaPrice()));

        holder.bookPooja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonClass.GLOBAL_VARIABLE_CLASS.clickPoojaList.passPoojaProduct(position);
            }
        });

        holder.poojaCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonClass.GLOBAL_VARIABLE_CLASS.clickForProductDetails.passPoojaPosition(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return CommonClass.GLOBAL_LIST_CLASS.poojaList.size();
    }

    static class PoojaListViewHolder extends RecyclerView.ViewHolder {

        private TextView poojaName, poojaPrice, bookPooja;
        private ImageView poojaImage;
        private CardView poojaCard;



        public PoojaListViewHolder(@NonNull View itemView) {
            super(itemView);

            poojaName = itemView.findViewById(R.id.poojaName);
            poojaImage = itemView.findViewById(R.id.poojaImage);
            poojaCard = itemView.findViewById(R.id.poojaCard);
            bookPooja = itemView.findViewById(R.id.textView9);
            poojaPrice = itemView.findViewById(R.id.productPrice3);
        }
    }
}
