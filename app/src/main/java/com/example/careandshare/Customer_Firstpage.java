package com.example.careandshare;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;

public class Customer_Firstpage extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button Roombtn,Ridebtn,findRoombtn,findRidebtn;

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
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
   /*    Toolbar toolbar =(Toolbar)  findViewById(R.id.toolBar);
     //setActionBar(toolbar);
         nav = (NavigationView)findViewById(R.id.nav_menu);
          drawerLayout = (DrawerLayout)findViewById(R.id.drawer);

          toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);

               drawerLayout.addDrawerListener(toggle);

           toggle.syncState();

           nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
               @Override
               public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                   switch (menuItem.getItemId()){
                       case R.id.menu_home:
                           Toast.makeText(getApplicationContext(),"home panel",Toast.LENGTH_LONG).show();
                           drawerLayout.closeDrawer(GravityCompat.START);

                           break;


                       case R.id.menu_call:
                           Toast.makeText(getApplicationContext(),"call panel",Toast.LENGTH_LONG).show();
                           drawerLayout.closeDrawer(GravityCompat.START);

                           break;



                       case R.id.menu_settings:
                           Toast.makeText(getApplicationContext(),"setting panel",Toast.LENGTH_LONG).show();
                           drawerLayout.closeDrawer(GravityCompat.START);

                           break;



                   }
                   return true;
               }
           });




*/





        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        Ridebtn= findViewById(R.id.ridebtn);
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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
         LatLng Ghauri = new LatLng(33.623862, 73.126591);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Ghauri,10F));
       }
       }