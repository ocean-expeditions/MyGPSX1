package com.haroonfazal.myapps.gpsnavigation;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import com.karan.churi.PermissionManager.PermissionManager;

import java.util.ArrayList;



public class MyNavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {

    CardView cardViewMyLoc,cardViewRoute,cardViewstreet,cardViewNearby,cardviewAbout;
    PermissionManager permissionManager;
    InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            finish();
            Intent myIntent = new Intent(MyNavigationActivity.this,LocationOffActivity.class);
            startActivity(myIntent);
        }
        else
        {
            setContentView(R.layout.activity_my_navigation);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            interstitialAd = new InterstitialAd(this);
                    interstitialAd.setAdUnitId(getResources().getString(R.string.interstional_full_screen));
            interstitialAd.loadAd(new AdRequest.Builder().build());

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);


            cardviewAbout = (CardView)findViewById(R.id.aboutDevId);
            cardViewMyLoc = (CardView)findViewById(R.id.myLocationId);
            cardViewRoute = (CardView)findViewById(R.id.routeFinderId);
            cardViewstreet = (CardView)findViewById(R.id.streetViewId);
            cardViewNearby = (CardView)findViewById(R.id.nearbyId);




            cardviewAbout.setOnClickListener(this);
            cardViewMyLoc.setOnClickListener(this);
            cardViewRoute.setOnClickListener(this);
            cardViewstreet.setOnClickListener(this);
            cardViewNearby.setOnClickListener(this);

            permissionManager = new PermissionManager() {};
            permissionManager.checkAndRequestPermissions(this);

        }






    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionManager.checkResult(requestCode,permissions,grantResults);

        ArrayList<String> denied_perms = permissionManager.getStatus().get(0).denied;

        if(denied_perms.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Location permission granted.",Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.aboutDevId:
                if(interstitialAd.isLoaded())
                {
                    interstitialAd.show();
                    interstitialAd.setAdListener(new AdListener()
                    {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            interstitialAd.loadAd(new AdRequest.Builder().build());
                            Intent myIntentAbout = new Intent(MyNavigationActivity.this,AboutActivity.class);
                            startActivity(myIntentAbout);
                        }
                    });

                }
                else
                {
                    interstitialAd.loadAd(new AdRequest.Builder().build());
                    Intent myIntentAbout = new Intent(MyNavigationActivity.this,AboutActivity.class);
                    startActivity(myIntentAbout);
                }

                break;
            case R.id.myLocationId:
                if(interstitialAd.isLoaded())
                {
                    interstitialAd.show();
                    interstitialAd.setAdListener(new AdListener()
                    {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            Intent myIntentLocation = new Intent(MyNavigationActivity.this,MyLocationActivity.class);
                            startActivity(myIntentLocation);
                            interstitialAd.loadAd(new AdRequest.Builder().build());
                        }
                    });

                }
                else
                {
                    interstitialAd.loadAd(new AdRequest.Builder().build());
                    Intent myIntentLocation = new Intent(MyNavigationActivity.this,MyLocationActivity.class);
                    startActivity(myIntentLocation);
                }

                break;
            case R.id.routeFinderId:
                if(interstitialAd.isLoaded())
                {
                    interstitialAd.show();
                    interstitialAd.setAdListener(new AdListener()
                    {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            interstitialAd.loadAd(new AdRequest.Builder().build());
                            Intent myIntentRoute = new Intent(MyNavigationActivity.this,RouteFinderActivity.class);
                            startActivity(myIntentRoute);

                        }
                    });

                }
                else
                {
                    interstitialAd.loadAd(new AdRequest.Builder().build());
                    Intent myIntentRoute = new Intent(MyNavigationActivity.this,RouteFinderActivity.class);
                    startActivity(myIntentRoute);

                }

                break;
            case R.id.streetViewId:

                Intent myIntentStreet = new Intent(MyNavigationActivity.this,StreetViewActivity.class);
                startActivity(myIntentStreet);
                break;
            case R.id.nearbyId:
                if(interstitialAd.isLoaded())
                {
                    interstitialAd.show();
                    interstitialAd.setAdListener(new AdListener()
                    {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            interstitialAd.loadAd(new AdRequest.Builder().build());
                            Intent myIntentNearby = new Intent(MyNavigationActivity.this,NearbyLocationActivity.class);
                            startActivity(myIntentNearby);


                        }
                    });
                }
                else
                {
                    interstitialAd.loadAd(new AdRequest.Builder().build());
                    Intent myIntentNearby = new Intent(MyNavigationActivity.this,NearbyLocationActivity.class);
                    startActivity(myIntentNearby);
                }
                break;

        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if(interstitialAd.isLoaded())
            {
                interstitialAd.show();
                interstitialAd.setAdListener(new AdListener()
                {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        Intent i = new Intent(getApplicationContext(),AboutActivity.class);
                        startActivity(i);
                        interstitialAd.loadAd(new AdRequest.Builder().build());
                    }
                });

            }
            else
            {
                Intent i = new Intent(getApplicationContext(),AboutActivity.class);
                startActivity(i);
                interstitialAd.loadAd(new AdRequest.Builder().build());
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_myLocation) {
            // Handle the camera action

            if(interstitialAd.isLoaded())
            {
                interstitialAd.show();
                interstitialAd.setAdListener(new AdListener()
                {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        Intent myIntentAbout = new Intent(MyNavigationActivity.this,MyLocationActivity.class);
                        startActivity(myIntentAbout);
                        interstitialAd.loadAd(new AdRequest.Builder().build());
                    }
                });
            }
            else
            {
                interstitialAd.loadAd(new AdRequest.Builder().build());
                Intent myIntentAbout = new Intent(MyNavigationActivity.this,MyLocationActivity.class);
                startActivity(myIntentAbout);
            }



        } else if (id == R.id.nav_nearby) {
            if(interstitialAd.isLoaded())
            {
                interstitialAd.show();
                interstitialAd.setAdListener(new AdListener()
                {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        interstitialAd.loadAd(new AdRequest.Builder().build());
                        Intent myIntentAbout = new Intent(MyNavigationActivity.this,NearbyLocationActivity.class);
                        startActivity(myIntentAbout);


                    }
                });
            }
            else
            {
                interstitialAd.loadAd(new AdRequest.Builder().build());
                Intent myIntentAbout = new Intent(MyNavigationActivity.this,NearbyLocationActivity.class);
                startActivity(myIntentAbout);
            }
        } else if (id == R.id.nav_routeFinder)
        {
            if(interstitialAd.isLoaded())
            {
                interstitialAd.show();
                interstitialAd.setAdListener(new AdListener()
                {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        Intent myIntentAbout = new Intent(MyNavigationActivity.this,RouteFinderActivity.class);
                        startActivity(myIntentAbout);
                        interstitialAd.loadAd(new AdRequest.Builder().build());

                    }
                });

            }
            else{
                interstitialAd.loadAd(new AdRequest.Builder().build());
                Intent myIntentAbout = new Intent(MyNavigationActivity.this,RouteFinderActivity.class);
                startActivity(myIntentAbout);
            }




        } else if (id == R.id.nav_streetview)
        {

            Intent myIntentAbout = new Intent(MyNavigationActivity.this,StreetViewActivity.class);
            startActivity(myIntentAbout);


        } else if (id == R.id.nav_about) {
            if(interstitialAd.isLoaded())
            {
                interstitialAd.show();
                interstitialAd.setAdListener(new AdListener()
                {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        interstitialAd.loadAd(new AdRequest.Builder().build());
                        Intent myIntentAbout = new Intent(MyNavigationActivity.this,AboutActivity.class);
                        startActivity(myIntentAbout);
                    }
                });

            }
            else {
                interstitialAd.loadAd(new AdRequest.Builder().build());
                Intent myIntentAbout = new Intent(MyNavigationActivity.this,AboutActivity.class);
                startActivity(myIntentAbout);
            }


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
