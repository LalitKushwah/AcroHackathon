package com.acro.hackathon.trekking.adapter;

/**
 * Created by abc123 on 26/3/17.
 */

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.acro.hackathon.trekking.NearByPlacesActivity;
import com.acro.hackathon.trekking.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;


/**
 * Created by abc123 on 10/3/17.
 */

public class NearByPlacesList extends RecyclerView.Adapter<NearByPlacesList.TextViews> {

    ArrayList<String> name=new ArrayList<>();
    ArrayList<String> images=new ArrayList<>();
    ArrayList<String> distance=new ArrayList<>();
    Context context;

    public NearByPlacesList(ArrayList<String> resultArrayList,ArrayList<String> images,ArrayList<String> distance,Context context) {
        this.name=resultArrayList;
        this.images=images;
        this.context=context;
        this.distance=distance;
    }
    @Override
    public TextViews onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.near_by_places_list_item, parent, false);
        TextViews vh = new TextViews(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(NearByPlacesList.TextViews holder, final int position) {
        holder.getName().setText(name.get(position).toString());
        holder.getDistance().setText(distance.get(position));

        holder.getCardView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });


        int r=R.mipmap.image_not_found_robo;
        if(NearByPlacesActivity.defaultLogoFlag.equals("hotel")) {
            r=R.mipmap.hotel;
        }
        if(NearByPlacesActivity.defaultLogoFlag.equals("hospital")) {
            r=R.mipmap.hospital;
        }

        Picasso.with(context)
                .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+images.get(position)+"&key=AIzaSyBMD05Jxc4JmbVNQSzIn9UC-6HcrbnO6F0")
                .placeholder(r)
                .resize(200,200)
                .into(holder.getImageView());

    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class TextViews extends RecyclerView.ViewHolder{
        TextView name;

        public TextView getDistance() {
            return distance;
        }

        TextView distance;
        ImageView imageView;
        CardView cardView;

        public TextViews(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.textView);
            cardView=(CardView)itemView.findViewById(R.id.cardView);
            imageView=(ImageView)itemView.findViewById(R.id.imageView);
            distance=(TextView)itemView.findViewById(R.id.distance);
        }
        public TextView getName(){return name;}
        public CardView getCardView() {
            return cardView;
        }
        public ImageView getImageView() {
            return imageView;
        }


    }
}
