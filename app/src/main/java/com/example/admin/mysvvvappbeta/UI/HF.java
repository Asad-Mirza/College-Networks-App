package com.example.admin.mysvvvappbeta.UI;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.admin.mysvvvappbeta.J.cd;
import com.example.admin.mysvvvappbeta.R;
import com.example.admin.mysvvvappbeta.models.feedbackPOJO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class HF extends AppCompatActivity {

    private EditText feedbackEditText;
    boolean fromLogin;


    private boolean connected = false;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hand_f);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(cd.getResource("$M&j&q&u&%&f&s&i&%&K&j&j&i&g&f&h&p&%"));

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
   progressBar  =findViewById(R.id.progress_bar);
feedbackEditText  = findViewById(R.id.feedback_editext);
 Intent intent = getIntent();
fromLogin = intent.getBooleanExtra("fromLogin",false);
if (fromLogin)
    feedbackEditText.setHint("What is your problem ?");






        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();







    }
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.helpandfeedmenu, menu);

        return  true;
    }
    @Override
    public void onBackPressed() {
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.send) {
            String feedbackText = feedbackEditText.getText().toString();
            connected  = isNetworkConnected(HF.this);
          if(fromLogin){


              if (connected){
                  feedbackEditText.setText("");
                  if (!TextUtils.isEmpty(feedbackText)) {
                      progressBar.setVisibility(View.VISIBLE);
                      feedbackPOJO pojo = new feedbackPOJO();
                      pojo.setText(feedbackText);
                      pojo.setTime(ServerValue.TIMESTAMP);

                      pojo.setDevice_info(getDeviceName());
                      pojo.setNetwork_type(getNetworkType(HF.this));
                      FirebaseDatabase.getInstance().getReference().child("feedback").child("feedbackPOJOS").child("login-Feedback").push().setValue(pojo).addOnCompleteListener(new OnCompleteListener<Void>() {
                          @Override
                          public void onComplete(@NonNull Task<Void> task) {
                              if (task.isSuccessful()){
                                  Toast.makeText(HF.this, "Thanks for your Feedback !", Toast.LENGTH_SHORT).show();

                              }
                              else{
                                  Toast.makeText(HF.this, "Error in sending feedback Try again", Toast.LENGTH_SHORT).show();


                              }
                          }
                      });
                      progressBar.setVisibility(View.GONE);

                  }

                  else {
                      Toast.makeText(HF.this, "Write your Feedback before sending", Toast.LENGTH_SHORT).show();

                  }


              }
              else {
                  Toast.makeText(HF.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
              }






          }else{

            if (connected){
                feedbackEditText.setText("");
                if (!TextUtils.isEmpty(feedbackText)) {
                    progressBar.setVisibility(View.VISIBLE);
                    feedbackPOJO pojo = new feedbackPOJO();
                    pojo.setText(feedbackText);
                    pojo.setTime(ServerValue.TIMESTAMP);
                    pojo.setSender_name(firebaseUser.getDisplayName());
                    pojo.setSender_UID(firebaseUser.getUid());
                    pojo.setDevice_info(getDeviceName());
                    pojo.setNetwork_type(getNetworkType(HF.this));
                    FirebaseDatabase.getInstance().getReference().child("feedback").child("feedbackPOJOS").child("inside-feedback").push().setValue(pojo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(HF.this, "Thanks for your Feedback !", Toast.LENGTH_SHORT).show();

                            }
                            else{
                                Toast.makeText(HF.this, "Error in sending feedback Try again", Toast.LENGTH_SHORT).show();


                            }
                        }
                    });
                    progressBar.setVisibility(View.GONE);

                }

                else {
                    Toast.makeText(HF.this, "Write your Feedback before sending", Toast.LENGTH_SHORT).show();

                }


            }
            else {
                Toast.makeText(HF.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }}
        }

    return true;
    }






        public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
    static String getNetworkType(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = mTelephonyManager.getNetworkType();
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2G";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                /**
                 From this link https://en.wikipedia.org/wiki/Evolution-Data_Optimized ..NETWORK_TYPE_EVDO_0 & NETWORK_TYPE_EVDO_A
                 EV-DO is an evolution of the CDMA2000 (IS-2000) standard that supports high data rates.

                 Where CDMA2000 https://en.wikipedia.org/wiki/CDMA2000 .CDMA2000 is a family of 3G[1] mobile technology standards for sending voice,
                 data, and signaling data between mobile phones and cell sites.
                 */
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                //Log.d("Type", "3g");
                //For 3g HSDPA , HSPAP(HSPA+) are main  networktype which are under 3g Network
                //But from other constants also it will 3g like HSPA,HSDPA etc which are in 3g case.
                //Some cases are added after  testing(real) in device with 3g enable data
                //and speed also matters to decide 3g network type
                //https://en.wikipedia.org/wiki/4G#Data_rate_comparison
                return "3G";
            case TelephonyManager.NETWORK_TYPE_LTE:
                //No specification for the 4g but from wiki
                //I found(LTE (Long-Term Evolution, commonly marketed as 4G LTE))
                //https://en.wikipedia.org/wiki/LTE_(telecommunication)
                return "4G";
            default:
                return "Notfound";
        }
    }

    /**
     * To check device has internet
     *
     * @param context
     * @return boolean as per status
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

}
