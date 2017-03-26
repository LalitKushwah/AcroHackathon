package com.acro.hackathon.trekking.network;

import com.acro.hackathon.trekking.POJO.nearByLocation.NearByLocationResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by abc123 on 24/3/17.
 */

public interface NearByLocation {

        @GET("maps/api/place/nearbysearch/json")
        Call<NearByLocationResponse> serachNearByLocation(@Query("location") String location, @Query("radius") int radius, @Query("types") String type, @Query("key") String key);


}
