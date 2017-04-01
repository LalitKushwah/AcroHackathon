package com.acro.hackathon.trekking.network;

import com.acro.hackathon.trekking.POJO.Result;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by DELL on 4/1/2017.
 */

public interface Registration {
    @GET("/bc/create-account.php")
    Call<Result> updateUser(@Query("name") String name, @Query("number") String number
            , @Query("gender") String gender, @Query("address") String address);
}