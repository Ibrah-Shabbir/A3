package com.example.ibra.oxp.activities;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.ibra.oxp.R;



public class Base extends AppCompatActivity
{

    //protected static final String IP_PORT="192.168.8.101:8000";
    protected static final String IP_PORT="192.168.8.100:8000";
    //protected static final String IP_PORT="192.168.43.10:8000";
    //protected static final String IP_PORT="192.168.0.106:8000";
    //protected static final String IP_PORT="192.168.0.105:8000";
    //protected static final String IP_PORT="192.168.43.26:8000";
    protected static final String product_url="http://"+IP_PORT+"/oxp/product/";
    protected static final String category_url="http://"+IP_PORT+"/oxp/category/";
    protected static final String service_url="http://"+IP_PORT+"/oxp/service/";




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // menu.clear();
        // Inflate the menu items for use in the action bar
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.app_bar_menu, menu);
        // MenuItem item=menu.findItem(R.id.)
        getMenuInflater().inflate(R.menu.app_bar_menu,menu);
        MenuItem search_view=menu.findItem(R.id.search);
        ShareActionProvider shareActionProvider=(ShareActionProvider) MenuItemCompat.getActionProvider(search_view);

        return super.onCreateOptionsMenu(menu);
    }

     public void bottom()
     {
         BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
         navigation.setItemBackgroundResource(R.drawable.menu_background);
         navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
     }

     private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent i;
            switch (item.getItemId()) {

                case R.id.navigation_account:
                    i=new Intent(getApplicationContext(),Login.class);
                    startActivity(i);
                    return true;
                case R.id.navigation_add:
                    i=new Intent(getApplicationContext(),Login.class);
                    startActivity(i);
                    return true;
                case R.id.navigation_df:
                    i=new Intent(getApplicationContext(),DiscussionForum.class);
                    startActivity(i);
                    return true;
                case R.id.navigation_fav:
                    i=new Intent(getApplicationContext(),Login.class);
                    startActivity(i);
                    return true;
                case R.id.navigation_home:
                    i=new Intent(getApplicationContext(),HomePage.class);
                    startActivity(i);
                    return true;
            }
            return false;
        }
    };

    public void inflateSearchMenu()
    {


    }

}
