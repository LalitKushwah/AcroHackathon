package com.acro.hackathon.trekking;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.acro.hackathon.trekking.POJO.nearByLocation.Result;
import com.acro.hackathon.trekking.adapter.NearByPlacesList;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.acro.hackathon.trekking.POJO.nearByLocation.NearByLocationResponse;
import com.acro.hackathon.trekking.network.NearByLocation;
import com.google.android.gms.vision.text.Line;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NearByPlacesActivity extends AppCompatActivity {

     String type;
     String latitude,longitude;
     RecyclerView recyclerView;
     LinearLayoutManager lm;
    NearByPlacesList adapter;
    ArrayList<String> images=new ArrayList<>();
    ArrayList<String> name=new ArrayList<>();
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_places);

        context=NearByPlacesActivity.this;
        recyclerView=(RecyclerView)findViewById(R.id.nearByPlacerecyclerView);
        lm=new LinearLayoutManager(NearByPlacesActivity.this);
        recyclerView.setLayoutManager(lm);

        adapter=new NearByPlacesList(name,images,context);
        recyclerView.setAdapter(adapter);



        Intent i =getIntent();
        type=i.getStringExtra("type");
        latitude=i.getStringExtra("latitude");
        longitude=i.getStringExtra("longitude");



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .client(MainActivity.getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NearByLocation service=retrofit.create(NearByLocation.class);
        Call<NearByLocationResponse> nearByLocationResponseCall=service.serachNearByLocation(latitude+","+longitude,5000,type,"AIzaSyBMD05Jxc4JmbVNQSzIn9UC-6HcrbnO6F0");
        nearByLocationResponseCall.enqueue(new Callback<NearByLocationResponse>() {
            @Override
            public void onResponse(Call<NearByLocationResponse> call, Response<NearByLocationResponse> response) {
                name.clear();
                images.clear();
                for(int i=0;i<response.body().getResults().size()-1;i++) {
                    name.add(response.body().getResults().get(i).getName());
                    images.add(response.body().getResults().get(i).getReference());
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<NearByLocationResponse> call, Throwable t) {
                Log.d("failure",t.getMessage());
            }
        });
    }


}
