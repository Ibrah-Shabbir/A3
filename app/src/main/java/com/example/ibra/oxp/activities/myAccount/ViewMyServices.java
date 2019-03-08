package com.example.ibra.oxp.activities.myAccount;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.example.ibra.oxp.activities.service.AddService;
import com.example.ibra.oxp.activities.service.ServiceAdapter;
import com.example.ibra.oxp.models.MyProduct;
import com.example.ibra.oxp.models.MyService;
import com.example.ibra.oxp.utils.SharedPref;
import com.example.ibra.oxp.utils.Space;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewMyServices extends Base {

    ServiceAdapter serviceAdapter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview_service)
    RecyclerView recyclerViewService;
    private List<MyService> services;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_services);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        bottom();
        services = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2,//span count no of items in single row
                GridLayoutManager.VERTICAL,//Orientation
                false);//reverse scrolling of recyclerview
        //set layout manager as gridLayoutManager
        recyclerViewService.setLayoutManager(gridLayoutManager);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (serviceAdapter.getItemViewType(position)) {
                    case ServiceAdapter.PRODUCT_ITEM:
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
        recyclerViewService.addItemDecoration(new Space(2, 20, true, 0));
        //Finally set the adapter

        //load first page of recyclerview
        // endlessScrollListener.onLoadMore(0, 0);
        feedData();
    }


    private void feedData() {
        //show loading in recyclerview
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        SharedPref sharedPref = new SharedPref(getApplicationContext());
        String uid=sharedPref.readValue("id","0");
        int user_id = Integer.parseInt(uid);
        Toast.makeText(ViewMyServices.this,sharedPref.readValue("id", "0") , Toast.LENGTH_SHORT).show();
        //String completeURL = String.format("http://" + IP_PORT + "/oxp/service/?user_id=%1$s", _id);
        String completeURL = String.format(service_url+"?user_id=%1$s", user_id);

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
                            int serviceID = Integer.parseInt(jsonObject.getString("ID"));
                            String name = jsonObject.getString("name");
                            String description = jsonObject.getString("description");
                            MyService service_obj = new MyService(serviceID, name, description);
                            services.add(service_obj);
                        }
                        setDataInAdapter();
                    } else {
                        String string_response = response.getString("data");
                        Log.d("ERROR SHOWING PRODUCTS!", string_response);
                        Toast.makeText(ViewMyServices.this, string_response, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR!", error.toString());
                Toast.makeText(ViewMyServices.this, "VOLLEY ERROR OCCURED", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
        /*{
         *//** Passing some request headers* *//*
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("email", _email);
                return headers;
            }};*/
        /*
        dbHelper.getProductData2(list);
        boolean[] isNew = {true, false, false, true};
        for (int i = 0; i < list.size(); i++) {
            MyService product = new MyService(list.get(i).getName(), list.get(i).getDescription(), list.get(i).getPrice(), list.get(i).getQuantity(),"hh");

        }
*/

    }

    @OnClick(R.id.my_services_fab)
    public void my_services_fab() {
        Toast.makeText(ViewMyServices.this, "SERVICEEEEE PLZ ADD HOJAO", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ViewMyServices.this, AddService.class);
        startActivity(intent);
        finish();
    }




    private void setDataInAdapter() {
        serviceAdapter = new ServiceAdapter(services, this);
        recyclerViewService.setAdapter(serviceAdapter);
        serviceAdapter.notifyDataSetChanged();
    }

}
