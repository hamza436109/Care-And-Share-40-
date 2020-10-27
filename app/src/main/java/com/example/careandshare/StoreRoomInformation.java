package com.example.careandshare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import de.hdodenhof.circleimageview.CircleImageView;

public class StoreRoomInformation extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE = 1;
    ProgressDialog progressDialog;
    private static final int PICK_FILE =1 ;
    private CircleImageView ProfileimageView;
    private ImageView closebutton, savebutton;
    private EditText roomdescription, price, City, roomtype;
    private TextView addpictures;

    private String checker = "", myUrl = "";
    private Uri imageuri;
    private StorageTask uploadtask;
    private StorageReference storageprofilepicsref;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private Button savebtnend;
    private String getroomnumber;

    private TextView mSelectButton;
    private RecyclerView mUploadList;
    int roomownersobjects = 0;
    private List<String> fileNameList;
    ArrayList<Uri> FileList = new ArrayList<Uri>();

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
        getroomnumber = getIntent().getStringExtra("Number");

        // uploading multiple  pictures testin

        mSelectButton = findViewById(R.id.change_picture_btn);



        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Room Owners");


        progressDialog  = new ProgressDialog(this);
        progressDialog.setMessage("Processing Please wait....");




     mSelectButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Pictures"), RESULT_LOAD_IMAGE);



            }

        });


        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StoreRoomInformation.this, MyRooms.class));
            }
        });


        getuserinformaition();
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


                    roomdescription.setText(description);
                    price.setText(price1);
                    City.setText(city);
                    roomtype.setText(room_type);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void validatecontrollers() {

        if (TextUtils.isEmpty(roomdescription.getText().toString())) {
            Toast.makeText(this, "Please provide Room Description", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(City.getText().toString())) {
            Toast.makeText(this, "Please Enter your City", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(price.getText().toString())) {
            Toast.makeText(this, "Please please provide price", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(roomtype.getText().toString())) {
            Toast.makeText(this, "Select Type of Room", Toast.LENGTH_SHORT).show();
        }


        HashMap<String, Object> usermap = new HashMap<>();

        usermap.put("Room Description", roomdescription.getText().toString());
        usermap.put("City", City.getText().toString());
        usermap.put("Price", price.getText().toString());
        usermap.put("Room Type", roomtype.getText().toString());

        databaseReference.child(mAuth.getCurrentUser().getUid()).child(getroomnumber).updateChildren(usermap);


    }


    @Override
   protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

                       if (requestCode == PICK_FILE){
                          if (resultCode == RESULT_OK){
                            if (data.getClipData() !=null){

                         int count = data.getClipData().getItemCount();
                         int i = 0;

                            while (i<count){

                            Uri File = data.getClipData().getItemAt(i).getUri();
                            FileList.add(File);
                            i++;

                           }

                            Toast.makeText(this, "you have selected "+FileList.size()+" Pictures", Toast.LENGTH_LONG).show();

                            }



                          }
                       }















     /*  if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {

           if (data.getClipData() != null)      {

         Toast.makeText(this, "Selected multiple files", Toast.LENGTH_SHORT).show();

       }

       else if (data.getData() != null) {


           Toast.makeText(this, "Selected single file", Toast.LENGTH_SHORT).show();

       }


        }
*/

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
}

