package com.example.ibra.oxp.activities.myAccount;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ibra.oxp.R;
import com.example.ibra.oxp.activities.Base;
import com.example.ibra.oxp.activities.DiscussionForum;
import com.example.ibra.oxp.activities.HomePage;
import com.example.ibra.oxp.activities.product.AddProduct;
import com.example.ibra.oxp.activities.product.ProductsAdapter;
import com.example.ibra.oxp.database.MyDatabaseHelper;
import com.example.ibra.oxp.models.MyProduct;
import com.example.ibra.oxp.utils.EndlessScrollListener;
import com.example.ibra.oxp.utils.SharedPref;
import com.example.ibra.oxp.utils.Space;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewMyProducts extends Base {

    ProductsAdapter productsAdapter;
    MyDatabaseHelper dbHelper;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview_product)
    RecyclerView recyclerViewProducts;
    private Map<Integer, String> imagesURL;
    private List<MyProduct> products;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_products);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        bottom();
        imagesURL = new HashMap<>();
        products = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2,//span count no of items in single row
                GridLayoutManager.VERTICAL,//Orientation
                false);//reverse scrolling of recyclerview
        //set layout manager as gridLayoutManager
        recyclerViewProducts.setLayoutManager(gridLayoutManager);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (productsAdapter.getItemViewType(position)) {
                    case ProductsAdapter.PRODUCT_ITEM:
                        return 1;
                    case ProductsAdapter.LOADING_ITEM:
                        return 2; //number of columns of the grid
                    default:
                        return -1;
                }
            }
        });

        recyclerViewProducts.addItemDecoration(new Space(2, 20, true, 0));
        feedData();
        pullToRefresh();
    }

    private void feedData() {
        //show loading in recyclerview
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        SharedPref sharedPref = new SharedPref(getApplicationContext());
        final String id = sharedPref.readValue("id", "0");
        final int _id = Integer.parseInt(id);
        Toast.makeText(ViewMyProducts.this,sharedPref.readValue("email", "xyx@gmail.com") , Toast.LENGTH_SHORT).show();
        String completeURL = String.format(product_url+"?user_id=%1$s", _id);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, completeURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {//run

                    String status_code = response.getString("status_code");
                    if (status_code.equals("200")) {
                        JSONArray jsonArray = response.getJSONArray("data");
                        int length = jsonArray.length();
                        for (int i = length - 1; i >= 0; i--) //////newly added products will be shown first
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int productID = Integer.parseInt(jsonObject.getString("ID"));
                            String name = jsonObject.getString("name");
                            String price = jsonObject.getString("price");
                            String description = jsonObject.getString("description");
                            String quantity = jsonObject.getString("quantity");
                            String imagURL = jsonObject.getString("image_url");
                            String category = jsonObject.getString("category");
                            MyProduct product = new MyProduct(productID, name, description, price, quantity, imagURL,category);
                            imagesURL.put(productID, imagURL);
                            products.add(product);
                        }
                        setDataInAdapter();
                    } else {
                        String string_response = response.getString("data");
                        Log.d("ERROR SHOWING PRODUCTS!", string_response);
                        Toast.makeText(ViewMyProducts.this, string_response, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR!", error.toString());
                Toast.makeText(ViewMyProducts.this, "VOLLEY ERROR OCCURED", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);


    }

    private void setDataInAdapter() {
        productsAdapter = new ProductsAdapter(products, this, imagesURL);
        recyclerViewProducts.setAdapter(productsAdapter);
        productsAdapter.notifyDataSetChanged();
    }


    @OnClick(R.id.my_products_fab)
    public void my_products_fab() {
        Intent intent = new Intent(ViewMyProducts.this, AddProduct.class);
        startActivity(intent);
        finish();
    }
    private void pullToRefresh()
    {
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                products.clear();
                feedData(); // your code
                pullToRefresh.setRefreshing(false);
            }
        });
    }

}
