package com.example.careandshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Signupasvehicleowner extends AppCompatActivity {
    EditText mFullName,mEmail,mPassword,mPhone,confirmassword;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;

    String userID;

    private DatabaseReference Driverdbref,databaseReference;
    private String onlineDriverID;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupasvehicleowner);

        mFullName = findViewById(R.id.nameofvehicleowner);
        mEmail = findViewById(R.id.Emailofvehicleowner);
        mPassword = findViewById(R.id.passwordofvehicleowner);
        mPhone = findViewById(R.id.Mobilephoneofvehicleowner);
        confirmassword = findViewById(R.id.confirmPasswordofvehicleowner);
        mRegisterBtn = findViewById(R.id.signupbuttonofvehicleowner);
        mLoginBtn = findViewById(R.id.logintextfromvehicleowner);
        fAuth= FirebaseAuth.getInstance();
        fstore= FirebaseFirestore.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers");



        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String fullname = mFullName.getText().toString();
                final String email = mEmail.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();
                final String phone = mPhone.getText().toString().trim();
                final String confirmpassword  = confirmassword.getText().toString().trim();


                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Please enter your email address ");
                      return;

                }
                if (TextUtils.isEmpty(fullname)){
                    mFullName.setError("Please enter your Full Name ");
                    return;


                }
                if (TextUtils.isEmpty(phone)){
                    mPhone.setError("Enter your phone number");
                    return;

                }



                if (TextUtils.isEmpty(password)){
                    mPassword.setError("Enter your password");
                    return;

                }

                if(password.length()<6 ){

                    mPassword.setError("Password must be more than six characters");

                    return;

                }
                if (!confirmpassword.equals(password)){
                    confirmassword.setError("Passwords do not match");
                    return;

                }


                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){



                            onlineDriverID = fAuth.getCurrentUser().getUid();
                            Driverdbref = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(onlineDriverID);
                            Driverdbref.setValue(true);
                            validateandsaveonlyinfo();
                            startActivity (new Intent(getApplicationContext(),vehicle_owner_firstpage.class));
                            finish();












                            /*
                            Toast.makeText(Signupasvehicleowner.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fstore.collection("Veicle Owner").document(userID);
                            Map<String,Object> Vehicle_Owner = new HashMap<>();
                            Vehicle_Owner.put("Full Name",fullname);
                            Vehicle_Owner.put("Email",email);
                            Vehicle_Owner.put("Phone Number",phone);
                            documentReference.set(Vehicle_Owner).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG","on success: user profile is created for "+userID);
                                }
                            });  



                            startActivity (new Intent(getApplicationContext(),vehicle_owner_firstpage.class));
                            finish();
                       */ }

                        else{
                            Toast.makeText(Signupasvehicleowner.this, "Error!"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });




            }
        });
    }

    private void validateandsaveonlyinfo() {

int wallet=0;
        if (TextUtils.isEmpty(mFullName.getText().toString())) {
            Toast.makeText(this, "Please provide your name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(mPhone.getText().toString())) {
            Toast.makeText(this, "Please provide your Phone Number", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, Object> usermap = new HashMap<>();
            usermap.put("uid", fAuth.getCurrentUser().getUid());
            usermap.put("Name", mFullName.getText().toString());
            usermap.put("Phone", mPhone.getText().toString());
            usermap.put("Email", mEmail.getText().toString());
            usermap.put("Password", mPassword.getText().toString());
            usermap.put("Wallet", String.valueOf(wallet));
            databaseReference.child(fAuth.getCurrentUser().getUid()).updateChildren(usermap);


        }
    }







}