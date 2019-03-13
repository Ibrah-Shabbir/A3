package com.example.ibra.oxp.activities.service;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.example.ibra.oxp.activities.product.EditProduct;
import com.example.ibra.oxp.activities.product.ProductDetail;
import com.example.ibra.oxp.models.MyProduct;
import com.example.ibra.oxp.models.MyService;
import com.example.ibra.oxp.utils.DialogBuilder;
import com.example.ibra.oxp.utils.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditService extends Base {

    @BindView(R.id.edit_service_name)
    TextView name;
    @BindView(R.id.edit_service_description)
    TextView description;
    @BindView(R.id.toolbar2)
    Toolbar toolbar;
    RequestQueue requestQueue;
    private ArrayAdapter<String> adapter;
    private MyService oldService;
    MyService updatedService;
    private DialogBuilder dialogBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_service);
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
        dialogBuilder = new DialogBuilder(this);
        requestQueue = Volley.newRequestQueue(this);
        updatedService=new MyService();
        receiveData();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.search).setVisible(false);
        return true;
    }

    private void setViewData()
    {
        //String contact_no=sharedPref.readValue("contact_no","No contact number to show");
        name.setText(name.getText()+oldService.getName());
        description.setText(description.getText()+oldService.getDescription());

        //contactNumber.setText(contactNumber.getText()+contact_no);


    }

    private void getUpdatedViewData()
    {
        updatedService.setName(name.getText().toString().trim());
        updatedService.setDescription(description.getText().toString().trim());

        updatedService.setId(oldService.getId());
    }

    private void receiveData()
    {
        oldService=(MyService) getIntent().getSerializableExtra("Service");
        Toast.makeText(EditService.this, oldService.getName()+" "+oldService.getDescription(), Toast.LENGTH_SHORT).show();

        setViewData();

    }

    @OnClick(R.id.edit_service_button)
    public void updateProduct()
    {
        getUpdatedViewData();
        SharedPref sharedPref = new SharedPref(getApplicationContext());
        final String id = sharedPref.readValue("id", "0");
        final int user_id = Integer.parseInt(id);
        String completeURL = String.format(service_url+"?user_id=%1$s&id=%2$s&name=%3$s&description=%4$s", user_id,updatedService.getId(),updatedService.getName(),updatedService.getDescription());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", updatedService.getId());
            jsonObject.put("user_id",user_id);
            jsonObject.put("name", updatedService.getName());
            jsonObject.put("description", updatedService.getDescription());

            } catch (Exception e) {
            e.printStackTrace();
        }

        dialogBuilder.loadingDialog("Updating Service...");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, service_url,jsonObject , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialogBuilder.dismissDialog();
                String status_code = "";
                String string_response = "";
                try {
                    status_code = response.getString("status_code");
                    string_response = response.getString("string_response");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (status_code.equals("200")) {
                    //myDatabaseHelper.InsertProduct(p);
                    Log.d("SERVICE UPDATED!", response.toString());
                    Toast.makeText(EditService.this, string_response, Toast.LENGTH_SHORT).show();
                    goBack();
                } else {
                    Log.d("SERVICE NOT UPDATED!", response.toString());
                    Toast.makeText(EditService.this, string_response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialogBuilder.dismissDialog();
                Log.d("EDIT SERVICE ERROR!", error.toString());
                Toast.makeText(EditService.this, "SERVICE PUT VOLLEY ERROR OCCURED", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    private void goBack()
    {

        Intent intent = new Intent(EditService.this,ServiceDetail.class);

        intent.putExtra("Product", updatedService);
        startActivity(intent);
        finish();
    }

}
