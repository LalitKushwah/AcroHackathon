package com.acro.hackathon.trekking.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by DELL on 4/1/2017.
 */

public class Result {

    @SerializedName("result")
    @Expose
    private String result=null;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
