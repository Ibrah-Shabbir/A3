package com.example.ibra.oxp.activities.service;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.ibra.oxp.R;
import com.example.ibra.oxp.activities.product.ProductDetail;
import com.example.ibra.oxp.models.MyService;

import java.util.List;
import java.util.Map;

public class ServiceAdapter extends RecyclerView.Adapter {

    List<MyService> myServices;
    Context mContext;
    public static final int LOADING_ITEM = 0;
    public static final int PRODUCT_ITEM = 1;
    int LoadingItemPos;
    public boolean loading = false;

    public ServiceAdapter(List<MyService> mServices, Context mContext) {
        this.myServices = mServices;
        this.mContext = mContext;
    }

    @Override
    public int getItemViewType(int position) {
        MyService currentProduct = myServices.get(position);
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
            View row = inflater.inflate(R.layout.custom_row_service, parent, false);
            return new ServiceHolder(row);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //get current product
        final MyService currentService = myServices.get(position);
        if (holder instanceof ServiceHolder) {
            final ServiceHolder productHolder = (ServiceHolder) holder;
            productHolder.textViewServiceName.setText(currentService.getName());

            productHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // user selected product now you can show details of that product

                    Toast.makeText(mContext, "Selected "+currentService.getName(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext,ServiceDetail.class);
                    //Bundle bundle=new Bundle();
                    //bundle.put;
                    intent.putExtra("Product", currentService);
                    mContext.startActivity(intent);

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return myServices.size();
    }

    //Holds view of product with information
    private class ServiceHolder extends RecyclerView.ViewHolder {

        TextView textViewServiceName;


        public ServiceHolder(View itemView) {
            super(itemView);
            textViewServiceName = itemView.findViewById(R.id.textViewProductName);
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
        MyService service = new MyService();
        service.setLoading(true);
        myServices.add(service);
        LoadingItemPos = myServices.size();
        notifyItemInserted(myServices.size());
        loading = true;
    }

    //method to hide loading
    public void hideLoading() {
        if (LoadingItemPos <= myServices.size()) {
            myServices.remove(LoadingItemPos - 1);
            notifyItemRemoved(LoadingItemPos);
            loading = false;
        }
    }
}
