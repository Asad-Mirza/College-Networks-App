package com.example.admin.mysvvvappbeta.O;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.admin.mysvvvappbeta.R;
import com.example.admin.mysvvvappbeta.UI.timeAgo;
import com.example.admin.mysvvvappbeta.ADA.cmtadapter;
import com.example.admin.mysvvvappbeta.models.comment;
import com.example.admin.mysvvvappbeta.models.post;
import com.example.admin.mysvvvappbeta.models.user;
import com.example.admin.mysvvvappbeta.UI.sharedData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Asad Mirzaon 04-01-2018.
 */

public class PopupActivity extends AppCompatActivity {


    private post p;
    private RecyclerView recyclerView;
    ArrayList<comment> comments;
    private cmtadapter cmtadapter_ob;

    private EditText comment_edittext;
    private Button button;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mrootref;
    private FirebaseAuth auth;
  //  private ScrollView scrollView;
    private FirebaseUser firebaseUser;
    private ProgressBar progressBar;
    user userOB;
    String PID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout); //anim
        recyclerView = findViewById(R.id.recyclerview);
       // scrollView=findViewById(R.id.scrollView);
        progressBar = findViewById(R.id.progress_bar);

        comment_edittext = findViewById(R.id.comment_edittext);


        sharedData sharedData_ob = new sharedData();
        userOB = sharedData_ob.getUserData(PopupActivity.this);
        Bundle bundle = getIntent().getExtras();
        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("Comments");


        setSupportActionBar(toolbar);
        PID = bundle.getString("PID");
        if (PID == null) {
            Toast.makeText(this, "Error please try again", Toast.LENGTH_SHORT).show();
        }
        String name  = bundle.getString("name");
        if (name!=null) {
            toolbar.setTitle("Comments on "+name);
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
            public void onClick(View v) {
                finish();

            }
        });
        button = findViewById(R.id.comment_button);
        firebaseDatabase = FirebaseDatabase.getInstance();
        mrootref = firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        comments = new ArrayList<comment>();
      LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(this);
      linearLayoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        cmtadapter_ob = new cmtadapter(comments, PopupActivity.this);
        recyclerView.setAdapter(cmtadapter_ob);


        cmtadapter_ob.notifyDataSetChanged();
        check(); //method to check is comment persist.
        mrootref.child(userOB.getCollege()).child("D").child("C").child(PID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                comment c = dataSnapshot.getValue(comment.class);
                String time = timeAgo.getTimeAgo(Long.parseLong(c.getTime().toString()));
                c.setTime(time);
                comments.add(c);
                cmtadapter_ob.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
               // scrollView.fullScroll(ScrollView.FOCUS_DOWN);
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


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String text = comment_edittext.getText().toString();
                comment_edittext.setText("");
                if (TextUtils.isEmpty(text)) {
                    Toast.makeText(PopupActivity.this, "Empty Comment", Toast.LENGTH_SHORT).show();
                } else {
                  /*  FirebaseStorage.getInstance().getReference().child("profiles_images").child(firebaseUser.getUid()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {*/

                    View view = PopupActivity.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    comment cmt = new comment();
                    cmt.setSenderImageURL(firebaseUser.getPhotoUrl().toString());
                    cmt.setSender(firebaseUser.getDisplayName());
                    cmt.setSender_UID(firebaseUser.getUid());
                    cmt.setTime(ServerValue.TIMESTAMP);
                    cmt.setCommenttext(text);
                    mrootref.child(userOB.getCollege()).child("D").child("C").child(PID).push().setValue(cmt).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                               // scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                                Toast.makeText(PopupActivity.this, "You commented", Toast.LENGTH_SHORT).show();

                            } else {
                                comment_edittext.setText(text);
                                Toast.makeText(PopupActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    // }
                    //  })
                            /*.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PopupActivity.this, "please try again", Toast.LENGTH_SHORT).show();
                        }
                    });*/


                }
            }
        });


    }
    @Override
    public void onBackPressed() {
        finish();
    }

    public void check() {
        mrootref.child(userOB.getCollege()).child("D").child("C").child(PID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}