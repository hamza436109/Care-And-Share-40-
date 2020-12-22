package com.example.careandshare;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private ArrayList<User> mDataSet;

    private Context context;


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
       TextView address,availability,city,price,roomdescription,roomtype,uid;
       RelativeLayout parentLayout;


        public ViewHolder(View v) {
            super(v);
            address=(TextView)v.findViewById(R.id.address1);
            availability=(TextView)v.findViewById(R.id.availablity1);
           // city=(TextView)v.findViewById(R.id.city1);
            price=(TextView)v.findViewById(R.id.price);
            roomdescription=(TextView)v.findViewById(R.id.roomdescription);
            roomtype=(TextView)v.findViewById(R.id.roomtype);
            parentLayout=(RelativeLayout)v.findViewById(R.id.relativelayout);
           // uid=(TextView)v.findViewById(R.id.uid);






        }



    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @ring[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public MyAdapter(ArrayList<User> mdataSet)
    {
        this.mDataSet = mdataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater= LayoutInflater.from(parent.getContext());
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.row_style, parent, false);

        ViewHolder view= new ViewHolder(v);
        context=parent.getContext();

        return view;

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
       User user = mDataSet.get(position);

       holder.address.setText(user.getaddress());
       holder.price.setText(user.getPrice());
       holder.roomtype.setText(user.getRoomtype());
     //  holder.city.setText(user.getCity());
     //  holder.uid.setText(user.getUid());

       holder.roomdescription.setText(user.getRoomdescription());
       holder.availability.setText(user.getAvailability());
       holder.parentLayout.setOnClickListener(new View.OnClickListener() {


           @Override
           public void onClick(View view) {
              Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
             //  Toast.makeText(context, user.getUid(), Toast.LENGTH_SHORT).show();
             Intent intent =new Intent(context,bookroom.class);
             intent.putExtra("UID",user.getUid());
             intent.putExtra("Room Description",user.getRoomdescription());
             intent.putExtra("username",user.getUid());
             intent.putExtra("availability",user.getAvailability());
             intent.putExtra("Price",user.getPrice());
             intent.putExtra("Address",user.getaddress());
             intent.putExtra("Room number",String.valueOf(position+1));
             intent.putExtra("type","room");


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

