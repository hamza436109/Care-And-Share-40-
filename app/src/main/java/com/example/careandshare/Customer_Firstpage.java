package com.example.careandshare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;

public class Customer_Firstpage extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {


    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    GoogleApiClient mGoogleApiClient;
    Location mlastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mlocationRequest;
    Button Roombtn, Ridebtn, findRoombtn, findRidebtn;

    com.mancj.materialsearchbar.MaterialSearchBar searchroombar;
    com.mancj.materialsearchbar.MaterialSearchBar searchridebar;

    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

         mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Customer_Firstpage.this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION}
                    ,LOCATION_REQUEST_CODE);
        }else
        {
            mapFragment.getMapAsync(this);

        }






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
        });


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
LatLng Latlng = new LatLng(location.getLatitude(),location.getLongitude());
mMap.moveCamera(CameraUpdateFactory.newLatLng(Latlng));
mMap.animateCamera(CameraUpdateFactory.zoomTo(11));



    }
protected synchronized   void buildGoogleApiClient(){
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
           ActivityCompat.requestPermissions(Customer_Firstpage.this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION}
           ,LOCATION_REQUEST_CODE);
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mlocationRequest, this);

    }
final int LOCATION_REQUEST_CODE=1;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case LOCATION_REQUEST_CODE:{
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    mapFragment.getMapAsync(this);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please provide the permission",Toast.LENGTH_LONG).show();
                }
                break;
            }

        }

    }

    @Override
    public void onConnectionSuspended(int i) {
Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_LONG).show();
    }


}






