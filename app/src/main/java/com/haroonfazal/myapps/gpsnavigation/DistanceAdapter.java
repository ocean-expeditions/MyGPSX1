package com.haroonfazal.myapps.gpsnavigation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class DistanceAdapter extends RecyclerView.Adapter<DistanceAdapter.DistanceViewHolder>
{


    private Context c;
    String distanceTxt,durationTxt;
    DistanceAdapter(Context c,String distanceTxt,String durationTxt)
    {
        this.c=c;
        this.distanceTxt = distanceTxt;
        this.durationTxt = durationTxt;
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public void onBindViewHolder(DistanceViewHolder holder, int position) {

       holder.distance_txt.setText(distanceTxt);
        holder.duration_textview.setText(durationTxt);





    }

    @Override
    public DistanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.distancelayout,parent,false);
        DistanceAdapter.DistanceViewHolder distanceViewHolder = new DistanceAdapter.DistanceViewHolder(view);
        return distanceViewHolder;
    }

    public static class DistanceViewHolder extends RecyclerView.ViewHolder
    {
        TextView distance_txt,duration_textview;

        View v;


        public DistanceViewHolder(View itemView) {
            super(itemView);
            this.v = itemView;
            distance_txt = itemView.findViewById(R.id.item_distance);
            duration_textview = itemView.findViewById(R.id.item_duration);

        }

    }






}
