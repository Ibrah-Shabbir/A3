package com.example.ibra.oxp.activities.myAccount;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.ibra.oxp.R;
import com.example.ibra.oxp.activities.Base;
import com.example.ibra.oxp.activities.Login;
import com.example.ibra.oxp.activities.product.AddProduct;
import com.example.ibra.oxp.activities.product.ProductDetail;
import com.example.ibra.oxp.utils.SharedPref;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Pro_service extends Base {
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

    @OnClick(R.id.pro_service_products)
    public void sendMessageProduct() {
        // Do something in response to button click
        Intent intent = new Intent(Pro_service.this, ViewMyProducts.class);
        startActivity(intent);
        //finish();
    }

    @OnClick(R.id.pro_service_services)
    public void sendMessageService() {
        // Do something in response to button click
        Intent intent = new Intent(Pro_service.this, ViewMyServices.class);
        startActivity(intent);
        //finish();
    }

    @OnClick(R.id.pro_service_posts)
    public void sendMessagePost() {
        // Do something in response to button click
        Intent intent = new Intent(Pro_service.this, ViewMyPosts.class);
        startActivity(intent);
        //finish();
    }


/*    private void ConfirmationMessage() {
        final CharSequence[] items = {"Log out","Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Pro_service.this);
        builder.setTitle("Logout of OXP?");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("Log out")) {
                    delete();
                } else if (items[which].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }*/

    @OnClick(R.id.pro_service_logout)
    public void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Pro_service.this);
        builder.setMessage("Log out of OXP?").setPositiveButton("Log out", dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener).show();
        //ConfirmationMessage();
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    delete();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.dismiss();
                    break;
            }
        }
    };


    private void delete()
    {
        SharedPref sharedPref=new SharedPref(this);
        sharedPref.deleteData();
        Intent intent = new Intent(Pro_service.this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        //finish();
    }

}
