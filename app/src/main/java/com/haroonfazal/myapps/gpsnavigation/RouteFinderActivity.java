package com.haroonfazal.myapps.gpsnavigation;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class RouteFinderActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener,GoogleApiClient.ConnectionCallbacks
        ,GoogleApiClient.OnConnectionFailedListener{

    EditText e1;
    Button b2;
    private GoogleMap mMap;
    GoogleApiClient client;
    LocationRequest request;
    LatLng latLngStart,latLngEnd;
    Marker currentMarker,destinationMarker;
    ImageView i1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_finder);
        e1 = (EditText)findViewById(R.id.editText);
        b2 = (Button)findViewById(R.id.editText3);

        i1 = (ImageView)findViewById(R.id.imageView2);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mylocMap);
        mapFragment.getMapAsync(this);
        Toolbar toolbar =  (Toolbar)findViewById(R.id.myToolbar);
        toolbar.setTitle("Route Finder");
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              if(latLngStart==null)
              {
                  Toast.makeText(getApplicationContext(),"First select starting position",Toast.LENGTH_SHORT).show();
              }
              else
              {
                  try {
                      Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(RouteFinderActivity.this);
                      startActivityForResult(intent, 200);
                  } catch (GooglePlayServicesNotAvailableException e) {
                      e.printStackTrace();
                  } catch (GooglePlayServicesRepairableException e) {
                      e.printStackTrace();
                  }
              }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 200 && resultCode == RESULT_OK)
        {
            Place place = PlaceAutocomplete.getPlace(this, data);
            latLngEnd = place.getLatLng();

            if(destinationMarker == null)
            {
                destinationMarker = mMap.addMarker(new MarkerOptions().position(latLngEnd).title("Destination"));
                b2.setText(getStringAddress(latLngEnd.latitude,latLngEnd.longitude));

                Intent myIntent = new Intent(this,MapDetailActivity.class);
                myIntent.putExtra("source",latLngStart);
                myIntent.putExtra("destination",latLngEnd);
                startActivity(myIntent);
                // send latLngStart and latLngEnd
            }
            else
            {
                destinationMarker.remove();
                destinationMarker = mMap.addMarker(new MarkerOptions().position(latLngEnd).title("Destination"));
                b2.setText(getStringAddress(latLngEnd.latitude,latLngEnd.longitude));
                Intent myIntent = new Intent(this,MapDetailActivity.class);
                myIntent.putExtra("source",latLngStart);
                myIntent.putExtra("destination",latLngEnd);
                startActivity(myIntent);
                // send LatLngStart and LatLngEnd
            }



        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Add a marker in Sydney and move the camera
        client = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        client.connect();


    }

    @Override
    public void onLocationChanged(Location location) {
        LocationServices.FusedLocationApi.removeLocationUpdates(client,this);
        if(location==null)
        {
            Toast.makeText(getApplicationContext(),"Location could not be found",Toast.LENGTH_LONG).show();
        }
        else
        {
            latLngStart = new LatLng(location.getLatitude(),location.getLongitude());

            MarkerOptions options = new MarkerOptions();
            options.position(latLngStart);
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.markersmall));
            options.title("Current Location");
            currentMarker =  mMap.addMarker(options);

           // get string address

           e1.setText(getStringAddress(latLngStart.latitude,latLngStart.longitude));

            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLngStart, 15);
            mMap.moveCamera(update);


            mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                    LatLng center = mMap.getCameraPosition().target;

                    if(b2.getText().toString().equals("") && currentMarker!=null)
                    {
                        currentMarker.remove();
                      //  Toast.makeText(getApplicationContext(),"in first condition",Toast.LENGTH_SHORT).show();
                        currentMarker = mMap.addMarker(new MarkerOptions().title("New Location").position(center).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                        latLngStart = currentMarker.getPosition();
                        e1.setText(getStringAddress(currentMarker.getPosition().latitude,currentMarker.getPosition().longitude));
                    }
                    else
                    {
                          i1.setVisibility(View.GONE);

                    }
                }
            });


        }
    }

    public String getStringAddress(Double lat,Double lng)
    {
        String address="";
        String city = "";
        Geocoder geocoder;
        List<Address> addresses;

try
{
    geocoder = new Geocoder(this, Locale.getDefault());
    addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

     address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
     city = addresses.get(0).getLocality();


}catch(Exception e)
{
    e.printStackTrace();
    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
}
        return address + " "+city;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        request = new LocationRequest().create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(1000);

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
}
