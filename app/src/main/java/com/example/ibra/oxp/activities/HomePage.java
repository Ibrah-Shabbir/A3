package com.example.ibra.oxp.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridLayout;

import com.example.ibra.oxp.R;
import com.example.ibra.oxp.activities.myAccount.Pro_service;
import com.example.ibra.oxp.activities.myAccount.ViewMyProducts;
import com.example.ibra.oxp.activities.product.AddProduct;
import com.example.ibra.oxp.activities.product.ViewProducts;
import com.example.ibra.oxp.activities.service.ViewServices;
import com.example.ibra.oxp.database.MyDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomePage extends Base {


    GridLayout mainGrid;
    MyDatabase mydb;
    @BindView(R.id.toolbar)Toolbar toolbar;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        bottom();

        mainGrid = (GridLayout) findViewById(R.id.mainGrid);
        //Set Event
        setSingleEvent(mainGrid);

    }

    private void setSingleEvent(GridLayout mainGrid) {
        //Loop all child item of Main Grid
       for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent;
                    switch (finalI){

                        case 0:
                            intent = new Intent(HomePage.this,Pro_service.class);
                            startActivity(intent);
                            //ofinish();
                            break;
                        case 1:
                            intent = new Intent(HomePage.this,ViewProducts.class);
                            startActivity(intent);
                            finish();
                            break;
                        case 2:
                            intent = new Intent(HomePage.this,AddProduct.class);
                            startActivity(intent);
                            finish();
                            break;
                        case 3:
                            intent = new Intent(HomePage.this,ViewServices.class);
                            startActivity(intent);
                            finish();
                            break;
                        case 4:
                            intent = new Intent(HomePage.this,DiscussionForum.class);
                            startActivity(intent);
                            finish();
                            break;
                        case 5:
                            intent = new Intent(HomePage.this,HomePage.class);
                            startActivity(intent);
                            finish();
                            break;
                        case 6:
                            intent = new Intent(HomePage.this,HomePage.class);
                            startActivity(intent);
                            finish();
                            break;
                        case 7:
                            intent = new Intent(HomePage.this,Category.class);
                            startActivity(intent);
                            finish();
                            break;
                        case 8:
                            intent = new Intent(HomePage.this,HomePage.class);
                            startActivity(intent);
                            finish();
                            break;
                        case 9:
                            intent = new Intent(HomePage.this,HomePage.class);
                            startActivity(intent);
                            finish();
                            break;
                        case 10:
                            intent = new Intent(HomePage.this,HomePage.class);
                            startActivity(intent);
                            finish();
                            break;
                        case 11:
                            intent = new Intent(HomePage.this,HomePage.class);
                            startActivity(intent);
                            finish();
                            break;
                        case 12:
                            intent = new Intent(HomePage.this,HomePage.class);
                            startActivity(intent);
                            finish();
                            break;


                    }
                   // Intent intent = new Intent(HomePage.this,ViewProducts.class);
                    //intent.putExtra("info","This is activity from card item index  "+finalI);
                    //startActivity(intent);

                }
            });
 }




    }

}
