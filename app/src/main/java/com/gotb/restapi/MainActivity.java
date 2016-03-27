package com.gotb.restapi;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    public final String URL = "http://jsonplaceholder.typicode.com";
    private GetPostsService service;
    private TextView tvSwipeDown;
    private List<String> list;
    private ListAdapter listAdapter;
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        tvSwipeDown = (TextView) findViewById(R.id.tvSwipeDown);
        list = new ArrayList<>();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        if (swipeRefreshLayout != null) {swipeRefreshLayout.setOnRefreshListener(this);}

        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        service = retrofit.create(GetPostsService.class);
    }


    @Override
    public void onRefresh() {
        service.getPosts().enqueue(new Callback<List<Posts>>() {
            @Override
            public void onResponse(Response<List<Posts>> response, Retrofit retrofit) {

                for (Posts posts : response.body()) {
                    list.add("User id: " + posts.getUserId()
                            + "\nId: " + posts.getId()
                            + "\nTittle: " + posts.getTitle()
                            + "\nBody: " + posts.getBody());
                }
                listAdapter = new ArrayAdapter<>(MainActivity.this,R.layout.list_item, list);
                tvSwipeDown.setVisibility(View.GONE);
                listView.setAdapter(listAdapter);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }
}
