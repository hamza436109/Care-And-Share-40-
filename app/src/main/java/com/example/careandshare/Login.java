package com.example.careandshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

public class

Login extends AppCompatActivity {

    EditText mEmail,mPassword;
    Button mLoginBtn;
    TextView mCreateBtn;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    private ProgressDialog loadingbar;
    DatabaseReference dbcustomer,dbdriver;
    String[] newArray;
    String usercategory="none";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        newArray = new String[500];

        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.password);
        mLoginBtn = findViewById(R.id.loginBtn);
        mCreateBtn = findViewById(R.id.createText);
        mAuth = FirebaseAuth.getInstance();
        loadingbar  = new ProgressDialog(this);
       dbcustomer =FirebaseDatabase.getInstance().getReference().child("Users").child("Customers");
       dbdriver=FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers");































        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingbar.setTitle("Login");
                loadingbar.setMessage("Please Wait, while Login is completed...");

               String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Please enter your email address ");


                }

                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Enter your password");


                }

                if (password.length() < 6) {


                    mPassword.setError("Password must be more than six characters");


                    return;

                }
                loadingbar.show();
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {


                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {



                                if (task.isSuccessful()) {

                                   Driversusercheck();
                                    roomowners();
                                    customercheck();

                                    loadingbar.dismiss();

                                } else {
                                    Toast.makeText(Login.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                            loadingbar.dismiss();
                                    // ...
                                }

                                // ...
                            }
                        });








            }
        });




        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, selectusertype.class));
            }
        });
        }

    void Driversusercheck() {


        FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {


                        int cout=0;
                        for (DataSnapshot data : dataSnapshot.getChildren()){
                            // String a  = data.child("uid").getValue().toString();



                            newArray[++cout] = data.getKey();


                        }
                        String ab = newArray[1];


                        for (int i=1;i<=cout;i++){
                            if (newArray[i].equals(mAuth.getCurrentUser().getUid())){
                                //   Toast.makeText(Login.this,newArray[i], Toast.LENGTH_SHORT).show();

                                usercategory="Drivers";
                                Toast.makeText(Login.this, "Drivers category", Toast.LENGTH_SHORT).show();
                                startActivity (new Intent(Login.this,vehicle_owner_firstpage.class));

                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




    }

    private void roomowners() {


        FirebaseDatabase.getInstance().getReference().child("Users").child("Room Owners")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {


                        int cout=0;
                        for (DataSnapshot data : dataSnapshot.getChildren()){
                            // String a  = data.child("uid").getValue().toString();



                            newArray[++cout] = data.getKey();


                        }
                        for (int i=1;i<=cout;i++){
                            if (newArray[i].equals(mAuth.getCurrentUser().getUid())){
                                //   Toast.makeText(Login.this,newArray[i], Toast.LENGTH_SHORT).show();

                                usercategory="Room Owners";
                                Toast.makeText(Login.this, "Room owners category", Toast.LENGTH_SHORT).show();
                                startActivity (new Intent(Login.this,Room_owner_firstpage.class));

                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });







    }


    private void customercheck(){


        FirebaseDatabase.getInstance().getReference().child("Users").child("Customers")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {


                        int cout=0;
                        for (DataSnapshot data : dataSnapshot.getChildren()){
                            // String a  = data.child("uid").getValue().toString();



                            newArray[++cout] = data.getKey();


                        }
                         for (int i=1;i<=cout;i++){
                            if (newArray[i].equals(mAuth.getCurrentUser().getUid())){
                                //   Toast.makeText(Login.this,newArray[i], Toast.LENGTH_SHORT).show();

                                usercategory="Customers";
                                Toast.makeText(Login.this, String.valueOf(cout), Toast.LENGTH_SHORT).show();
                             startActivity (new Intent(Login.this,Customer_Firstpage
                                    .class));

                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



    }


}
