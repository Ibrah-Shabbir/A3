package com.example.ibra.oxp.activities.product;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.ibra.oxp.R;
import com.example.ibra.oxp.activities.Base;
import com.example.ibra.oxp.activities.myAccount.ViewMyProducts;
import com.example.ibra.oxp.models.MyProduct;
import com.example.ibra.oxp.models.Product;
import com.example.ibra.oxp.utils.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductDetail extends Base {

        @BindView(R.id.product_detail_name)
        TextView name;
        @BindView(R.id.product_detail_image)
        ImageView image;
        @BindView(R.id.product_detail_price)
        TextView price;
        @BindView(R.id.product_detail_quantity)
        TextView quantity;
        @BindView(R.id.product_detail_description)
        TextView description;
        @BindView(R.id.product_detail_contactNumber)
        TextView contactNumber;
        @BindView(R.id.toolbar2)
        Toolbar toolbar2;

        SharedPref sharedPref;
        private MyProduct myProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar2);
        bottom();
        sharedPref=new SharedPref(this);
        receiveData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dots_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id=item.getItemId();
        if(item_id==R.id.dots_menu_edit)
        {
            goToEdit();
        }
        else if(item_id==R.id.dots_menu_delete)
        {
            deleteProduct();
            Intent intent = new Intent(ProductDetail.this,ViewMyProducts.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setViewData()
    {

        String contact_no=sharedPref.readValue("contact_no","No contact number to show");

        name.setText(name.getText()+myProduct.getName());
        description.setText(description.getText()+myProduct.getDescription());
        quantity.setText(quantity.getText()+myProduct.getQuantity());
        price.setText(price.getText()+myProduct.getPrice());
        contactNumber.setText(contactNumber.getText()+contact_no);

        Glide.with(this)
                .asBitmap()
                .load(myProduct.getImage())
                .into(new BitmapImageViewTarget(image));

    }

    private void receiveData()
    {
        myProduct=(MyProduct)getIntent().getSerializableExtra("Product");
        Toast.makeText(ProductDetail.this, myProduct.getName()+" "+myProduct.getPrice()+" "+myProduct.getDescription(), Toast.LENGTH_SHORT).show();
        setViewData();
    }

    private void  deleteProduct()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("product_id", myProduct.getId());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        String uid=sharedPref.readValue("id","0");
        int user_id = Integer.parseInt(uid);
        String completeURL = String.format("http://" + IP_PORT + "/oxp/product/?user_id=%1$s&id=%2$s",user_id, myProduct.getId());
        Toast.makeText(ProductDetail.this,jsonObject.toString(), Toast.LENGTH_SHORT).show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, completeURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    String status_code = response.getString("status_code");
                    String string_response = response.getString("string_response");
                    if (status_code.equals("200")) {
                        Log.d("PRODUCT DELETED!", string_response);
                        Toast.makeText(ProductDetail.this, string_response, Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("ERROR DELETING PRODUCT!", string_response);
                        Toast.makeText(ProductDetail.this, string_response, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("CATCH DELETING PRODUCT!", e.toString());
                    Toast.makeText(ProductDetail.this, e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VOLLEY ERROR!", error.toString());
                Toast.makeText(ProductDetail.this, "VOLLEY ERROR OCCURED", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    private void goToEdit()
    {
        Intent intent = new Intent(ProductDetail.this,EditProduct.class);
        intent.putExtra("Product", myProduct);
        startActivity(intent);
        finish();
    }
}
