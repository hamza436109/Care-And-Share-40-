package com.example.careandshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class roominvoice extends AppCompatActivity {
    Button btn;
    String type;
    EditText feedback;
    String message;
    Button submit;
    FirebaseAuth mAuth;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roominvoice);

        mAuth=FirebaseAuth.getInstance();

        type=getIntent().getStringExtra("type");
        feedback=findViewById(R.id.feedback);
        submit=findViewById(R.id.submit12);

        Toast.makeText(this, type, Toast.LENGTH_SHORT).show();



       submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                FirebaseDatabase.getInstance().getReference().child("FeedBack").child(type).child(mAuth.getCurrentUser().getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                message=feedback.getText().toString();
                                HashMap<String,Object> usermap = new HashMap<>();
                                usermap.put("Feedback",message);

                                FirebaseDatabase.getInstance().getReference().child("FeedBack").child(type).child(mAuth.getCurrentUser().getUid())
                                .updateChildren(usermap);


                                AlertDialog alertDialog = new AlertDialog.Builder(roominvoice.this).create();
                                alertDialog.setTitle("Feedback Submitted");
                                alertDialog.setMessage("Thank you for your feedback ");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();



                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


            }
        });















    }
}