package com.example.careandshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class selectusertype extends AppCompatActivity {
TextView gotologin;
Button  vehicle_owner;
Button room_owner;;
Button customer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectusertype);




                  gotologin= findViewById(R.id.textView7);
                  vehicle_owner= findViewById(R.id.Vehicleowner);
                  room_owner= findViewById(R.id.roomowner);
                  customer = findViewById(R.id.customer);

           customer.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   signupascustomer();
               }
           });


           room_owner.setOnClickListener(new View.OnClickListener() {
             @Override
                public void onClick(View view) {
                signupasroomowner();
               }
           });




         vehicle_owner.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 vehicleownersignup();
             }
         });
         gotologin.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 openlogin();
             }
         });



    }


    //METHODS TO GOTO CLASSES TO CREATE ACCOUNT


    public void signupasroomowner(){
        Intent signupasroomowner = new Intent(this, signupasroomowner.class);
        startActivity(signupasroomowner);
    }

    public void openlogin() {

    Intent intent= new Intent(selectusertype.this, Login.class);
    startActivity(intent);

    }

    public void vehicleownersignup(){
    Intent signupasvehicleowner = new Intent(this, Signupasvehicleowner.class);

        startActivity(signupasvehicleowner);


    }
    public void signupascustomer(){
        Intent customerclass = new Intent(this,signupascustomer.class);

        startActivity(customerclass);
    }




}