package com.example.admin.mysvvvappbeta.meta;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.example.admin.mysvvvappbeta.J.cd;
import com.example.admin.mysvvvappbeta.R;

public class AU extends AppCompatActivity {
    private ImageView fb1,fb2,insta1,insta2,youtube;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(cd.getResource("$F&g&t&z&y&%&z&x&%"));

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fb1  =findViewById(R.id.fb);
        fb2 = findViewById(R.id.fb2);
        insta1  =findViewById(R.id.insta_icon);
        insta2  =findViewById(R.id.insta_icon2);
       youtube = findViewById(R.id.yt_icon);
       fb1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/link2asad"));
               startActivity(intent);

           }
       });

        fb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/tosif.khan.7169"));
                startActivity(intent);


            }
        });



        insta1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/asad_mirza10"));
                startActivity(intent);

            }
        });

        insta2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/tosif8509"));
                startActivity(intent);
            }
        });
        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/learneasy9"));
                startActivity(intent);

            }
        });












    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
