package com.example.careandshare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.maps.GeoApiContext;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class vehicle_owner_firstpage extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
    com.google.android.gms.location.LocationListener, TaskLoadedCallback{

    private GoogleMap mMap;
    GoogleApiClient googleApiClient;
    Location LastLocation;
    LocationRequest locationRequest;

    private Button LogoutDriverBtn;
    private Button SettingsDriverButton;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Boolean currentLogOutUserStatus = false;

    private GeoApiContext mGeoApiContext = null;



    //getting request customer's id
    private String customerID = "";
    private String driverID;
    private DatabaseReference AssignedCustomerRef;
    private DatabaseReference AssignedCustomerPickUpRef;
    Marker PickUpMarker,destinationmarker;
    private Point originposition,destinationposition;

    private ValueEventListener AssignedCustomerPickUpRefListner;

    private TextView txtName, txtPhone;
    private CircleImageView profilePic;
    private RelativeLayout relativeLayout;

    private Button btngetdirection;
     MarkerOptions place1, place2;
     Polyline currentPolyline;



    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //notice
        setContentView(R.layout.activity_vehicle_owner_firstpage);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel  = new NotificationChannel("MyNotifications","MyNotifications",
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager  = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);


        }

        FirebaseMessaging.getInstance().subscribeToTopic("Drivers")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg ="successful";
                        if (!task.isSuccessful()){
                            msg = "failed";
                        }
                        Toast.makeText(vehicle_owner_firstpage.this, msg, Toast.LENGTH_SHORT).show();
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

                      //  Intent intent = new Intent(vehicle_owner_firstpage.this, MainActivity.class);
                        //startActivity(intent);
                        break;


                    case  R.id.settings_drawer:

                        Intent gotosettings = new Intent(vehicle_owner_firstpage.this,settingsActivity.class);
                        gotosettings.putExtra("type","Drivers");
                        startActivity(gotosettings);
                        break;

                    case  R.id.nav_mybookings:

                        Intent gotomybookings = new Intent(vehicle_owner_firstpage.this,tabclasstesting.class);
                        gotomybookings.putExtra("type","Drivers");
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
                        Intent gotowallet = new Intent(vehicle_owner_firstpage.this,Wallet.class);
                        gotowallet.putExtra("type","Driver");
                        startActivity(gotowallet);




                    }



                }
                return false;
            }
        });




















        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        driverID = mAuth.getCurrentUser().getUid();


        LogoutDriverBtn = (Button) findViewById(R.id.Logout);
       // SettingsDriverButton = (Button) findViewById(R.id.settings_driver_btn);

        txtName = findViewById(R.id.name_customer);
        txtPhone = findViewById(R.id.phone_customer);
        profilePic = findViewById(R.id.profile_image_customer);
        relativeLayout = findViewById(R.id.rell2);
/*
        place1 = new MarkerOptions().position(new LatLng(27.658143,85.3199503)).title("Sourcee");
        place2 = new MarkerOptions().position(new LatLng(27.667491,85.3208583)).title("Destination");

        String url = getUrl(place1.getPosition(),place2.getPosition(),"driving");

        new FetchURL(vehicle_owner_firstpage.this).execute(url,"driving");
*/




        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(vehicle_owner_firstpage.this);








        LogoutDriverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                currentLogOutUserStatus = true;
                DisconnectDriver();

                mAuth.signOut();

                LogOutUser();
            }
        });


        getAssignedCustomersRequest();
    }

    private void setUpToolbar() {

        drawerLayout = findViewById(R.id.drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //  setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }


    private void getAssignedCustomersRequest()
    {
        AssignedCustomerRef = FirebaseDatabase.getInstance().getReference().child("Users")
                .child("Drivers").child(driverID).child("CustomerRideID");

        AssignedCustomerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    customerID = dataSnapshot.getValue().toString();
                    //getting assigned customer location
                    GetAssignedCustomerPickupLocation();
                    relativeLayout.setVisibility(View.VISIBLE);
                    getAssignedCustomerInformation();


                }
                else
                {
                    customerID = "";

                    if (PickUpMarker != null)
                    {
                        PickUpMarker.remove();
                    }

                    if (AssignedCustomerPickUpRefListner != null)
                    {
                        AssignedCustomerPickUpRef.removeEventListener(AssignedCustomerPickUpRefListner);
                    }

                    relativeLayout.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }





    private void GetAssignedCustomerPickupLocation()
    {
        AssignedCustomerPickUpRef = FirebaseDatabase.getInstance().getReference().child("Customer Requests")
                .child(customerID).child("l");

        AssignedCustomerPickUpRefListner = AssignedCustomerPickUpRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    List<Object> customerLocationMap = (List<Object>) dataSnapshot.getValue();
                    double LocationLat = 0;
                    double LocationLng = 0;

                    if(customerLocationMap.get(0) != null)
                    {
                        LocationLat = Double.parseDouble(customerLocationMap.get(0).toString());
                    }
                    if(customerLocationMap.get(1) != null)
                    {
                        LocationLng = Double.parseDouble(customerLocationMap.get(1).toString());
                    }

                    LatLng DriverLatLng = new LatLng(LocationLat, LocationLng);
                    PickUpMarker = mMap.addMarker(new MarkerOptions().position(DriverLatLng).title("Customer PickUp Location"));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



        // now let set user location enable
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);
    }

  /*  @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState)
    {
        super.onCreate(savedInstanceState, persistentState);


    }*/

    @Override
    public void onLocationChanged(Location location)
    {
        if(getApplicationContext() != null)
        {
            //getting the updated location
            LastLocation = location;

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(12));


            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            DatabaseReference DriversAvailabilityRef = FirebaseDatabase.getInstance().getReference().child("Drivers Available");
            GeoFire geoFireAvailability = new GeoFire(DriversAvailabilityRef);

            DatabaseReference DriversWorkingRef = FirebaseDatabase.getInstance().getReference().child("Drivers Working");
            GeoFire geoFireWorking = new GeoFire(DriversWorkingRef);

            switch (customerID)
            {
                case "":
                    geoFireWorking.removeLocation(userID);
                    geoFireAvailability.setLocation(userID, new GeoLocation(location.getLatitude(), location.getLongitude()));
                    break;

                default:
                    geoFireAvailability.removeLocation(userID);
                    geoFireWorking.setLocation(userID, new GeoLocation(location.getLatitude(), location.getLongitude()));
                    break;
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
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

         if(!currentLogOutUserStatus)
        {
            DisconnectDriver();
        }
    }


    private void DisconnectDriver()
    {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference DriversAvailabiltyRef = FirebaseDatabase.getInstance().getReference().child("Drivers Available");

        GeoFire geoFire = new GeoFire(DriversAvailabiltyRef);
        geoFire.removeLocation(userID);

    }



    public void LogOutUser()
    {
        Intent startPageIntent = new Intent(vehicle_owner_firstpage.this,Login.class);
       // startPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //startActivity(startPageIntent);
        finish();
    }




    private void getAssignedCustomerInformation()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child("Customers").child(customerID);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists()  &&  dataSnapshot.getChildrenCount() > 0)
                {
                    String name = dataSnapshot.child("Name").getValue().toString();
                    String phone = dataSnapshot.child("Phone").getValue().toString();
                    txtName.setText(name);
                    txtPhone.setText(phone);




                    if (dataSnapshot.hasChild("Image")){
                        String Image = dataSnapshot.child("Image").getValue().toString();
                        Picasso.get().load(Image).into(profilePic);

                    }

                    }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }





private  String getUrl(LatLng origin, LatLng destination, String directionmode){


    String str_origin = "origin="+ origin.latitude + "," + origin.longitude;
    String str_dest = "origin=" + destination.latitude + "," + destination.longitude;

    String mode = "mode"+ directionmode;

    String parameters  = str_origin + "&" + str_dest + "&" + mode;

    String output = "json";

    String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);



return url;
}

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline!= null )
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }





































































/*
    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    Location mlastLocation;
    GoogleApiClient mGoogleApiClient;
    Marker mCurrLocationMarker;
    LocationRequest mlocationRequest;
    MarkerOptions place1,place2;
    FirebaseAuth mAuth;
    FirebaseUser CurrentUser;
    Marker pickupmarker;
    FirebaseFirestore ab;
    Polyline currentPolyline;;
    private Button logout;
    private boolean currentlogout=false;
    private DatabaseReference Assignedcustomerref,AssignedCustomerPickupRef;
    private String DriverID,customerID="";
    private ValueEventListener AssignedCustomerPickupReflistner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_owner_firstpage);
        mAuth=FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser();
        logout = findViewById(R.id.Logout);;
DriverID = mAuth.getCurrentUser().getUid();

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

logout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        currentlogout = true;
     Disconnectdriver();
      mAuth.signOut();
        Intent login = new Intent(vehicle_owner_firstpage.this,Login.class);
       // login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(login);


    }
});


GetAssignedCustomerRequest();








        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(vehicle_owner_firstpage.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                    , LOCATION_REQUEST_CODE);
        } else {
            mapFragment.getMapAsync(this);

        }





place1= new MarkerOptions().position(new LatLng(27.658143,85.3199503)).title("location 1");
        place2= new MarkerOptions().position(new LatLng(27.667491,85.3208583)).title("location 2");

        String url= getUrl(place1.getPosition(),place2.getPosition(),"Driving");


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
/*

    }

    private void GetAssignedCustomerRequest() {

       Assignedcustomerref= FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(DriverID).child("CustomerRideID");
          Assignedcustomerref.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  if (dataSnapshot.exists()){

                 customerID=dataSnapshot.getValue().toString();
                 getAssignedCustomerPickupLocation();
                  }
                  else {

                      customerID="";
                      if (pickupmarker!= null){

                          pickupmarker.remove();
                      }

                      if (AssignedCustomerPickupReflistner != null){

                          AssignedCustomerPickupRef.removeEventListener(AssignedCustomerPickupReflistner);

                      }



                  }


              }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {

              }
          });

    }

    private void getAssignedCustomerPickupLocation() {
        AssignedCustomerPickupRef = FirebaseDatabase.getInstance().getReference()
        .child("Customer Requests:").child(customerID).child("l");

AssignedCustomerPickupReflistner = AssignedCustomerPickupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    List<Object> customerlocationmap = (List<Object>)  dataSnapshot.getValue();

                    double locationlat=0,locationlng=0;


                    if (customerlocationmap.get(0)!=null){

                        locationlat= Double.parseDouble(customerlocationmap.get(0).toString());
                    }

                    if (customerlocationmap.get(1)!=null){

                        locationlng = Double.parseDouble(customerlocationmap.get(1).toString());
                    }

                    LatLng DriverLatlng = new LatLng(locationlat,locationlng);
                pickupmarker= mMap.addMarker(new MarkerOptions().position(DriverLatlng).title("Pickup Location"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
       /* LatLng Ghauri = new LatLng(33.623862, 73.126591);
        mMap.addMarker(new MarkerOptions().position(Ghauri).title("Ghauri Town"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Ghauri, 100F));*//*

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);


    }

    @Override
    public void onLocationChanged(Location location) {

        if(getApplicationContext() != null){

      String msg= "updated location"+ Double.toString(location.getLatitude())+","+
                                        Double.toString(location.getLongitude());

/*
Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
        mlastLocation = location;*/
    /*


        LatLng Latlng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Latlng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));

        String userID =  FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference driveravailabilityref = FirebaseDatabase.getInstance().getReference().child("Drivers Available");
        GeoFire geoFireAvailability = new GeoFire(driveravailabilityref);

            DatabaseReference driverworkingref = FirebaseDatabase.getInstance().getReference().child("Drivers Working");
            GeoFire geoFireworking = new GeoFire(driverworkingref);
//busy driver or not;
        switch (customerID)
        {
            case "":
                geoFireworking.removeLocation(userID);

                geoFireAvailability.setLocation(userID, new GeoLocation(location.getLatitude(), location.getLongitude()));
                break;
            default:
                geoFireAvailability.removeLocation(userID);
                geoFireworking.setLocation(userID, new GeoLocation(location.getLatitude(), location.getLongitude()));
                break;



        }



        }








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
    protected void onStop() {
        super.onStop();

        if(!currentlogout){
            Disconnectdriver();
        }






     /*   LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("driversAvailable");

        GeoFire geoFire = new GeoFire(ref);
        geoFire.removeLocation(userId);




*/
  /*  }

    private void Disconnectdriver() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Drivers Available");
        GeoFire geoFire = new GeoFire(ref);
        geoFire.removeLocation(userID);

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mlocationRequest = new LocationRequest();
      mlocationRequest.setInterval(1000);
      mlocationRequest.setFastestInterval(1000);
        mlocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(vehicle_owner_firstpage.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
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



   private String getUrl(LatLng origin,LatLng dest, String directionMode){
String str_origin = "origin="+origin.latitude+","+origin.longitude;

String str_dest = "Destination="+dest.latitude+","+dest.longitude;

String mode = "mode="+directionMode;

String parameters = str_origin+"&"+str_dest+"&"+mode;

String output ="json";

String url= "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters+"&key="+getString(R.string.google_maps_key);

return url;



   }




*/
}