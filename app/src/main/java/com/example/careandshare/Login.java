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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.security.auth.login.LoginException;

public class Login extends AppCompatActivity {

    EditText mEmail,mPassword;
    Button mLoginBtn;
    TextView mCreateBtn;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.password);
        mLoginBtn = findViewById(R.id.loginBtn);
        mCreateBtn = findViewById(R.id.createText);
        mAuth = FirebaseAuth.getInstance();
        loadingbar  = new ProgressDialog(this);




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
                                    Toast.makeText(Login.this, "Authentication passed ",
                                            Toast.LENGTH_SHORT).show();
                                    startActivity (new Intent(Login.this,vehicle_owner_firstpage.class));
                                            loadingbar.dismiss();
                                  /*  FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);*/
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












    }
