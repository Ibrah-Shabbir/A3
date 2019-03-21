package com.example.ibra.oxp.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.example.ibra.oxp.activities.discussion.AddPost;
import com.example.ibra.oxp.activities.discussion.DiscussionPost;
import com.example.ibra.oxp.activities.discussion.DiscussionPostAdapter;
import com.example.ibra.oxp.activities.discussion.PostAdapter;
import com.example.ibra.oxp.activities.service.ServiceAdapter;
import com.example.ibra.oxp.activities.service.ViewServices;
import com.example.ibra.oxp.models.MyPost;
import com.example.ibra.oxp.models.MyService;
import com.example.ibra.oxp.utils.EndlessScrollListener;
import com.example.ibra.oxp.utils.Space;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiscussionForum extends Base
{

    private DiscussionPostAdapter adapter;
    PostAdapter postAdapter;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    @BindView(R.id.toolbar)Toolbar toolbar;
    private List<MyPost> posts;
    RecyclerView rv;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discussion_forum);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        bottom();
        posts= new ArrayList<>();

        rv = findViewById(R.id.discussion_posts);
       // postAdapter = new PostAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        rv.setLayoutManager(gridLayoutManager);
        rv.addItemDecoration(new Space(1, 20, true, 0));
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (postAdapter.getItemViewType(position)) {
                    case DiscussionPostAdapter.POST_ITEM:
                        return 1;
                    case DiscussionPostAdapter.LOADING_ITEM:
                        return 1;
                    default:
                        return -1;
                }
            }
        });

        mySwipeRefreshLayout = findViewById(R.id.refresh_post);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        loadPosts();
                    }
                }
        );

        EndlessScrollListener endlessScrollListener = new EndlessScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (!postAdapter.loading) {
                    loadPosts();
                    //feedData();
                }
            }
        };

        rv.setAdapter(postAdapter);
        endlessScrollListener.onLoadMore(0, 0);
    }

    private void feedData()
    {
        mySwipeRefreshLayout.setRefreshing(true);
       // adapter.showLoading();
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
        rv.setAdapter(postAdapter);
        postAdapter.notifyDataSetChanged();
    }


 private void loadPosts()
 {
     mySwipeRefreshLayout.setRefreshing(true);
     adapter.showLoading();
     // Load dummy posts with wait
      new Thread(new Runnable() {
      @Override
      public void run() {
           try {
              Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
     }
     final ArrayList<DiscussionPost> dummyPosts = new ArrayList<>();
     for(int i = 1 ; i <= 5 ; i++) {
         dummyPosts.add(new DiscussionPost("Ramsha", "This is a dummy post "+i+" and it will be replaced by the author."));
     }
     runOnUiThread(new Runnable() {
       @Override
     public void run() {
       adapter.hideLoading();
     adapter.setPosts(dummyPosts);
     mySwipeRefreshLayout.setRefreshing(false);
     }
       });
     }
     }).start();
 }



    public void addPost(View v) {
        Intent i = new Intent(this, AddPost.class);
        startActivity(i);
    }



}
