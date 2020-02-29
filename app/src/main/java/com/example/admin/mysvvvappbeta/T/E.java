package com.example.admin.mysvvvappbeta.T;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.support.annotation.Nullable;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.admin.mysvvvappbeta.GlideApp;
import com.example.admin.mysvvvappbeta.R;
import com.example.admin.mysvvvappbeta.UI.HF;
import com.example.admin.mysvvvappbeta.models.ad;
import com.example.admin.mysvvvappbeta.models.reportPOJO;
import com.example.admin.mysvvvappbeta.models.user;
import com.example.admin.mysvvvappbeta.UI.sharedData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;


public class E extends AppCompatActivity {
    String link;
    private  WebView webView;
    private ProgressBar progressBar;
    private TextView titleImTextView,URlTextView;
    private ImageView closeImageView;
    private TextView optionTextView;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erp_);
          titleImTextView = findViewById(R.id.title);
          URlTextView  =findViewById(R.id.urlTextView);
          optionTextView = findViewById(R.id.textViewOptions);









        closeImageView = findViewById(R.id.closeImageView);
        Intent intent = getIntent();
        link = intent.getStringExtra("link");
        String title = intent.getStringExtra("toolbar");


        titleImTextView.setText(title);
        URlTextView.setText(link);

        if(!link.contains("http")) {
            link = "http://"+link;
        }


        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUserAgentString("Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543a Safari/419.3");
        webView.setWebViewClient(new HelloWebViewClient());
        webView.loadUrl(link);



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
       user userOB = sharedData_ob.getUserData(E.this);


       optionTextView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               PopupMenu popup = new PopupMenu(E.this, optionTextView);
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





    }
    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
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
