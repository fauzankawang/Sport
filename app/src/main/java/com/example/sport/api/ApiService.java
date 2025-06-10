package com.example.sport.api;

import com.example.sport.model.SportResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("api/v1/sports")
    Call<SportResponse> getAllSports();

    @GET("api/v1/sports/search")
    Call<SportResponse> searchSports(@Query("query") String query);
}