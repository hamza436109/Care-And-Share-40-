package com.example.careandshare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class Room_owner_firstpage extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener  {
    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    Location mlastLocation;
    GoogleApiClient mGoogleApiClient;
    Marker mCurrLocationMarker;
    LocationRequest mlocationRequest;



    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_owner_firstpage);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel  = new NotificationChannel("MyNotifications","MyNotifications",
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager  = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);


        }

        FirebaseMessaging.getInstance().subscribeToTopic("Customers")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg ="successful";
                        if (!task.isSuccessful()){
                            msg = "failed";
                        }
                        Toast.makeText(Room_owner_firstpage.this, msg, Toast.LENGTH_SHORT).show();
                    }



                });





        setUpToolbar();
        navigationView = (NavigationView) findViewById(R.id.navigation_menu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case  R.id.nav_profile:

                        Intent intent = new Intent(Room_owner_firstpage.this, MainActivity.class);
                        startActivity(intent);
                        break;


                    case  R.id.settings_drawer:

                        Intent gotosettings = new Intent(Room_owner_firstpage.this,settingsActivity.class);
                        gotosettings.putExtra("type","Room Owners");
                        startActivity(gotosettings);
                        break;

                    case  R.id.nav_myrooms:

                        Intent gotomyrooms = new Intent(Room_owner_firstpage.this,MyRooms.class);
                        gotomyrooms.putExtra("type","Room Owners");
                        startActivity(gotomyrooms);
                        break;

                    case  R.id.nav_mybookings:

                        Intent gotomybookings = new Intent(Room_owner_firstpage.this,tabclasstesting.class);
                        gotomybookings.putExtra("type","Room Owners");
                        startActivity(gotomybookings);
                        break;



                    case  R.id.nav_share:
                    {

                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String shareBody =  "http://play.google.com/store/apps/detail?id=" + getPackageName();
                        String shareSub = "Try now";
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(sharingIntent, "Share using"));

                    }
                    break;
                    case R.id.nav_wallet:{
                        Intent gotowallet = new Intent(Room_owner_firstpage.this,Wallet.class);
                        gotowallet.putExtra("type","Room Owners");
                        startActivity(gotowallet);



                    }


                }
                return false;
            }
        });









        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Room_owner_firstpage.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                    , LOCATION_REQUEST_CODE);
        } else {
            mapFragment.getMapAsync(this);

        }





/*
        Ridebtn = findViewById(R.id.ridebtn);
        Roombtn = findViewById(R.id.roombtn);
        searchroombar = findViewById(R.id.searchBar);
        searchridebar = findViewById(R.id.searchrideBar);
        findRoombtn = findViewById(R.id.findroombtn);
        findRidebtn = findViewById(R.id.findridebtn);


        Ridebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchroombar.setVisibility(view.GONE);
                searchridebar.setVisibility(view.VISIBLE);
                findRoombtn.setVisibility(view.GONE);
                findRidebtn.setVisibility(view.VISIBLE);

            }
        });


        Roombtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchridebar.setVisibility(view.GONE);
                searchroombar.setVisibility(view.VISIBLE);
                findRidebtn.setVisibility(view.GONE);
                findRoombtn.setVisibility(view.VISIBLE);

            }
        });*/


    }

    private void setUpToolbar() {

        drawerLayout = findViewById(R.id.drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //  setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
       /* LatLng Ghauri = new LatLng(33.623862, 73.126591);
        mMap.addMarker(new MarkerOptions().position(Ghauri).title("Ghauri Town"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Ghauri, 100F));*/

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);


    }

    @Override
    public void onLocationChanged(Location location) {
        mlastLocation = location;
        LatLng Latlng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Latlng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
/*
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("DriversAvailable");
        GeoFire geoFire = new GeoFire(ref);
        geoFire.setLocation(userID, new GeoLocation(location.getLatitude(), location.getLongitude()));*/


    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mlocationRequest = new LocationRequest();
        // mlocationRequest.setInterval(1000);
        // mlocationRequest.setFastestInterval(5000);
        mlocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Room_owner_firstpage.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                    , LOCATION_REQUEST_CODE);
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mlocationRequest, this);

    }

    final int LOCATION_REQUEST_CODE = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mapFragment.getMapAsync(this);
                } else {
                    Toast.makeText(getApplicationContext(), "Please provide the permission", Toast.LENGTH_LONG).show();
                }
                break;
            }

        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();

     /*   LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("driversAvailable");

        GeoFire geoFire = new GeoFire(ref);
        geoFire.removeLocation(userId);



   String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Drivers Available");
        GeoFire geoFire = new GeoFire(ref);
        geoFire.removeLocation(userID);
*/
    }


    }
