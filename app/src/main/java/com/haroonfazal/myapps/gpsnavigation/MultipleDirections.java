package com.haroonfazal.myapps.gpsnavigation;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Haroon on 12/9/2017.
 */

public class MultipleDirections extends AsyncTask<Object,String,String>
{

    GoogleMap mMap;
    String url;
    LatLng startLatLng,endLatLng;

    HttpURLConnection httpURLConnection=null;
    String data = "";
    InputStream inputStream = null;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    Context c;

    MultipleDirections(Context c)
    {
        this.c=c;
    }

    @Override
    protected String doInBackground(Object... params) {
        mMap = (GoogleMap)params[0];
        url = (String)params[1];
        startLatLng = (LatLng)params[2];
        endLatLng = (LatLng)params[3];
        recyclerView = (RecyclerView)params[4];

        try {
            URL myurl = new URL(url);
            httpURLConnection = (HttpURLConnection)myurl.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer sb = new StringBuffer();
            String line="";
            while((line= bufferedReader.readLine())!=null)
            {
                sb.append(line);
            }
            data = sb.toString();
            bufferedReader.close();




        }catch(MalformedURLException e)
        {
            e.printStackTrace();
        }catch(IOException e)
        {
            e.printStackTrace();
        }


        return data;
    }

    @Override
    protected void onPostExecute(String str) {
        List<List<List<HashMap<String, String>>>> routes = null;

        try {
            JSONObject jsonObject = new JSONObject(str);

            JSONArray routesArray = jsonObject.getJSONArray("routes");
            JSONObject routesObj = routesArray.getJSONObject(0);

            JSONArray legsArray = routesObj.getJSONArray("legs");

            JSONArray jsonArray = jsonObject.getJSONArray("routes").getJSONObject(0)
                    .getJSONArray("legs");


            JSONObject jsonObject1 = jsonArray.getJSONObject(0);

            String distancetxt = jsonObject1.getJSONObject("distance").getString("text");
            String durationtxt = jsonObject1.getJSONObject("duration").getString("text");

            DirectionsJsonParser parser = new DirectionsJsonParser();

            // Starts parsing data
            routes = parser.parse(jsonObject);

            ArrayList<LatLng> points = null;
        //    PolylineOptions lineOptions = new PolylineOptions();
            PolylineOptions lineOptions1 = null;
            String distance = "";
            String duration = "";

            Integer size1 = 0;
            Integer size2 = 0;
            Integer size3 = 0;


            List<LatLng> aline1 = new ArrayList<LatLng>();
            List<LatLng> aline2 = new ArrayList<LatLng>();
            List<LatLng> aline3 = new ArrayList<LatLng>();




            if (routes != null) {
                int i = 0;


                while (i < routes.size()) {


                    points = new ArrayList<LatLng>();

                    List<List<HashMap<String, String>>> path1 = routes.get(i);

                    for (int s = 0; s < path1.size(); s++) {


                        // Fetching i-th route
                        List<HashMap<String, String>> path = path1.get(s);

                        // Fetching all the points in i-th route

                        for (int j = 0; j < path.size(); j++) {
                            lineOptions1 = new PolylineOptions();
                            HashMap<String, String> point = path.get(j);

                            double lat = Double.parseDouble(point.get("lat"));
                            double lng = Double.parseDouble(point.get("lng"));
                            LatLng position = new LatLng(lat, lng);
                            points.add(position);
                        }


                    }
                    // }
                    if (i == 0) {


//                        line1.addAll(points);
//                        mMap.addPolyline(line1);

                        size1 = points.size();

                        aline1.addAll(points);
                    } else if (i == 1) {


//                        line2.addAll(points);
//                        mMap.addPolyline(line2);

                        aline2.addAll(points);
                        size2 = points.size();
                    } else if (i == 2) {


//                        line3.addAll(points);
//                        mMap.addPolyline(line3);

                        aline3.addAll(points);
                        size3 = points.size();
                    }
                    // Adding all the points in the route to LineOptions
                    i++;


                }
                // Drawing polyline in the Google Map for the i-th route
                // map.addPolyline(lineOptions);
            }

            if (size3 != 0)
            {

                if ((size1 > size2 && size1 > size3)) {
                    if (size2 > size3) {
                        PolylineOptions line1 = new PolylineOptions().width(8).color(Color.BLACK);
                        PolylineOptions line2 = new PolylineOptions().width(8).color(Color.BLACK);
                        PolylineOptions line3 = new PolylineOptions().width(11).color(Color.parseColor("#9C27B0"));

                        line1.addAll(aline1);
                        line2.addAll(aline2);
                        line3.addAll(aline3);

                        mMap.addPolyline(line1);
                        mMap.addPolyline(line2);
                        mMap.addPolyline(line3);


                    } else {

                        PolylineOptions line1 = new PolylineOptions().width(8).color(Color.BLACK);
                        PolylineOptions line2 = new PolylineOptions().width(11).color(Color.parseColor("#9C27B0"));
                        PolylineOptions line3 = new PolylineOptions().width(8).color(Color.BLACK);

                        line1.addAll(aline1);
                        line2.addAll(aline2);
                        line3.addAll(aline3);

                        mMap.addPolyline(line1);
                        mMap.addPolyline(line3);

                        mMap.addPolyline(line2);


                    }
                } else if ((size2 > size1 && size2 > size3)) {
                    if (size1 > size3) {
                        PolylineOptions line1 = new PolylineOptions().width(8).color(Color.BLACK);
                        PolylineOptions line2 = new PolylineOptions().width(8).color(Color.BLACK);
                        PolylineOptions line3 = new PolylineOptions().width(11).color(Color.parseColor("#9C27B0"));

                        line1.addAll(aline1);
                        line2.addAll(aline2);
                        line3.addAll(aline3);

                        mMap.addPolyline(line1);
                        mMap.addPolyline(line2);

                        mMap.addPolyline(line3);



                    } else {

                        PolylineOptions line1 = new PolylineOptions().width(11).color(Color.parseColor("#9C27B0"));
                        PolylineOptions line2 = new PolylineOptions().width(8).color(Color.BLACK);
                        PolylineOptions line3 = new PolylineOptions().width(8).color(Color.BLACK);

                        line1.addAll(aline1);
                        line2.addAll(aline2);
                        line3.addAll(aline3);


                        mMap.addPolyline(line2);
                        mMap.addPolyline(line3);

                        mMap.addPolyline(line1);

                    }
                } else if ((size3 > size1 && size3 > size2)) {
                    if (size1 > size2) {
                        PolylineOptions line1 = new PolylineOptions().width(8).color(Color.BLACK);
                        PolylineOptions line2 = new PolylineOptions().width(11).color(Color.parseColor("#9C27B0"));
                        PolylineOptions line3 = new PolylineOptions().width(8).color(Color.BLACK);

                        line1.addAll(aline1);
                        line2.addAll(aline2);
                        line3.addAll(aline3);


                        mMap.addPolyline(line3);
                        mMap.addPolyline(line1);
                        mMap.addPolyline(line2);

                    } else {
                        PolylineOptions line1 = new PolylineOptions().width(11).color(Color.parseColor("#9C27B0"));
                        PolylineOptions line2 = new PolylineOptions().width(8).color(Color.BLACK);
                        PolylineOptions line3 = new PolylineOptions().width(8).color(Color.BLACK);

                        line1.addAll(aline1);
                        line2.addAll(aline2);
                        line3.addAll(aline3);

                        mMap.addPolyline(line3);
                        mMap.addPolyline(line2);

                        mMap.addPolyline(line1);


                    }
                } else {
                    System.out.println("ERROR!");
                }

            }else if(size2!=0)
            {
                if(size1>size2){

                    PolylineOptions line1 = new PolylineOptions().width(8).color(Color.BLACK);
                    PolylineOptions line2 = new PolylineOptions().width(11).color(Color.parseColor("#9C27B0"));


                    line1.addAll(aline1);
                    line2.addAll(aline2);

                    mMap.addPolyline(line1);
                    mMap.addPolyline(line2);

                }else
                {
                    PolylineOptions line1 = new PolylineOptions().width(11).color(Color.parseColor("#9C27B0"));
                    PolylineOptions line2 = new PolylineOptions().width(8).color(Color.BLACK);

                    line1.addAll(aline1);
                    line2.addAll(aline2);

                    mMap.addPolyline(line2);
                    mMap.addPolyline(line1);
                }



            }
            else if(size1!=0){
                PolylineOptions line1 = new PolylineOptions().width(11).color(Color.parseColor("#9C27B0"));
                line1.addAll(aline1);
                mMap.addPolyline(line1);
            }

            adapter = new DistanceAdapter(c,distancetxt,durationtxt);


            recyclerView.setAdapter(adapter);



    }catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(c,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}
