package com.example.careandshare.firebaseUtils;


import android.app.Activity;
import androidx.annotation.NonNull;
import com.example.careandshare.CallbackInterfaces.PostCallback;
import com.example.careandshare.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FirebaseMethods {

    private static final String TAG = "FirebaseMethods";
    private Activity mActivity;
    private String userID;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private long mediaCount = 0;

    public FirebaseMethods(Activity activity) {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        mActivity = activity;
        if (mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
        }
    }


    public void adPath(ArrayList<LatLng> path, PostCallback callback) {

        String id = myRef.child(mActivity.getString(R.string.paths))
                .push().getKey();
        myRef.child(mActivity.getString(R.string.paths))
                .child(id)
                .setValue(path).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        callback.success(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.success(false);
                    }
                });
    }

}






