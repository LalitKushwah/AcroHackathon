package com.acro.hackathon.trekking.network;

import retrofit2.Call;
import retrofit2.http.POST;

/**
 * Created by ps11 on 31/03/17.
 */

public interface TrekkingRoutes {

    @POST("/new_routes.json")
    Call<com.acro.hackathon.trekking.POJO.routes.TrekkingRoutes> getTrekkingRoutes();


}
