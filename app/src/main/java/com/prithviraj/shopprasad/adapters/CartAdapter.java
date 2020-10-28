package com.prithviraj.shopprasad.adapters;

import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.prithviraj.shopprasad.R;
import com.prithviraj.shopprasad.utils.CommonClass;
import com.squareup.picasso.Picasso;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ProductViewHolder> {


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, final int position) {

        int discountedPrice= 0;

        holder.productName.setText(CommonClass.GLOBAL_LIST_CLASS.cartList.get(position).getProductName());
        Picasso.get()
                .load(CommonClass.GLOBAL_LIST_CLASS.cartList.get(position).getProductImage())
                .into(holder.productImage);

        holder.productQuantity.setText(String.format("x%d", CommonClass.GLOBAL_LIST_CLASS.cartList.get(position).getQuantity()));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.productDesc.setText(Html.fromHtml(CommonClass.GLOBAL_LIST_CLASS.cartList.get(position).getProductDesc(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.productDesc.setText(Html.fromHtml(CommonClass.GLOBAL_LIST_CLASS.cartList.get(position).getProductDesc()));
        }

       // holder.productDesc.setText(String.valueOf(CommonClass.GLOBAL_LIST_CLASS.cartList.get(position).getProductDesc().replaceAll("\\<.*?\\>", "")));

        if(CommonClass.GLOBAL_LIST_CLASS.cartList.get(position).getDiscount()>0){

            holder.productPrice.setText(String.format("₹ %s", CommonClass.GLOBAL_LIST_CLASS.cartList.get(position).getDiscount()));
        }
        else {

            holder.productPrice.setText(String.format("₹ %s", CommonClass.GLOBAL_LIST_CLASS.cartList.get(position).getPrice()));
        }

        holder.addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonClass.GLOBAL_VARIABLE_CLASS.increaseItemQuantityFromCart.setPosition(position);
            }
        });

        holder.removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonClass.GLOBAL_VARIABLE_CLASS.decreaseItemQuantityFromCart.setPosition(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return CommonClass.GLOBAL_LIST_CLASS.cartList.size();
    }


    static class ProductViewHolder extends RecyclerView.ViewHolder {

        private TextView productName, productPrice, productDesc, productQuantity;
        private ImageView productImage;
        private ConstraintLayout addItem,removeItem;
        private CardView cartItem;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productDesc = itemView.findViewById(R.id.productDesc);
            productQuantity = itemView.findViewById(R.id.productQuantity);

            productImage = itemView.findViewById(R.id.productImage);

            addItem = itemView.findViewById(R.id.addItem);
            removeItem = itemView.findViewById(R.id.removeItem);

            cartItem = itemView.findViewById(R.id.cartItem);

        }
    }
}
