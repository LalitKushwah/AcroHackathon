package com.acro.hackathon.trekking.network;

import com.acro.hackathon.trekking.MainActivity;
import com.acro.hackathon.trekking.POJO.DangerMedical.DangerMedicalResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by abc123 on 1/4/17.
 */

public interface DangerMedicalCall {

    @GET("/Originals/bc/danger_medical.php")
    //Call<DangerMedicalResponse> sendDangerMedical(@Body MainActivity.PostDataForMedicalAndDanger data);
    Call<DangerMedicalResponse> sendDangerMedical(@Query("mobile") String mobile,@Query("danger") String danger,@Query("medical") String medical);


}



