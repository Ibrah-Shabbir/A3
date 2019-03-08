package com.example.ibra.oxp.activities.product;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.ibra.oxp.R;
import com.example.ibra.oxp.activities.Base;
import com.example.ibra.oxp.models.Category;
import com.example.ibra.oxp.models.MyProduct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditProduct extends Base
{
    @BindView(R.id.edit_product_name)
    TextView name;
    @BindView(R.id.edit_product_image)
    ImageView image;
    @BindView(R.id.edit_product_price)
    TextView price;
    @BindView(R.id.edit_product_quantity)
    TextView quantity;
    @BindView(R.id.edit_product_description)
    TextView description;
    //@BindView(R.id.edit_product_contactNumber)
    //TextView contactNumber;
    @BindView(R.id.toolbar2)
    Toolbar toolbar2;
    RequestQueue requestQueue;
    private List<String> categoryList;
    private ArrayAdapter<String> adapter;
    @BindView(R.id.edit_product_spinner)
    Spinner spinner;
    private MyProduct myProduct;
    String new_category;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_product);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar2);
        bottom();
        requestQueue = Volley.newRequestQueue(this);
        categoryList = new ArrayList<>();
        receiveData();
        requestCategory();
        setCategoryData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.search).setVisible(false);
        return true;
    }

    private void setCategoryData(){
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categoryList);
        spinner.setAdapter(adapter);
    }

    private void requestCategory()
    {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, category_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String status_code = "";
                String string_response = "";
                try {
                    status_code = response.getString("status_code");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (status_code.equals("200")) {
                    //Log.d("PRODUCT ADDED!", response.toString());
                    try {
                        JSONArray jsonArray = response.getJSONArray("data");
                        int length = jsonArray.length();

                        for (int i = length - 1; i >= 0; i--) //////newly added products will be shown first
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            categoryList.add(jsonObject.getString("description"));
                        }
                        setCategoryData();
                    }
                    catch(JSONException e) {
                        Log.e("OXP_TAG", e.getMessage());
                    }
                    //Toast.makeText(AddProduct.this, string_response, Toast.LENGTH_SHORT).show();
                } else
                {
                    try { string_response=response.getString("data");}catch(JSONException e) { }
                    Log.d("ERROR ADDING PRODUCT!", string_response);
                    Toast.makeText(EditProduct.this,string_response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("OXP_TAG", error.toString());
                Toast.makeText(EditProduct.this,"CATEGORY VOLLEY ERROR OCCURED", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    private void setViewData()
    {

        //String contact_no=sharedPref.readValue("contact_no","No contact number to show");

        name.setText(name.getText()+myProduct.getName());
        description.setText(description.getText()+myProduct.getDescription());
        quantity.setText(quantity.getText()+myProduct.getQuantity());
        price.setText(price.getText()+myProduct.getPrice());
        //contactNumber.setText(contactNumber.getText()+contact_no);

        Glide.with(this)
                .asBitmap()
                .load(myProduct.getImage())
                .into(new BitmapImageViewTarget(image));
        new_category=myProduct.getCategory();

    }

    private void receiveData()
    {
        myProduct=(MyProduct)getIntent().getSerializableExtra("Product");
        Toast.makeText(EditProduct.this, myProduct.getName()+" "+myProduct.getPrice()+" "+myProduct.getDescription(), Toast.LENGTH_SHORT).show();
        setViewData();
        replaceCategory();
    }

    private void setSpinnerOnItemSelectedListener()
    {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //String item = (String) parent.getItemAtPosition(position);
                String item=categoryList.get(position);
                new_category=item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    void replaceCategory()
    {
        Toast.makeText(this, "Selected "+categoryList.contains(new_category), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, new_category, Toast.LENGTH_SHORT).show();
        categoryList.contains(new_category);

    }


}
