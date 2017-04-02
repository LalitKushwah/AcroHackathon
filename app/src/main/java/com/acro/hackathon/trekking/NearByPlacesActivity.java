package com.acro.hackathon.trekking;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.acro.hackathon.trekking.POJO.nearByLocation.Result;
import com.acro.hackathon.trekking.adapter.NearByPlacesList;
import com.acro.hackathon.trekking.utils.SharedPrefUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.acro.hackathon.trekking.POJO.nearByLocation.NearByLocationResponse;
import com.acro.hackathon.trekking.network.NearByLocation;
import com.google.android.gms.vision.text.Line;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NearByPlacesActivity extends AppCompatActivity {

     String type;
     public static String defaultLogoFlag ;
     String latitude,longitude;
     RecyclerView recyclerView;
     LinearLayoutManager lm;
     NearByPlacesList adapter;
     ArrayList<String> images=new ArrayList<>();
     ArrayList<String> name=new ArrayList<>();
     ArrayList<String> distance=new ArrayList<>();
     ArrayList<Double> destinationLat=new ArrayList<>();
     ArrayList<Double> destinationLng=new ArrayList<>();
    Context context;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_places);



        context=NearByPlacesActivity.this;

        recyclerView=(RecyclerView)findViewById(R.id.nearByPlacerecyclerView);
        lm=new LinearLayoutManager(NearByPlacesActivity.this);
        recyclerView.setLayoutManager(lm);
        title=(TextView)findViewById(R.id.title);
        adapter=new NearByPlacesList(name,images,distance,destinationLat,destinationLng,context);
        recyclerView.setAdapter(adapter);



        Intent i =getIntent();
        type=i.getStringExtra("type");
        latitude=i.getStringExtra("latitude");
        longitude=i.getStringExtra("longitude");

         defaultLogoFlag=type;

         title.setText(type.toUpperCase());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .client(MainActivity.getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NearByLocation service=retrofit.create(NearByLocation.class);
        Call<NearByLocationResponse> nearByLocationResponseCall=service.serachNearByLocation(MainActivity.latitude+","+MainActivity.longitude,50000,type,"AIzaSyBMD05Jxc4JmbVNQSzIn9UC-6HcrbnO6F0");
        nearByLocationResponseCall.enqueue(new Callback<NearByLocationResponse>() {
            @Override
            public void onResponse(Call<NearByLocationResponse> call, Response<NearByLocationResponse> response) {
                name.clear();
                images.clear();
                destinationLat.clear();
                destinationLng.clear();


                Log.d("size",String.valueOf(response.body().getResults().size()));
                for(int i=0;i<response.body().getResults().size()-1;i++) {
                    name.add(response.body().getResults().get(i).getName());
                    distance.add(String.valueOf(distFrom(MainActivity.latitude,MainActivity.longitude,response.body().getResults().get(i).getGeometry().getLocation().getLat(),response.body().getResults().get(i).getGeometry().getLocation().getLng())));
                    destinationLat.add(response.body().getResults().get(i).getGeometry().getLocation().getLat());
                    destinationLng.add(response.body().getResults().get(i).getGeometry().getLocation().getLng());
                    if(response.body().getResults().get(i).getPhotos()==null) {
                        images.add("CnRtAAAATLZNl354RwP_9UKbQ_5Psy40texXePv4oAlgP4qNEkdIrkyse7rPXYGd9D_Uj1rVsQdWT4oRz4QrYAJNpFX7rzqqMlZw2h2E2y5IKMUZ7ouD_SlcHxYq1yL4KbKUv3qtWgTK0A6QbGh87GB3sscrHRIQiG2RrmU_jF4tENr9wGS_YxoUSSDrYjWmrNfeEHSGSc3FyhNLlBU");
                    }
                    else {
                        images.add(response.body().getResults().get(i).getPhotos().get(0).getPhotoReference());
                    }
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<NearByLocationResponse> call, Throwable t) {
                Log.d("failure",t.getMessage());
            }
        });
    }

    public  Double distFrom(Double lat1, Double lng1, Double lat2, Double lng2) {
        double earthRadius = 6371; //km
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        Double dist = (Double) (earthRadius * c);
        Double truncatedDouble = BigDecimal.valueOf(dist)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        return truncatedDouble;
    }



}
