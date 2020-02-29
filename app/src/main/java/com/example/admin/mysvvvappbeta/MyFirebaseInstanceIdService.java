package com.example.admin.mysvvvappbeta;


import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static android.content.ContentValues.TAG;

/**
 * Created by admin on 01-01-2018.
 */
public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mRootRef;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("XXXXXXX", "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server
        sendRegistrationToServer(refreshedToken);
    }


    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        firebaseDatabase = FirebaseDatabase.getInstance();
        mRootRef = firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        if (token!=null){

            if (firebaseUser!=null){

                String uid = firebaseUser.getUid();
                mRootRef.child("users").child(uid).child("token").setValue(token);


            }
        }

       else
            Log.d("YYYYYYY", "Refreshed token: " + token);

    }


    }

