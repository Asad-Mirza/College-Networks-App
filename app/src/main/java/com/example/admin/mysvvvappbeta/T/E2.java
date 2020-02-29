package com.example.admin.mysvvvappbeta.T;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.support.annotation.Nullable;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;

import android.widget.TextView;

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


public class E2 extends AppCompatActivity {
    String link;
    private  WebView webView;
    private ProgressBar progressBar;
    private TextView titleImTextView,URlTextView;
    private ImageView closeImageView;
    private ImageView adImage;
    private TextView optionTextView;
    //private RelativeLayout relativeLayout;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e2);
        titleImTextView = findViewById(R.id.title);
        URlTextView  =findViewById(R.id.urlTextView);
        adImage  = findViewById(R.id.ad);
        optionTextView = findViewById(R.id.textViewOptions);
       // relativeLayout=findViewById(R.id.root);
        closeImageView = findViewById(R.id.closeImageView);

        Intent intent = getIntent();
        link = intent.getStringExtra("link");
        String title = intent.getStringExtra("toolbar");

       // boolean adFlag  = intent.getBooleanExtra("adflag",true);
        webView = (WebView) findViewById(R.id.webview);
        if(!link.contains("http")) {
            link = "http://"+link;
        }

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUserAgentString("Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543a Safari/419.3");
        webView.setWebViewClient(new HelloWebViewClient());
        webView.loadUrl(link);



        titleImTextView.setText(title);
        URlTextView.setText(link);



        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.setIndeterminate(true);


        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sharedData sharedData_ob = new sharedData();
        user userOB = sharedData_ob.getUserData(E2.this);
        optionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(E2.this, optionTextView);
                //inflating menu from xml resource
                popup.inflate(R.menu.erp_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                                startActivity(intent);
                                return true;




                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();



            }
        });





        FirebaseDatabase.getInstance().getReference().child(userOB.getCollege()).child("ads").child("erp").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final ad adOB = dataSnapshot.getValue(ad.class);

                    if (adOB!=null){
                        if (!TextUtils.isEmpty(adOB.getImageURL())) {


                            adImage.setVisibility(View.VISIBLE);
                            // Toast.makeText(E.this, "In not null561"+adOB.getImageURL(), Toast.LENGTH_SHORT).show();
                            GlideApp.with(E2.this)


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
                                    Intent intent = new Intent(E2.this, E.class);
                                    intent.putExtra("link", adOB.getUrl());
                                    intent.putExtra("toolbar", adOB.getTitle());
                                    intent.putExtra("adflag", true);
                                    startActivity(intent);
                                }
                            });


                        } }else {
                        adImage.setVisibility(View.INVISIBLE);

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    adImage.setVisibility(View.VISIBLE);

                }
            });


        }





    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            link = url;
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon)
        {
            // TODO show you progress image

            progressBar.setVisibility(View.VISIBLE);

            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url)
        {    progressBar.setVisibility(View.GONE);
        link = url;



            URlTextView.setText(view.getTitle());

            // TODO hide your progress image
            super.onPageFinished(view, url);
        }

    }
    @Override
    public void onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }

}
