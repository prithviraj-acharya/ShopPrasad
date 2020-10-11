package com.prithviraj.shopprasad.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prithviraj.shopprasad.R;
import com.prithviraj.shopprasad.dataModelClasses.ProductDataModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class OrderHistoryProductItemAdapter extends RecyclerView.Adapter<OrderHistoryProductItemAdapter.ProductViewHolder> {

    ArrayList<ProductDataModel> products;

    public OrderHistoryProductItemAdapter(ArrayList<ProductDataModel> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_product_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, final int position) {

        holder.productName.setText(products.get(position).getProductName());
        Picasso.get()
                .load(products.get(position).getProductImage())
                .into(holder.productImage);

        holder.productQuantity.setText(String.format("x%d", products.get(position).getQuantity()));
        holder.productPrice.setText(String.format("₹ %s", products.get(position).getPrice()));

    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    static class ProductViewHolder extends RecyclerView.ViewHolder {

        private TextView productName, productPrice, productQuantity;
        private ImageView productImage;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productQuantity = itemView.findViewById(R.id.productQuantity);

            productImage = itemView.findViewById(R.id.productImage);

        }
    }
}
