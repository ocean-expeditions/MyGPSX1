package com.haroonfazal.myapps.gpsnavigation;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

public class MapDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    Marker currentMarker,destinationMarker;
    LatLng latLngStart,latLngEnd;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_detail);

        adView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mylocMap);
        mapFragment.getMapAsync(this);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView2);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        Intent myIntent = getIntent();
       latLngStart = myIntent.getParcelableExtra("source");
        latLngEnd = myIntent.getParcelableExtra("destination");

    }


    public void navigate(View v)
    {
        try
        {
            if(latLngStart!=null && latLngEnd!=null)
            {
                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%f,%f(%s)&daddr=%f,%f (%s)", latLngStart.latitude, latLngStart.longitude, "Source", latLngEnd.latitude, latLngEnd.longitude, "Destination");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        }catch(ActivityNotFoundException e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Google Maps is not installed in your phone. Please install.",Toast.LENGTH_SHORT).show();
        }


    }



    public void requestDirections()
    {
        try
        {
            StringBuilder sb;

            Object[] dataTransfer = new Object[5];

            sb = new StringBuilder();
            sb.append("https://maps.googleapis.com/maps/api/directions/json?");
            sb.append("origin=" + latLngStart.latitude + "," + latLngStart.longitude);
            sb.append("&destination=" + latLngEnd.latitude + "," + latLngEnd.longitude);
            sb.append("&key=" + getResources().getString(R.string.directions_api));
            sb.append("&alternatives=true");


            MultipleDirections directions = new MultipleDirections(this);
            dataTransfer[0] = mMap;
            dataTransfer[1] = sb.toString();
            dataTransfer[2] = new LatLng(latLngStart.latitude, latLngStart.longitude);
            dataTransfer[3] = new LatLng(latLngEnd.latitude, latLngEnd.longitude);
            dataTransfer[4] = recyclerView;
            directions.execute(dataTransfer);
        }catch(Exception e)
        {
            e.printStackTrace();
        }



        // set the distance

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(latLngStart!=null && latLngEnd!=null)
        {
            MarkerOptions options = new MarkerOptions();
            options.position(latLngStart);
            options.title("Current Location");
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            currentMarker = mMap.addMarker(options);

          //  currentMarker = mMap.addMarker(new MarkerOptions().position(latLngStart).title("Current Location"));
            destinationMarker = mMap.addMarker(new MarkerOptions().position(latLngEnd).title("Destination Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLngStart, 15);
            mMap.moveCamera(update);

            requestDirections();
        }


    }
}
