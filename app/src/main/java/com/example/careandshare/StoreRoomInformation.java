package com.example.careandshare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.google.firestore.admin.v1beta1.Progress;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class StoreRoomInformation extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final int RESULT_LOAD_IMAGE = 1;
    ProgressDialog progressDialog;
    private static int PICK_FILE =0 ;

    private ImageView  savebutton,deleteroom;
    private EditText roomdescription, price, City, roomtype,bedspace,locationaddress;
    private TextView addpictures;
    private Button savebtnend;
    String text ="Islamabad";
    Switch roomavailableswitch;
    HashMap<String, Object> usermap;
    String longitude,latitude;



    private DatabaseReference databaseReference,availableroomsdbref;
    private FirebaseAuth mAuth;

    private String getroomnumber;

    private TextView mSelectButton;

    ArrayList<Uri> FileList = new ArrayList<Uri>();


    // place pickerr
    Button btpicker;

    int PLACE_PICKER_REQUEST=0;
    String address;
    int roomnumber =1;


     @SuppressLint("WrongViewCast")
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_room_information);

        savebutton = findViewById(R.id.savebtn);
        roomdescription = findViewById(R.id.Room_Description);
        price = findViewById(R.id.price);
        City = findViewById(R.id.city);
        roomtype = findViewById(R.id.roomtype);
        savebtnend = findViewById(R.id.btnend);
        bedspace=findViewById(R.id.Numberofcustomers);
        btpicker=findViewById(R.id.picker);
        locationaddress=findViewById(R.id.location_Address);
        getroomnumber = getIntent().getStringExtra("Number");
        roomavailableswitch= findViewById(R.id.switch1);
        roomavailableswitch.setTextOff("Unavailable");
        deleteroom=findViewById(R.id.deleteroombtn);



         mAuth = FirebaseAuth.getInstance();
         databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Room Owners");
         availableroomsdbref=FirebaseDatabase.getInstance().getReference().child("Available Rooms");


         availableroomsdbref.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 if (dataSnapshot.getChildrenCount()>0){
                     roomavailableswitch.setChecked(true);
                 }else{
                     roomavailableswitch.setChecked(false);
                 }

             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });










        // uploading multiple  pictures testin

        mSelectButton = findViewById(R.id.change_picture_btn);






        progressDialog  = new ProgressDialog(this);
        progressDialog.setMessage("Processing Please wait....");

        Spinner spinner  = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.numbers,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


         btpicker.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 PlacePicker.IntentBuilder builder =  new PlacePicker.IntentBuilder();
                 try {PLACE_PICKER_REQUEST=1;;
                     startActivityForResult(builder.build(StoreRoomInformation.this),
                             PLACE_PICKER_REQUEST);

                 } catch (GooglePlayServicesRepairableException e) {
                     e.printStackTrace();
                 } catch (GooglePlayServicesNotAvailableException e) {
                     e.printStackTrace();
                 }
             }
         });


         databaseReference.child(mAuth.getCurrentUser().getUid())
                 .child(getroomnumber).addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 if (dataSnapshot.child("Address").exists()) {
                     DataSnapshot data = dataSnapshot.child("Address");
                     address = data.getValue().toString();
                     Toast.makeText(StoreRoomInformation.this, address, Toast.LENGTH_SHORT).show();
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });

deleteroom.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(StoreRoomInformation.this);

        builder.setMessage("Are you sure you want to delete this room?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(StoreRoomInformation.this, "FINIH", Toast.LENGTH_SHORT).show();
                        databaseReference.child(mAuth.getCurrentUser().getUid()).child(getroomnumber).getRef().removeValue();
                        availableroomsdbref.child(mAuth.getCurrentUser().getUid()).child(getroomnumber).getRef().removeValue();
                        startActivity(new Intent(StoreRoomInformation.this,MyRooms.class));
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


     mSelectButton.setOnClickListener(new View.OnClickListener() {

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


        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StoreRoomInformation.this, MyRooms.class));
            }
        });
         checkAvailability();
        getuserinformaition();







    }

    private void checkAvailability() {
        roomavailableswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked==true){

                   validatecontrollers();
                  Toast.makeText(StoreRoomInformation.this, "CHECKED", Toast.LENGTH_SHORT).show();
                   getavailableroom();
                }else{

                    Toast.makeText(StoreRoomInformation.this, "UNCHECKED", Toast.LENGTH_SHORT).show();
                    availableroomsdbref.child(mAuth.getCurrentUser().getUid()).getRef().removeValue();
                }
            }
        });

    }

    private void getuserinformaition() {


        databaseReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists() && dataSnapshot.child(getroomnumber).getChildrenCount() > 1) {

                    String description = dataSnapshot.child(getroomnumber).child("Room Description").getValue().toString();
                    String price1 = dataSnapshot.child(getroomnumber).child("Price").getValue().toString();
                    String city = dataSnapshot.child(getroomnumber).child("City").getValue().toString();
                    String room_type = dataSnapshot.child(getroomnumber).child("Room Type").getValue().toString();
                    String Availability = dataSnapshot.child(getroomnumber).child("Availability").getValue().toString();
                  if (dataSnapshot.child(getroomnumber).child("Address").exists()){
                        String addresss=dataSnapshot.child(getroomnumber).child("Address").getValue().toString();
                        locationaddress.setText(addresss);
                        longitude=dataSnapshot.child(getroomnumber).child("Longitude").getValue().toString();
                        latitude=dataSnapshot.child(getroomnumber).child("latitude").getValue().toString();

                    }



                   roomdescription.setText(description);
                    price.setText(price1);
                    City.setText(city);
                    roomtype.setText(room_type);
                    bedspace.setText(Availability);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }

    private void validatecontrollers() {




        if (TextUtils.isEmpty(roomdescription.getText().toString())) {
            roomdescription.setError("Please Provide Room Description");

            return;
        } else if (TextUtils.isEmpty(City.getText().toString())) {
            City.setError("Please Enter your City");

            return;
        } else if (TextUtils.isEmpty(price.getText().toString())) {
            price.setError("Please provide Price");

            return;
        } else if (TextUtils.isEmpty(roomtype.getText().toString())) {
            roomtype.setError("Select type of Room");

            return;
        }else if (TextUtils.isEmpty(bedspace.getText().toString())) {
            bedspace.setError("Select Availability");

            return;
        }else
            if (Integer.parseInt(bedspace.getText().toString())>4){
                bedspace.setError("Sorry,You cannot add more than 4 bed spaces");

                return;
            }




            {


            usermap = new HashMap<>();

            usermap.put("Room Description", roomdescription.getText().toString());
            usermap.put("City", text);
            usermap.put("Price", price.getText().toString());
            usermap.put("Room Type", roomtype.getText().toString());
            usermap.put("Availability", bedspace.getText().toString());
            usermap.put("Address", address);
            usermap.put("Longitude", longitude);
            usermap.put("latitude", latitude);

            databaseReference.child(mAuth.getCurrentUser().getUid()).child(getroomnumber).updateChildren(usermap);

            if (roomavailableswitch.isChecked()) {
                usermap.put("UID", mAuth.getCurrentUser().getUid());


                availableroomsdbref.child(mAuth.getCurrentUser().getUid()).updateChildren(usermap);

            }
                checkAvailability();
              }


    }


    @Override
   protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

                      if (requestCode == PICK_FILE){
                          PICK_FILE=0;
                          Toast.makeText(this, "pick file", Toast.LENGTH_SHORT).show();
                          if (resultCode == RESULT_OK){
                            if (data.getClipData() !=null){

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
                      else if(requestCode==PLACE_PICKER_REQUEST) {

                              if (resultCode == RESULT_OK) {

                              Place place = PlacePicker.getPlace(data, this);
                              address =getcompleteaddress(place.getLatLng().latitude,place.getLatLng().longitude);
                              double longitu=place.getLatLng().longitude;
                              double latitu=place.getLatLng().latitude;

                              longitude=String.valueOf(longitu);
                              latitude=String.valueOf(latitu);



                          }




                      }
     }


    public void choosefiles(View view) {
        Intent intent  = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        startActivityForResult(intent,PICK_FILE);



    }




    public void uploadfiles(View view) {


         if (FileList.size()>0)
         progressDialog.show();

         for (int j=0; j<FileList.size();j++){

             Uri perFile = FileList.get(j);
             StorageReference Folder = FirebaseStorage.getInstance().getReference().child("Room Pictures").child(mAuth.getCurrentUser().getUid()).child(getroomnumber);
             StorageReference filename = Folder.child("file"+perFile.getLastPathSegment());

             filename.putFile(perFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                 @Override
                 public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                     filename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                         @Override
                         public void onSuccess(Uri uri) {
                             DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Users").child("Room Owners").child(mAuth.getCurrentUser().getUid())
                                     .child(getroomnumber).child("PICTURES");

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        text = parent.getItemAtPosition(position).toString();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    void getavailableroom(){


         FirebaseDatabase.getInstance().getReference().child("Available Rooms").addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long number = dataSnapshot.getChildrenCount();


             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });



    }



    private String getcompleteaddress(double latitude,double longitude){

         String address="";
        Geocoder geocoder = new Geocoder(StoreRoomInformation.this, Locale.getDefault());

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

