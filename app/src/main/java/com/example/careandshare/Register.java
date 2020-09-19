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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
 EditText mFullName,mEmail,mPassword,mPhone;
 Button mRegisterBtn;
TextView mLoginBtn;
FirebaseAuth fAuth;
ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        mFullName = findViewById(R.id.fullName);
        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.password);
        mPhone = findViewById(R.id.phone);
        mRegisterBtn = findViewById(R.id.registerBtn);
        mLoginBtn = findViewById(R.id.createText);
        fAuth= FirebaseAuth.getInstance();
        progressBar= findViewById(R.id.progressBar);



        if(fAuth.getCurrentUser()!=null){
             //   startActivity (new Intent(getApplicationContext(),MainActivity.class));

            }
          mLoginBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        startActivity (new Intent(getApplicationContext(),Login.class));
    }
});





        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String Password = mPassword.getText().toString().trim();


                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Please enter your email address ");
                    return;

                }

                if (TextUtils.isEmpty(Password)){
                    mPassword.setError("Enter your password");
                    return;
                }

                if(Password.length()<6){

                    mPassword.setError("Password must be more than six characters");

                }



                progressBar.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                            startActivity (new Intent(getApplicationContext(),loggedin.class));
                          finish();
                        }

                        else{
                            Toast.makeText(Register.this, "Error!"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });




            }
        });











    }
}











