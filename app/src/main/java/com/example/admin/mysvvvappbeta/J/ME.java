package com.example.admin.mysvvvappbeta.J;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.admin.mysvvvappbeta.ADA.MyEvents_adapter;
import com.example.admin.mysvvvappbeta.O.PDA;
import com.example.admin.mysvvvappbeta.R;
import com.example.admin.mysvvvappbeta.UI.sharedData;
import com.example.admin.mysvvvappbeta.models.post;
import com.example.admin.mysvvvappbeta.models.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ME extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference mrootref;
    user userOB;
    FirebaseUser firebaseUser;
   MyEvents_adapter myEvents_adapterOB;
   ArrayList<post> data ;
   ProgressBar  progressBar;
   TextView  textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
   progressBar = findViewById(R.id.progress_bar);
   textView = findViewById(R.id.textview);

   recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setVisibility(View.GONE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("My Events");

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        mrootref  = FirebaseDatabase.getInstance().getReference();
        sharedData sharedData_ob = new sharedData();
        userOB =sharedData_ob.getUserData(ME.this);
      firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
      data = new ArrayList<>();

      myEvents_adapterOB = new MyEvents_adapter(data,ME.this,userOB);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ME.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myEvents_adapterOB);
        check();


        mrootref.child(userOB.getCollege()).child("D").child("SE").child(firebaseUser.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                post p = dataSnapshot.getValue(post.class);
                data.add(p);
                myEvents_adapterOB.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                post p = dataSnapshot.getValue(post.class);
                for (int i=0 ; i<data.size();i++)
                {
                      if (data.get(i).getP_id().equals(p.getP_id())){
                          data.remove(i);
                          myEvents_adapterOB.notifyDataSetChanged();
                          break;

                      }

                  }

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
    finish();

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_event_menu_top, menu);


    return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle ♠clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
    if (id == R.id.add_icon){

        startActivity(new Intent(ME.this,PDA.class));

    }
    return  true;
    }



    public void check() {
        mrootref.child(userOB.getCollege()).child("D").child("SE").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                  progressBar.setVisibility(View.GONE);
                  recyclerView.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                  textView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
