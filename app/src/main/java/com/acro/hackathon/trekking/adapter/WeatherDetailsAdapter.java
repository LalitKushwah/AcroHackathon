package com.acro.hackathon.trekking.adapter;

/**
 * Created by abc123 on 1/4/17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.acro.hackathon.trekking.MainActivity;
import com.acro.hackathon.trekking.R;

import java.util.ArrayList;


/**
 * Created by abc123 on 10/3/17.
 */

public class WeatherDetailsAdapter extends RecyclerView.Adapter<WeatherDetailsAdapter.TextViews> {

    Context context;
    ArrayList<String> daysName=new ArrayList<>();
    public WeatherDetailsAdapter(Context context) {
        this.context=context;
        daysName.add("Monday");
        daysName.add("Tuesday");
        daysName.add("Wednesday");
        daysName.add("Thursday");
        daysName.add("Friday");
        daysName.add("Saturday");
        daysName.add("Sunday");
    }
    @Override
    public TextViews onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_list_item, parent, false);
        TextViews vh = new TextViews(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(WeatherDetailsAdapter.TextViews holder, final int position) {
        holder.getMaxTemp().setText(MainActivity.weatherList.get(position).getMain().getTempMax()+"\u00B0");
        float value=Float.parseFloat(MainActivity.weatherList.get(position).getMain().getTempMin())-10;
        holder.getMinTemp().setText(String.format("%.2f",value)+"\u00B0");
        holder.getDayName().setText(daysName.get(position));

        if(MainActivity.weatherList.get(position).getMain()==null)
        {
            holder.getWeatherDiscription().setText("NA");
        }
        else {
            holder.getWeatherDiscription().setText(MainActivity.weatherList.get(position).getWeather().get(0).getDescription());
        }
        if(MainActivity.weatherList.get(position).getWeather().get(0).getMain().equals("Clear")) {
            holder.getWeatherImage().setImageResource(R.mipmap.clear_sky_icon);
        }

        if(MainActivity.weatherList.get(position).getWeather().get(0).getMain().equals("Rain")) {
            holder.getWeatherImage().setImageResource(R.mipmap.light_rain_icon);
        }
        if(MainActivity.weatherList.get(position).getWeather().get(0).getMain().equals("Clouds")) {
            holder.getWeatherImage().setImageResource(R.mipmap.clouds_icon_image);
        }


    }

    @Override
    public int getItemCount() {
        return 7;
    }

    public class TextViews extends RecyclerView.ViewHolder{

        TextView maxTemp,minTemp,weatherDiscription,dayName;
        ImageView weatherImage;

        public TextViews(View itemView) {
            super(itemView);
            weatherDiscription=(TextView)itemView.findViewById(R.id.weatherDiscription);
            dayName=(TextView)itemView.findViewById(R.id.dayName);
            maxTemp=(TextView)itemView.findViewById(R.id.maxTemp);
            minTemp=(TextView)itemView.findViewById(R.id.minTemp);
            weatherImage=(ImageView)itemView.findViewById(R.id.imageView);
        }

        public TextView getWeatherDiscription() {
            return weatherDiscription;
        }

        public TextView getDayName() {
            return dayName;
        }

        public TextView getMaxTemp() {
            return maxTemp;
        }

        public TextView getMinTemp() {
            return minTemp;
        }


        public ImageView getWeatherImage() {
            return weatherImage;
        }


    }
}
