package com.example.careandshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

public class availablerooms extends AppCompatActivity {
    ArrayList<User> listusers = new ArrayList<>();
    DatabaseReference ref;
    private RecyclerView list;
    EditText txt_search;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availablerooms);
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

        ref = FirebaseDatabase.getInstance().getReference().child("Available Rooms");
        list= findViewById(R.id.list);

        list.setLayoutManager(new LinearLayoutManager(this));


        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    for(DataSnapshot ds: snapshot.getChildren()){

                        Toast.makeText(availablerooms.this, ds.child("UID").getValue().toString(), Toast.LENGTH_SHORT).show();

                        User user =new User(ds.child("Address").getValue(String.class),
                                            ds.child("Availability").getValue(String.class),
                                            ds.child("City").getValue().toString(),
                                            ds.child("Price").getValue().toString(),
                                            ds.child("Room Description").getValue().toString(),
                                            ds.child("Room Type").getValue().toString(),
                                            ds.child("UID").getValue().toString());
                        listusers.add(user);

                     }

                    MyAdapter adapter =  new MyAdapter(listusers);
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

ArrayList<User> filteredlist = new ArrayList<>();

for (User item:listusers){

    if (item.getaddress().toLowerCase().contains(text.toLowerCase())){

        filteredlist.add(item);
    }
}
        MyAdapter adapter =  new MyAdapter(filteredlist);
        list.setAdapter(adapter);



    }

}