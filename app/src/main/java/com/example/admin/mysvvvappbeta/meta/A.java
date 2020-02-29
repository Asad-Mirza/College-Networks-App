package com.example.admin.mysvvvappbeta.meta;
import com.example.admin.mysvvvappbeta.J.cd;
import com.example.admin.mysvvvappbeta.UI.sharedData;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import com.example.admin.mysvvvappbeta.T.E;
import com.example.admin.mysvvvappbeta.GlideApp;
import com.example.admin.mysvvvappbeta.R;
import com.example.admin.mysvvvappbeta.models.ad;
import com.example.admin.mysvvvappbeta.models.user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class A extends AppCompatActivity {
    private EditText feedbackEditText;
    private ProgressBar progressBar;
    private user userOB;
    private ImageView adImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anony_feed);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(cd.getResource("$F&s&t&s&~&r&t&z&x&%&K&j&j&i&g&f&h&p&%"));


        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        adImage  = findViewById(R.id.ad);

        feedbackEditText = findViewById(R.id.feedback_editext);
        progressBar = findViewById(R.id.progress_bar);

       sharedData data = new sharedData();
       userOB = data.getUserData(A.this);

        FirebaseDatabase.getInstance().getReference().child(userOB.getCollege()).child("ads").child("feedback").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ad adOB = dataSnapshot.getValue(ad.class);

                if (adOB!=null){
                    adImage.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(adOB.getImageURL())) {

                        // Toast.makeText(E.this, "In not null561"+adOB.getImageURL(), Toast.LENGTH_SHORT).show();
                        GlideApp.with(A.this)


                                .load(adOB.getImageURL())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                //.error(R.drawable.defaultimg)

                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                        adImage.setVisibility(View.GONE);
                                        //  Toast.makeText(E.this, "In not null", Toast.LENGTH_SHORT).show();



                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {


                                        return false;
                                    }
                                })
                                .into(adImage);

                        adImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(A.this, E.class);
                                intent.putExtra("link", adOB.getUrl());
                                intent.putExtra("toolbar", adOB.getTitle());
                                intent.putExtra("adflag", true);
                                startActivity(intent);
                            }
                        });


                    } }else
                    adImage.setVisibility(View.GONE);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                adImage.setVisibility(View.VISIBLE);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.helpandfeedmenu, menu);

        return  true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.send) {
            String feedbackText = feedbackEditText.getText().toString();
         boolean   connected = isNetworkConnected(A.this);

         if (connected){
             feedbackEditText.setText("");
             if (!TextUtils.isEmpty(feedbackText)){
                 progressBar.setVisibility(View.VISIBLE);
                 HashMap hashMap = new HashMap<String,Object>();
                 hashMap.put("name",userOB.getName());
                 hashMap.put("userType",userOB.getUserType());
                 hashMap.put("sender_UID", FirebaseAuth.getInstance().getCurrentUser().getUid());
                 hashMap.put("feedbackText",feedbackText);
                 hashMap.put("time", ServerValue.TIMESTAMP);
               FirebaseDatabase.getInstance().getReference().child(userOB.getCollege()).child("feedbacks").push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if (task.isSuccessful()){
                           Toast.makeText(A.this, "Feedback sent to your College Management !", Toast.LENGTH_LONG).show();
                           progressBar.setVisibility(View.GONE);
                       }else
                       {
                           Toast.makeText(A.this, "Error in sending Feedback .Try again", Toast.LENGTH_LONG).show();
                           progressBar.setVisibility(View.GONE);

                       }
                   }
               });



             }
             else {
                 Toast.makeText(this, "Write your Feedback before sending", Toast.LENGTH_SHORT).show();


             }






         }
         else
         {
             Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();

         }

        }
    return  true;
    }
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
    @Override
    public void onBackPressed() {
        finish();
    }

}
