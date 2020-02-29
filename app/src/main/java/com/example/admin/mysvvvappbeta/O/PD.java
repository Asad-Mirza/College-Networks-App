package com.example.admin.mysvvvappbeta.O;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.admin.mysvvvappbeta.GlideApp;
import com.example.admin.mysvvvappbeta.J.cd;
import com.example.admin.mysvvvappbeta.R;
import com.example.admin.mysvvvappbeta.T.E;
import com.example.admin.mysvvvappbeta.UI.timeAgo;
import com.example.admin.mysvvvappbeta.ADA.cmtadapter;
import com.example.admin.mysvvvappbeta.models.comment;
import com.example.admin.mysvvvappbeta.models.post;
import com.example.admin.mysvvvappbeta.models.user;
import com.example.admin.mysvvvappbeta.UI.sharedData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Asad Mirza on 26-12-2017.
 */

public class PD extends AppCompatActivity {
   // private RecyclerView recyclerView;
   private post p;
   private CircleImageView circleImageView;
   private TextView sender_name,time,title,dis;
   private ImageView main_image;
   private EditText comment_edittext;
   //private Button button;
   private FirebaseDatabase firebaseDatabase;
   private DatabaseReference mrootref;
   private FirebaseAuth auth;
   private FirebaseUser firebaseUser;
   private cmtadapter cmtadapter_ob;
   ArrayList<comment> comments;
   private Button registorButton,commentButton,shareButton;
   private ProgressBar progressBar,progressBar2;
   private ScrollView scrollView;
   int loaded=0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_details);
      //  overridePendingTransition(R.anim.fadein, R.anim.fadeout);//anim
        // recyclerView = findViewById(R.id.comment_hanger);

        circleImageView = findViewById(R.id.hang_dp);
        sender_name = findViewById(R.id.hang_name);
        time = findViewById(R.id.hang_time);
        title = findViewById(R.id.hang_title);
        dis = findViewById(R.id.hang_dis);
        main_image = findViewById(R.id.hang_image);
        registorButton = findViewById(R.id.registor_button);
        commentButton = findViewById(R.id.comment_button);
        shareButton = findViewById(R.id.share_button);
      /* comment_edittext=findViewById(R.id.comment_edittext);
       button=findViewById(R.id.comment_button);*/
        progressBar = findViewById(R.id.progress_bar);
        progressBar2 = findViewById(R.id.progress_bar2);
        scrollView = findViewById(R.id.scroll_view);
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(cd.getResource("$J&{&j&s&y&%&I&j&y&f&n&q&x&%"));

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        // dis.setMovementMethod(LinkMovementMethod.getInstance());

        firebaseDatabase = FirebaseDatabase.getInstance();
        mrootref = firebaseDatabase.getReference();





        user userOB;
        Intent intent = getIntent();
        p = (post) intent.getSerializableExtra("post");
        if (p == null) {
            scrollView.setVisibility(View.INVISIBLE);
            progressBar2.setVisibility(View.VISIBLE);
            sharedData sharedData_ob = new sharedData();
            userOB = sharedData_ob.getUserData(PD.this);

            String PID = intent.getStringExtra("PID");
            mrootref.child(userOB.getCollege()).child("dashboard").child("post").child(PID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    p = dataSnapshot.getValue(post.class);
                    loaded++;
                    setData();
                    if (loaded!=0){
                        scrollView.setVisibility(View.VISIBLE);
                        progressBar2.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }else setData();


     /*   button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String text=comment_edittext.getText().toString();
                if(TextUtils.isEmpty(text)){
                    Toast.makeText(PD.this, "Empty Comment", Toast.LENGTH_SHORT).show();
                }
                else{
                    FirebaseStorage.getInstance().getReference().child("profiles_images").child(firebaseUser.getUid()+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            comment cmt = new comment();
                            cmt.setSenderImageURL(uri.toString());
                            cmt.setSender(firebaseUser.getDisplayName());
                            cmt.setTime(ServerValue.TIMESTAMP);
                            cmt.setCommenttext(text);
                            mrootref.child("dashboard_comments").child(p.getP_id()).push().setValue(cmt).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        comment_edittext.setText("");
                                        Toast.makeText(PD.this, "You commented", Toast.LENGTH_SHORT).show();

                                    } else
                                        Toast.makeText(PD.this, "Try again", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PD.this, "please try again", Toast.LENGTH_SHORT).show();
                        }
                    });



                }
            }
        });*/
        // comments = new ArrayList<comment>();
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // cmtadapter_ob = new cmtadapter(comments,PD.this);
        // recyclerView.setAdapter(cmtadapter_ob);

        //cmtadapter_ob.notifyDataSetChanged();
        /*mrootref.child("dashboard_comments").child(p.getP_id()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                comment c = dataSnapshot.getValue(comment.class);
                String time= timeAgo.getTimeAgo(Long.parseLong(c.getTime().toString()));
                c.setTime(time);
                comments.add(c);
                cmtadapter_ob.notifyDataSetChanged();
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

*/






    }
    @Override
    public void onBackPressed() {
        finish();
    }
void setData(){
    if (!TextUtils.isEmpty(p.getSender_dp())){
        GlideApp.with(PD.this).load(p.getSender_dp())




                .error(R.drawable.pro_pic)
                .placeholder(R.drawable.pro_pic)


                .into(circleImageView);
    }


    if (!TextUtils.isEmpty(p.getImage_uri())){


        GlideApp.with(PD.this)

                .load(p.getImage_uri())

                //.diskCacheStrategy(DiskCacheStrategy.ALL)

                //.placeholder(R.drawable.loading_image)
                //.thumbnail(0.1f)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);



                        return false;
                    }
                })




                //.placeholder(R.drawable.loading_image)

                .into(main_image);
    }


    sender_name.setText(p.getSender_name());
    time.setText(timeAgo.getTimeAgo(Long.parseLong(p.getTime().toString())));
    title.setText(p.getTitle());
    dis.setText(p.getDis());
    final String registorLink = p.getLink1();
    if (p.getStatus().equals("no")){
        shareButton.setVisibility(View.GONE);
        commentButton.setVisibility(View.GONE);

    }
    if (registorLink!=null)
        registorButton.setVisibility(View.VISIBLE);
    registorButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(PD.this,E.class);
            intent.putExtra("link",registorLink);
            intent.putExtra("toolbar","Event Registration");
            startActivity(intent);

        }
    });

    commentButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(PD.this, PopupActivity.class);
            intent.putExtra("PID", p.getP_id());
            intent.putExtra("name",p.getTitle());
            startActivity(intent);

        }
    });





}

}



