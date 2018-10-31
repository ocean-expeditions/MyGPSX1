package com.haroonfazal.myapps.gpsnavigation;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder>
{

    ArrayList<SingleRow> countryList = new ArrayList<>();
Context c;
String source_lat,source_lng;
    InterstitialAd interstitialAd;

    public CountryAdapter(Context c, ArrayList<SingleRow> countryList, ArrayList<String> nameList, ArrayList<String> vicinityList, String source_lat, String source_lng)
    {
        this.c=c;
        this.countryList = countryList;
        this.source_lat = source_lat;
        this.source_lng = source_lng;
        interstitialAd = new InterstitialAd(c);
        interstitialAd.setAdUnitId(c.getString(R.string.interstional_full_screen));
        interstitialAd.loadAd(new AdRequest.Builder().build());
    }


    @Override
    public CountryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout,parent,false);
        CountryViewHolder countryViewHolder = new CountryViewHolder(view);
        return countryViewHolder;

    }

    @Override
    public void onBindViewHolder(final CountryViewHolder holder, int position) {
            final SingleRow con = countryList.get(position);
       holder.name_txt.setText(con.name);
        holder.address_txt.setText(con.vicinity);

        String lat_desti = con.latitude;
        String lng_desti = con.longitude;

        final double source_lat_double = Double.parseDouble(source_lat);
        final double source_lng_double = Double.parseDouble(source_lng);

        double desti_lat_double = Double.parseDouble(lat_desti);
        double desti_lng_double = Double.parseDouble(lng_desti);


        LatLng latLngStart = new LatLng(source_lat_double,source_lng_double);
        final LatLng latLngEnd = new LatLng(desti_lat_double,desti_lng_double);

        double distance = CalculationByDistance(latLngStart,latLngEnd);
        DecimalFormat df = new DecimalFormat("#.##");
        String dist = df.format(distance);


        holder.distance_txt.setText(dist + " KM");

        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interstitialAd.loadAd(new AdRequest.Builder().build());
                if(interstitialAd.isLoaded())
                {
                    interstitialAd.show();
                    interstitialAd.setAdListener(new AdListener()
                    {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            interstitialAd.loadAd(new AdRequest.Builder().build());
                        try {
                            Intent i = new Intent(c,MapDetailActivity.class);

                            LatLng latLngDesti = new LatLng(Double.parseDouble(con.latitude),Double.parseDouble(con.longitude));
                            LatLng latLngSource = new LatLng(source_lat_double,source_lng_double);
                            i.putExtra("destination",latLngDesti);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("source",latLngSource);

                            c.startActivity(i);
                        }catch(Exception e)
                        {
                            e.printStackTrace();
                        }

                        }
                    });
                }

                else
                {
                    Intent i = new Intent(c,MapDetailActivity.class);
                    LatLng latLngDesti = new LatLng(Double.parseDouble(con.latitude),Double.parseDouble(con.longitude));
                    LatLng latLngSource = new LatLng(source_lat_double,source_lng_double);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("latitude_destination",latLngDesti);
                    i.putExtra("longitude_source",latLngSource);


                    c.startActivity(i);
                }



            }
        });
    }


     double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius=6371;//radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult= Radius*c;
        double km=valueResult/1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec =  Integer.valueOf(newFormat.format(km));
        double meter=valueResult%1000;
        int  meterInDec= Integer.valueOf(newFormat.format(meter));


        return meter;
    }


    @Override
    public int getItemCount()
    {
        return countryList.size();
    }








     static class CountryViewHolder extends RecyclerView.ViewHolder
    {
        TextView name_txt,distance_txt,address_txt;

        View v;


        public CountryViewHolder(View itemView) {
            super(itemView);

            this.v = itemView;

            name_txt = itemView.findViewById(R.id.item_title);
            distance_txt = itemView.findViewById(R.id.item_distance);
            address_txt = itemView.findViewById(R.id.item_address);



        }

    }
}

