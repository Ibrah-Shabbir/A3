package com.example.ibra.oxp.activities.service;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.ibra.oxp.R;
import com.example.ibra.oxp.activities.Base;
import com.example.ibra.oxp.activities.myAccount.ViewMyServices;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddService extends Base {

    //@BindView(R.id.toolbar)
    //Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(AddService.this, "=beg=foree" +
                "eeeeee", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.add_services);

        //Toast.makeText(AddService.this, "afterrrrrrrr", Toast.LENGTH_SHORT).show();
        ButterKnife.bind(this);
        //setSupportActionBar(toolbar);
        //bottom();
    }
}
