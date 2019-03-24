package com.example.ibra.oxp.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ibra.oxp.R;
import com.example.ibra.oxp.activities.Base;
import com.example.ibra.oxp.activities.discussion.AddPost;
import com.example.ibra.oxp.activities.discussion.PostAdapter;
import com.example.ibra.oxp.activities.product.ProductsAdapter;
import com.example.ibra.oxp.activities.product.ViewProducts;
import com.example.ibra.oxp.database.MyDatabaseHelper;

import com.example.ibra.oxp.models.MyPost;
import com.example.ibra.oxp.models.MyProduct;
import com.example.ibra.oxp.models.MyService;
import com.example.ibra.oxp.utils.Space;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiscussionForum extends Base {

    PostAdapter postAdapter;
    MyDatabaseHelper dbHelper;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview_post)
    RecyclerView recyclerViewPost;
    private List<MyPost> posts;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discussion_forum);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        bottom();
        posts= new ArrayList<>();

        //////////////////////////////////////////////////VIEWWWWW//////////////////////////////////////////////////////////

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2,//span count no of items in single row
                GridLayoutManager.VERTICAL,//Orientation
                false);//reverse scrolling of recyclerview
        //set layout manager as gridLayoutManager
        recyclerViewPost.setLayoutManager(gridLayoutManager);

        //to give loading item full single row
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (postAdapter.getItemViewType(position)) {
                    case PostAdapter.POST_ITEM:
                        return 1;
                    case PostAdapter.LOADING_ITEM:
                        return 2; //number of columns of the grid
                    default:
                        return -1;
                }
            }
        });

        //add space between cards
        recyclerViewPost.addItemDecoration(new Space(2, 20, true, 0));
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        feedData();
        pullToRefresh();
    }


    private void feedData() {
        //show loading in recyclerview
        //productsAdapter.showLoading();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String completeURL=post_url+"?user_id=0";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, completeURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    String status_code=response.getString("status_code");
                    if(status_code.equals("200")) {
                        JSONArray jsonArray = response.getJSONArray("data");
                        int length = jsonArray.length();
                        for (int i = length - 1; i >= 0; i--) //////newly added products will be shown first
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int postID = Integer.parseInt(jsonObject.getString("id"));
                            String name = jsonObject.getString("name");

                            String description = jsonObject.getString("description");

                            MyPost post = new MyPost(postID,name,description);

                            posts.add(post);
                        }
                        setDataInAdapter();
                    }
                    else
                    {
                        //String string_response=response.getString("string_response");
                        Log.d("ERROR SHOWING POSTS!","no post to show");
                        Toast.makeText(DiscussionForum.this,"no post to show", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR!", error.toString());
                Toast.makeText(DiscussionForum.this,"VOLLEY ERROR OCCURED", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);



    }

    private void setDataInAdapter() {
        postAdapter = new PostAdapter(posts, this);
        recyclerViewPost.setAdapter(postAdapter);
        postAdapter.notifyDataSetChanged();
    }
    private void pullToRefresh()
    {
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                posts.clear();
                feedData(); // your code
                pullToRefresh.setRefreshing(false);
            }
        });
    }


    public void addPost(View v) {
        Intent i = new Intent(this, AddPost.class);
        startActivity(i);
    }

}
