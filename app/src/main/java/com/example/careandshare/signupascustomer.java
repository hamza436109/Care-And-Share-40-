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

public class signupascustomer extends AppCompatActivity {
    EditText mFullName,mEmail,mPassword,mPhone,confirmassword;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String userID;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupascustomer);

        mFullName = findViewById(R.id.editTextTextPersonName);
        mEmail = findViewById(R.id.editTextTextPersonName2);
        mPassword = findViewById(R.id.editTextTextPassword);
        mPhone = findViewById(R.id.editTextPhone);
        confirmassword = findViewById(R.id.editTextTextPassword2);
        mRegisterBtn = findViewById(R.id.button2);
        mLoginBtn = findViewById(R.id.logintextfromcustomer);
        fAuth= FirebaseAuth.getInstance();
        fstore= FirebaseFirestore.getInstance();

mLoginBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        startActivity(new Intent(signupascustomer.this,Login.class));
    }
});

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name= mFullName.getText().toString().trim();
                final String email = mEmail.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();
                final String fullname = mFullName.getText().toString();
                final String phone = mPhone.getText().toString().trim();
                final  String confirmpassword = confirmassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Please enter your email address ");


                }
                if (TextUtils.isEmpty(name)){
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

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(signupascustomer.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                           userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fstore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("Full Name",fullname);
                            user.put("Email",email);
                            user.put("Phone Number",phone);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG","on success: user profile is created for "+userID);
                                }
                            });



                            startActivity (new Intent(getApplicationContext(),Customer_Firstpage.class));
                            finish();
                        }

                        else{
                            Toast.makeText(signupascustomer.this, "Error!"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });




            }
        });





















    }
}