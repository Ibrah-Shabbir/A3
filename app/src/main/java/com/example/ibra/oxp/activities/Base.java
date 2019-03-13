package com.example.ibra.oxp.activities;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ibra.oxp.R;
import com.example.ibra.oxp.models.MyProduct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Base extends AppCompatActivity
{

    protected static final String IP_PORT="192.168.43.10:8000";
    //protected static final String IP_PORT="192.168.8.102:8000";
    //protected static final String IP_PORT="192.168.8.100:8000";
    //protected static final String IP_PORT="192.168.43.10:8000";
    //protected static final String IP_PORT="192.168.0.106:8000";
    //protected static final String IP_PORT="192.168.0.105:8000";
    //protected static final String IP_PORT="192.168.43.26:8000";
    protected static final String product_url="http://"+IP_PORT+"/oxp/product/";
    protected static final String category_url="http://"+IP_PORT+"/oxp/category/";
    protected static final String service_url="http://"+IP_PORT+"/oxp/service/";





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // menu.clear();
        // Inflate the menu items for use in the action bar
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.app_bar_menu, menu);
        // MenuItem item=menu.findItem(R.id.)
        getMenuInflater().inflate(R.menu.app_bar_menu,menu);
        MenuItem search_view=menu.findItem(R.id.search);
        ShareActionProvider shareActionProvider=(ShareActionProvider) MenuItemCompat.getActionProvider(search_view);
        return super.onCreateOptionsMenu(menu);
    }

     public void bottom()
     {
         BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
         navigation.setItemBackgroundResource(R.drawable.menu_background);
         navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
     }

     private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent i;
            switch (item.getItemId()) {

                case R.id.navigation_account:
                    i=new Intent(getApplicationContext(),Login.class);
                    startActivity(i);
                    return true;
                case R.id.navigation_add:
                    i=new Intent(getApplicationContext(),Login.class);
                    startActivity(i);
                    return true;
                case R.id.navigation_df:
                    i=new Intent(getApplicationContext(),DiscussionForum.class);
                    startActivity(i);
                    return true;
                case R.id.navigation_fav:
                    i=new Intent(getApplicationContext(),Login.class);
                    startActivity(i);
                    return true;
                case R.id.navigation_home:
                    i=new Intent(getApplicationContext(),HomePage.class);
                    startActivity(i);
                    return true;
            }
            return false;
        }
    };

    protected void getSpecificProduct(int product_id, final MyProduct myProduct) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Toast.makeText(Base.this,Integer.toString(product_id), Toast.LENGTH_SHORT).show();
        String completeURL = String.format(product_url+"?id=%1$s", product_id);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, completeURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {//run
                    String status_code = response.getString("status_code");
                    if (status_code.equals("200")) {
                        JSONArray jsonArray = response.getJSONArray("data");
                        int length = jsonArray.length();

                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        int productID = Integer.parseInt(jsonObject.getString("ID"));
                        String name = jsonObject.getString("name");
                        String price = jsonObject.getString("price");
                        String description = jsonObject.getString("description");
                        String quantity = jsonObject.getString("quantity");
                        String imagURL = jsonObject.getString("image_url");
                        String category = jsonObject.getString("category");
                        myProduct.setId(productID);
                        myProduct.setName(name);
                        myProduct.setPrice(price);
                        myProduct.setDescription(description);
                        myProduct.setQuantity(quantity);
                        myProduct.setImage(imagURL);
                        myProduct.setCategory(category);

                    } else {
                        String string_response = response.getString("data");
                        Log.d("Error in Base", string_response);
                        Toast.makeText(Base.this, string_response, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("Error in Base",e.toString());
                    Toast.makeText(Base.this, e.toString(), Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR BASE VOLLEY!", error.toString());
                Toast.makeText(Base.this, "BASE VOLLEY ERROR OCCURED", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);


    }



}
