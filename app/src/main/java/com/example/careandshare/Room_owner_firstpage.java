package com.example.careandshare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

    DatabaseReference Userinfo;
    DatabaseReference dbref;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    TextView Username,phonenumber,email;
    FirebaseAuth mAuth;
    TextView okay;
    Button addroombtn;;
    Dialog dialog;




    @SuppressLint("ResourceType")
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
                      //  Toast.makeText(Room_owner_firstpage.this, msg, Toast.LENGTH_SHORT).show();
                    }



                });





        setUpToolbar();
        navigationView = (NavigationView) findViewById(R.id.navigation_menu);

        Menu menu =navigationView.getMenu();
        MenuItem target = menu.findItem(R.id.nav_Policy);
        MenuItem myrides = menu.findItem(R.id.my_rides);
        myrides.setVisible(false);
        target.setTitle("Feedback");
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case  R.id.nav_Policy:

                        Intent intent = new Intent(Room_owner_firstpage.this, roominvoice.class);
                        intent.putExtra("type","Room Owners");
                        startActivity(intent);
                        break;


                    case  R.id.settings_drawer:

                        Intent gotosettings = new Intent(Room_owner_firstpage.this,settingsActivity.class);
                        gotosettings.putExtra("type","Room Owners");
                        startActivity(gotosettings);
                        break;

                    case  R.id.nav_myrooms:


                        FirebaseDatabase.getInstance().getReference().child("Available Rooms").child(mAuth.getCurrentUser().getUid())
                                .child("Booked By").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getChildrenCount()>0){
                                    Toast.makeText(Room_owner_firstpage.this, "You cannot edit now. Room is already booked!!", Toast.LENGTH_SHORT).show();
                                }
                                  else {
                                    Intent gotomyrooms = new Intent(Room_owner_firstpage.this, MyRooms.class);
                                    gotomyrooms.putExtra("type", "Room Owners");
                                    startActivity(gotomyrooms);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });





                        break;


                    case  R.id.nav_mybookings:

                        Intent gotomybookings = new Intent(Room_owner_firstpage.this,Scheduledroombooking.class);
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
        mAuth = FirebaseAuth.getInstance();


        Userinfo = FirebaseDatabase.getInstance().getReference().child("Users").child("Room Owners");
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_menu);
        View headerView = navigationView.getHeaderView(0);
        Username = (TextView) headerView.findViewById(R.id.headernameasas);
        phonenumber= (TextView) headerView.findViewById(R.id.headerphone);
        email= (TextView) headerView.findViewById(R.id.headeremail);
        addroombtn=findViewById(R.id.Addroom);

        Username.setText("Hello hello testing");
        dbref=FirebaseDatabase.getInstance().getReference().child("Available Rooms");


        dialog=new Dialog(Room_owner_firstpage.this);
        dialog.setContentView(R.layout.custom_dialog);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)

            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false);
            okay=dialog.findViewById(R.id.okay);



            okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            addroombtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase.getInstance().getReference().child("Available Rooms").child(mAuth.getCurrentUser().getUid())
                            .child("Booked By").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getChildrenCount()>0){
                                Toast.makeText(Room_owner_firstpage.this, "You cannot edit now. Room is already booked!!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Intent gotomyrooms = new Intent(Room_owner_firstpage.this, MyRooms.class);
                                gotomyrooms.putExtra("type", "Room Owners");
                                startActivity(gotomyrooms);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
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


       /* okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });*/




        Roombookedbycustomer();
        setinfo();



    }

    private void Roombookedbycustomer() {



        dbref.child(mAuth.getCurrentUser().getUid()).child("Booked By").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){


                    dialog.show();
                    Toast.makeText(Room_owner_firstpage.this, "ROOM BOOKED", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(Room_owner_firstpage.this, "ROOM CANCELLED" , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setinfo() {
        {



           Userinfo.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){

                        String name1 = dataSnapshot.child("Name").getValue().toString();
                        String phone1 = dataSnapshot.child("Phone").getValue().toString();
                        String emaill=dataSnapshot.child("Email").getValue().toString();
                        Username.setText(name1);
                        email.setText(emaill);
                        phonenumber.setText(phone1);





                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });;





        }



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

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
