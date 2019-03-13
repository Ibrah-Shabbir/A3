package com.example.ibra.oxp.activities.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.ibra.oxp.R;
import com.example.ibra.oxp.activities.Base;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Settings extends Base {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pro_service);
        ButterKnife.bind(this);
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bottom();
    }

    @OnClick(R.id.settings_update_profile)
    public void sendMessageProduct() {
        // Do something in response to button click
        Intent intent = new Intent(Settings.this, UpdateProfile.class);
        startActivity(intent);
        //finish();
    }


}
