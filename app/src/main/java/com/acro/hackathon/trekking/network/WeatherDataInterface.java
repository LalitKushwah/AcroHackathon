package com.acro.hackathon.trekking.network;

import com.acro.hackathon.trekking.POJO.weather.ResponseData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by abc123 on 26/1/17.
 */

public interface WeatherDataInterface {


    @GET("data/2.5/forecast")
    Call<ResponseData> getWheatherReport(@Query("lat") String lat, @Query("lon") String longi,@Query("appid") String appid,@Query("units") String units);
}
