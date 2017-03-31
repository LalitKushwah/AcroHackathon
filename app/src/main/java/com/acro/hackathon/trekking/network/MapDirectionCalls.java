package com.acro.hackathon.trekking.network;

import com.acro.hackathon.trekking.POJO.mapDirection.MapDirectionResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by abc123 on 29/3/17.
 */

public interface MapDirectionCalls {


    @GET("maps/api/directions/json")
    Call<MapDirectionResponse> mapDirectionPath(@Query("origin") String location, @Query("destination") String radius, @Query("mode") String type, @Query("key") String key);




}
