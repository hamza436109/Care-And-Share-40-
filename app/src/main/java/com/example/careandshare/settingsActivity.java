package com.example.careandshare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class settingsActivity extends AppCompatActivity {
    private CircleImageView ProfileimageView;
    private ImageView closebutton,savebutton;
    private EditText name,phone,drivercar;
    private TextView profilechangebtn;
    private String gettype;;
    private String  checker ="",myUrl="";
    private Uri imageuri;
    private StorageTask uploadtask;
    private StorageReference storageprofilepicsref;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        ProfileimageView= findViewById(R.id.profile_image);
        closebutton= findViewById(R.id.closebtn);
        savebutton= findViewById(R.id.savebtn);
        name= findViewById(R.id.name);
        phone= findViewById(R.id.phone_number);
        drivercar= findViewById(R.id.driver_car_name);
        profilechangebtn= findViewById(R.id.change_picture_btn);
        gettype= getIntent().getStringExtra("type");




        if (gettype.equals("Drivers"))
        {
            drivercar.setVisibility(View.VISIBLE);
        }

        Toast.makeText(this, gettype, Toast.LENGTH_SHORT).show();



        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(gettype);
        storageprofilepicsref = FirebaseStorage.getInstance().getReference().child("Profile Pictures ");


     closebutton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if(gettype.equals("Customers")) {
            startActivity(new Intent(settingsActivity.this, Customer_Firstpage.class));
        }
        else if(gettype.equals("Drivers")){
            startActivity(new Intent(settingsActivity.this, vehicle_owner_firstpage.class));
        }
        else {
            startActivity(new Intent(settingsActivity.this, Room_owner_firstpage.class));
        }
    }
});

     savebutton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

         if (checker.equals("clicked"))
         {
             validatecontrollers();;
         }
         else
             {
                 validateandsaveonlyinfo();
             }


    }});


profilechangebtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        checker="clicked";
        CropImage.activity().setAspectRatio(1,1).start(settingsActivity.this);
    }
});


getuserinformaition();

    }

    private void validateandsaveonlyinfo() {
        if (TextUtils.isEmpty(name.getText().toString())){
           name.setError("Please provide your name");
            return;
        }
        if (!isValid(phone.getText().toString())){
            phone.setError("Invalid Number");
            return;
        }
        if (TextUtils.isEmpty(phone.getText().toString())){
            phone.setError("Please provide your Phone Number");
            Toast.makeText(this, "Please provide your Phone Number", Toast.LENGTH_SHORT).show();
            return;
        }



        if (gettype.equals("Drivers") && TextUtils.isEmpty(drivercar.getText().toString())){
            drivercar.setError("Please provide your Car name");

            return;
        }

        else {
            HashMap<String,Object> usermap = new HashMap<>();
            usermap.put("uid",mAuth.getCurrentUser().getUid());
            usermap.put("Name",name.getText().toString());
            usermap.put("Phone",phone.getText().toString());


            if (gettype.equals("Drivers")){
                usermap.put("Car",drivercar.getText().toString());
                drivercar.setVisibility(View.VISIBLE);
            }


            databaseReference.child(mAuth.getCurrentUser().getUid()).updateChildren(usermap);

            if(gettype.equals("Drivers")) {

                startActivity(new Intent(settingsActivity.this, vehicle_owner_firstpage.class));
            }

            else if(gettype.equals("Customers")){

                startActivity(new Intent(settingsActivity.this, Customer_Firstpage.class));

            }
            else {

                startActivity(new Intent(settingsActivity.this, Room_owner_firstpage.class));
            }

        }
    }


    public static boolean isValid(String s)
    {



        Pattern p = Pattern.compile("[0][3][0-4][0-9][0-9]{7}");


        Matcher m = p.matcher(s);
        return (m.find() && m.group().equals(s));
    }
    private void getuserinformaition(){

 databaseReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){

            String name1 = dataSnapshot.child("Name").getValue().toString();
            String phone1 = dataSnapshot.child("Phone").getValue().toString();
            name.setText(name1);
            phone.setText(phone1);


            if(gettype.equals("Drivers")) {
              if (dataSnapshot.hasChild("Car")) {
                        String car = dataSnapshot.child("Car").getValue().toString();
                        drivercar.setText(car);
              }
            }

            if (dataSnapshot.hasChild("Image")){
                String image = dataSnapshot.child("Image").getValue().toString();
                Picasso.get().load(image).into(ProfileimageView);

            }
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});;




}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            CropImage.ActivityResult result =  CropImage.getActivityResult(data);
            imageuri  =result.getUri();
            ProfileimageView.setImageURI(imageuri);

        }
        else {

            if(gettype.equals("Customers")) {
                startActivity(new Intent(settingsActivity.this, Customer_Firstpage.class));
            }
            else if(gettype.equals("Drivers")){
                startActivity(new Intent(settingsActivity.this, vehicle_owner_firstpage.class));
            }
            else {
                startActivity(new Intent(settingsActivity.this, Room_owner_firstpage.class));
            }

        }

    }

    private void validatecontrollers(){


        if (TextUtils.isEmpty(name.getText().toString())){
            Toast.makeText(this, "Please provide your name", Toast.LENGTH_SHORT).show();
        }
            else
        if (TextUtils.isEmpty(phone.getText().toString())){
            Toast.makeText(this, "Please provide your Phone Number", Toast.LENGTH_SHORT).show();
        }

            else
        if (gettype.equals("Drivers") && TextUtils.isEmpty(drivercar.getText().toString())){
            Toast.makeText(this, "Please provide your Car name", Toast.LENGTH_SHORT).show();
        }
        else if (checker.equals("clicked"))
        {

             uploadprofilepicture();
        }







    }

    private void uploadprofilepicture() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
         progressDialog.setTitle("Settings Account Information");
         progressDialog.setMessage("Please wait, while your Account settings are updated...");
         progressDialog.show();
if (imageuri != null){

    final StorageReference  filref= storageprofilepicsref
            .child(mAuth.getCurrentUser().getUid()+".jpg");
    uploadtask = filref.putFile(imageuri);
    uploadtask.continueWithTask(new Continuation() {
        @Override
        public Object then(@NonNull Task task) throws Exception {

            if (!task.isSuccessful()){

                throw task.getException();
            }
            return filref.getDownloadUrl();
        }
    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
        @Override
        public void onComplete(@NonNull Task<Uri> task) {
              if (task.isSuccessful()){
                  Uri downloadurl = task.getResult();
                  myUrl = downloadurl.toString();

                  HashMap<String,Object> usermap = new HashMap<>();
                  usermap.put("uid",mAuth.getCurrentUser().getUid());
                  usermap.put("Name",name.getText().toString());
                  usermap.put("Phone",phone.getText().toString());
                  usermap.put("Image",myUrl);

                  if (gettype.equals("Drivers")){
                      usermap.put("Car",drivercar.getText().toString());
                  }


                  databaseReference.child(mAuth.getCurrentUser().getUid()).updateChildren(usermap);
                  progressDialog.dismiss();


                  if(gettype.equals("Drivers")) {
                      startActivity(new Intent(settingsActivity.this, vehicle_owner_firstpage.class));
                  }

                  else if (gettype.equals("Customers")){

                      startActivity(new Intent(settingsActivity.this, Customer_Firstpage.class));

                  }  else {

                      startActivity(new Intent(settingsActivity.this,Room_owner_firstpage.class));
                  }











              }
        }
    });

}
else {
    Toast.makeText(this, "please select image", Toast.LENGTH_SHORT).show();
}



    }

}