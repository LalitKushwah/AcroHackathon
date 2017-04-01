package com.acro.hackathon.trekking.network;

import com.acro.hackathon.trekking.POJO.routes.Treks;

import retrofit2.Call;
import retrofit2.http.POST;

/**
 * Created by ps11 on 31/03/17.
 */

public interface TrekkingRoutes {

    @POST("/Trek.json")
    Call<Treks> getTrekkingRoutes();
}
