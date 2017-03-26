package com.acro.hackathon.trekking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.acro.hackathon.trekking.POJO.nearByLocation.NearByLocationResponse;
import com.acro.hackathon.trekking.network.NearByLocation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NearByPlacesActivity extends AppCompatActivity {

     String type;
     String latitude,longitude;
     Double placeLatitude,placeLongitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_places);


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

            }

            @Override
            public void onFailure(Call<NearByLocationResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(lati,longi);
        LatLng talawlai =new LatLng((lati+0.05),(longi+0.05));
        Marker customer= mMap.addMarker(new MarkerOptions().position(sydney)
                .title("Your Location"));
        Marker laundary= mMap.addMarker(new MarkerOptions().position(talawlai).title("Employee"));
        laundary.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
        laundary.showInfoWindow();

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lati, longi), 11.0f));
        Circle circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(lati, longi))
                .radius(10000)
                .strokeColor(Color.GRAY)
                .fillColor(Color.WHITE)); //Inside color

    }
}
