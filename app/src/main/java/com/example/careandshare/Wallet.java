package com.example.careandshare;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

public class Wallet extends AppCompatActivity {
    TextView amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        amount = findViewById(R.id.Amount);


        int price = 75;


        amount.setText(String.valueOf(price));

        if (price>0) {
          amount.setTextColor(Color.parseColor("#44ad02"));
        }
        if (price<0){
            amount.setTextColor(Color.parseColor("#ff0d05"));
        }


    }



}