package com.example.careandshare;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Slide;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class MyRooms extends AppCompatActivity {
      DatabaseReference databaseReference;
      FirebaseAuth mAuth;

      private TextView [] roomdescription;
      private TextView [] price;
      private TextView [] city;
      private TextView [] roomtype;

      String[] room ={"hel","Room 1","Room 2","Room 3","Room 4","Room 5"};
      String userid;

      ImageSlider mainslider;
      ImageSlider mainslider2;
      ImageSlider mainslider3;
      ImageSlider mainslider4;
      ImageSlider mainslider5;

      Handler mHandler;
      List<SlideModel> remoteimages;
      List<SlideModel> remoteimages2;
      List<SlideModel> remoteimages3;
      List<SlideModel> remoteimages4;
      List<SlideModel> remoteimages5;

      CardView cardview1,cardview2,cardview3,cardview4,cardview5;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rooms);


        this.mHandler = new Handler();
        //m_Runnable.run();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


         mAuth = FirebaseAuth.getInstance();

         roomdescription = new TextView[10];
         price = new TextView[10];
         city = new TextView[10];
         roomtype = new TextView[10];

         mainslider = (ImageSlider)findViewById(R.id.image_slider);
         mainslider2 = (ImageSlider)findViewById(R.id.image_slider2);
         mainslider3 = (ImageSlider)findViewById(R.id.image_slider3);
         mainslider4 = (ImageSlider)findViewById(R.id.image_slider4);
         mainslider5 = (ImageSlider)findViewById(R.id.image_slider5);


         remoteimages =  new ArrayList<>();
         remoteimages2 =  new ArrayList<>();
         remoteimages3 =  new ArrayList<>();
         remoteimages4 =  new ArrayList<>();
         remoteimages5 =  new ArrayList<>();



         userid = mAuth.getCurrentUser().getUid();


         Addpicturestoslider();
         databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Room Owners");


       // information MY Rooms Page

        roomtype[1] =findViewById(R.id.roomtypeonnmyrooms1);
        roomtype[2] =findViewById(R.id.roomtypeonnmyrooms2);
        roomtype[3] =findViewById(R.id.roomtypeonnmyrooms3);
        roomtype[4] =findViewById(R.id.roomtypeonnmyrooms4);
        roomtype[5] =findViewById(R.id.roomtypeonnmyrooms5);


        roomdescription[1] = findViewById(R.id.roomdesconmyrooms1);
        roomdescription[2] = findViewById(R.id.roomdesconmyrooms2);
        roomdescription[3] = findViewById(R.id.roomdesconmyrooms3);
        roomdescription[4] = findViewById(R.id.roomdesconmyrooms4);
        roomdescription[5] = findViewById(R.id.roomdesconmyroom5);

        price[1]  = findViewById(R.id.priceonmyrooms1);
        price[2]  = findViewById(R.id.priceonmyrooms2);
        price[3]  = findViewById(R.id.priceonmyrooms3);
        price[4]  = findViewById(R.id.priceonmyrooms4);
        price[5]  = findViewById(R.id.priceonmyrooms5);

        city[1]  = findViewById(R.id.cityonmyrooms1);
        city[2]  = findViewById(R.id.cityonmyrooms2);
        city[3]  = findViewById(R.id.cityonmyrooms3);
        city[4]  = findViewById(R.id.cityonmyrooms4);
        city[5]  = findViewById(R.id.cityonmyrooms5);

        cardview1 = findViewById(R.id.card_view1);
        cardview2 = findViewById(R.id.card_view1);
        cardview3 = findViewById(R.id.card_view1);
        cardview4 = findViewById(R.id.card_view1);
        cardview5 = findViewById(R.id.card_view1);


        getuserinformaition();



    }



    private void Addpicturestoslider() {



        //1st room

        FirebaseDatabase.getInstance().getReference().child("Users").child("Room Owners").child(userid).child("Room 1").child("PICTURES")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        int count=1;
                        if (dataSnapshot.getChildrenCount()>0) {

                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                remoteimages.add(new SlideModel(data.child("Link").getValue().toString(),
                                        "Picture: " + count, ScaleTypes.FIT));
                                count++;
                            }

                            mainslider.setImageList(remoteimages, ScaleTypes.FIT);

                         }
                        else
                        {
                            remoteimages.add(new SlideModel(R.drawable.background_image,"Add Pictures",ScaleTypes.FIT));
                            mainslider.setImageList(remoteimages, ScaleTypes.FIT);

                        }

                        mainslider.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onItemSelected(int i) {
                                Intent gotosettings = new Intent(MyRooms.this,StoreRoomInformation.class);
                                gotosettings.putExtra("Number","Room 1");
                                startActivity(gotosettings);


                            }
                        });



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


//2nd room
        FirebaseDatabase.getInstance().getReference().child("Users").child("Room Owners").child(userid).child("Room 2").child("PICTURES")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {

                        if (dataSnapshot.getChildrenCount()>0) {

                            int count=1;

                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                remoteimages2.add(new SlideModel(data.child("Link").getValue().toString(),
                                        "Picture: " + count, ScaleTypes.FIT));
                                count++;
                            }

                            mainslider2.setImageList(remoteimages2, ScaleTypes.FIT);
                        }
                        else
                        {
                            remoteimages2.add(new SlideModel(R.drawable.background_image,"Add Pictures",ScaleTypes.FIT));
                            mainslider2.setImageList(remoteimages2, ScaleTypes.FIT);

                        }

                        mainslider2.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onItemSelected(int i) {
                                Intent gotosettings = new Intent(MyRooms.this,StoreRoomInformation.class);
                                gotosettings.putExtra("Number","Room 2");
                                startActivity(gotosettings);


                            }
                        });



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




        //Third Room



        FirebaseDatabase.getInstance().getReference().child("Users").child("Room Owners").child(userid).child("Room 3").child("PICTURES")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {

                        if (dataSnapshot.getChildrenCount()>0) {

                            int count=1;

                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                remoteimages3.add(new SlideModel(data.child("Link").getValue().toString(),
                                        "Picture: " + count, ScaleTypes.FIT));
                                count++;
                            }

                            mainslider3.setImageList(remoteimages3, ScaleTypes.FIT);
                        }
                        else
                        {
                            remoteimages3.add(new SlideModel(R.drawable.background_image,"Add Pictures",ScaleTypes.FIT));
                            mainslider3.setImageList(remoteimages3, ScaleTypes.FIT);
                        }

                        mainslider3.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onItemSelected(int i) {
                                Intent gotosettings = new Intent(MyRooms.this,StoreRoomInformation.class);
                                gotosettings.putExtra("Number","Room 3");
                                startActivity(gotosettings);


                            }
                        });



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        //Fourth Room


        FirebaseDatabase.getInstance().getReference().child("Users").child("Room Owners").child(userid).child("Room 4").child("PICTURES")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {

                        if (dataSnapshot.getChildrenCount()>0) {

                            int count=1;

                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                remoteimages4.add(new SlideModel(data.child("Link").getValue().toString(),
                                        "Picture: " + count, ScaleTypes.FIT));
                                count++;
                            }

                            mainslider4.setImageList(remoteimages4, ScaleTypes.FIT);
                        }
                        else
                        {
                            remoteimages4.add(new SlideModel(R.drawable.background_image,"Add Pictures",ScaleTypes.FIT));
                            mainslider4.setImageList(remoteimages4, ScaleTypes.FIT);
                        }

                        mainslider4.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onItemSelected(int i) {
                                Intent gotosettings = new Intent(MyRooms.this,StoreRoomInformation.class);
                                gotosettings.putExtra("Number","Room 4");
                                startActivity(gotosettings);


                            }
                        });



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        //5th Room



        FirebaseDatabase.getInstance().getReference().child("Users").child("Room Owners").child(userid).child("Room 5").child("PICTURES")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {

                        if (dataSnapshot.getChildrenCount()>0) {

                            int count=1;

                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                remoteimages5.add(new SlideModel(data.child("Link").getValue().toString(),
                                        "Picture: " + count, ScaleTypes.FIT));
                                count++;
                            }

                            mainslider5.setImageList(remoteimages5, ScaleTypes.FIT);
                        }
                        else
                        {
                            remoteimages5.add(new SlideModel(R.drawable.background_image,"Add Pictures",ScaleTypes.FIT));
                            mainslider5.setImageList(remoteimages5, ScaleTypes.FIT);
                        }

                        mainslider5.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onItemSelected(int i) {
                                Intent gotosettings = new Intent(MyRooms.this,StoreRoomInformation.class);
                                gotosettings.putExtra("Number","Room 5");
                                startActivity(gotosettings);


                            }
                        });



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




    }

    public void storeroominfo1(View view) {
        Intent storeinfo = new Intent(MyRooms.this,StoreRoomInformation.class);
        storeinfo.putExtra("Number","Room 1");
        startActivity(storeinfo);

    }

    public void storeroominfo2(View view) {
        Intent storeinfo = new Intent(MyRooms.this,StoreRoomInformation.class);
        storeinfo.putExtra("Number","Room 2");
        startActivity(storeinfo);

    }
    public void storeroominfo3(View view) {
        Intent gotosettings = new Intent(MyRooms.this,StoreRoomInformation.class);
        gotosettings.putExtra("Number","Room 3");
        startActivity(gotosettings);

    }
    public void storeroominfo4(View view) {
        Intent gotosettings = new Intent(MyRooms.this,StoreRoomInformation.class);
        gotosettings.putExtra("Number","Room 4");
        startActivity(gotosettings);

    }
    public void storeroominfo5(View view) {
        Intent gotosettings = new Intent(MyRooms.this,StoreRoomInformation.class);
        gotosettings.putExtra("Number","Room 5");
        startActivity(gotosettings);

    }

  /*  private final Runnable m_Runnable = new Runnable()
    {
        public void run()

        {
            Toast.makeText(MyRooms.this,"in runnable",Toast.LENGTH_SHORT).show();

            MyRooms.this.mHandler.postDelayed(m_Runnable,1000);
        }

    };
*/


    private void getuserinformaition() {

        databaseReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(int i = 1;i<6;i++){

                if (dataSnapshot.exists() && dataSnapshot.child(room[i]).getChildrenCount()>1) {

                    String description = dataSnapshot.child(room[i]).child("Room Description").getValue().toString();
                    String price1 = dataSnapshot.child(room[i]).child("Price").getValue().toString();
                    String city1 = dataSnapshot.child(room[i]).child("City").getValue().toString();
                    String room_type = dataSnapshot.child(room[i]).child("Room Type").getValue().toString();


                    roomtype[i].setText(room_type);
                    roomdescription[i].setText(description);
                    price[i].setText(price1);
                    city[i].setText(city1);
                    // ab.setVisibility(View.GONE);



                }
                else {


                    }



                }}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}