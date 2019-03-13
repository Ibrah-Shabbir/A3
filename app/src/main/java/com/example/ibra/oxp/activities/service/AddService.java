package com.example.ibra.oxp.activities.service;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ibra.oxp.R;
import com.example.ibra.oxp.activities.Base;
import com.example.ibra.oxp.activities.myAccount.ViewMyProducts;
import com.example.ibra.oxp.activities.myAccount.ViewMyServices;
import com.example.ibra.oxp.activities.product.AddProduct;
import com.example.ibra.oxp.database.MyDatabaseHelper;
import com.example.ibra.oxp.models.MyProduct;
import com.example.ibra.oxp.models.MyService;
import com.example.ibra.oxp.utils.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.ibra.oxp.utils.DialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class AddService extends Base {

    //@BindView(R.id.toolbar)
    //Toolbar toolbar;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.add_services_name)EditText name;
    @BindView(R.id.add_services_description)EditText description;
    public RequestQueue requestQueue;
    private DialogBuilder dialogBuilder;
    private MyDatabaseHelper myDatabaseHelper;
    private ArrayAdapter<String> adapter;
    private List<String> itemsList;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(AddService.this, "before" +
                "eeeeee", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.add_services);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        bottom();
        dialogBuilder = new DialogBuilder(this);
        Toast.makeText(AddService.this, "afterrrrrrrr1111", Toast.LENGTH_SHORT).show();
        //Toast.makeText(AddService.this, "afterrrrrrrr", Toast.LENGTH_SHORT).show();

        requestQueue = Volley.newRequestQueue(this);
        itemsList = new ArrayList<>();


        Toast.makeText(AddService.this, "afterrrrrrrr1", Toast.LENGTH_SHORT).show();

    }
    @OnClick(R.id.add_services_button)
    public void btn_add_services() {
        Toast.makeText(AddService.this, "volley service", Toast.LENGTH_SHORT).show();
        String _name = name.getText().toString().trim();
        String _description = description.getText().toString().trim();
        if (!_name.isEmpty()) {
            //String email = getSharedPreferences("prefs", MODE_PRIVATE).getString("email", null);
            SharedPref sharedPref = new SharedPref(getApplicationContext());
            String _email = sharedPref.readValue("email", "defaultvaluerbeenread");
            Toast.makeText(AddService.this, _email, Toast.LENGTH_SHORT).show();
            final MyService p = new MyService(_name, _description);
            //String URL = String.format("http://"+IP_PORT+"/oxp/product/?name=%1$s&price=%2$s&description=%3$s&quantity=%4$s&email=%5$s&", _name, _price, _description, _quantity,email);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("email", _email);
                jsonObject.put("name", _name);
                jsonObject.put("description", _description);


            } catch (Exception e) {
                e.printStackTrace();
            }
            dialogBuilder.loadingDialog("Adding Service...");
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, service_url, jsonObject, new Response.Listener<JSONObject>() {
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
                        Log.d("Service ADDED!", response.toString());
                        Toast.makeText(AddService.this, string_response, Toast.LENGTH_SHORT).show();
                        goBack();
                    } else {
                        Log.d("SERVICE NOT ADDED!", response.toString());
                        Toast.makeText(AddService.this, string_response, Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialogBuilder.dismissDialog();
                    Log.d("ADD SERVICE ERROR!", error.toString());
                    Toast.makeText(AddService.this, "SERVICE VOLLEY ERROR OCCURED", Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(jsonObjectRequest);
            freed_the_fields();

        }



    }

    private void goBack()
    {
        Intent intent = new Intent(AddService.this,ViewMyServices.class);
        startActivity(intent);
        finish();
    }
    private void freed_the_fields() {
        name.setText("");
        description.setText("");

    }
}


