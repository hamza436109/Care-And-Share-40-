package com.example.careandshare;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class roomreceipt extends AppCompatActivity implements OnMapReadyCallback {


    FirebaseAuth mAuth;

    RatingBar ratingBar;
    TextView amount,customer;
    EditText addcash;
    static  String roomnumber;
    Button submit;
    static String a,date;
    static String Roomownername,customername,price;
    private Object GoogleMap;
    String checker;
   String longitude,latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roomreceipt);

       checker =getIntent().getStringExtra("type");
        Toast.makeText(this, checker, Toast.LENGTH_SHORT).show();





        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);



        if (checker.equals("Room Owners")) {

            date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());


            ratingBar = findViewById(R.id.rating);
            amount = findViewById(R.id.bill);
            customer = findViewById(R.id.name);
            submit = findViewById(R.id.submit);
            addcash = findViewById(R.id.addcash);
            roomnumber = getIntent().getStringExtra("Room Number");
            mAuth = FirebaseAuth.getInstance();
            a = "hellooo mar jaaoo dushmno";
            ;

            completebooking("Available Rooms");
            setcustomername("Available Rooms","Room Owners");


            FirebaseDatabase.getInstance().getReference().child("Users").child("Room Owners").child(mAuth.getCurrentUser().getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Roomownername = dataSnapshot.child("Name").getValue().toString();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


        }

        else {

            //for rides receipt

            date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());


            ratingBar = findViewById(R.id.rating);
            amount = findViewById(R.id.bill);
            customer = findViewById(R.id.name);
            submit = findViewById(R.id.submit);
            addcash = findViewById(R.id.addcash);
            roomnumber = getIntent().getStringExtra("Ride Number");
            mAuth = FirebaseAuth.getInstance();

            ;

            completebooking("Available Rides");
           setcustomername("Available Rides","Drivers");


            FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(mAuth.getCurrentUser().getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Roomownername = dataSnapshot.child("Name").getValue().toString();
                            customer.setText(Roomownername);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });





        }




    }


    public void onMapReady(com.google.android.gms.maps.GoogleMap googleMap) {

        if (checker.equals("Room Owners")){

        FirebaseDatabase.getInstance().getReference().child("Users").child("Room Owners")
                .child(mAuth.getCurrentUser().getUid()).child("Room 1").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String longi = dataSnapshot.child("Longitude").getValue().toString();
                String lati = dataSnapshot.child("latitude").getValue().toString();
                String address = dataSnapshot.child("Address").getValue().toString();
                double longid = Double.parseDouble(longi);
                double latid = Double.parseDouble(lati);


                LatLng sydney = new LatLng(latid, longid);
                googleMap.addMarker(new MarkerOptions()
                        .position(sydney)
                        .title(address).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

        else {

            FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(mAuth.getCurrentUser().getUid()).child("Ride")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String olng = dataSnapshot.child("Origin longitude").getValue().toString();
                            String olat=dataSnapshot.child("Origin latitude").getValue().toString();
                            String dlong=dataSnapshot.child("Destination longitude").getValue().toString();
                            String dlat =dataSnapshot.child("Destination latitude").getValue().toString();

                            String origin=dataSnapshot.child("Origin").getValue().toString();
                            String destination=dataSnapshot.child("Destination").getValue().toString();



                            double longid = Double.parseDouble(olng);
                            double latid = Double.parseDouble(olat);

                            double longid2 = Double.parseDouble(dlong);
                            double latid2 = Double.parseDouble(dlat);


                            LatLng sydney = new LatLng(latid, longid);
                            LatLng sydney2 = new LatLng(latid2, longid2);
                            googleMap.addMarker(new MarkerOptions()
                                    .position(sydney2)
                                    .title(destination).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                            googleMap.addMarker(new MarkerOptions()
                                    .position(sydney)
                                    .title(origin).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });






        }


    }

    private void completebooking(String bookingtype) {
        FirebaseDatabase.getInstance().getReference().child(bookingtype).child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (checker.equals("Drivers")) {
                    price = dataSnapshot.child("Fare").getValue().toString();
                }
                else{
                        price = dataSnapshot.child("Price").getValue().toString();

                    }






            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    private void setcustomername(String bookingtype, String usertype) {
//booking type = available rides or room
       FirebaseDatabase.getInstance().getReference().child(bookingtype).child(mAuth.getCurrentUser().getUid())
               .child("Booked By").addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               String user[];
              user=new String[5];
               int cout=0;
               for (DataSnapshot ds:dataSnapshot.getChildren()){
                         user[++cout] = ds.getKey();

               }










               FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(user[Integer.parseInt(roomnumber)])
                       .addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               String name = dataSnapshot.child("Name").getValue().toString();
                               String wallet =dataSnapshot.child("Wallet").getValue().toString();
                               int walletint=Integer.parseInt(wallet);
                               int priceint=Integer.parseInt(price);
                               priceint=priceint-walletint;


                               customer.setText(name);
                               customername=name;
                               amount.setText(String.valueOf(priceint));
                               addcash.setText(String.valueOf(priceint));




                               submit.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {


                                       AlertDialog.Builder builder = new AlertDialog.Builder(roomreceipt.this);

                                       builder.setMessage("Are you sure you want to complete this Booking?").setCancelable(false)
                                               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                   @Override
                                                   public void onClick(DialogInterface dialogInterface, int is) {


                                                       String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
                                                       StringBuilder code = new StringBuilder(28);
                                                       for (int i = 0; i < 28; i++) {

                                                           int index = (int) (AlphaNumericString.length() * Math.random());


                                                           code.append(AlphaNumericString
                                                                   .charAt(index));
                                                       }

                                                       //        Toast.makeText(roomreceipt.this, code, Toast.LENGTH_SHORT).show();




                                                       //update Rating

                                                      FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(user[Integer.parseInt(roomnumber)])
                                                               .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                   @Override
                                                                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                       float currentratingint=0;
                                                                       float ratingint=0;
                                                                       if (dataSnapshot.child("Rating").exists()){
                                                                           String currentrating=dataSnapshot.child("Rating").getValue().toString();
                                                                           String rating = String.valueOf(ratingBar.getRating());


                                                                          currentratingint=Float.parseFloat(currentrating);
                                                                          ratingint=Float.parseFloat(rating);


                                                                         float totalrating = (currentratingint+ratingint)/2;

                                                                           HashMap usermap = new HashMap<>();

                                                                           usermap.put("Rating", String.valueOf(totalrating));

                                                                           FirebaseDatabase.getInstance().getReference().child("Users").child("Customers")
                                                                                   .child(user[Integer.parseInt(roomnumber)]).updateChildren(usermap);







                                                                       }


                                                                   }

                                                                   @Override
                                                                   public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                   }
                                                               });


                                                       //update customer history
                                                       FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(user[Integer.parseInt(roomnumber)])
                                                               .child("History").child(date).child(String.valueOf(code)).addListenerForSingleValueEvent(new ValueEventListener() {
                                                           @RequiresApi(api = Build.VERSION_CODES.O)
                                                           @Override
                                                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                               HashMap usermap = new HashMap<>();

                                                               usermap.put("Vendor", Roomownername);
                                                               usermap.put("Date", date);
                                                               usermap.put("Amount Paid", addcash.getText().toString());
                                                               usermap.put("Rating",String.valueOf(ratingBar.getRating()));
                                                               if (checker.equals("Room Owners")) {
                                                                   usermap.put("Room owner", mAuth.getCurrentUser().getUid());
                                                               }else{
                                                                   usermap.put("Driver", mAuth.getCurrentUser().getUid());
                                                               }

                                                               FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(user[Integer.parseInt(roomnumber)])
                                                                       .child("History").child(date).child(String.valueOf(code)).updateChildren(usermap);


                                                           }

                                                           @Override
                                                           public void onCancelled(@NonNull DatabaseError databaseError) {

                                                           }
                                                       });




                                                       //update roomowner history
                                                       FirebaseDatabase.getInstance().getReference().child("Users").child(usertype).child(mAuth.getCurrentUser().getUid())
                                                               .child("History").child(date).child(String.valueOf(code)).addListenerForSingleValueEvent(new ValueEventListener() {
                                                           @Override
                                                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                               HashMap usermap = new HashMap<>();

                                                               usermap.put("Amount Recieved", addcash.getText().toString());
                                                               usermap.put("Date", date);
                                                               usermap.put("Customer ID", user[Integer.parseInt(roomnumber)]);
                                                               usermap.put("Customer", customername);

                                                               FirebaseDatabase.getInstance().getReference().child("Users").child(usertype).child(mAuth.getCurrentUser().getUid())
                                                                       .child("History").child(date).child(String.valueOf(code)).updateChildren(usermap);

                                                           }

                                                           @Override
                                                           public void onCancelled(@NonNull DatabaseError databaseError) {

                                                           }
                                                       });





                                                       FirebaseDatabase.getInstance().getReference().child(bookingtype).child(mAuth.getCurrentUser().getUid())
                                                               .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                   @Override
                                                                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                       if (bookingtype.equals("Available Rides")){
                                                                           String availability =dataSnapshot.child("Seats Availability").getValue().toString();
                                                                           int availabilityint = Integer.parseInt(availability);
                                                                           availabilityint++;

                                                                           HashMap usermap = new HashMap<>();

                                                                           usermap.put("Seats Availability", String.valueOf(availabilityint));
                                                                           FirebaseDatabase.getInstance().getReference().child(bookingtype).child(mAuth.getCurrentUser().getUid()).updateChildren(usermap);





                                                                       }
                                                                       else {

                                                                           String availability =dataSnapshot.child("Availability").getValue().toString();
                                                                           int availabilityint = Integer.parseInt(availability);
                                                                           availabilityint++;

                                                                           HashMap usermap = new HashMap<>();

                                                                           usermap.put("Availability", String.valueOf(availabilityint));
                                                                           FirebaseDatabase.getInstance().getReference().child(bookingtype).child(mAuth.getCurrentUser().getUid()).updateChildren(usermap);






                                                                       }


                                                                   }

                                                                   @Override
                                                                   public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                   }
                                                               });



















                                                       //update customer wallet
                                                       FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(user[Integer.parseInt(roomnumber)])
                                                               .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                   @Override
                                                                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                       String wal = dataSnapshot.child("Wallet").getValue().toString();
                                                                       int wallet =Integer.parseInt(wal);
                                                                       int priceint=Integer.parseInt(price);
                                                                       int amountpaid = Integer.parseInt(addcash.getText().toString());

                                                                       if (amountpaid>priceint){
                                                                           int diff= amountpaid-priceint;
                                                                           wallet=wallet+diff;

                                                                       }
                                                                       else if (amountpaid<priceint){
                                                                           int diff =priceint-amountpaid;
                                                                           wallet=wallet-diff;


                                                                       }

                                                                       HashMap usermap = new HashMap<>();

                                                                       usermap.put("Wallet", String.valueOf(wallet));
                                                                       FirebaseDatabase.getInstance().getReference().child("Users").child("Customers")
                                                                               .child(user[Integer.parseInt(roomnumber)]).updateChildren(usermap);




                                                                   }

                                                                   @Override
                                                                   public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                   }
                                                               });



                                                       FirebaseDatabase.getInstance().getReference().child("Users").child(usertype).child(mAuth.getCurrentUser().getUid())
                                                               .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                   @Override
                                                                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                       String wal = dataSnapshot.child("Wallet").getValue().toString();
                                                                       int wallet =Integer.parseInt(wal);
                                                                       int priceint=Integer.parseInt(price);
                                                                       int amountpaid = Integer.parseInt(addcash.getText().toString());

                                                                       if (amountpaid>priceint){
                                                                           int diff= priceint-amountpaid;
                                                                           wallet=wallet+diff;

                                                                       }
                                                                       else if (amountpaid<priceint){
                                                                           int diff =amountpaid-priceint;
                                                                           wallet=wallet-diff;


                                                                       }



                                                                       HashMap usermap = new HashMap<>();

                                                                       usermap.put("Wallet", String.valueOf(wallet));
                                                                       FirebaseDatabase.getInstance().getReference().child("Users").child(usertype)
                                                                               .child(mAuth.getCurrentUser().getUid()).updateChildren(usermap);



                                                                   }

                                                                   @Override
                                                                   public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                   }
                                                               });
                                                       if (checker.equals("Room Owners"))
                                                           startActivity(new Intent(roomreceipt.this,Room_owner_firstpage.class));
                                                       else{
                                                           startActivity(new Intent(roomreceipt.this,vehicle_owner_firstpage.class));
                                                       }

                                                       FirebaseDatabase.getInstance().getReference().child("Bookings").child("Scheduled Bookings").child(user[Integer.parseInt(roomnumber)])
                                                               .getRef().removeValue();

                                                       FirebaseDatabase.getInstance().getReference().child(bookingtype).child(mAuth.getCurrentUser().getUid()).child("Booked By").child(user[Integer.parseInt(roomnumber)])
                                                               .getRef().removeValue();








                                                   }












                                               }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                           @Override
                                           public void onClick(DialogInterface dialogInterface, int i) {
                                               dialogInterface.cancel();
                                           }
                                       });

                                       AlertDialog alertDialog =builder.create();
                                       alertDialog.show();







                                   }



                               });













                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {

                           }
                       });

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
    }
}