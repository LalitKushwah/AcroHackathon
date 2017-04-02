package com.acro.hackathon.trekking;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.acro.hackathon.trekking.POJO.DangerMedical.DangerMedicalResponse;
import com.acro.hackathon.trekking.network.DangerMedicalCall;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.acro.hackathon.trekking.MainActivity.getUnsafeOkHttpClient;
import static com.acro.hackathon.trekking.MainActivity.latitude;
import static com.acro.hackathon.trekking.MainActivity.longitude;

public class TrekkingActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap mMap;
    Button danger;
    RadioGroup radioGroup;
    String emergencyType,mobileNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trekking);
        danger=(Button)findViewById(R.id.danger);



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        danger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog=new Dialog(TrekkingActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.emergency_dialog_item);
                dialog.getWindow().setLayout(700,485);
                dialog.show();

                Button baseCamp=(Button)dialog.findViewById(R.id.baseCamp);

                baseCamp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(TrekkingActivity.this, "Data has been successfully sent.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });



                radioGroup=(RadioGroup)dialog.findViewById(R.id.radioGroup);

                TelephonyManager telemamanger = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                String getSimSerialNumber = telemamanger.getSimSerialNumber();
                Toast.makeText(TrekkingActivity.this, getSimSerialNumber, Toast.LENGTH_SHORT).show();


                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if(checkedId==R.id.minorHealth) {
                            emergencyType="Minor Health";
                            Toast.makeText(TrekkingActivity.this, "Minor Health", Toast.LENGTH_SHORT).show();
                        }
                        if(checkedId==R.id.majorHealth) {
                            emergencyType="Major Health";
                            Toast.makeText(TrekkingActivity.this, "Major Health", Toast.LENGTH_SHORT).show();
                        }
                        if(checkedId==R.id.equipmentDamage) {
                            emergencyType=" equipment Need";
                            Toast.makeText(TrekkingActivity.this, "equipment Need", Toast.LENGTH_SHORT).show();
                        }
                        if(checkedId==R.id.stuckInStorm) {
                            emergencyType="Stuck in storm";
                            Toast.makeText(TrekkingActivity.this, "Stuck in storm", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.0.112:80")
                        .client(getUnsafeOkHttpClient())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                DangerMedicalCall service=retrofit.create(DangerMedicalCall.class);
                Call<DangerMedicalResponse> responseCall=service.sendDangerMedical("6545665","y","n");
                responseCall.enqueue(new Callback<DangerMedicalResponse>() {
                    @Override
                    public void onResponse(Call<DangerMedicalResponse> call, Response<DangerMedicalResponse> response) {

                    }

                    @Override
                    public void onFailure(Call<DangerMedicalResponse> call, Throwable t) {

                    }
                });


            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng indore = new LatLng((latitude), (longitude));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 11.0f));
        Marker laundary = mMap.addMarker(new MarkerOptions().position(indore).title("Your location"));
    }

    public void onClick(View view) {
        switch(view.getId()) {

            case R.id.bankBtn:
                //   Toast.makeText(this, "Bank button clicked", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(TrekkingActivity.this,NearByPlacesActivity.class);
                i.putExtra("type","bank");
                i.putExtra("latitude",String.valueOf(latitude));
                i.putExtra("longitude",String.valueOf(longitude));
                startActivity(i);
                break;
            case R.id.hospitalBtn:


                //   Toast.makeText(this, "Hospital button clicked", Toast.LENGTH_SHORT).show();
               Intent i1=new Intent(TrekkingActivity.this,NearByPlacesActivity.class);
                i1.putExtra("type","hospital");
                i1.putExtra("latitude",String.valueOf(latitude));
                i1.putExtra("longitude",String.valueOf(longitude));
                startActivity(i1);
                break;
            case R.id.eatries:

                //    Toast.makeText(this, "Eatries button clicked", Toast.LENGTH_SHORT).show();
                Intent i2=new Intent(TrekkingActivity.this,NearByPlacesActivity.class);
                i2.putExtra("type","eatries");
                i2.putExtra("latitude",String.valueOf(latitude));
                i2.putExtra("longitude",String.valueOf(longitude));
                startActivity(i2);
                break;
            case R.id.accomodation:

                //   Toast.makeText(this, "Accomodation button clicked", Toast.LENGTH_SHORT).show();
                Intent i3=new Intent(TrekkingActivity.this,NearByPlacesActivity.class);
                i3.putExtra("type","hotel");
                i3.putExtra("latitude",String.valueOf(latitude));
                i3.putExtra("longitude",String.valueOf(longitude));
                startActivity(i3);
                break;


        }

    }

}