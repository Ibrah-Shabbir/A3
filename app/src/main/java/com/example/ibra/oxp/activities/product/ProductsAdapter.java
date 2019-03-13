package com.example.ibra.oxp.activities.product;


import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.ibra.oxp.activities.DiscussionForum;
import com.example.ibra.oxp.activities.HomePage;
import com.example.ibra.oxp.models.MyProduct;
import com.example.ibra.oxp.models.MyProduct;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.ibra.oxp.R;

public class ProductsAdapter extends RecyclerView.Adapter {

    List<MyProduct> mProducts;
    Context mContext;
    Map<Integer, String> imagesURL;
    public static final int LOADING_ITEM = 0;
    public static final int PRODUCT_ITEM = 1;
    int LoadingItemPos;
    public boolean loading = false;

    public ProductsAdapter(List<MyProduct> mProducts, Context mContext, Map<Integer, String> imagesURL) {
        this.mProducts = mProducts;
        this.mContext = mContext;
        this.imagesURL = imagesURL;
    }

    @Override
    public int getItemViewType(int position) {
        MyProduct currentProduct = mProducts.get(position);
        if (currentProduct.isLoading()) {
            return LOADING_ITEM;
        } else {
            return PRODUCT_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //Check which view has to be populated
        if (viewType == LOADING_ITEM) {
            View row = inflater.inflate(R.layout.custom_row_loading, parent, false);
            return new LoadingHolder(row);
        } else if (viewType == PRODUCT_ITEM) {
            View row = inflater.inflate(R.layout.custom_row_product, parent, false);
            return new ProductHolder(row);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //get current product
        final MyProduct currentProduct = mProducts.get(position);
        if (holder instanceof ProductHolder) {
            final ProductHolder productHolder = (ProductHolder) holder;
            productHolder.textViewProductName.setText(currentProduct.getName());
            productHolder.textViewProductPrice.setText(currentProduct.getPrice()+ " Rs");

            Glide.with(mContext)
                    .asBitmap()
                    .load(imagesURL.get(currentProduct.getId()))
                    .into(new BitmapImageViewTarget(productHolder.imageViewProduct));

            productHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // user selected product now you can show details of that product

                    Toast.makeText(mContext, "Selected "+currentProduct.getName(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext,ProductDetail.class);
                    //Bundle bundle=new Bundle();
                    //bundle.put;
                    intent.putExtra("Product", currentProduct);
                    mContext.startActivity(intent);

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    //Holds view of product with information
    private class ProductHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProduct;
        TextView textViewProductName, textViewProductPrice;


        public ProductHolder(View itemView) {
            super(itemView);
            imageViewProduct = itemView.findViewById(R.id.imageee);
            textViewProductName = itemView.findViewById(R.id.textViewProductName);
            textViewProductPrice = itemView.findViewById(R.id.textViewProductPrice);
        }
    }
    //holds view of loading item
    private class LoadingHolder extends RecyclerView.ViewHolder {
        public LoadingHolder(View itemView) {
            super(itemView);
        }
    }

    //method to show loading
    public void showLoading() {
        MyProduct product = new MyProduct();
        product.setLoading(true);
        mProducts.add(product);
        LoadingItemPos = mProducts.size();
        notifyItemInserted(mProducts.size());
        loading = true;
    }

    //method to hide loading
    public void hideLoading() {
        if (LoadingItemPos <= mProducts.size()) {
            mProducts.remove(LoadingItemPos - 1);
            notifyItemRemoved(LoadingItemPos);
            loading = false;
        }
    }
}
