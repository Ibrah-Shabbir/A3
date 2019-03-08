package com.example.ibra.oxp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.example.ibra.oxp.R;
import com.example.ibra.oxp.activities.myAccount.ViewMyProducts;
import com.example.ibra.oxp.database.MyDatabaseHelper;
import com.example.ibra.oxp.models.MyProduct;
import com.example.ibra.oxp.utils.SharedPref;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Login extends Base {
    public static final String API_URL = "http://" + IP_PORT + "/oxp/login/";
    RequestQueue rq;
    JSONArray data;

    String name;
    EditText password;
    @BindView(R.id.login_email)
    EditText login_email;
    @BindView(R.id.login_password)
    EditText login_password;
    MyDatabaseHelper mydbhelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mydbhelper = new MyDatabaseHelper(this);
        rq = Volley.newRequestQueue(this);
        rq.start();


    }


    @OnClick(R.id.link_signup)
    public void link_signup_listener() {
        Intent i = new Intent(getApplicationContext(), SignUp.class);
        startActivity(i);
    }

    @OnClick(R.id.btn_login)
    public void btn_login_listener() {

        Toast.makeText(Login.this, "buttton", Toast.LENGTH_SHORT).show();
        final String _email = login_email.getText().toString().trim();
        final String _password = login_password.getText().toString().trim();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", _email);
            jsonObject.put("password", _password);
            Toast.makeText(Login.this,_email+" "+_password, Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String validemail = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+";

        if (!_password.isEmpty() && !_email.isEmpty()) {
            Matcher matcher = Pattern.compile(validemail).matcher(_email);
            if (matcher.matches()) {
                Toast.makeText(Login.this, "matched email", Toast.LENGTH_SHORT).show();
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                JsonObjectRequest jb2 = new JsonObjectRequest(Request.Method.POST, API_URL, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            Toast.makeText(Login.this, "please chal paro", Toast.LENGTH_SHORT).show();

                            String status_code=response.getString("status_code");
                            if(status_code.equals("200")) {
                                Toast.makeText(Login.this, "200", Toast.LENGTH_SHORT).show();
                                JSONArray jsonArray = response.getJSONArray("data");
                                int length = jsonArray.length();
                                Toast.makeText(Login.this, "length"+" "+length, Toast.LENGTH_SHORT).show();
                                if(length==1) {
                                        Toast.makeText(Login.this, "hello to the future", Toast.LENGTH_SHORT).show();
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        String f_name=jsonObject.getString("f_name");
                                        String l_name=jsonObject.getString("l_name");
                                        int id=jsonObject.getInt("id");
                                        String email=jsonObject.getString("email");
                                        String contact_no=jsonObject.getString("contact_no");


                                    shared_pref(String.valueOf(id),email,f_name,l_name,contact_no);
                                    Toast.makeText(Login.this, response.toString(), Toast.LENGTH_SHORT).show();
                                    Log.d("LOGIN SUCCESS! ", response.toString());
                                    Intent intent = new Intent(Login.this, HomePage.class);
                                    startActivity(intent);
                                }
                            }
                            else
                            {
                                String string_response=response.getString("data");
                                Log.d("ERROR LOGGING IN!", string_response);
                                Toast.makeText(Login.this,string_response, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(Login.this, "Error logging in", Toast.LENGTH_SHORT).show();
                        Log.d("LOGIN FAILED! ", error.toString());
                    }
                });

                requestQueue.add(jb2);
            }
        }

    }

    private void shared_pref(String id,String email,String first_name,String sur_name,String contact_no)
    {
        SharedPref sharedPref = new SharedPref(this);

        sharedPref.writeValue("id", id);
        sharedPref.writeValue("email", email);
        sharedPref.writeValue("first_name", first_name);
        sharedPref.writeValue("sur_name", sur_name);
        sharedPref.writeValue("contact_no", contact_no);
    }
}