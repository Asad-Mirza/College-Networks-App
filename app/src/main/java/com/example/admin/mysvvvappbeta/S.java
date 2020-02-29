package com.example.admin.mysvvvappbeta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.crashlytics.android.Crashlytics;
import com.example.admin.mysvvvappbeta.L.LGC;
import com.example.admin.mysvvvappbeta.L.V;
import com.example.admin.mysvvvappbeta.models.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.fabric.sdk.android.Fabric;

/**
 * Created by admin on 17-12-2017.
 */

public class S extends Activity{
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private static int SPLASH_TIME_OUT = 1500;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        startService(new Intent(this, MyFirebaseInstanceIdService.class));
        startService(new Intent(this, MyFirebaseMessagingService.class));

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        setContentView(R.layout.splash_layout);
        Fabric.with(this, new Crashlytics());
        final Intent intent;

        if(firebaseUser==null){


            intent= new Intent(this, LGC.class);

        }
        else {

            SharedPreferences pref= getSharedPreferences("v", Context.MODE_MULTI_PROCESS);
          boolean f = pref.getBoolean("f",false);
          if (f) intent = new Intent(S.this,V.class);
else
            //FirebaseDatabase.getInstance().getReference().keepSynced(true);


            intent = new Intent(this, MA.class);

        }

        new Handler().postDelayed(new Runnable() {



            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                startActivity(intent);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }


}
