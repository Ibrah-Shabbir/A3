package com.example.ibra.oxp.activities.service;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ibra.oxp.R;
import com.example.ibra.oxp.activities.Base;
import com.example.ibra.oxp.activities.product.ProductsAdapter;
import com.example.ibra.oxp.activities.product.ViewProducts;
import com.example.ibra.oxp.database.MyDatabaseHelper;

import com.example.ibra.oxp.models.MyProduct;
import com.example.ibra.oxp.models.MyService;
import com.example.ibra.oxp.utils.Space;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewServices extends Base {

    ServiceAdapter serviceAdapter;
    MyDatabaseHelper dbHelper;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview_service)
    RecyclerView recyclerViewServices;
    private List<MyService> services;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.signup);
        //setContentView(R.layout.com.example.ibra.app1.Activity.awein);
        setContentView(R.layout.all_services);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        bottom();

        services= new ArrayList<>();
        //dbHelper = new MyDatabaseHelper(this);
        // dbHelper.getProductData();


        //////////////////////////////////////////////////VIEWWWWW//////////////////////////////////////////////////////////

        //Bind RecyclerView from layout to recyclerViewProducts object
        //RecyclerView recyclerViewProducts = findViewById(R.id.recyclerview_product);
        //productsAdapter = new ProductsAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2,//span count no of items in single row
                GridLayoutManager.VERTICAL,//Orientation
                false);//reverse scrolling of recyclerview
        //set layout manager as gridLayoutManager
        recyclerViewServices.setLayoutManager(gridLayoutManager);

        //Crete new EndlessScrollListener fo endless recyclerview loading
        /*
        EndlessScrollListener endlessScrollListener = new EndlessScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (!productsAdapter.loading)
                    feedData();
            }
        };
        */

        //to give loading item full single row
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (serviceAdapter.getItemViewType(position)) {
                    case ServiceAdapter.SERVICE_ITEM:
                        return 1;
                    case ServiceAdapter.LOADING_ITEM:
                        return 2; //number of columns of the grid
                    default:
                        return -1;
                }
            }
        });
        //add on on Scroll listener
        //recyclerViewProducts.addOnScrollListener(endlessScrollListener);
        //add space between cards
        recyclerViewServices.addItemDecoration(new Space(2, 20, true, 0));
        //Finally set the adapter
        //recyclerViewProducts.setAdapter(productsAdapter);
        //load first page of recyclerview
        //endlessScrollListener.onLoadMore(0, 0);
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        feedData();
        pullToRefresh();
    }


    private void feedData() {
        //show loading in recyclerview
        //productsAdapter.showLoading();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String completeURL=service_url+"?user_id=0";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, completeURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    String status_code=response.getString("status_code");
                    if(status_code.equals("200")) {
                        JSONArray jsonArray = response.getJSONArray("data");
                        int length = jsonArray.length();
                        for (int i = length - 1; i >= 0; i--) //////newly added products will be shown first
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int serviceID = Integer.parseInt(jsonObject.getString("id"));
                            String name = jsonObject.getString("name");

                            String description = jsonObject.getString("description");

                            MyService service = new MyService(serviceID,name,description);

                            services.add(service);
                        }
                        setDataInAdapter();
                    }
                    else
                    {
                        //String string_response=response.getString("string_response");
                        Log.d("ERROR SHOWING SERVICES!","no service to show");
                        Toast.makeText(ViewServices.this,"no service to show", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR!", error.toString());
                Toast.makeText(ViewServices.this,"VOLLEY ERROR OCCURED", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);

        //dbHelper.getProductData2(list);
        //boolean[] isNew = {true, false, false, true};

        //for (int i = 0; i < list.size(); i++) {
        //  MyService product = new MyService(list.get(i).getName(), list.get(i).getDescription(), list.get(i).getPrice(), list.get(i).getQuantity(),"hh");

        //}

    }

    private void setDataInAdapter() {
        serviceAdapter = new ServiceAdapter(services, this);
        recyclerViewServices.setAdapter(serviceAdapter);
        serviceAdapter.notifyDataSetChanged();
    }
    private void pullToRefresh()
    {
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                services.clear();
                feedData(); // your code
                pullToRefresh.setRefreshing(false);
            }
        });
    }
}
