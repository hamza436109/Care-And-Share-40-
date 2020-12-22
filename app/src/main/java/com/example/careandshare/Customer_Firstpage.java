package com.example.careandshare;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import  androidx.appcompat.widget.Toolbar;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;



import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class Customer_Firstpage extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    GoogleApiClient googleApiClient;
    Location LastLocation;
    LocationRequest locationRequest;

    private Button Logout;
    private Button SettingsButton;
    private Button CallCabCarButton,Searchroom;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference CustomerDatabaseRef;
    private LatLng CustomerPickUpLocation;

    private DatabaseReference DriverAvailableRef, DriverLocationRef;
    private DatabaseReference DriversRef;
    private int radius = 1;

    private Boolean driverFound = false, requestType = false;
    private String driverFoundID;
    private String customerID;
    Marker DriverMarker, PickUpMarker;
    GeoQuery geoQuery;
    DatabaseReference Userinfo;

    private ValueEventListener DriverLocationRefListner;


    private TextView txtName, txtPhone, txtCarName,headername;
    private CircleImageView profilePic;
    private RelativeLayout relativeLayout;


    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    TextView Username,phonenumber,email;
    int AVAILABLE_ROOMS=0;
    int AVAILABLE_RIDES=0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){


            NotificationChannel channel  = new NotificationChannel("MyNotifications","MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);
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

                        Toast.makeText(Customer_Firstpage.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });


        setUpToolbar();
        navigationView = (NavigationView) findViewById(R.id.navigation_menu);


        Menu menu =navigationView.getMenu();
        MenuItem target = menu.findItem(R.id.nav_Policy);
        MenuItem myrooms = menu.findItem(R.id.nav_myrooms);
        MenuItem myrides = menu.findItem(R.id.my_rides);
        myrooms.setVisible(false);
        myrides.setVisible(false);
        target.setTitle("Feedback");

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case  R.id.nav_Policy:

                        Intent intent = new Intent(Customer_Firstpage.this, roominvoice.class);
                        intent.putExtra("type","Customer");
                        startActivity(intent);
                        break;


                    case  R.id.settings_drawer:

                        Intent gotosettings = new Intent(Customer_Firstpage.this,settingsActivity.class);
                        gotosettings.putExtra("type","Customers");
                        startActivity(gotosettings);
                        break;

                    case  R.id.nav_mybookings:

                        Intent gotomybookings = new Intent(Customer_Firstpage.this,tabclasstesting.class);
                        gotomybookings.putExtra("type","Customers");
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
                        Intent gotowallet = new Intent(Customer_Firstpage.this,Wallet.class);
                        gotowallet.putExtra("type","Customers");
                        startActivity(gotowallet);



                    }
                }

                return false;
            }
        });



        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        customerID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        CustomerDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Customer Requests");
        DriverAvailableRef = FirebaseDatabase.getInstance().getReference().child("Drivers Available");
        DriverLocationRef = FirebaseDatabase.getInstance().getReference().child("Drivers Working");
        Searchroom=findViewById(R.id.roombtn);
        CallCabCarButton =  (Button) findViewById(R.id.findridebtn);
        txtName = findViewById(R.id.name_driver);
        txtPhone = findViewById(R.id.phone_driver);
        txtCarName = findViewById(R.id.car_name_driver);
        profilePic = findViewById(R.id.profile_image_driver);
        relativeLayout = findViewById(R.id.rell);
        Userinfo = FirebaseDatabase.getInstance().getReference().child("Users").child("Customers");



        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_menu);
        View headerView = navigationView.getHeaderView(0);
        Username = (TextView) headerView.findViewById(R.id.headernameasas);
        phonenumber= (TextView) headerView.findViewById(R.id.headerphone);
        email= (TextView) headerView.findViewById(R.id.headeremail);
        Username.setText("Hello hello testing");






        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

      /*  SettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotosettings = new Intent(Customer_Firstpage.this,settingsActivity.class);
                gotosettings.putExtra("type","Customers");
                startActivity(gotosettings);
            }
        });*/

Searchroom.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        PlacePicker.IntentBuilder builder =  new PlacePicker.IntentBuilder();
        try {
            AVAILABLE_ROOMS=1;
            int PLACE_PICKER_REQUEST=AVAILABLE_ROOMS;;
            startActivityForResult(builder.build(Customer_Firstpage.this),
                    PLACE_PICKER_REQUEST);


        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }


        //
    }
});



        CallCabCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                PlacePicker.IntentBuilder builder =  new PlacePicker.IntentBuilder();
                try {
                    AVAILABLE_RIDES=1;
                    int PLACE_PICKER_REQUEST=AVAILABLE_RIDES;;
                    startActivityForResult(builder.build(Customer_Firstpage.this),
                            PLACE_PICKER_REQUEST);


                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }









               // startActivity(new Intent(Customer_Firstpage.this,Availablerides.class));
            }
        });



                /*.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (requestType)
                {

                    requestType = false;
                    geoQuery.removeAllListeners();
                    DriverLocationRef.removeEventListener(DriverLocationRefListner);




                    if (driverFound != null)
                    {
                        DriversRef = FirebaseDatabase.getInstance().getReference()
                                .child("Users").child("Drivers").child(driverFoundID).child("CustomerRideID");

                        DriversRef.removeValue();

                        driverFoundID = null;
                    }



                    driverFound = false;
                    radius = 1;

                    String customerId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    GeoFire geoFire = new GeoFire(CustomerDatabaseRef);
                    geoFire.removeLocation(customerId);

                    if (PickUpMarker != null)
                    {
                        PickUpMarker.remove();
                    }
                    if (DriverMarker != null)
                    {
                        DriverMarker.remove();
                    }

                    CallCabCarButton.setText("Call a Cab");

                    relativeLayout.setVisibility(View.GONE);
                }
                else
                {
                    requestType = true;

                    String customerId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    GeoFire CustomerRequestedlocation = new GeoFire(CustomerDatabaseRef);
                    CustomerRequestedlocation.setLocation(customerId, new GeoLocation(LastLocation.getLatitude(), LastLocation.getLongitude()));

                    CustomerPickUpLocation = new LatLng(LastLocation.getLatitude(), LastLocation.getLongitude());
                    PickUpMarker = mMap.addMarker(new MarkerOptions().position(CustomerPickUpLocation).title("My Location"));


                    CallCabCarButton.setText("Getting your Driver...");
                    getClosetDriverCab();
                }
            }
        });
        setinfo();*/
    }

    private void setUpToolbar() {

        drawerLayout = findViewById(R.id.drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //  setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }

    private void getClosetDriverCab()
    {
        GeoFire geoFire = new GeoFire(DriverAvailableRef);
        geoQuery = geoFire.queryAtLocation(new GeoLocation(CustomerPickUpLocation.latitude, CustomerPickUpLocation.longitude), radius);
        geoQuery.removeAllListeners();

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location)
            {
                //anytime the driver is called this method will be called
                //key=driverID and the location
                if(!driverFound && requestType)
                {
                    driverFound = true;
                    driverFoundID = key;


                    //we tell driver which customer he is going to have

                    DriversRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(driverFoundID);
                    HashMap driversMap = new HashMap();
                    driversMap.put("CustomerRideID", customerID);
                    DriversRef.updateChildren(driversMap);

                    //Show driver location on customerMapActivity
                    GettingDriverLocation();
                    CallCabCarButton.setText("Looking for Driver Location...");
                }
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady()
            {
                if(!driverFound)
                {
                    radius = radius + 1;
                    getClosetDriverCab();
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if(requestCode==AVAILABLE_ROOMS) {
                AVAILABLE_ROOMS=0;

            if (resultCode == RESULT_OK) {

                Place place = PlacePicker.getPlace(data, this);
               // address =getcompleteaddress(place.getLatLng().latitude,place.getLatLng().longitude);
                Toast.makeText(this, "To available Rooms", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Customer_Firstpage.this,availablerooms.class));



            }}
            else
        if(requestCode==AVAILABLE_RIDES) {
            AVAILABLE_RIDES=0;

            if (resultCode == RESULT_OK) {

                Place place = PlacePicker.getPlace(data, this);
                // address =getcompleteaddress(place.getLatLng().latitude,place.getLatLng().longitude);
                Toast.makeText(this, "To available Rides", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Customer_Firstpage.this,Availablerides.class));



            }}
    }

















    //and then we get to the driver location - to tell customer where is the driver
    private void GettingDriverLocation()
    {
        DriverLocationRefListner = DriverLocationRef.child(driverFoundID).child("l")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if(dataSnapshot.exists()  &&  requestType)
                        {
                            List<Object> driverLocationMap = (List<Object>) dataSnapshot.getValue();
                            double LocationLat = 0;
                            double LocationLng = 0;
                            CallCabCarButton.setText("Driver Found");


                            relativeLayout.setVisibility(View.VISIBLE);
                            getAssignedDriverInformation();




                            if(driverLocationMap.get(0) != null)
                            {
                                LocationLat = Double.parseDouble(driverLocationMap.get(0).toString());
                            }
                            if(driverLocationMap.get(1) != null)
                            {
                                LocationLng = Double.parseDouble(driverLocationMap.get(1).toString());
                            }

                            //adding marker - to pointing where driver is - using this lat lng
                            LatLng DriverLatLng = new LatLng(LocationLat, LocationLng);
                            if(DriverMarker != null)
                            {
                                DriverMarker.remove();
                            }


                            Location location1 = new Location("");
                            location1.setLatitude(CustomerPickUpLocation.latitude);
                            location1.setLongitude(CustomerPickUpLocation.longitude);

                            Location location2 = new Location("");
                            location2.setLatitude(DriverLatLng.latitude);
                            location2.setLongitude(DriverLatLng.longitude);

                            float Distance = location1.distanceTo(location2);

                            if (Distance < 90)
                            {
                                CallCabCarButton.setText("Driver's Reached");
                            }
                            else
                            {
                                CallCabCarButton.setText("Driver Found: " + String.valueOf(Distance));
                            }

                            DriverMarker = mMap.addMarker(new MarkerOptions().position(DriverLatLng).title("your driver is here"));
                            //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car))
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }




    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        // now let set user location enable
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            //

            return;
        }
        //it will handle the refreshment of the location
        //if we dont call it we will get location only once
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location)
    {
        //getting the updated location
        LastLocation = location;

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
    }


    //create this method -- for useing apis
    protected synchronized void buildGoogleApiClient()
    {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();
    }


    @Override
    protected void onStop()
    {
        super.onStop();
    }


    public void LogOutUser()
    {
        Intent startPageIntent = new Intent(Customer_Firstpage.this, Login.class);
        startPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(startPageIntent);
        finish();
    }



    private void getAssignedDriverInformation()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child("Drivers").child(driverFoundID);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists()  &&  dataSnapshot.getChildrenCount() > 0)
                {
                    if (dataSnapshot.child("Name").exists()) {
                        String name = dataSnapshot.child("Name").getValue().toString();


                        txtName.setText(name);
                    }
                    if (dataSnapshot.child("Phone").exists()) {
                        String phone = dataSnapshot.child("Phone").getValue().toString();
                        txtPhone.setText(phone);

                    }

                    if (dataSnapshot.child("Car").exists()) {
                       String car = dataSnapshot.child("Car").getValue().toString();
                       txtCarName.setText(car);

                }




                    if (dataSnapshot.hasChild("Image"))
                    {
                        String image = dataSnapshot.child("Image").getValue().toString();
                    Picasso.get().load(image).into(profilePic);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void setinfo(){

        Toast.makeText(this, mAuth.getUid(), Toast.LENGTH_SHORT).show();

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






