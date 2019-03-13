package com.example.ibra.oxp.activities.service;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
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
import com.example.ibra.oxp.activities.myAccount.ViewMyProducts;
import com.example.ibra.oxp.activities.myAccount.ViewMyServices;
import com.example.ibra.oxp.activities.product.EditProduct;
import com.example.ibra.oxp.activities.product.ProductDetail;
import com.example.ibra.oxp.models.MyProduct;
import com.example.ibra.oxp.models.MyService;
import com.example.ibra.oxp.utils.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceDetail extends Base {

        @BindView(R.id.service_detail_name)
        TextView name;
        @BindView(R.id.service_detail_description)
        TextView description;
        @BindView(R.id.service_detail_contactNumber)
        TextView contactNumber;
        @BindView(R.id.toolbar2)
        Toolbar toolbar2;

        SharedPref sharedPref;
        private MyService myService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_detail);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(ServiceDetail.this);
            builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();

        }
        return super.onOptionsItemSelected(item);
    }
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    deleteService();
                    Intent intent = new Intent(ServiceDetail.this,ViewMyServices.class);
                    startActivity(intent);
                    finish();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.dismiss();
                    break;
            }
        }
    };


    private void setViewData()
    {

        String contact_no=sharedPref.readValue("contact_no","No contact number to show");

        name.setText(name.getText()+myService.getName());
        description.setText(description.getText()+myService.getDescription());
        contactNumber.setText(contactNumber.getText()+contact_no);
    }

    private void receiveData()
    {
        myService=(MyService)getIntent().getSerializableExtra("Service");
        Toast.makeText(ServiceDetail.this, myService.getName()+" "+myService.getDescription(), Toast.LENGTH_SHORT).show();
        setViewData();
    }

    private void  deleteService()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("service_id", myService.getId());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        String uid=sharedPref.readValue("id","0");
        int user_id = Integer.parseInt(uid);
        String completeURL = String.format("http://" + IP_PORT + "/oxp/service/?user_id=%1$s&id=%2$s",user_id, myService.getId());
        Toast.makeText(ServiceDetail.this,jsonObject.toString(), Toast.LENGTH_SHORT).show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, completeURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    String status_code = response.getString("status_code");
                    String string_response = response.getString("string_response");
                    if (status_code.equals("200")) {
                        Log.d("SERVICE DELETED!", string_response);
                        Toast.makeText(ServiceDetail.this, string_response, Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("ERROR DELETING SERVICE!", string_response);
                        Toast.makeText(ServiceDetail.this, string_response, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("CATCH DELETING SERVICE!", e.toString());
                    Toast.makeText(ServiceDetail.this, e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VOLLEY ERROR!", error.toString());
                Toast.makeText(ServiceDetail.this, "VOLLEY ERROR OCCURED", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);

    }
    private void goToEdit()
    {
        Intent intent = new Intent(ServiceDetail.this,EditService.class);
        intent.putExtra("Service", myService);
        startActivity(intent);
        finish();
    }
}
