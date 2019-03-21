package com.example.ibra.oxp.activities.discussion;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
import com.example.ibra.oxp.activities.DiscussionForum;
import com.example.ibra.oxp.activities.myAccount.ViewMyServices;
import com.example.ibra.oxp.activities.service.AddService;
import com.example.ibra.oxp.database.MyDatabaseHelper;
import com.example.ibra.oxp.models.MyPost;
import com.example.ibra.oxp.models.MyService;
import com.example.ibra.oxp.utils.DialogBuilder;
import com.example.ibra.oxp.utils.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddPost extends Base {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.add_post_name)EditText name;
    @BindView(R.id.add_post_description)EditText description;
    public RequestQueue requestQueue;
    private DialogBuilder dialogBuilder;
    private MyDatabaseHelper myDatabaseHelper;
    private ArrayAdapter<String> adapter;
    private List<String> itemsList;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        bottom();
        dialogBuilder = new DialogBuilder(this);
        requestQueue = Volley.newRequestQueue(this);
        itemsList = new ArrayList<>();
    }


    @OnClick(R.id.add_post_button)
    public void btn_add_post() {
        Toast.makeText(AddPost.this, "volley post", Toast.LENGTH_SHORT).show();
        String _name = name.getText().toString().trim();
        String _description = description.getText().toString().trim();
        int _likes=0;
        if (!_name.isEmpty()) {
            //String email = getSharedPreferences("prefs", MODE_PRIVATE).getString("email", null);
            SharedPref sharedPref = new SharedPref(getApplicationContext());
            String _email = sharedPref.readValue("email", "defaultvaluerbeenread");
            Toast.makeText(AddPost.this, _email, Toast.LENGTH_SHORT).show();
            final MyPost p = new MyPost(_name, _description,_likes);
            //String URL = String.format("http://"+IP_PORT+"/oxp/product/?name=%1$s&price=%2$s&description=%3$s&quantity=%4$s&email=%5$s&", _name, _price, _description, _quantity,email);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("email", _email);
                jsonObject.put("name", _name);
                jsonObject.put("description", _description);
                jsonObject.put("likes",_likes);


            } catch (Exception e) {
                e.printStackTrace();
            }
            dialogBuilder.loadingDialog("Adding Post...");
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, post_url, jsonObject, new Response.Listener<JSONObject>() {
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
                        Log.d("POST ADDED!", response.toString());
                        Toast.makeText(AddPost.this, string_response, Toast.LENGTH_SHORT).show();
                        goBack();
                    } else {
                        Log.d("POST NOT ADDED!", response.toString());
                        Toast.makeText(AddPost.this, string_response, Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialogBuilder.dismissDialog();
                    Log.d("ADD POST ERROR!", error.toString());
                    Toast.makeText(AddPost.this, "POST VOLLEY ERROR OCCURED", Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(jsonObjectRequest);
            freed_the_fields();

        }



    }

    private void goBack()
    {
        Intent intent = new Intent(AddPost.this,DiscussionForum.class);
        startActivity(intent);
        finish();
    }
    private void freed_the_fields() {
        name.setText("");
        description.setText("");

    }



    public void savePost(View v) {
        Toast.makeText(this, "Your post has been published successfully.", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, DiscussionForum.class);
        startActivity(i);
    }


}
