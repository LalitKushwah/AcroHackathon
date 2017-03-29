package com.acro.hackathon.trekking.adapter;

/**
 * Created by abc123 on 26/3/17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.acro.hackathon.trekking.R;
import java.util.ArrayList;


/**
 * Created by abc123 on 10/3/17.
 */

public class NearByPlacesList extends RecyclerView.Adapter<NearByPlacesList.TextViews> {

    ArrayList<String> name=new ArrayList<>();
    ArrayList<String> images=new ArrayList<>();
    Context context;

    public NearByPlacesList(ArrayList<String> resultArrayList,ArrayList<String> images,Context context) {
        this.name=resultArrayList;
        this.images=images;
        this.context=context;
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

        /*Picasso.with(context)
                .load("https://maps.googleapis.com/maps/api/place/photo?photoreference="+images.get(position))
                .into(holder.getImageView());*/
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class TextViews extends RecyclerView.ViewHolder{
        TextView name;
        ImageView imageView;

        public ImageView getImageView() {
            return imageView;
        }

        public TextViews(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.textView);
            imageView=(ImageView)itemView.findViewById(R.id.imageView);
        }
        public TextView getName(){return name;}
    }
}
