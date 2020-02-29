package com.example.admin.mysvvvappbeta.T;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.admin.mysvvvappbeta.GlideApp;
import com.example.admin.mysvvvappbeta.R;
import com.example.admin.mysvvvappbeta.models.ad;
import com.example.admin.mysvvvappbeta.models.user;
import com.example.admin.mysvvvappbeta.UI.sharedData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class F extends AppCompatActivity {
    private ProgressBar progressBar;
    private ImageView view;
    private user userOB;
    private ImageView adImage;
    private boolean isFromHomeScreen=false;
    private String imageURL;
private DownloadManager downloadManager;
    ArrayList<Long> list = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_full_screen_image);
        progressBar = findViewById(R.id.progress_bar);
        view = findViewById(R.id.imageView);
        Intent intent = getIntent();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        BroadcastReceiver onComplete = new BroadcastReceiver() {

            public void onReceive(Context ctxt, Intent intent) {

                // get the refid from the download manager
                long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

// remove it from our list
                list.remove(referenceId);

// if list is empty means all downloads completed
                if (list.isEmpty())
                {

// show a notification
                    Log.e("INSIDE", "" + referenceId);

                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(F.this,"DOWNLOAD_ID")
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentTitle("College Networks")
                                    .setColor(getResources().getColor(R.color.colorPrimary))
                                    .setContentText("All Downloads completed")

                                    .setSmallIcon(R.drawable.logo_cn_new);




                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(455, mBuilder.build());


                }

            }
        };

        registerReceiver(onComplete,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


        imageURL = intent.getStringExtra("image");
        boolean isTimeTable = intent.getBooleanExtra("flag", false);
      isFromHomeScreen  =intent.getBooleanExtra("isFromHomeScreen", false);
        adImage = findViewById(R.id.ad);
        sharedData sharedData_ob = new sharedData();
        userOB = sharedData_ob.getUserData(F.this);




        if (isTimeTable) {


















            FirebaseDatabase.getInstance().getReference().child(userOB.getCollege()).child("ads").child("timetable").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final ad adOB = dataSnapshot.getValue(ad.class);
                    if (adOB != null) {
                        adImage.setVisibility(View.VISIBLE);
                        if (!TextUtils.isEmpty(adOB.getImageURL())) {

                            GlideApp.with(F.this)

                                    .load(adOB.getImageURL())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    //.error(R.drawable.defaultimg)

                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                            adImage.setVisibility(View.GONE);


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
                                    Intent intent = new Intent(F.this, E.class);
                                    intent.putExtra("link", adOB.getUrl());
                                    intent.putExtra("toolbar", adOB.getTitle());
                                    intent.putExtra("adflag", true);
                                    startActivity(intent);
                                }
                            });


                        }
                    } else
                        adImage.setVisibility(View.GONE);


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            toolbar.setTitle("Time Table");
            reciever();
            FirebaseDatabase.getInstance().getReference().child(userOB.getCollege()).child("Time_Table").child(userOB.getCourse()).child(userOB.getBranch()).child(userOB.getYear()).child(userOB.getSection()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    imageURL= dataSnapshot.getValue(String.class);
                    if (!TextUtils.isEmpty(imageURL)) {

                        GlideApp.with(F.this)
                                .load(imageURL)
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

                                .into(view);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {


            if (!TextUtils.isEmpty(imageURL)) {

                GlideApp.with(F.this)
                        .load(imageURL)
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

                        .into(view);
            }
        }















    }
   void downloadImage(){


       Uri Download_Uri = Uri.parse(imageURL);

       DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
       request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
       request.setAllowedOverRoaming(false);
       request.setTitle("GadgetSaint Downloading " + "IMG" + ".png");
       request.setDescription("Downloading " + "Sample" + ".png");
       request.setVisibleInDownloadsUi(true);
       request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/College Networks/"  + "/" + "Sample" + ".png");


       long refid = downloadManager.enqueue(request);
       list.add(refid);
       /*NotificationCompat.Builder mBuilder =
               new NotificationCompat.Builder(F.this,"DOWNLOAD_ID")
                       .setSmallIcon(R.mipmap.ic_launcher)
                       .setContentTitle("College Networks")
                       .setColor(getResources().getColor(R.color.colorPrimary))
                       .setContentText("Downloading")
                       .setProgress(100,30,true)
                       .setSmallIcon(R.drawable.logo_cn_new);



       NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
       notificationManager.notify((int)refid, mBuilder.build());*/





   }
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       if (!isFromHomeScreen)
           getMenuInflater().inflate(R.menu.full_screen_image_menu, menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu2) {
            AlertDialog.Builder builder = new AlertDialog.Builder(F.this);
            builder.setMessage("Do you want to create shortcut of Time Table on Home Screen ?");
            builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ShortcutIcon();

                }
            });

        builder.setNegativeButton("Cancel",null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        }else if (item.getItemId() == R.id.menu1){
          downloadImage();



        }
return  true;


        }








    public void ShortcutIcon() {
        try {


        Intent shortcutIntent = new Intent(getApplicationContext(), F.class);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        shortcutIntent.putExtra("flag", true);
        shortcutIntent.putExtra("isFromHomeScreen",true);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Time Table");
        addIntent.putExtra("duplicate", false);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.icon_cn_circle));
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(addIntent);
        Toast.makeText(this, "Shortcut Created", Toast.LENGTH_SHORT).show();




        }catch (Exception e){
            Toast.makeText(this, "Error in creating shortcut", Toast.LENGTH_SHORT).show();


        }

    }



    @Override
    public void onBackPressed() {
        finish();

    }

    public void reciever() {
        FirebaseDatabase.getInstance().getReference().child(userOB.getCollege()).child("Time_Table").child(userOB.getCourse()).child(userOB.getBranch()).child(userOB.getYear()).child(userOB.getSection()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(F.this, "No Time table found for this section", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}
