package com.haroonfazal.myapps.gpsnavigation;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class OpenPlacesActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener,LocationListener,OnMapReadyCallback{

     RecyclerView recyclerView;
     RecyclerView.Adapter adapter;
    LatLng latLngStart;
        Marker currentMarker;
    GoogleMap mMap;
    RecyclerView.LayoutManager layoutManager;
    GoogleApiClient client;
    LocationRequest request;
    ArrayList<String> namesList,lat_list,lng_list,vicinity_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_places);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView1);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mylocMap);
        mapFragment.getMapAsync(this);

      namesList= (ArrayList<String>) getIntent().getSerializableExtra("names_list");
      lat_list=  (ArrayList<String>) getIntent().getSerializableExtra("lat_list");
      lng_list =   (ArrayList<String>) getIntent().getSerializableExtra("lng_list");
      vicinity_list=   (ArrayList<String>) getIntent().getSerializableExtra("vicinity_list");

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        client = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        client.connect();

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        request = new LocationRequest().create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(7000);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(client, request, this);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if(location==null)
        {
        Toast.makeText(getApplicationContext(),"Location could not be found",Toast.LENGTH_SHORT).show();
        }
        else
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(client,this);
                latLngStart = new LatLng(location.getLatitude(),location.getLongitude());

            MarkerOptions options = new MarkerOptions();
            options.position(latLngStart);
            options.title("Current Location");
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            currentMarker =  mMap.addMarker(options);

            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLngStart, 14);
            mMap.moveCamera(update);

            put_list(namesList,lat_list,lng_list,vicinity_list);
        }
    }

    public void put_list(ArrayList<String> name_List, ArrayList<String> latList, ArrayList<String> lngList,
                                ArrayList<String> vicinity_list)
    {
        try
        {
            String source_lat = String.valueOf(latLngStart.latitude);
            String source_lng = String.valueOf(latLngStart.longitude);
            ArrayList<SingleRow> mylist = new ArrayList<>();

            if(name_List.size()==0)
            {
                Toast.makeText(this,"No place found nearby.",Toast.LENGTH_LONG).show();

            }
            else
            {
                for(int i=0;i<name_List.size();i++)
                {
                    mylist.add(new SingleRow(name_List.get(i),vicinity_list.get(i),latList.get(i),lngList.get(i)));
                }

                adapter = new CountryAdapter(OpenPlacesActivity.this,  mylist,  name_List  ,vicinity_list,source_lat,source_lng);

                recyclerView.setAdapter(adapter);

                for(int i = 0;i<name_List.size(); i++)
                {
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.title(vicinity_list.get(i));
                    LatLng latLng = new LatLng(Double.parseDouble(latList.get(i)),Double.parseDouble(lngList.get(i)));
                    markerOptions.position(latLng);
                   markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    mMap.addMarker(markerOptions);

                }

            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }



        //  l1.setAdapter(new MyCustomAdapter(c,name_List,latList,lngList,source_lat,source_lng,vicinity_list));

    }

}
