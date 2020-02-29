package com.example.admin.mysvvvappbeta.T;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.admin.mysvvvappbeta.GlideApp;
import com.example.admin.mysvvvappbeta.MA;
import com.example.admin.mysvvvappbeta.R;
import com.example.admin.mysvvvappbeta.UI.sharedData;
import com.example.admin.mysvvvappbeta.models.user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TT extends AppCompatActivity {
    private ImageView imageView;
    private Button button;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mrootref;
    private user userObj;
    private TextView textView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        imageView = findViewById(R.id.timetable);
        progressBar = findViewById(R.id.progress_bar);
   textView=findViewById(R.id.text);

        //     button=findViewById(R.id.yeartable);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Time Table");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           finish();

            }
        });
        sharedData sharedData_ob = new sharedData();
        userObj = sharedData_ob.getUserData(TT.this);


        firebaseDatabase = FirebaseDatabase.getInstance();
        mrootref = firebaseDatabase.getReference();
        mrootref.child("TimeTable").child(userObj.getBranch()).child(userObj.getYear()).child(userObj.getSection()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    progressBar.setVisibility(View.GONE);
                    String image = dataSnapshot.getValue(String.class);
                    if (!TextUtils.isEmpty(image)) {
                        GlideApp.with(TT.this)
                                .load(image)
                                .into(imageView);
                    }

                } else {
                    textView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
