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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class signupasroomowner extends AppCompatActivity {
    EditText mFullName,mEmail,mPassword,mPhone,confirmassword;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String userID;
    private ProgressDialog loadingbar;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupasroomowner);

        mFullName = findViewById(R.id.editTextTextPersonName);
        mEmail = findViewById(R.id.editTextTextPersonName2);
        mPassword = findViewById(R.id.editTextTextPassword);
        mPhone = findViewById(R.id.editTextPhone);
        confirmassword = findViewById(R.id.editTextTextPassword2);
        mRegisterBtn = findViewById(R.id.button2);
        mLoginBtn = findViewById(R.id.logintextfromroomowner);
        fAuth= FirebaseAuth.getInstance();
        loadingbar  = new ProgressDialog(this);
        fstore= FirebaseFirestore.getInstance();




        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(signupasroomowner.this,Login.class));
            }
        });



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


                }
                if (TextUtils.isEmpty(fullname)){
                    mFullName.setError("Please enter your Full Name ");


                }
                if (TextUtils.isEmpty(phone)){
                    mPhone.setError("Enter your phone number");

                }



                if (TextUtils.isEmpty(password)){
                    mPassword.setError("Enter your password");

                }

                if(password.length()<6 ){

                    mPassword.setError("Password must be more than six characters");

                    return;

                }
                if (!confirmpassword.equals(password)){
                    confirmassword.setError("Passwords do not match");
                    return;

                }

loadingbar.setTitle("Room Owner Registration");
                loadingbar.setMessage("Please Wait, while registration is completed...");
                loadingbar.show();

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(signupasroomowner.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fstore.collection("Room Owners").document(userID);
                            Map<String,Object> Room_Owners= new HashMap<>();
                            Room_Owners.put("Full Name",fullname);
                            Room_Owners.put("Email",email);
                            Room_Owners.put("Phone Number",phone);
                            documentReference.set(Room_Owners).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG","on success: user profile is created for "+userID);
                                    loadingbar.dismiss();;
                                }
                            });



                            startActivity (new Intent(getApplicationContext(),Room_owner_firstpage.class));
                            finish();
                        }

                        else{
                            Toast.makeText(signupasroomowner.this, "Error!"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();;
                        }
                    }
                });




            }
        });





















    }
}