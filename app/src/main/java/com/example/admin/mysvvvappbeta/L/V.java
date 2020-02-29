package com.example.admin.mysvvvappbeta.L;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.admin.mysvvvappbeta.MA;
import com.example.admin.mysvvvappbeta.R;
import com.example.admin.mysvvvappbeta.UI.sharedData;
import com.example.admin.mysvvvappbeta.models.user;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class V extends AppCompatActivity {
    private Button try_againButton,sign_outButton;
    private DatabaseReference mRootRef;
    private user userOB;
    private FirebaseUser firebaseUser;
    private boolean fromLoginFlag = false,buttonClick=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v);
         try_againButton = findViewById(R.id.tryagain);
         sign_outButton = findViewById(R.id.signout);
         mRootRef = FirebaseDatabase.getInstance().getReference();
         userOB = new sharedData().getUserData(V.this);
         firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("***********",userOB.getCollege()+userOB.getBranch());

         Intent intent = getIntent();
         fromLoginFlag= intent.getBooleanExtra("flag",false);
         if (!fromLoginFlag){
             checkVerification();
         }

         sign_outButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 final ProgressDialog progressDialog  = new ProgressDialog(V.this);
                 progressDialog.setMessage("Signing out...");
                 progressDialog.setCancelable(false);
                 progressDialog.show();
                 FirebaseAuth.getInstance().signOut();
                 SharedPreferences pref= getSharedPreferences("v", Context.MODE_MULTI_PROCESS);
                 SharedPreferences.Editor edt = pref.edit();
                 edt.putBoolean("f",false);
                 edt.commit();
                 if (AccessToken.getCurrentAccessToken() != null)
                     LoginManager.getInstance().logOut();

                 startActivity(new Intent(V.this,LGC.class));
                 progressDialog.dismiss();
                 finish();


             }
         });

         try_againButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 buttonClick = true;
                 checkVerification();
             }
         });



    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void checkVerification() {
        if (userOB!=null) {
            final ProgressDialog progressDialog = new ProgressDialog(V.this);
            progressDialog.setMessage("Checking your Account...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            mRootRef.child(userOB.getCollege()).child("V_Faculty").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        SharedPreferences pref = getSharedPreferences("v", Context.MODE_MULTI_PROCESS);
                        SharedPreferences.Editor edt = pref.edit();
                        edt.commit();

                        edt.putBoolean("f", false);
                        startActivity(new Intent(V.this, MA.class));

                        progressDialog.show();
                        finish();

                    } else {
                        progressDialog.dismiss();
                        if (buttonClick)
                            Toast.makeText(V.this, "Not Verified yet", Toast.LENGTH_SHORT).show();
                        buttonClick = false;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else Toast.makeText(this, "Error Try Again", Toast.LENGTH_SHORT).show();
    }
}
