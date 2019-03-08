package com.example.ibra.oxp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.ibra.oxp.R;
import com.example.ibra.oxp.utils.SharedPref;

public class Splash extends Activity {


    private static int SPLASH_TIME_OUT = 3000;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                SharedPref sharedPref = new SharedPref(getApplicationContext());
                final String id = sharedPref.readValue("id", "0");
                if(id.equals("0")) {
                    Intent i = new Intent(Splash.this, Login.class);
                    startActivity(i);
                }else{
                    Intent i = new Intent(Splash.this, HomePage.class);
                    startActivity(i);
                }

                // close this activity
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
