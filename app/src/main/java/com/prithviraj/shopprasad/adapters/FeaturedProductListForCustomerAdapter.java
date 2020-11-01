package com.prithviraj.shopprasad.adapters;

import android.graphics.Paint;
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

public class FeaturedProductListForCustomerAdapter extends RecyclerView.Adapter<FeaturedProductListForCustomerAdapter.ProductListViewHolder> {


    @NonNull
    @Override
    public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListViewHolder holder, final int position) {

        holder.productName.setText(CommonClass.GLOBAL_LIST_CLASS.featuredProductList.get(position).getPujaName());

        if (CommonClass.GLOBAL_LIST_CLASS.featuredProductList.get(position).getPercentageOff().equalsIgnoreCase("null")|| CommonClass.GLOBAL_LIST_CLASS.featuredProductList.get(position).getPercentageOff().equalsIgnoreCase("0")) {
            holder.offerPrice.setVisibility(View.INVISIBLE);
            holder.percentageOff.setVisibility(View.INVISIBLE);
            holder.productPrice.setText(String.format("₹ %s", CommonClass.GLOBAL_LIST_CLASS.featuredProductList.get(position).getPujaPrice()));
        } else {
            holder.percentageOff.setText(String.format("%s %% OFF", CommonClass.GLOBAL_LIST_CLASS.featuredProductList.get(position).getPercentageOff()));
            holder.offerPrice.setText(String.format("₹ %s", CommonClass.GLOBAL_LIST_CLASS.featuredProductList.get(position).getPujaPrice()));
            holder.offerPrice.setPaintFlags(holder.offerPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            holder.productPrice.setText(String.format("₹ %s", CommonClass.GLOBAL_LIST_CLASS.featuredProductList.get(position).getOfferPrice()));
        }


        if (!CommonClass.GLOBAL_LIST_CLASS.featuredProductList.get(position).getPujaImage().equalsIgnoreCase("null"))
            Picasso.get()
                    .load(CommonClass.GLOBAL_LIST_CLASS.featuredProductList.get(position).getPujaImage())
                    .into(holder.productImage);


        holder.productCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonClass.GLOBAL_VARIABLE_CLASS.clickFeaturedProductList.passProductId(CommonClass.GLOBAL_LIST_CLASS.featuredProductList.get(position).getId());
            }
        });

        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonClass.GLOBAL_VARIABLE_CLASS.addToCartFeaturedProductList.passPosition(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return CommonClass.GLOBAL_LIST_CLASS.featuredProductList.size();
    }

    static class ProductListViewHolder extends RecyclerView.ViewHolder {

        private TextView productName, productPrice, offerPrice, percentageOff;
        private ImageView productImage;
        private CardView productCard;
        private TextView addToCart;


        public ProductListViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.productName);
            offerPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImage);
            productCard = itemView.findViewById(R.id.productCard);
            productPrice = itemView.findViewById(R.id.productPrice2);
            percentageOff = itemView.findViewById(R.id.percentageOff);
            addToCart = itemView.findViewById(R.id.textView4);
        }
    }
}
