package com.example.admin.mysvvvappbeta.O;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.admin.mysvvvappbeta.R;
import com.example.admin.mysvvvappbeta.models.sectionPOJO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NU extends AppCompatActivity {
    private Intent intent;
    private Spinner bspinner, yspinner, cspinner,sspinner;
    private ProgressBar progressBar;
    private Button button;
    private ArrayList<String> year_array, branch_array, course_array,section_array;
    private boolean bflag = false, yflag = false, connected = false, cflag = false,sflag=false,vis=false;
    private String mbranch, myear, mcourse,msection,cid,clgname;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mrootref,b,c;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_noticeupload2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Add Section");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
     //   toolbar=findViewById(R.id.bar);
      //  toolbar.setTitle("Create Batch");
        //toolbar.setTitleTextColor(getResources().getColor(R.color.w));
     //   toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_navigation_arrow_back));
      //  setSupportActionBar(toolbar);
     /*   toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/





        progressBar=findViewById(R.id.mprogressbar);
        progressBar.setVisibility(View.INVISIBLE);
        final SharedPreferences sp=getSharedPreferences("USER_PROFILE",MODE_MULTI_PROCESS);
       // cid= sp.getString("cid",null);
        clgname=sp.getString("college",null);
         cid=sp.getString("cid",null);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        mrootref=firebaseDatabase.getReference();

        b= mrootref.child("app_data")
                .child("college_structure")
                .child(cid)/////////////////////////???????????????????????
                .child("branches");
        c=mrootref.child("app_data")
                .child("college_structure")
                .child(cid)////////////////??????????????????????
                .child("courses");
        b.keepSynced(true);
        c.keepSynced(true);

        bspinner = findViewById(R.id.branch_spinner);
        cspinner = findViewById(R.id.course);
        sspinner=findViewById(R.id.section_spinner);
        yspinner = findViewById(R.id.year_spinner);
        button = findViewById(R.id.procee_button);

        section_array =new ArrayList<>();
        section_array.add("Select section");
        section_array.add("A");
        section_array.add("B");
        section_array.add("C");
        section_array.add("D");
        section_array.add("E");
        section_array.add("F");


        year_array = new ArrayList<String>();
        year_array.add("Select Year");
        year_array.add("First");
        year_array.add("Second");
        year_array.add("Third");
        year_array.add("Fourth");

        course_array=new ArrayList<>();
        course_array.add("Select course");
       /* course_array.add("BTECH");
        course_array.add("MTECH");
        course_array.add("BA");
        course_array.add("MBA");*/

        branch_array=new ArrayList<>();
        branch_array.add("Select branch");
      /*  branch_array.add("CSE");
        branch_array.add("IT");
        branch_array.add("EC");
        branch_array.add("EE");
        branch_array.add("ME");
        branch_array.add("CIVIL");
        branch_array.add("TEXTILE");
        branch_array.add("HR");
        branch_array.add("MARKETING");
        branch_array.add("FINANCE");
*/










        final ArrayAdapter<String> Cadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, course_array);

        final ArrayAdapter<String> Badapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, branch_array);



        b.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String data=dataSnapshot.getValue().toString();
                branch_array.add(data);
                Badapter.notifyDataSetChanged();
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

        c.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String data=dataSnapshot.getValue().toString();
                course_array.add(data);
                Cadapter.notifyDataSetChanged();
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

        cspinner.setAdapter(Cadapter);
        Cadapter.setDropDownViewResource(R.layout.spinner_item);

        cspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i !=0){

                    mcourse = adapterView.getItemAtPosition(i).toString();
                    cflag = true;
                }else{cflag=false;}
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Badapter.setDropDownViewResource(R.layout.spinner_item);
        bspinner.setAdapter(Badapter);
        bspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    bflag = true;
                    mbranch = adapterView.getItemAtPosition(i).toString();
                }else{bflag=false;}
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        final ArrayAdapter<String> Yadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, year_array);
        Yadapter.setDropDownViewResource(R.layout.spinner_item);
        yspinner.setAdapter(Yadapter);
        yspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    yflag = true;
                    myear = adapterView.getItemAtPosition(i).toString();

                }else{yflag=false;}
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }


        });

        final ArrayAdapter<String> Sadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, section_array);
        Sadapter.setDropDownViewResource(R.layout.spinner_item);
        sspinner.setAdapter(Sadapter);
        sspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    sflag = true;
                    msection = adapterView.getItemAtPosition(i).toString();
                }else{sflag=false;}
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        connecting();






        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connected) {
                    if (cflag ) {
                        if (bflag) {
                            if (yflag){
                                if (sflag) {

                                    String uid = firebaseUser.getUid();
                                    String key = mrootref.child("onlyforkey").push().getKey();
                                    final DatabaseReference sref = mrootref.child(clgname).child("V_Faculty").child(uid).child("sections").child(key);

                                    sectionPOJO c = new sectionPOJO();
                                    c.setBranch(mbranch);
                                    c.setCourse(mcourse);
                                    c.setSection(msection);
                                    c.setKey(key);
                                    c.setYear(myear);
                                    sref.setValue(c).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(NU.this, "Section Created", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }  //task
                                            else {
                                                Toast.makeText(NU.this, "Please Try again", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });

                                } else
                                    Toast.makeText(NU.this, "Select Section", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(NU.this, "Select Year", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(NU.this, "Select Branch", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(NU.this, "Select Course", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(NU.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }


    public void connecting(){   DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try{  connected = snapshot.getValue(Boolean.class);}
                catch (Exception e){
                    Log.d("connection","null pointer exception");
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");

            }
        });}



}
