package com.acro.hackathon.trekking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.acro.hackathon.trekking.adapter.WeatherDetailsAdapter;

import org.w3c.dom.Text;

public class WeatherDetailsActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    public TextView maxTemp,minTemp,locationName,weatherDiscription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_details);

            recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
            LinearLayoutManager lm=new LinearLayoutManager(WeatherDetailsActivity.this);
            recyclerView.setLayoutManager(lm);
            WeatherDetailsAdapter adapter=new WeatherDetailsAdapter(WeatherDetailsActivity.this);
            recyclerView.setAdapter(adapter);

            maxTemp=(TextView)findViewById(R.id.maxTemp);
            minTemp=(TextView)findViewById(R.id.minTemp);
            locationName=(TextView)findViewById(R.id.locationName);
            weatherDiscription=(TextView)findViewById(R.id.weatherDiscription);

            maxTemp.setText(MainActivity.details.get(0)+"\u00B0");
            minTemp.setText(MainActivity.details.get(1)+"\u00B0");
            weatherDiscription.setText(MainActivity.details.get(2));

    }
}
