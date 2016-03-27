package com.gotb.restapi;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;

public interface GetPostsService {
    @GET("/posts")
    Call<List<Posts>> getPosts();
}
