package com.example.admin.mysvvvappbeta.T;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.admin.mysvvvappbeta.J.NExceptionHandler;
import com.example.admin.mysvvvappbeta.O.FT;
import com.example.admin.mysvvvappbeta.R;
import com.example.admin.mysvvvappbeta.RTL;
import com.example.admin.mysvvvappbeta.models.sectionPOJO;
import com.example.admin.mysvvvappbeta.O.NU;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by CrYsIs on 29-01-2018.
 */

public class SC extends AppCompatActivity {
    private TextView textView;
    private RecyclerView recyclerView;
    private Intent intent;
    private ArrayList<sectionPOJO> data;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference, mrootref;

    private ChildEventListener childEventListener;
    private String clgname;
    private boolean connected = false;

    private ProgressBar progressBar;

    private sections_adapter sections_adapter_obj;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_section);
        Toolbar toolbar = findViewById(R.id.toolbar);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        intent = getIntent();
      final Boolean val= getIntent().getExtras().getBoolean("val",false);
      if (val) {
          toolbar.setTitle("Upload Time Table");
          toolbar.setNavigationOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  finish();

              }
          });
          setSupportActionBar(toolbar);

      }
      else
      {
          toolbar.setTitle("Notice Board");
          toolbar.setNavigationOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  finish();

              }
          });
          setSupportActionBar(toolbar);
      }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        connection();


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        SharedPreferences sp = getSharedPreferences("USER_PROFILE", MODE_MULTI_PROCESS);
        clgname = sp.getString("college", null);
        recyclerView = findViewById(R.id.recyclerview);
        textView = findViewById(R.id.add_section);
        progressBar = findViewById(R.id.progressbar);
        data = new ArrayList<>();
        sections_adapter_obj = new sections_adapter(data, SC.this);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //     linearLayoutManager.setReverseLayout(true);
        //   linearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(sections_adapter_obj);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(SC.this, NU.class);//
                startActivity(intent);
            }
        });

        mrootref = databaseReference.child(clgname).child("V_Faculty").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("sections");

        meth();

        recyclerView.addOnItemTouchListener(
                new RTL(SC.this, recyclerView, new RTL.OnItemClickListener() {

                    @Override
                    public void onItemClick(final View view, final int position) {

                        final sectionPOJO s = data.get(position);
                        if (val) {

                            intent = new Intent(SC.this, FT.class);
                            intent.putExtra("obj", s);
                            startActivity(intent);
                        }
                        else {
                            intent = new Intent(SC.this, NExceptionHandler.class);
                            intent.putExtra("obj", s);
                            startActivity(intent);
                        }

                    } //normal click


                    @Override
                    public void onLongItemClick(View view, final int position) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SC.this);
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage("Delete Section ?");


                        alertDialog.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });


                        alertDialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                sectionPOJO s = data.get(position);
                                databaseReference
                                        .child(clgname)
                                        .child("V_Faculty")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("sections")
                                        .child(s.getKey())
                                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                        /*    sections_adapter_obj.notifyItemRemoved(position);
                                            data.remove(position);
                                            sections_adapter_obj.notifyItemRangeChanged(position, data.size());*/
                                            Toast.makeText(SC.this, "Deleted", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(SC.this, "Try Again", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                                if (!connected) {
                                    Toast.makeText(SC.this, "you are offline", Toast.LENGTH_SHORT).show();
                                }
                                sections_adapter_obj.notifyItemRemoved(position);
                                data.remove(position);
                                sections_adapter_obj.notifyItemRangeChanged(position, data.size());
                                Toast.makeText(SC.this, "Deleted", Toast.LENGTH_SHORT).show();

                            }
                        });

                        alertDialog.show();
                    }
                    //long click
                })
        );


    }

    public void meth() {
        childEventListener = mrootref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                sectionPOJO section = dataSnapshot.getValue(sectionPOJO.class);
                data.add(section);

                sections_adapter_obj.notifyDataSetChanged();
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
        databaseReference.child(clgname).child("V_Faculty").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild("sections")) {
                    progressBar.setVisibility(View.GONE);
                }else{
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    /* @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Destroy", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "Restart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "Pause", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "Resume", Toast.LENGTH_SHORT).show();

    }*/

    public void connection() {
        mrootref = FirebaseDatabase.getInstance().getReference(".info/connected");
        mrootref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                connected = snapshot.getValue(Boolean.class);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });
    }
    @Override
    public void onBackPressed() {
        finish();
    }

}