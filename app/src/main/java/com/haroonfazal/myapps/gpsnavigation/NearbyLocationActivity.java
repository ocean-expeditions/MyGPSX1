package com.haroonfazal.myapps.gpsnavigation;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class NearbyLocationActivity extends AppCompatActivity implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener
, View.OnClickListener{

    LatLng latLngStart;
    InterstitialAd interstitialAd;

    GoogleApiClient client;
    LocationRequest request;
    CardView hospital,police,petrol,bank,bakery,restaurant,parking,mosque,firestation,dentist,gym,pharmacy,hotel,parks
            ,postoffice,toilet,servicestation,metrostation,shoppingmall,zoo,shoesstore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mylayout);
            interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.interstional_full_screen));
        interstitialAd.loadAd(new AdRequest.Builder().build());
        Toolbar toolbar =  (Toolbar)findViewById(R.id.myToolbar);
        toolbar.setTitle("Nearby Locations");
        setSupportActionBar(toolbar);

        AdView adView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }



        hospital = (CardView)findViewById(R.id.hospital);
        police = (CardView)findViewById(R.id.police);
        petrol = (CardView)findViewById(R.id.petrol);
        bank = (CardView)findViewById(R.id.bank);
        bakery = (CardView)findViewById(R.id.bakery);


        restaurant = (CardView)findViewById(R.id.restaurant);
        parking = (CardView)findViewById(R.id.parking);
        mosque = (CardView)findViewById(R.id.mosque);
        firestation = (CardView)findViewById(R.id.firestation);
        dentist = (CardView)findViewById(R.id.dentist);

        gym = (CardView)findViewById(R.id.gym);
        pharmacy = (CardView)findViewById(R.id.pharmacy);
        hotel = (CardView)findViewById(R.id.hotel);
        parks = (CardView)findViewById(R.id.park);
        postoffice = (CardView)findViewById(R.id.postoffice);

        toilet = (CardView)findViewById(R.id.toilet);
        servicestation = (CardView)findViewById(R.id.servicestation);
        metrostation = (CardView)findViewById(R.id.metrostation);
        shoppingmall = (CardView)findViewById(R.id.shoppingmall);
        zoo = (CardView)findViewById(R.id.zoo);
        shoesstore = (CardView)findViewById(R.id.shoesstore);

        shoesstore.setOnClickListener(this);
        zoo.setOnClickListener(this);
        shoppingmall.setOnClickListener(this);
        metrostation.setOnClickListener(this);
        servicestation.setOnClickListener(this);
        toilet.setOnClickListener(this);
        postoffice.setOnClickListener(this);
        parks.setOnClickListener(this);
        hotel.setOnClickListener(this);
        pharmacy.setOnClickListener(this);



        gym.setOnClickListener(this);
        dentist.setOnClickListener(this);
        firestation.setOnClickListener(this);
        mosque.setOnClickListener(this);
        parking.setOnClickListener(this);



        restaurant.setOnClickListener(this);
        bakery.setOnClickListener(this);
        bank.setOnClickListener(this);
        petrol.setOnClickListener(this);
        police.setOnClickListener(this);
        hospital.setOnClickListener(this);


        client = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        client.connect();

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
    public void onConnectionSuspended(int i) {

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

        }
    }


    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.hospital:

                if(latLngStart == null)
                {
                        Toast.makeText(getApplicationContext(),"GPS is not enabled. Please enable it.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(interstitialAd.isLoaded())
                    {
                        interstitialAd.show();
                        interstitialAd.setAdListener(new AdListener()
                        {
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                                interstitialAd.loadAd(new AdRequest.Builder().build());
                                String url = getUrlSpecial(String.valueOf(latLngStart.latitude),String.valueOf(latLngStart.longitude),"hospital");
                                Object dataTransfer[] = new Object[1];
                                //  dataTransfer[0] = mMap;
                                dataTransfer[0] = url;

                                GetNearByPlacesData getNearByPlacesData  = new GetNearByPlacesData(getApplicationContext());
                                getNearByPlacesData.execute(dataTransfer);
                                Toast.makeText(getApplicationContext(),"Showing nearby hospitals",Toast.LENGTH_LONG).show();

                            }
                        });

                    }
                    else
                    {
                        interstitialAd.loadAd(new AdRequest.Builder().build());
                        String url = getUrlSpecial(String.valueOf(latLngStart.latitude),String.valueOf(latLngStart.longitude),"hospital");
                        Object dataTransfer[] = new Object[1];
                        //  dataTransfer[0] = mMap;
                        dataTransfer[0] = url;

                        GetNearByPlacesData getNearByPlacesData  = new GetNearByPlacesData(getApplicationContext());
                        getNearByPlacesData.execute(dataTransfer);
                        Toast.makeText(getApplicationContext(),"Showing nearby hospitals",Toast.LENGTH_LONG).show();
                    }


                }

                break;
            case R.id.police:

                if(latLngStart == null)
                {
                    Toast.makeText(getApplicationContext(),"GPS is not enabled. Please enable it.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String url = getUrlSpecial(String.valueOf(latLngStart.latitude),String.valueOf(latLngStart.longitude),"police_station");
                    Object dataTransfer[] = new Object[1];
                    //  dataTransfer[0] = mMap;
                    dataTransfer[0] = url;

                    GetNearByPlacesData getNearByPlacesData  = new GetNearByPlacesData(getApplicationContext());
                    getNearByPlacesData.execute(dataTransfer);
                    Toast.makeText(getApplicationContext(),"Showing nearby police stations",Toast.LENGTH_LONG).show();

                }
                break;
            case R.id.petrol:
                if(latLngStart == null)
                {
                    Toast.makeText(getApplicationContext(),"GPS is not enabled. Please enable it.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(interstitialAd.isLoaded())
                    {
                        interstitialAd.show();
                        interstitialAd.setAdListener(new AdListener()
                        {
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                                String url = getUrlSpecial(String.valueOf(latLngStart.latitude),String.valueOf(latLngStart.longitude),"petrol");
                                Object dataTransfer[] = new Object[1];
                                //  dataTransfer[0] = mMap;
                                dataTransfer[0] = url;

                                GetNearByPlacesData getNearByPlacesData  = new GetNearByPlacesData(getApplicationContext());
                                getNearByPlacesData.execute(dataTransfer);
                                Toast.makeText(getApplicationContext(),"Showing nearby Petrol Stations",Toast.LENGTH_LONG).show();
                                interstitialAd.loadAd(new AdRequest.Builder().build());
                            }
                        });

                    }
                    else
                    {
                        String url = getUrlSpecial(String.valueOf(latLngStart.latitude),String.valueOf(latLngStart.longitude),"petrol");
                        Object dataTransfer[] = new Object[1];
                        //  dataTransfer[0] = mMap;
                        dataTransfer[0] = url;

                        GetNearByPlacesData getNearByPlacesData  = new GetNearByPlacesData(getApplicationContext());
                        getNearByPlacesData.execute(dataTransfer);
                        Toast.makeText(getApplicationContext(),"Showing nearby Petrol Stations",Toast.LENGTH_LONG).show();
                    }


                }
                break;
            case R.id.bank:
                if(latLngStart == null)
                {
                    Toast.makeText(getApplicationContext(),"GPS is not enabled. Please enable it.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String url = getUrl(String.valueOf(latLngStart.latitude),String.valueOf(latLngStart.longitude),"bank");
                    Object dataTransfer[] = new Object[1];
                    //  dataTransfer[0] = mMap;
                    dataTransfer[0] = url;

                    GetNearByPlacesData getNearByPlacesData  = new GetNearByPlacesData(getApplicationContext());
                    getNearByPlacesData.execute(dataTransfer);
                    Toast.makeText(getApplicationContext(),"Showing nearby banks",Toast.LENGTH_LONG).show();

                }
                break;
            case R.id.bakery:
                if(latLngStart == null)
                {
                    Toast.makeText(getApplicationContext(),"GPS is not enabled. Please enable it.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String url = getUrl(String.valueOf(latLngStart.latitude),String.valueOf(latLngStart.longitude),"bakery");
                    Object dataTransfer[] = new Object[1];
                    //  dataTransfer[0] = mMap;
                    dataTransfer[0] = url;

                    GetNearByPlacesData getNearByPlacesData  = new GetNearByPlacesData(getApplicationContext());
                    getNearByPlacesData.execute(dataTransfer);
                    Toast.makeText(getApplicationContext(),"Showing nearby bakery",Toast.LENGTH_LONG).show();

                }
                break;


            case R.id.restaurant:
                if(latLngStart == null)
                {
                    Toast.makeText(getApplicationContext(),"GPS is not enabled. Please enable it.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(interstitialAd.isLoaded())
                    {
                        interstitialAd.show();
                        interstitialAd.setAdListener(new AdListener()
                        {
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                                interstitialAd.loadAd(new AdRequest.Builder().build());
                                String url = getUrlSpecial(String.valueOf(latLngStart.latitude),String.valueOf(latLngStart.longitude),"restaurant");
                                Object dataTransfer[] = new Object[1];
                                //  dataTransfer[0] = mMap;
                                dataTransfer[0] = url;
                                GetNearByPlacesData getNearByPlacesData  = new GetNearByPlacesData(getApplicationContext());
                                getNearByPlacesData.execute(dataTransfer);
                                Toast.makeText(getApplicationContext(),"Showing nearby restaurants",Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                    else
                    {
                        String url = getUrlSpecial(String.valueOf(latLngStart.latitude),String.valueOf(latLngStart.longitude),"restaurant");
                        Object dataTransfer[] = new Object[1];
                        //  dataTransfer[0] = mMap;
                        dataTransfer[0] = url;

                        GetNearByPlacesData getNearByPlacesData  = new GetNearByPlacesData(getApplicationContext());
                        getNearByPlacesData.execute(dataTransfer);
                        Toast.makeText(getApplicationContext(),"Showing nearby restaurants",Toast.LENGTH_LONG).show();
                        interstitialAd.loadAd(new AdRequest.Builder().build());
                    }


                }

                break;
            case R.id.parking:
                if(latLngStart == null)
                {
                    Toast.makeText(getApplicationContext(),"GPS is not enabled. Please enable it.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String url = getUrl(String.valueOf(latLngStart.latitude),String.valueOf(latLngStart.longitude),"parking");
                    Object dataTransfer[] = new Object[1];
                    //  dataTransfer[0] = mMap;
                    dataTransfer[0] = url;

                    GetNearByPlacesData getNearByPlacesData  = new GetNearByPlacesData(getApplicationContext());
                    getNearByPlacesData.execute(dataTransfer);
                    Toast.makeText(getApplicationContext(),"Showing nearby parkings",Toast.LENGTH_LONG).show();

                }
                break;
            case R.id.mosque:
                if(latLngStart == null)
                {
                    Toast.makeText(getApplicationContext(),"GPS is not enabled. Please enable it.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String url = getUrlSpecial(String.valueOf(latLngStart.latitude),String.valueOf(latLngStart.longitude),"mosque");
                    Object dataTransfer[] = new Object[1];
                    //  dataTransfer[0] = mMap;
                    dataTransfer[0] = url;

                    GetNearByPlacesData getNearByPlacesData  = new GetNearByPlacesData(getApplicationContext());
                    getNearByPlacesData.execute(dataTransfer);
                    Toast.makeText(getApplicationContext(),"Showing nearby mosques",Toast.LENGTH_LONG).show();

                }
                break;
            case R.id.firestation:
                if(latLngStart == null)
                {
                    Toast.makeText(getApplicationContext(),"GPS is not enabled. Please enable it.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String url = getUrlSpecial(String.valueOf(latLngStart.latitude),String.valueOf(latLngStart.longitude),"fire_station");
                    Object dataTransfer[] = new Object[1];
                    //  dataTransfer[0] = mMap;
                    dataTransfer[0] = url;

                    GetNearByPlacesData getNearByPlacesData  = new GetNearByPlacesData(getApplicationContext());
                    getNearByPlacesData.execute(dataTransfer);
                    Toast.makeText(getApplicationContext(),"Showing nearby Fire stations",Toast.LENGTH_LONG).show();

                }
                break;
            case R.id.dentist:
                if(latLngStart == null)
                {
                    Toast.makeText(getApplicationContext(),"GPS is not enabled. Please enable it.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String url = getUrl(String.valueOf(latLngStart.latitude),String.valueOf(latLngStart.longitude),"dentist");
                    Object dataTransfer[] = new Object[1];
                    //  dataTransfer[0] = mMap;
                    dataTransfer[0] = url;

                    GetNearByPlacesData getNearByPlacesData  = new GetNearByPlacesData(getApplicationContext());
                    getNearByPlacesData.execute(dataTransfer);
                    Toast.makeText(getApplicationContext(),"Showing nearby dentists",Toast.LENGTH_LONG).show();

                }
                break;

            case R.id.gym:
                if(latLngStart == null)
                {
                    Toast.makeText(getApplicationContext(),"GPS is not enabled. Please enable it.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String url = getUrl(String.valueOf(latLngStart.latitude),String.valueOf(latLngStart.longitude),"gym");
                    Object dataTransfer[] = new Object[1];
                    //  dataTransfer[0] = mMap;
                    dataTransfer[0] = url;

                    GetNearByPlacesData getNearByPlacesData  = new GetNearByPlacesData(getApplicationContext());
                    getNearByPlacesData.execute(dataTransfer);
                    Toast.makeText(getApplicationContext(),"Showing nearby gyms",Toast.LENGTH_LONG).show();

                }

                break;
            case R.id.pharmacy:
                if(latLngStart == null)
                {
                    Toast.makeText(getApplicationContext(),"GPS is not enabled. Please enable it.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String url = getUrl(String.valueOf(latLngStart.latitude),String.valueOf(latLngStart.longitude),"pharmacy");
                    Object dataTransfer[] = new Object[1];
                    //  dataTransfer[0] = mMap;
                    dataTransfer[0] = url;

                    GetNearByPlacesData getNearByPlacesData  = new GetNearByPlacesData(getApplicationContext());
                    getNearByPlacesData.execute(dataTransfer);
                    Toast.makeText(getApplicationContext(),"Showing nearby pharmacy",Toast.LENGTH_LONG).show();

                }
                break;
            case R.id.hotel:
                if(latLngStart == null)
                {
                    Toast.makeText(getApplicationContext(),"GPS is not enabled. Please enable it.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(interstitialAd.isLoaded())
                    {
                        interstitialAd.show();
                        interstitialAd.setAdListener(new AdListener()
                        {
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                                interstitialAd.loadAd(new AdRequest.Builder().build());
                                String url = getUrl(String.valueOf(latLngStart.latitude),String.valueOf(latLngStart.longitude),"hotel");
                                Object dataTransfer[] = new Object[1];
                                //  dataTransfer[0] = mMap;
                                dataTransfer[0] = url;

                                GetNearByPlacesData getNearByPlacesData  = new GetNearByPlacesData(getApplicationContext());
                                getNearByPlacesData.execute(dataTransfer);
                                Toast.makeText(getApplicationContext(),"Showing nearby hotels",Toast.LENGTH_LONG).show();

                            }
                        });

                    }
                    else
                    {
                        interstitialAd.loadAd(new AdRequest.Builder().build());
                        String url = getUrl(String.valueOf(latLngStart.latitude),String.valueOf(latLngStart.longitude),"hotel");
                        Object dataTransfer[] = new Object[1];
                        //  dataTransfer[0] = mMap;
                        dataTransfer[0] = url;

                        GetNearByPlacesData getNearByPlacesData  = new GetNearByPlacesData(getApplicationContext());
                        getNearByPlacesData.execute(dataTransfer);
                        Toast.makeText(getApplicationContext(),"Showing nearby hotels",Toast.LENGTH_LONG).show();

                    }

                }
                break;
            case R.id.park:
                if(latLngStart == null)
                {
                    Toast.makeText(getApplicationContext(),"GPS is not enabled. Please enable it.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String url = getUrl(String.valueOf(latLngStart.latitude),String.valueOf(latLngStart.longitude),"parks");
                    Object dataTransfer[] = new Object[1];
                    //  dataTransfer[0] = mMap;
                    dataTransfer[0] = url;

                    GetNearByPlacesData getNearByPlacesData  = new GetNearByPlacesData(getApplicationContext());
                    getNearByPlacesData.execute(dataTransfer);
                    Toast.makeText(getApplicationContext(),"Showing nearby parks",Toast.LENGTH_LONG).show();

                }
                break;
            case R.id.postoffice:
                if(latLngStart == null)
                {
                    Toast.makeText(getApplicationContext(),"GPS is not enabled. Please enable it.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String url = getUrl(String.valueOf(latLngStart.latitude),String.valueOf(latLngStart.longitude),"post_office");
                    Object dataTransfer[] = new Object[1];
                    //  dataTransfer[0] = mMap;
                    dataTransfer[0] = url;

                    GetNearByPlacesData getNearByPlacesData  = new GetNearByPlacesData(getApplicationContext());
                    getNearByPlacesData.execute(dataTransfer);
                    Toast.makeText(getApplicationContext(),"Showing nearby Post office",Toast.LENGTH_LONG).show();

                }
                break;


            case R.id.toilet:
                if(latLngStart == null)
                {
                    Toast.makeText(getApplicationContext(),"GPS is not enabled. Please enable it.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String url = getUrlSpecial(String.valueOf(latLngStart.latitude),String.valueOf(latLngStart.longitude),"toilet");
                    Object dataTransfer[] = new Object[1];
                    //  dataTransfer[0] = mMap;
                    dataTransfer[0] = url;

                    GetNearByPlacesData getNearByPlacesData  = new GetNearByPlacesData(getApplicationContext());
                    getNearByPlacesData.execute(dataTransfer);
                    Toast.makeText(getApplicationContext(),"Showing nearby toilets",Toast.LENGTH_LONG).show();

                }

                break;
            case R.id.servicestation:
                if(latLngStart == null)
                {
                    Toast.makeText(getApplicationContext(),"GPS is not enabled. Please enable it.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String url = getUrl(String.valueOf(latLngStart.latitude),String.valueOf(latLngStart.longitude),"service_station");
                    Object dataTransfer[] = new Object[1];
                    //  dataTransfer[0] = mMap;
                    dataTransfer[0] = url;

                    GetNearByPlacesData getNearByPlacesData  = new GetNearByPlacesData(getApplicationContext());
                    getNearByPlacesData.execute(dataTransfer);
                    Toast.makeText(getApplicationContext(),"Showing nearby service stations",Toast.LENGTH_LONG).show();

                }
                break;
            case R.id.metrostation:
                if(latLngStart == null)
                {
                    Toast.makeText(getApplicationContext(),"GPS is not enabled. Please enable it.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String url = getUrlSpecial(String.valueOf(latLngStart.latitude),String.valueOf(latLngStart.longitude),"metro_stations");
                    Object dataTransfer[] = new Object[1];
                    //  dataTransfer[0] = mMap;
                    dataTransfer[0] = url;

                    GetNearByPlacesData getNearByPlacesData  = new GetNearByPlacesData(getApplicationContext());
                    getNearByPlacesData.execute(dataTransfer);
                    Toast.makeText(getApplicationContext(),"Showing nearby metro stations",Toast.LENGTH_LONG).show();

                }
                break;
            case R.id.shoppingmall:
                if(latLngStart == null)
                {
                    Toast.makeText(getApplicationContext(),"GPS is not enabled. Please enable it.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String url = getUrl(String.valueOf(latLngStart.latitude),String.valueOf(latLngStart.longitude),"shopping_mall");
                    Object dataTransfer[] = new Object[1];
                    //  dataTransfer[0] = mMap;
                    dataTransfer[0] = url;

                    GetNearByPlacesData getNearByPlacesData  = new GetNearByPlacesData(getApplicationContext());
                    getNearByPlacesData.execute(dataTransfer);
                    Toast.makeText(getApplicationContext(),"Showing nearby shopping malls",Toast.LENGTH_LONG).show();

                }
                break;
            case R.id.zoo:
                if(latLngStart == null)
                {
                    Toast.makeText(getApplicationContext(),"GPS is not enabled. Please enable it.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String url = getUrlSpecial(String.valueOf(latLngStart.latitude),String.valueOf(latLngStart.longitude),"zoo");
                    Object dataTransfer[] = new Object[1];
                    //  dataTransfer[0] = mMap;
                    dataTransfer[0] = url;

                    GetNearByPlacesData getNearByPlacesData  = new GetNearByPlacesData(getApplicationContext());
                    getNearByPlacesData.execute(dataTransfer);
                    Toast.makeText(getApplicationContext(),"Showing nearby zoo",Toast.LENGTH_LONG).show();

                }
                break;
            case R.id.shoesstore:
                if(latLngStart == null)
                {
                    Toast.makeText(getApplicationContext(),"GPS is not enabled. Please enable it.",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String url = getUrl(String.valueOf(latLngStart.latitude),String.valueOf(latLngStart.longitude),"shoe_stores");
                    Object dataTransfer[] = new Object[1];
                    //  dataTransfer[0] = mMap;
                    dataTransfer[0] = url;

                    GetNearByPlacesData getNearByPlacesData  = new GetNearByPlacesData(getApplicationContext());
                    getNearByPlacesData.execute(dataTransfer);
                    Toast.makeText(getApplicationContext(),"Showing nearby shoe stores",Toast.LENGTH_LONG).show();

                }
                break;




        }

    }


    private String getUrl(String lat1, String long1,String nearbyPlace)
    {
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+lat1+","+long1);
        googlePlaceUrl.append("&radius="+850);
        googlePlaceUrl.append("&keyword="+nearbyPlace);
        googlePlaceUrl.append("&key="+"AIzaSyAV8rECz0UCeVbOCe7BvS-A8-TpVQT3sho");
        Log.d("CHECK",googlePlaceUrl.toString());
        return googlePlaceUrl.toString();

    }



    private String getUrlSpecial(String lat1, String long1,String nearbyPlace)
    {
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+lat1+","+long1);
        googlePlaceUrl.append("&radius="+1200);
        googlePlaceUrl.append("&keyword="+nearbyPlace);
        googlePlaceUrl.append("&key="+"");
        Log.d("CHECK",googlePlaceUrl.toString());
        return googlePlaceUrl.toString();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }



}
