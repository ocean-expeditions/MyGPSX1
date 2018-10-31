package com.haroonfazal.myapps.gpsnavigation;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GetNearByPlacesData extends AsyncTask<Object,String,String>

{
    private String googlePlacesData;
    private String url;


    Context c;
    GetNearByPlacesData(Context c)
    {
        this.c=c;
    }
    @Override
    protected String doInBackground(Object... params) {

        url = (String)params[0];
        DownloadUrl downloadUrl = new DownloadUrl();
        try
        {
            googlePlacesData = downloadUrl.readUrl(url);

        }catch(IOException e)
        {
            e.printStackTrace();
        }

        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(final String s) {

        try
        {
            List<HashMap<String,String>> nearbyPlaceList;
            DataParser parser = new DataParser();
            nearbyPlaceList = parser.parse(s);
            showNearByPlaces(nearbyPlaceList);
        }catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(c,e.getMessage(),Toast.LENGTH_SHORT).show();
        }



    }

    private void showNearByPlaces(List<HashMap<String,String>> nearbyPlaceList)
    {
        try
        {
            String place_name="";
            String vicinity="";
            ArrayList<String> names_list = new ArrayList<>();


            ArrayList<String> lat_list = new ArrayList<>();
            ArrayList<String> lng_list = new ArrayList<>();
            ArrayList<String> vicinity_list = new ArrayList<>();


            double lat,lng;
            for(int i= 0;i<nearbyPlaceList.size();i++)
            {
                MarkerOptions options  = new MarkerOptions();
                HashMap<String,String>  googlePlace = nearbyPlaceList.get(i);
                place_name = googlePlace.get("place_name");

                names_list.add(place_name);


                vicinity = googlePlace.get("vicinity");

                vicinity_list.add(vicinity);
                lat = Double.parseDouble(googlePlace.get("lat"));

                lng = Double.parseDouble(googlePlace.get("lng"));

                String lat_send = String.valueOf(lat);
                String lng_send = String.valueOf(lng);

                lat_list.add(lat_send);
                lng_list.add(lng_send);

                LatLng latLng = new LatLng(lat,lng);


                options.position(latLng);
                options.title(place_name+": "+vicinity);

            }

            // send values to OpenPlaces activity

            Intent myIntent = new Intent(c,OpenPlacesActivity.class);
            myIntent.putExtra("names_list",names_list);
            myIntent.putExtra("lat_list",lat_list);
            myIntent.putExtra("lng_list",lng_list);
            myIntent.putExtra("vicinity_list",vicinity_list);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            c.startActivity(myIntent);
        }catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(c,e.getMessage(),Toast.LENGTH_SHORT).show();
        }



      //  SearchActivity.put_list(names_list,lat_list,lng_list,vicinity_list);
    }

}
