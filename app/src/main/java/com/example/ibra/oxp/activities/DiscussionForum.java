package com.example.ibra.oxp.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import com.example.ibra.oxp.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DiscussionForum extends Base
{

    @BindView(R.id.toolbar)Toolbar toolbar;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.signup);
        //setContentView(R.layout.com.example.ibra.app1.Activity.awein);
        setContentView(R.layout.discussion_forum);
        ButterKnife.bind(this);
        //setActionBar((Toolbar)findViewById(R.id.toolbar));


        //Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(tb);

        setSupportActionBar(toolbar);
        bottom();

    }

}
