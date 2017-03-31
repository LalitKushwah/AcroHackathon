package com.acro.hackathon.trekking.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by abc123 on 24/11/16.
 */

public class SharedPrefUtil {

    Context mContext;

    public SharedPrefUtil(Context mContext) {
        this.mContext = mContext;
    }

    public void addString(String key, String value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("APOS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public void addInteger(String key, Integer value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("APOS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key,value);
        editor.commit();
    }

    public String retrieveString(String key, String defValue){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("APOS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        return sharedPreferences.getString(key,defValue);
    }

    public int retrieveInt(String key, int defValue){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("APOS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        return sharedPreferences.getInt(key,defValue);
    }

}
