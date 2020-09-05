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

public class ProductListForCustomerAdapter extends RecyclerView.Adapter<ProductListForCustomerAdapter.ProductListViewHolder> {


    @NonNull
    @Override
    public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListViewHolder holder, final int position) {

        holder.productName.setText(CommonClass.GLOBAL_LIST_CLASS.productList.get(position).getPujaName());
        holder.productPrice.setText(String.format("₹ %s", CommonClass.GLOBAL_LIST_CLASS.productList.get(position).getPujaPrice()));

            if(!CommonClass.GLOBAL_LIST_CLASS.productList.get(position).getPujaImage().equalsIgnoreCase("null"))
            Picasso.get()
                    .load(CommonClass.GLOBAL_LIST_CLASS.productList.get(position).getPujaImage())
                    .into(holder.productImage);


    }

    @Override
    public int getItemCount() {
        return CommonClass.GLOBAL_LIST_CLASS.productList.size();
    }

    static class ProductListViewHolder extends RecyclerView.ViewHolder {

        private TextView productName, productPrice;
        private ImageView productImage;
        private CardView productCard;


        public ProductListViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImage);
            productCard = itemView.findViewById(R.id.productCard);
        }
    }
}
