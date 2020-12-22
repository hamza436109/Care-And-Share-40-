package com.example.careandshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class bookroom extends AppCompatActivity {
    Button reserve,cancelbooking;

    ImageSlider mainslider;
    Handler mHandler;
    List<SlideModel> remoteimages;

    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    TextView roomdecription,username,availability;
    private String roomdesc,username1,availability1,price,address,userid,getroomnumber;
    int availabilityinteger=0;

    private ProgressDialog loadingbar;
    String checker="h",destination;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookroom);
        checker = getIntent().getStringExtra("type");
        Toast.makeText(this, checker, Toast.LENGTH_SHORT).show();




        if (checker.equals("room")){


            TextView TO,destinationas;
            TO = findViewById(R.id.TO);
            destinationas=findViewById(R.id.destination);
            destinationas.setVisibility(View.INVISIBLE);
            TO.setVisibility(View.INVISIBLE);

            roomdecription = findViewById(R.id.roomdesconbookroom);
        username = findViewById(R.id.username);
        availability = findViewById(R.id.availability);
        reserve = findViewById(R.id.reservebutton);
        ;
        cancelbooking = findViewById(R.id.cancelbooking);


        this.mHandler = new Handler();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        roomdesc = getIntent().getStringExtra("Room Description");
        availability1 = getIntent().getStringExtra("availability");
        userid = getIntent().getStringExtra("UID");
        price = getIntent().getStringExtra("Price");
        address = getIntent().getStringExtra("Address");
        getroomnumber = getIntent().getStringExtra("Room number");
        ;
        availabilityinteger = Integer.parseInt(availability1);
        loadingbar = new ProgressDialog(this);


        roomdecription.setText(roomdesc);

        availability.setText(availability1);
        mAuth = FirebaseAuth.getInstance();


        mainslider = (ImageSlider) findViewById(R.id.image_slider);
        remoteimages = new ArrayList<>();



            FirebaseDatabase.getInstance().getReference().child("Users").child("Room Owners").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String uname =dataSnapshot.child("Name").getValue().toString();
                    username.setText(uname);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });














        checkbookingstatus();

        Toast.makeText(this, mAuth.getCurrentUser().getUid() + " hello ppakistna " + getroomnumber, Toast.LENGTH_SHORT).show();

        //uploadinng pictures
        FirebaseDatabase.getInstance().getReference().child("Users").child("Room Owners").child(userid).child("Room 1").child("PICTURES")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int count = 1;
                        if (dataSnapshot.getChildrenCount() > 0) {

                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                remoteimages.add(new SlideModel(data.child("Link").getValue().toString(),
                                        "Picture: " + count, ScaleTypes.FIT));
                                count++;
                            }

                            mainslider.setImageList(remoteimages, ScaleTypes.FIT);

                        } else {
                            remoteimages.add(new SlideModel(R.drawable.background_image, "Add Pictures", ScaleTypes.FIT));
                            mainslider.setImageList(remoteimages, ScaleTypes.FIT);

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        cancelbooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(bookroom.this);
                builder.setMessage("Are you sure you want to Cancel your booking ?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                                NotificationCompat.Builder cancelnotificationride = new NotificationCompat.Builder(bookroom.this,"My Notification");
                                cancelnotificationride.setContentTitle("Cancelled Booking");
                                cancelnotificationride.setContentText("Oops, Your Booking has been cancelled");
                                cancelnotificationride.setSmallIcon(R.drawable.ic_notification);
                                cancelnotificationride.setAutoCancel(true);

                                NotificationManagerCompat managercompat = NotificationManagerCompat.from(bookroom.this);
                                managercompat.notify(1,cancelnotificationride.build());


                                cancelbooking.setVisibility(View.GONE);
                                reserve.setVisibility(View.VISIBLE);

                                FirebaseDatabase.getInstance().getReference().child("Available Rooms")
                                        .child(userid).child("Booked By").child(mAuth.getCurrentUser().getUid()).getRef().removeValue();




                                FirebaseDatabase.getInstance().getReference().child("Available Rooms").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        int bedspace =Integer.parseInt(dataSnapshot.child("Availability").getValue().toString());
                                        bedspace++;

                                        HashMap<String, Object> usermap = new HashMap<>();
                                        usermap.put("Availability", String.valueOf(bedspace));

                                        FirebaseDatabase.getInstance().getReference().child("Available Rooms").child(userid).updateChildren(usermap);







                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                                FirebaseDatabase.getInstance().getReference().child("Available Rooms")
                                        .child(userid).child("Booked By").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        // updateroomavailability();


                                        FirebaseDatabase.getInstance().getReference().child("Bookings").child("Scheduled Bookings").child(mAuth.getCurrentUser().getUid())
                                                .child("Room " + getroomnumber).removeValue();






                                    }


                                    private void updateroomavailability() {


                                        FirebaseDatabase.getInstance().getReference().child("Available Rooms").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                String availability = dataSnapshot.child("Availability").getValue().toString();
                                                availabilityinteger = Integer.parseInt(availability);

                                                availabilityinteger++;
                                                Toast.makeText(bookroom.this, String.valueOf(availabilityinteger), Toast.LENGTH_SHORT).show();

                                                HashMap<String, Object> updateavailability = new HashMap<>();
                                                updateavailability.put("Availability", String.valueOf(availabilityinteger));
                                                FirebaseDatabase.getInstance().getReference().child("Available Rooms").child(userid).updateChildren(updateavailability);


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


                                //   startActivity(getIntent());

                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.cancel();
                    }
                });


                AlertDialog alertDialog = builder.create();
                alertDialog.show();





            

            }
        });


        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reserve.setText("Loading...");
                loadingbar.setTitle("Room Booking");
                loadingbar.setMessage("Please Wait, while Room is booked...");


                reserve.setText("Book This Room");


                FirebaseDatabase.getInstance().getReference().child("Available Rooms").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        reserve.setVisibility(View.INVISIBLE);
                        reserve.setText("Book This Room");

                        loadingbar.show();
                        int availability;
                        String bedspace = (String) dataSnapshot.child("Availability").getValue();
                        availability = Integer.parseInt(bedspace);
                        Toast.makeText(bookroom.this, String.valueOf(availability), Toast.LENGTH_SHORT).show();

                        if (availability > 0) {

                            availabilityinteger = Integer.parseInt(bedspace);
                            availabilityinteger--;

                            HashMap<String, Object> usermap = new HashMap<>();
                            usermap.put("Availability", String.valueOf(availabilityinteger));
                            usermap.put("Price", price);
                            usermap.put("User ID", userid);


                            FirebaseDatabase.getInstance().getReference().child("Bookings").child("Scheduled Bookings").child(mAuth.getCurrentUser().getUid())
                                    .child("Room " + getroomnumber).updateChildren(usermap);

                            updateroomavailability();
                            reserve.setVisibility(View.GONE);
                            cancelbooking.setVisibility(View.VISIBLE);
                            loadingbar.dismiss();


                            FirebaseDatabase.getInstance().getReference().child("Available Rooms")
                                    .child(userid).child("Booked By").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    HashMap<String, Object> customerinfo = new HashMap<>();
                                    customerinfo.put("booked", "TRUE");
                                    FirebaseDatabase.getInstance().getReference().child("Available Rooms")
                                            .child(userid).child("Booked By").child(mAuth.getCurrentUser().getUid()).updateChildren(customerinfo);


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(bookroom.this,"My Notification");
                            builder.setContentTitle("Booked Room");
                            builder.setContentText("Congratulations, you have successfully booked a Room. we are waiting to provide" +
                                    "you our services");
                            builder.setSmallIcon(R.drawable.ic_notification);
                            builder.setAutoCancel(true);

                            NotificationManagerCompat managercompat = NotificationManagerCompat.from(bookroom.this);
                            managercompat.notify(1,builder.build());








                        } else {
                            loadingbar.dismiss();
                            Toast.makeText(bookroom.this, "This Rooom is not available", Toast.LENGTH_SHORT).show();
                            reserve.setVisibility(View.VISIBLE);

                        }


                            /*finish();
                            startActivity(getIntent());*/
                    }

                    private void updateroomavailability() {


                        FirebaseDatabase.getInstance().getReference().child("Available Rooms").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                HashMap<String, Object> updateavailability = new HashMap<>();
                                updateavailability.put("Availability", String.valueOf(availabilityinteger));
                                updateavailability.put("Price", price);

                                FirebaseDatabase.getInstance().getReference().child("Available Rooms").child(userid).updateChildren(updateavailability);
                                updateavailability.put("User ID", userid);
                                FirebaseDatabase.getInstance().getReference().child("Bookings").child("Scheduled Bookings").child(mAuth.getCurrentUser().getUid()).child("Room " + getroomnumber).updateChildren(updateavailability);


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
        });


    }
        else if (checker.equals("ride")){


             TextView userinfo,guests,carinfo,dot,destination1;
             Button reserve1;




            roomdecription = findViewById(R.id.roomdesconbookroom);
            username = findViewById(R.id.username);
            availability = findViewById(R.id.availability);
            userinfo=findViewById(R.id.userinfo);
            carinfo=findViewById(R.id.guests);
            reserve=findViewById(R.id.reservebutton);;
            dot=findViewById(R.id.dot);
            destination1=findViewById(R.id.destination);

            cancelbooking = findViewById(R.id.cancelbooking);


            dot.setVisibility(View.INVISIBLE);




            this.mHandler = new Handler();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


            roomdesc = getIntent().getStringExtra("Origin");
            availability1 = getIntent().getStringExtra("availability");
            userid = getIntent().getStringExtra("UID");
            destination=getIntent().getStringExtra("Destination");;
            price = getIntent().getStringExtra("Fare");
            address = getIntent().getStringExtra("Address");
            getroomnumber = getIntent().getStringExtra("Ride number");
            ;
            availabilityinteger = Integer.parseInt(availability1);
            loadingbar = new ProgressDialog(this);


            roomdecription.setText(roomdesc);
            roomdecription.setTextColor(Color.parseColor("#FF0000"));

            userinfo.setText("Private Ride hosted by:");

            reserve.setText("Book this Ride");
            destination1.setText(destination);
            destination1.setTextColor(Color.parseColor("#006400"));

            mAuth = FirebaseAuth.getInstance();


            mainslider = (ImageSlider) findViewById(R.id.image_slider);
            remoteimages = new ArrayList<>();

            checkbookingstatus();

            Toast.makeText(this, mAuth.getCurrentUser().getUid() + " hello ppakistna " + getroomnumber, Toast.LENGTH_SHORT).show();

            //uploadinng pictures
           FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(userid).child("PICTURES")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int count = 1;
                            if (dataSnapshot.getChildrenCount() > 0) {

                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    remoteimages.add(new SlideModel(data.child("Link").getValue().toString(),
                                            "Picture: " + count, ScaleTypes.FIT));
                                    count++;
                                }

                                mainslider.setImageList(remoteimages, ScaleTypes.FIT);

                            } else {
                                remoteimages.add(new SlideModel(R.drawable.background_image, "Add Pictures", ScaleTypes.FIT));
                                mainslider.setImageList(remoteimages, ScaleTypes.FIT);

                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

            FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String uname =dataSnapshot.child("Name").getValue().toString();
                    String car=dataSnapshot.child("Car").getValue().toString();
                    username.setText(uname);
                    carinfo.setText(car);
                    carinfo.setTypeface(null, Typeface.BOLD);
                    availability.setText("CAR: ");
                     availability.setTypeface(null, Typeface.BOLD);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



            cancelbooking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(bookroom.this);
                    builder.setMessage("Are you sure you want to Cancel your booking ?").setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {


                                    cancelbooking.setVisibility(View.GONE);
                                    reserve.setText("Book this Ride");
                                    reserve.setVisibility(View.VISIBLE);

                                    FirebaseDatabase.getInstance().getReference().child("Available Rides")
                                            .child(userid).child("Booked By").child(mAuth.getCurrentUser().getUid()).getRef().removeValue();




                                    FirebaseDatabase.getInstance().getReference().child("Available Rides").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            int bedspace =Integer.parseInt(dataSnapshot.child("Seats Availability").getValue().toString());
                                            bedspace++;

                                            HashMap<String, Object> usermap = new HashMap<>();
                                            usermap.put("Seats Availability", String.valueOf(bedspace));

                                            FirebaseDatabase.getInstance().getReference().child("Available Rides").child(userid).updateChildren(usermap);







                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                                    FirebaseDatabase.getInstance().getReference().child("Available Rides")
                                            .child(userid).child("Booked By").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            // updateroomavailability();


                                            FirebaseDatabase.getInstance().getReference().child("Bookings").child("Scheduled Bookings").child(mAuth.getCurrentUser().getUid())
                                                    .child("Ride 1").removeValue();





                                            NotificationCompat.Builder cancelnotification = new NotificationCompat.Builder(bookroom.this,"My Notification");
                                            cancelnotification.setContentTitle("Cancelled Ride");
                                            cancelnotification.setContentText("Oops, Your Booking has been cancelled");
                                            cancelnotification.setSmallIcon(R.drawable.ic_notification);
                                            cancelnotification.setAutoCancel(true);

                                            NotificationManagerCompat managercompat = NotificationManagerCompat.from(bookroom.this);
                                            managercompat.notify(1,cancelnotification.build());






                                        }


                                        private void updateroomavailability() {


                                            FirebaseDatabase.getInstance().getReference().child("Available Rooms").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    String availability = dataSnapshot.child("Availability").getValue().toString();
                                                    availabilityinteger = Integer.parseInt(availability);

                                                    availabilityinteger++;
                                                    Toast.makeText(bookroom.this, String.valueOf(availabilityinteger), Toast.LENGTH_SHORT).show();

                                                    HashMap<String, Object> updateavailability = new HashMap<>();
                                                    updateavailability.put("Availability", String.valueOf(availabilityinteger));
                                                    FirebaseDatabase.getInstance().getReference().child("Available Rooms").child(userid).updateChildren(updateavailability);


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


                                    //   startActivity(getIntent());

                                }
                            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.cancel();
                        }
                    });


                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();













                }
















            });


            reserve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reserve.setText("Loading...");
                    loadingbar.setTitle("Ride Booking");
                    loadingbar.setMessage("Please Wait, while Ride is booked...");


                    reserve.setText("Book This Ride");


                    FirebaseDatabase.getInstance().getReference().child("Available Rides").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            reserve.setVisibility(View.INVISIBLE);
                            reserve.setText("Book This Room");

                            loadingbar.show();
                            int availability;
                            String bedspace = (String) dataSnapshot.child("Seats Availability").getValue();
                            availability = Integer.parseInt(bedspace);
                            Toast.makeText(bookroom.this, String.valueOf(availability), Toast.LENGTH_SHORT).show();

                            if (availability > 0) {


                                NotificationCompat.Builder builder = new NotificationCompat.Builder(bookroom.this,"My Notification");
                                builder.setContentTitle("Booked Ride");
                                builder.setContentText("Congratulations, you have successfully booked a ride. we are waiting to provide" +
                                        "you our services");
                                builder.setSmallIcon(R.drawable.ic_notification);
                                builder.setAutoCancel(true);

                                NotificationManagerCompat managercompat = NotificationManagerCompat.from(bookroom.this);
                                managercompat.notify(1,builder.build());

                                availabilityinteger = Integer.parseInt(bedspace);
                                availabilityinteger--;

                                HashMap<String, Object> usermap = new HashMap<>();
                                usermap.put("Seats Availability", String.valueOf(availabilityinteger));
                                usermap.put("Fare", price);
                                usermap.put("UID", userid);


                                FirebaseDatabase.getInstance().getReference().child("Bookings").child("Scheduled Bookings").child(mAuth.getCurrentUser().getUid()).child("Ride 1")
                                        .updateChildren(usermap);

                                updaterideavailability();
                                reserve.setVisibility(View.GONE);
                                cancelbooking.setVisibility(View.VISIBLE);
                                loadingbar.dismiss();


                                FirebaseDatabase.getInstance().getReference().child("Available Rides")
                                        .child(userid).child("Booked By").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        HashMap<String, Object> customerinfo = new HashMap<>();
                                        customerinfo.put("booked", "TRUE");
                                        FirebaseDatabase.getInstance().getReference().child("Available Rides")
                                                .child(userid).child("Booked By").child(mAuth.getCurrentUser().getUid()).updateChildren(customerinfo);


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                            } else {

                                Toast.makeText(bookroom.this, "This ride is not available", Toast.LENGTH_SHORT).show();
                                reserve.setVisibility(View.VISIBLE);
                                loadingbar.dismiss();
                            }


                            /*finish();
                            startActivity(getIntent());*/
                        }

                        private void updaterideavailability() {


                            FirebaseDatabase.getInstance().getReference().child("Available Rides").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    HashMap<String, Object> updateavailability = new HashMap<>();
                                    updateavailability.put("Seats Availability", String.valueOf(availabilityinteger));
                                    updateavailability.put("Fare", price);

                                    FirebaseDatabase.getInstance().getReference().child("Available Rides").child(userid).updateChildren(updateavailability);
                                    updateavailability.put("UID", userid);
                                    FirebaseDatabase.getInstance().getReference().child("Bookings").child("Scheduled Bookings").child(mAuth.getCurrentUser().getUid()).child("Ride 1").updateChildren(updateavailability);


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



                  /*  if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                        NotificationChannel channel = new NotificationChannel("My Notification","My Notification",NotificationManager.IMPORTANCE_DEFAULT);
                        NotificationManager manager = getSystemService(NotificationManager.class);
                        manager.createNotificationChannel(channel);


                    }*/


















                }
            });




















        }







    }

    private void checkbookingstatus() {

        if (checker.equals("ride")){
            FirebaseDatabase.getInstance().getReference().child("Bookings").child("Scheduled Bookings").child(mAuth.getCurrentUser().getUid())
                .child("Ride 1").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount()>0) {

                    cancelbooking.setVisibility(View.VISIBLE);
                    reserve.setVisibility(View.GONE);

                }
                else  if(dataSnapshot.getChildrenCount()==0) {

                    cancelbooking.setVisibility(View.GONE);
                    reserve.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });}
         else if (checker.equals("room")){




            FirebaseDatabase.getInstance().getReference().child("Bookings").child("Scheduled Bookings").child(mAuth.getCurrentUser().getUid())
                    .child("Room 1").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount()>0) {

                        cancelbooking.setVisibility(View.VISIBLE);
                        reserve.setVisibility(View.GONE);

                    }
                    else  if(dataSnapshot.getChildrenCount()==0) {

                        cancelbooking.setVisibility(View.GONE);
                        reserve.setVisibility(View.VISIBLE);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }










    }


}