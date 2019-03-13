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
        Toolbar toolbar;

        SharedPref sharedPref;
        private MyProduct myProduct;
        private MyProduct updatedProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bottom();
        sharedPref=new SharedPref(this);
        receiveData();
        getSpecificProduct(myProduct.getId());
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
            AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetail.this);
            builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setViewData()
    {
        String contact_no=sharedPref.readValue("contact_no","No contact number to show");

        name.setText(name.getText()+updatedProduct.getName());
        description.setText(description.getText()+updatedProduct.getDescription());
        quantity.setText(quantity.getText()+updatedProduct.getQuantity());
        price.setText(price.getText()+updatedProduct.getPrice());
        contactNumber.setText(contactNumber.getText()+contact_no);

        Glide.with(this)
                .asBitmap()
                .load(updatedProduct.getImage())
                .into(new BitmapImageViewTarget(image));

    }


    protected void getSpecificProduct(int product_id) {
        updatedProduct=new MyProduct();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Toast.makeText(ProductDetail.this,Integer.toString(product_id), Toast.LENGTH_SHORT).show();
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
                        updatedProduct.setId(productID);
                        updatedProduct.setName(name);
                        updatedProduct.setPrice(price);
                        updatedProduct.setDescription(description);
                        updatedProduct.setQuantity(quantity);
                        updatedProduct.setImage(imagURL);
                        updatedProduct.setCategory(category);
                        setViewData();

                    } else {
                        String string_response = response.getString("data");
                        Log.d("Error in Base", string_response);
                        Toast.makeText(ProductDetail.this, string_response, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("Error in Base",e.toString());
                    Toast.makeText(ProductDetail.this, e.toString(), Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR BASE VOLLEY!", error.toString());
                Toast.makeText(ProductDetail.this, "BASE VOLLEY ERROR OCCURED", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);


    }

    private void receiveData()
    {
        myProduct=(MyProduct)getIntent().getSerializableExtra("Product");
        Toast.makeText(ProductDetail.this, myProduct.getName()+" "+myProduct.getPrice()+" "+myProduct.getDescription(), Toast.LENGTH_SHORT).show();
        //setViewData();
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

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    deleteProduct();
                    Intent intent = new Intent(ProductDetail.this,ViewMyProducts.class);
                    startActivity(intent);
                    finish();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.dismiss();
                    break;
            }
        }
    };

}
