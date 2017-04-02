package com.acro.hackathon.trekking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.acro.hackathon.trekking.POJO.routes.Treks;
import com.acro.hackathon.trekking.network.TrekkingRoutes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IntroActivity extends AppCompatActivity {


    String treks[] = new String[14];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trek_select_screen);
        getTrekkingRoutesName();
    }
    public void getTrekkingRoutesName(){


        Retrofit adapter = new Retrofit.Builder()
                .baseUrl("http://acrokids-ps11.rhcloud.com/")
                .client(MainActivity.getUnsafeOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TrekkingRoutes service = adapter.create(TrekkingRoutes.class);

        Call<Treks> response = service.getTrekkingRoutes();

        response.enqueue(new Callback<Treks>() {
            @Override
            public void onResponse(Call<Treks> call, Response<Treks> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i <response.body().getFeatures().size() ; i++) {
                        treks[i] = response.body().getFeatures().get(i).getProperties().getName();
                    }
                    Intent i = new Intent(IntroActivity.this, MainActivity.class);
                    i.putExtra("treks", treks);
                    startActivity(i);
                    finish();
                }
            }
            @Override
            public void onFailure(Call<Treks> call, Throwable t) {

            }
        });






    }
}
