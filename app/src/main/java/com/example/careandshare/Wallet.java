package com.example.careandshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Wallet extends AppCompatActivity {
    TextView amount;
    private DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    private String gettype;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        amount = findViewById(R.id.Amount);
        mAuth = FirebaseAuth.getInstance();
        gettype= getIntent().getStringExtra("type");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(gettype);






       getwalletdetails();

    }
    private void getwalletdetails(){

        databaseReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){

                    String wallet = dataSnapshot.child("Wallet").getValue().toString();
                     int price  = Integer.parseInt(wallet);


                    amount.setText(String.valueOf(price));
                    if (price>0) {
                        amount.setTextColor(Color.parseColor("#44ad02"));
                    }
                    else
                    if (price<0){
                        amount.setTextColor(Color.parseColor("#ff0d05"));
                    }
                    else {

                        amount.setTextColor(Color.parseColor("#000000"));
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });;



}
}