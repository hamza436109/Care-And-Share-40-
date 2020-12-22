package com.example.careandshare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Storerideinformation extends AppCompatActivity {

    DatabaseReference databaseref,availableridesdbref;
    FirebaseAuth mAuth;
    EditText origin, destination, fare, availability;
    ProgressDialog progressDialog;
    HashMap<String, Object> usermap;
    String originlongitude,originlatitude;
    String destinationlongitude,destinationlatitude;
    String longitude,latitude;
    Button savebtn;
    Switch rideavailableswitch;
    TextView addpicture;
    ArrayList<Uri> FileList = new ArrayList<Uri>();

    Button btpickerorigin,btpickerdest;
    int PLACE_PICKER_REQUEST=0;
    int ORIGIN_PICKER_REQUEST=0;
    int DESTINATION_PICKER_REQUEST=0;
    int PICK_FILE =0,RESULT_LOAD_IMAGE=1;
    String originaddress;
    String destinationaddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storerideinformation);

        mAuth = FirebaseAuth.getInstance();
        origin = findViewById(R.id.origin);
        btpickerorigin=findViewById(R.id.pickerorigin);
        btpickerdest=findViewById(R.id.pickerdestination);
        destination = findViewById(R.id.destination);
        fare = findViewById(R.id.fare);
        availability = findViewById(R.id.seatavailability);
        addpicture=findViewById(R.id.change_picture_btn);
        savebtn=findViewById(R.id.savebtnend);
        rideavailableswitch=findViewById(R.id.switchride);
        databaseref = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers");
        availableridesdbref=FirebaseDatabase.getInstance().getReference().child("Available Rides");





















        progressDialog  = new ProgressDialog(this);
        progressDialog.setMessage("Processing Please wait....");


        addpicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                PICK_FILE=1;
                startActivityForResult(Intent.createChooser(intent, "Select Pictures"), RESULT_LOAD_IMAGE);
            }
        });





        availableridesdbref.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount()>0){
                    rideavailableswitch.setChecked(true);
                }else{
                    rideavailableswitch.setChecked(false);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        databaseref.child(mAuth.getCurrentUser().getUid()).child("Ride").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getChildrenCount()>0) {
                    String origin1 = dataSnapshot.child("Origin").getValue().toString();
                    String dest = dataSnapshot.child("Destination").getValue().toString();
                    String originl=dataSnapshot.child("Origin longitude").getValue().toString();
                    String originl2=dataSnapshot.child("Origin latitude").getValue().toString();
                    String destinationl=dataSnapshot.child("Destination longitude").getValue().toString();
                    String destinationl2=dataSnapshot.child("Destination latitude").getValue().toString();

                    originlongitude=originl;
                    originlatitude=originl2;
                    destinationlongitude=destinationl;
                    destinationlatitude=destinationl2;




                    originaddress = origin1;
                    destinationaddress = dest;

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        databaseref.child(mAuth.getCurrentUser().getUid()).child("Ride").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount()>0){
                    String destination1 = dataSnapshot.child("Destination").getValue().toString();
                    String origin1 = dataSnapshot.child("Origin").getValue().toString();
                    String fare1 = dataSnapshot.child("Fare").getValue().toString();
                    String availability1 = dataSnapshot.child("Seats Availability").getValue().toString();

                    origin.setText(origin1);
                    destination.setText(destination1);
                    fare.setText(fare1);
                    availability.setText(availability1);







                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });













  savebtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
          validatecontrollers();


          if (FileList.size()>0)
                progressDialog.show();

              for (int j=0; j<FileList.size();j++){

                  Uri perFile = FileList.get(j);
                  StorageReference Folder = FirebaseStorage.getInstance().getReference().child("Car Pictures").child(mAuth.getCurrentUser().getUid()).child("CAR");
                  StorageReference filename = Folder.child("file"+perFile.getLastPathSegment());

                  filename.putFile(perFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                      @Override
                      public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                          filename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                              @Override
                              public void onSuccess(Uri uri) {
                                  DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(mAuth.getCurrentUser().getUid())
                                         .child("PICTURES");

                                  HashMap<String, String> hashMap = new HashMap<>();
                                  hashMap.put("Link",String.valueOf(uri));

                                  dbref.push().setValue(hashMap);;

                                   progressDialog.dismiss();
                                  FileList.clear();



                              }
                          });
                      }

                  });


              }




          validatecontrollers();


      }
  });



        btpickerorigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PlacePicker.IntentBuilder builder =  new PlacePicker.IntentBuilder();
                try {ORIGIN_PICKER_REQUEST=1;
                    startActivityForResult(builder.build(Storerideinformation.this),
                            ORIGIN_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });


        btpickerdest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PlacePicker.IntentBuilder builder =  new PlacePicker.IntentBuilder();
                try {DESTINATION_PICKER_REQUEST=1;
                    startActivityForResult(builder.build(Storerideinformation.this),
                            DESTINATION_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });






checkAvailability();




    }

    private void checkAvailability() {
        rideavailableswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked==true){

                    validatecontrollers();
                    Toast.makeText(Storerideinformation.this, "CHECKED", Toast.LENGTH_SHORT).show();

                }else{

                    Toast.makeText(Storerideinformation.this, "UNCHECKED", Toast.LENGTH_SHORT).show();
                    availableridesdbref.child(mAuth.getCurrentUser().getUid()).getRef().removeValue();

                }
            }
        });

    }










    private void validatecontrollers() {


        if (originaddress== null) {

            Toast.makeText(this, "Please provide Origin Address", Toast.LENGTH_SHORT).show();
            return;
        } else if (destinationaddress==null) {
            Toast.makeText(this, "Please Enter your destination", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(fare.getText().toString())) {
            fare.setError("Please provide Fare");

            return;
        } else if (TextUtils.isEmpty(availability.getText().toString())) {
                  availability.setError("Please Provide Availability");
            return;
        } else
        if (Integer.parseInt(availability.getText().toString())>=4) {
            availability.setError("Please provide availability less than 4");
            return;
        }
        else





            {


            usermap = new HashMap<>();


            usermap.put("Fare", fare.getText().toString());
            usermap.put("Seats Availability", availability.getText().toString());
            usermap.put("Origin",originaddress);
            usermap.put("Destination",destinationaddress);
                usermap.put("Origin longitude",originlongitude);
                usermap.put("Origin latitude",originlatitude);
                usermap.put("Destination longitude",destinationlongitude);
                usermap.put("Destination latitude",destinationlatitude);


           databaseref.child(mAuth.getCurrentUser().getUid()).child("Ride").updateChildren(usermap);


            if (rideavailableswitch.isChecked()) {


                usermap.put("UID", mAuth.getCurrentUser().getUid());


                availableridesdbref.child(mAuth.getCurrentUser().getUid()).updateChildren(usermap);

            }


        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE){
            PICK_FILE=0;

            if (resultCode == RESULT_OK){
                if (data.getClipData() !=null){
                    Toast.makeText(this, "pick file", Toast.LENGTH_SHORT).show();
                    int count = data.getClipData().getItemCount();
                    int i = 0;

                    while (i<count){

                        Uri File = data.getClipData().getItemAt(i).getUri();
                        FileList.add(File);
                        i++;

                    }



                }



            }
        }
        if(requestCode==ORIGIN_PICKER_REQUEST) {
            ORIGIN_PICKER_REQUEST=0;

            if (resultCode == RESULT_OK) {

                Place place = PlacePicker.getPlace(data, this);
                originaddress =getcompleteaddress(place.getLatLng().latitude,place.getLatLng().longitude);
                origin.setText(originaddress);

                originlongitude=String.valueOf(place.getLatLng().longitude);
                originlatitude=String.valueOf(place.getLatLng().latitude);
                Toast.makeText(this, "Origin: "+originaddress, Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode==DESTINATION_PICKER_REQUEST) {
            DESTINATION_PICKER_REQUEST=0;
            if (resultCode == RESULT_OK) {

                Place place = PlacePicker.getPlace(data, this);
                destinationaddress =getcompleteaddress(place.getLatLng().latitude,place.getLatLng().longitude);
                destination.setText(destinationaddress);
                destinationlongitude=String.valueOf(place.getLatLng().longitude);
                destinationlatitude=String.valueOf(place.getLatLng().latitude);


                Toast.makeText(this, "Destination: "+destinationaddress, Toast.LENGTH_SHORT).show();
            }
        }






    }




    private String getcompleteaddress(double latitude,double longitude){

        String address="";
        Geocoder geocoder = new Geocoder(Storerideinformation.this, Locale.getDefault());

        try{
            List<Address> addresses= geocoder.getFromLocation(latitude,longitude,1);

            if(address!=null){
                Address returnAddress = addresses.get(0);
                StringBuilder stringBuilderreturnaddress = new StringBuilder("");

                for (int i=0;i<=returnAddress.getMaxAddressLineIndex();i++){

                    stringBuilderreturnaddress.append(returnAddress.getAddressLine(i)).append("\n");

                }
                address= stringBuilderreturnaddress.toString();


            }else {
                Toast.makeText(this, "Address not founddd !!!", Toast.LENGTH_SHORT).show();
            }

        }
        catch(Exception e){

            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }



        return address;   }




}