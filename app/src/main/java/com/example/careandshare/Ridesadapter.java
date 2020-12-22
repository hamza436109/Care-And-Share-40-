package com.example.careandshare;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Ridesadapter extends RecyclerView.Adapter<Ridesadapter.ViewHolder>  {

    private ArrayList<userdrivers> mDataSet;

    private Context context;


    /**
     * Provide a reference to the type of views that you are using
     *      * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView origin,destination,fare,availability,uid;
        RelativeLayout parentLayout;


        public ViewHolder(View v) {
            super(v);

            origin=(TextView)v.findViewById(R.id.origin);
            destination=(TextView)v.findViewById(R.id.destination);
            fare=(TextView)v.findViewById(R.id.fare);
            availability=(TextView)v.findViewById(R.id.seatavailability);
            uid=(TextView)v.findViewById(R.id.uid2);
            parentLayout=(RelativeLayout)v.findViewById(R.id.relativelayoutrides);







        }



    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @ring[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public Ridesadapter(ArrayList<userdrivers> mdataSet)
    {
        this.mDataSet = mdataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public Ridesadapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater= LayoutInflater.from(parent.getContext());
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.rowstyleforrides, parent, false);

        Ridesadapter.ViewHolder view= new Ridesadapter.ViewHolder(v);
        context=parent.getContext();

        return view;

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(Ridesadapter.ViewHolder holder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        userdrivers user = mDataSet.get(position);

        holder.destination.setText(user.getDestination());
        holder.origin.setText(user.getOrigin());
        holder.fare.setText(user.getFare());
        holder.availability.setText(user.getSeeatsavailability());


        FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("Name").getValue().toString();
                holder.uid.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });








        holder.parentLayout.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
                Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
                //  Toast.makeText(context, user.getUid(), Toast.LENGTH_SHORT).show();
                Intent intent =new Intent(context,bookroom.class);
                intent.putExtra("Destination",user.getDestination());
                intent.putExtra("Origin",user.getOrigin());
                intent.putExtra("Fare",user.getFare());
                intent.putExtra("availability",user.seeatsavailability);
                intent.putExtra("UID",user.getUid());
                intent.putExtra("Ride number",String.valueOf(position+1));
                intent.putExtra("type","ride");
                context.startActivity(intent);

            }
        });


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}


