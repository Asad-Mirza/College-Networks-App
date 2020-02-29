package com.example.admin.mysvvvappbeta.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.admin.mysvvvappbeta.J.cd;
import com.example.admin.mysvvvappbeta.R;
import com.example.admin.mysvvvappbeta.models.notif_OBJ;
import com.example.admin.mysvvvappbeta.T.notif_adapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Asad Mirza on 02-01-2018.
 */

public class NF extends AppCompatActivity {
    private ArrayList<notif_OBJ> data;
    private notif_adapter notif_adapter_ob;
    private RecyclerView recyclerView;
    //private TextView textView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);
      //  textView = findViewById(R.id.textview);
       // textView.setVisibility(View.INVISIBLE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(cd.getResource("$S&t&y&n&k&n&h&f&y&n&t&s&x&%"));
        setSupportActionBar(toolbar);
        sharedData datao = new sharedData();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        long lastseen = datao.getLastSeenNotif(NF.this);
        long cur_time = (Calendar.getInstance().getTimeInMillis()) ;
        datao.setLastSeenNotif(cur_time,NF.this);


        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        data = new ArrayList<notif_OBJ>();
        notif_adapter_ob = new notif_adapter(data,NF.this,lastseen);
        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(notif_adapter_ob);
         String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("self_notifs").child(UID).child("notif_state").setValue("read");

        FirebaseDatabase.getInstance().getReference().child("self_notifs").child(UID).child("notif_list").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                notif_OBJ obj = dataSnapshot.getValue(notif_OBJ.class);
                data.add(obj);
                notif_adapter_ob.notifyDataSetChanged();
//                textView.setVisibility(View.GONE);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

       if (data.size()==0){

           // textView.setText("Your notifications will appear here");
            //textView.setVisibility(View.VISIBLE);
       }


    }

    @Override
    public void onBackPressed() {
    finish();
    }
}
