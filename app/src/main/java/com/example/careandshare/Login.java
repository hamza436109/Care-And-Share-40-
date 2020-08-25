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

public class Login extends AppCompatActivity {

    EditText mEmail,mPassword;
    Button mLoginBtn;
    TextView mCreateBtn;
    ProgressBar progressBar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


         mEmail = findViewById(R.id.Email);
         mPassword = findViewById(R.id.password);
         mLoginBtn = findViewById(R.id.loginBtn);
         mCreateBtn =findViewById(R.id.createText);
         fAuth  = FirebaseAuth.getInstance();

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View view) {
                startActivity (new Intent(Login.this,Register.class));
            }
        });





         mLoginBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
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


                               return;

                 }
                 progressBar.setVisibility(View.VISIBLE);

                 //Authenticate the user
                 fAuth.signInWithEmailAndPassword(email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this, "logged  In Successfully", Toast.LENGTH_SHORT).show();
                            startActivity (new Intent(Login.this,vehicle_owner_firstpage.class));
                            Log.e("success","successfullly registeredddddddd");
                         }

                        else{
                            Toast.makeText(Login.this, "Error!"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            startActivity (new Intent(Login.this,vehicle_owner_firstpage.class));
                            Log.e("success","not registerd ropererrr");
                        }
                     }
                 });






             }
         });













    }
}