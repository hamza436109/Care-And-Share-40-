package com.example.careandshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;


class example{
     public static int a=1;

}

public class Scheduledroombooking extends AppCompatActivity {
    RelativeLayout relaltivelayout[];
     TextView customername[],roomdescription[],price[],roomtype[];
    Button completebtn[];

    FirebaseAuth fauth;
    DatabaseReference database;

    int totalnumbofcustomers=0;
    String user[];
    int ab=0;
    String testing;

    int ist =1;;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduledroombooking);
        type = getIntent().getStringExtra("type");



        if (type.equals("Room Owners")){


        fauth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference().child("Available Rooms");


        customername = new TextView[10];
        roomdescription = new TextView[10];
        price = new TextView[10];
        roomtype = new TextView[10];
        relaltivelayout = new RelativeLayout[10];
        completebtn = new Button[10];
        user = new String[10];


        customername[1] = findViewById(R.id.Customername1);
        customername[2] = findViewById(R.id.Customername2);

        roomdescription[1] = findViewById(R.id.roomdescription1);
        roomdescription[2] = findViewById(R.id.roomdescription2);

        price[1] = findViewById(R.id.price1);
        price[2] = findViewById(R.id.price2);

        roomtype[1] = findViewById(R.id.roomtype1);
        roomtype[2] = findViewById(R.id.roomtype2);

        relaltivelayout[1] = findViewById(R.id.rl1);
        relaltivelayout[2] = findViewById(R.id.rl2);

        completebtn[1] = findViewById(R.id.button1);
        completebtn[2] = findViewById(R.id.button2);

        counttotalcustomers();
        completebtnlisteners();


        FirebaseDatabase.getInstance().getReference().child("Available Rooms").child(fauth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getChildrenCount() > 0) {


                    database.child(fauth.getCurrentUser().getUid()).child("Booked By").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ab = 0;
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                user[++ab] = data.getKey();

                            }

                            for (int av = 1; av <= ab; av++) {
                                int finalAv = av;
                                FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(user[av]).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String name = dataSnapshot.child("Name").getValue().toString();
                                        Toast.makeText(Scheduledroombooking.this, name, Toast.LENGTH_SHORT).show();
                                        customername[finalAv].setText(name);

                                    }


                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    database.child(fauth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String roomtype1 = dataSnapshot.child("Room Type").getValue().toString();
                            String roomdesc1 = dataSnapshot.child("Room Description").getValue().toString();
                            String price1 = dataSnapshot.child("Price").getValue().toString();
                            for (int a = 1; a <= totalnumbofcustomers; a++) {

                                roomtype[a].setText(roomtype1);
                                roomdescription[a].setText(roomdesc1);
                                price[a].setText(price1);


                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

        else {
//for booked rides


            TextView header,pernight,pernight1,pernight3,pernight4,origin1,origin2,dest1,dest2;
            pernight=findViewById(R.id.pernightt);
            pernight1=findViewById(R.id.pernight1);
            pernight3=findViewById(R.id.pernightt3);
            pernight4=findViewById(R.id.pernightt4);
            origin1=findViewById(R.id.roomtype1txt);
            origin2=findViewById(R.id.roomtype2txt);
            dest1=findViewById(R.id.roomdescription1txxt);
            dest2=findViewById(R.id.roomdescription2txt);



            header=findViewById(R.id.hel);
            header.setText("Booked Rides");
            pernight.setText("/Passenger");
            pernight1.setText("/Passenger");
            pernight3.setText("/Passenger");
            pernight4.setText("/Passenger");

            origin1.setText("Origin:");
            origin2.setText("Origin:");
            dest1.setText("Destination:");
            dest2.setText("Destination:");

            fauth = FirebaseAuth.getInstance();
            database = FirebaseDatabase.getInstance().getReference().child("Available Rides");


            customername = new TextView[10];
            roomdescription = new TextView[10];
            price = new TextView[10];
            roomtype = new TextView[10];
            relaltivelayout = new RelativeLayout[10];
            completebtn = new Button[10];
            user = new String[10];


            customername[1] = findViewById(R.id.Customername1);
            customername[2] = findViewById(R.id.Customername2);
            customername[3] = findViewById(R.id.Customername3);
            customername[4] = findViewById(R.id.Customername4);

            roomdescription[1] = findViewById(R.id.roomdescription1);
            roomdescription[2] = findViewById(R.id.roomdescription2);
            roomdescription[3] = findViewById(R.id.roomdescription3);
            roomdescription[4] = findViewById(R.id.roomdescription4);

            price[1] = findViewById(R.id.price1);
            price[2] = findViewById(R.id.price2);
            price[3] = findViewById(R.id.price3);
            price[4] = findViewById(R.id.price4);

            roomtype[1] = findViewById(R.id.roomtype1);
            roomtype[2] = findViewById(R.id.roomtype2);
            roomtype[3] = findViewById(R.id.roomtype3);
            roomtype[4] = findViewById(R.id.roomtype4);

            relaltivelayout[1] = findViewById(R.id.rl1);
            relaltivelayout[2] = findViewById(R.id.rl2);
            relaltivelayout[3] = findViewById(R.id.rl3);
            relaltivelayout[4] = findViewById(R.id.rl4);



            completebtn[1] = findViewById(R.id.button1);
            completebtn[2] = findViewById(R.id.button2);
            completebtn[3] = findViewById(R.id.button3);
            completebtn[4] = findViewById(R.id.button4);

            counttotalcustomers();
            completebtnlisteners();


            FirebaseDatabase.getInstance().getReference().child("Available Rides").child(fauth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getChildrenCount() > 0) {


                        database.child(fauth.getCurrentUser().getUid()).child("Booked By").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                ab = 0;
                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    user[++ab] = data.getKey();

                                }

                                for (int av = 1; av <= ab; av++) {
                                    int finalAv = av;
                                    FirebaseDatabase.getInstance().getReference().child("Users").child("Customers").child(user[av]).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String name = dataSnapshot.child("Name").getValue().toString();
                                            String phone = dataSnapshot.child("Phone").getValue().toString();
                                            customername[finalAv].setText(name);

                                        }


                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        database.child(fauth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String roomtype1 = dataSnapshot.child("Origin").getValue().toString();
                                String roomdesc1 = dataSnapshot.child("Destination").getValue().toString();
                                String price1 = dataSnapshot.child("Fare").getValue().toString();
                                for (int a = 1; a <= totalnumbofcustomers; a++) {

                                    roomtype[a].setText(roomtype1);
                                    roomdescription[a].setText(roomdesc1);
                                    price[a].setText(price1);


                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });





        }


    }

    private void completebtnlisteners() {



        completebtn[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Scheduledroombooking.this);

                builder.setMessage("Are you sure you want to complete this Booking?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Intent gotoroomreceipt = new Intent(Scheduledroombooking.this,roomreceipt.class);
                                gotoroomreceipt.putExtra("Ride Number","1");
                                gotoroomreceipt.putExtra("Room Number","1");
                                gotoroomreceipt.putExtra("type",type);
                                startActivity(gotoroomreceipt);




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





        completebtn[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Scheduledroombooking.this);

                builder.setMessage("Are you sure you want to complete this Booking?").setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Intent gotoroomreceipt = new Intent(Scheduledroombooking.this,roomreceipt.class);
                                gotoroomreceipt.putExtra("Ride Number","2");
                                gotoroomreceipt.putExtra("Room Number","2");
                                gotoroomreceipt.putExtra("type",type);
                                startActivity(gotoroomreceipt);



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


    private void counttotalcustomers() {

        database.child(fauth.getCurrentUser().getUid()).child("Booked By").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount()>0) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        totalnumbofcustomers++;
                    }

                   // Toast.makeText(Scheduledroombooking.this, "Total Customers ARe :" + totalnumbofcustomers, Toast.LENGTH_SHORT).show();

                    for (int ab=1;ab<=totalnumbofcustomers;ab++){

                        relaltivelayout[ab].setVisibility(View.VISIBLE);


                    }
                }
                else
                    Toast.makeText(Scheduledroombooking.this, "Total Customers ARe :" + totalnumbofcustomers, Toast.LENGTH_SHORT).show();





                }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}