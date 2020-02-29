package com.example.admin.mysvvvappbeta;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Asad Mirza on 04-02-2018.
 */

public class MyApp extends android.app.Application
{
        @Override
        public void onCreate() {
            super.onCreate();
    /* Enable disk persistence  */
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }}