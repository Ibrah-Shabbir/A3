package com.example.ibra.oxp.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import com.example.ibra.oxp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class Category extends Base{


    @BindView(R.id.toolbar)Toolbar toolbar;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.signup);
        //setContentView(R.layout.com.example.ibra.app1.Activity.awein);
        setContentView(R.layout.categories);
        ButterKnife.bind(this);
        //setActionBar((Toolbar)findViewById(R.id.toolbar));

        setSupportActionBar(toolbar);
        bottom();


        //Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(tb);

    }

}
