package com.example.careandshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Availablerides extends AppCompatActivity {

    DatabaseReference ref;
    private RecyclerView list;

    ArrayList<userdrivers> listusers = new ArrayList<>();
    EditText txt_search;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availablerides);
        txt_search=findViewById(R.id.txt_search_txt);


        txt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());

            }
        });


        ref = FirebaseDatabase.getInstance().getReference().child("Available Rides");
        list= findViewById(R.id.listrides);
        list.setLayoutManager(new LinearLayoutManager(this));


        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    for(DataSnapshot ds: snapshot.getChildren()){



                       userdrivers user =new userdrivers(ds.child("Destination").getValue(String.class),
                                ds.child("Fare").getValue(String.class),
                                ds.child("Origin").getValue().toString(),
                                ds.child("Seats Availability").getValue().toString(),
                                ds.child("UID").getValue().toString()
                                );
                        listusers.add(user);

                    }

                    Ridesadapter adapter =  new Ridesadapter(listusers);
                    list.setAdapter(adapter);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ref.addListenerForSingleValueEvent(eventListener);


    }


    private void filter(String text){

        ArrayList<userdrivers> filteredlist = new ArrayList<>();

        for (userdrivers item:listusers){

            if (item.getDestination().toLowerCase().contains(text.toLowerCase())){

                filteredlist.add(item);
            }
            else if (item.getOrigin().toLowerCase().contains(text.toLowerCase())){

                filteredlist.add(item);
            }




        }
       Ridesadapter adapter =  new Ridesadapter(filteredlist);
        list.setAdapter(adapter);



    }

}