package com.example.careandshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class selectcustomerordriver extends AppCompatActivity {
Button driver,customer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectcustomerordriver);

        driver=findViewById(R.id.button3);
        customer=findViewById(R.id.button4);



        driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(selectcustomerordriver.this,vehicle_owner_firstpage.class));
            }
        });



        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(selectcustomerordriver.this,Customer_Firstpage.class));
            }
        });

    }
}