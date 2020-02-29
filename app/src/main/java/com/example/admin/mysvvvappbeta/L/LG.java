package com.example.admin.mysvvvappbeta.L;

import com.example.admin.mysvvvappbeta.J.cd;
import com.example.admin.mysvvvappbeta.MA;
import com.example.admin.mysvvvappbeta.R;
import com.example.admin.mysvvvappbeta.UI.HF;
import com.example.admin.mysvvvappbeta.models.collegePOJO;
import com.example.admin.mysvvvappbeta.models.user;
import com.example.admin.mysvvvappbeta.UI.sharedData;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;


import static android.content.ContentValues.TAG;

/**
 * Created by Asad Mirza on 17-12-2017.
 */

public class LG extends Activity {
    private Spinner branch_spinner, year_spinner, section_spinner, course_spinner;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mRootRef, mDataRef;
 TextView helpTextView;
   // private CircleImageView circleImageView;
    final ArrayList<String> branch_array = new ArrayList<String>();
    final ArrayList<String> year_array = new ArrayList<String>();
    final ArrayList<String> section_array = new ArrayList<String>();
    final ArrayList<String> course_array = new ArrayList<>();
    final ArrayList<String> collegeTopic_array = new ArrayList<>();
    private LoginButton loginButton;
    private CheckBox checkBox;
    boolean toVerifyActivity = false;
    private ScrollView scrollView;





    private ProgressDialog mprogressbar;

    private static final int RC_SIGN_IN = 1;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private TextView ClgNametextView;
    private RelativeLayout relativeLayout2;

    private FirebaseStorage firebaseStorage;
    private StorageReference rootStorageRef, dataStorageRef;
    private ProgressBar progressBar;
    int collegeIndex;

    private   CallbackManager mCallbackManager;

    // ...
    private SignInButton signInButton;
    private collegePOJO collegePOJO_ob;
    private String ubranch, uyear, usection,ucourse;
    private boolean flag5 = false, flag1 = false, flag2 = false, flag3 = false, flag4 = false,facultOK=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.login_activity);
        mprogressbar = new ProgressDialog(this);
        progressBar = findViewById(R.id.progress_bar);
        //circleImageView = findViewById(R.id.logo_image);
        ClgNametextView = findViewById(R.id.clgname);
        course_spinner = findViewById(R.id.course_spinner);
       // relativeLayout1 = findViewById(R.id.relativelayout);
        relativeLayout2 = findViewById(R.id.relativelayout2);
//        relativeLayout1.setVisibility(View.INVISIBLE);
       helpTextView = findViewById(R.id.helpTextView);
       helpTextView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(LG.this,HF.class);
               intent.putExtra("fromLogin",true);
               startActivity(intent);
           }
       });
        checkBox = findViewById(R.id.checkbox);
        scrollView = findViewById(R.id.scrollView);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkBox.isChecked()) {
                    fbLoginButtonEnabler();
                    year_spinner.setVisibility(View.INVISIBLE);
                    section_spinner.setVisibility(View.INVISIBLE);
                    course_spinner.setVisibility(View.INVISIBLE);
                }
                else {
                    fbLoginButtonEnabler();
                    year_spinner.setVisibility(View.VISIBLE);
                    section_spinner.setVisibility(View.VISIBLE);
                    course_spinner.setVisibility(View.VISIBLE);
                }

            }
        });


        relativeLayout2.setVisibility(View.INVISIBLE);
        signInButton = findViewById(R.id.googlebtn);
        signInButton.setEnabled(false);


        Intent intent = getIntent();
        collegePOJO_ob = (collegePOJO) intent.getSerializableExtra("data");


        firebaseDatabase = FirebaseDatabase.getInstance();
        mRootRef = firebaseDatabase.getReference();
        ClgNametextView.setText(collegePOJO_ob.getCollege_name());


      /*  if (!TextUtils.isEmpty(collegePOJO_ob.getCollege_logo())) {
            Picasso.with(LG.this).load(collegePOJO_ob.getCollege_logo())
                    .error(R.drawable.pro_pic)


                    .fit()
                    .into(circleImageView);
        }*/


        //firebase DB

        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                Snackbar snackbar  =Snackbar.make(scrollView,"No Internet Connection",Snackbar.LENGTH_INDEFINITE);
                View view = snackbar.getView();
                int snackTextID = android.support.design.R.id.snackbar_text;
                TextView  textView = (TextView)view.findViewById(snackTextID);
                textView.setTextColor(getResources().getColor(R.color.white));


                if (!connected) {



                    flag3 =false;

                    fbLoginButtonEnabler();

                 snackbar.setAction("Try Again", new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                      fbLoginButtonEnabler();

                       }
                   });

                    snackbar.show();
                } else {

                    flag3 = true;
                    snackbar.setDuration(Snackbar.LENGTH_LONG);
                    snackbar.setText("Connected");
                    snackbar.show();
                    fbLoginButtonEnabler();





                }
            }


            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });

        mAuth = FirebaseAuth.getInstance();


        firebaseStorage = FirebaseStorage.getInstance();
        rootStorageRef = firebaseStorage.getReference();

        // Configure Google Sign In
        //  mGoogleSignInClient
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "";
                if (flag3) {
                    if (checkBox.isChecked())
                    {
                    if (flag2){
                        signIn();
                        facultOK = true;





                    } else {
                        facultOK =false;
                        Toast.makeText(LG.this, "Select Branch", Toast.LENGTH_SHORT).show();

                    }



                    }
                     else {

                        if (flag1 && flag2 && flag4 && flag5)
                            signIn();
                        else {
                            msg = "Please fill your details";
                            Toast.makeText(LG.this, msg, Toast.LENGTH_SHORT).show();


                        }
                    }

                } else {
                    msg = "Please connect to internet";

                    Toast.makeText(LG.this, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //spinners


        year_spinner = (Spinner) findViewById(R.id.year_spinner);
        branch_spinner = (Spinner) findViewById(R.id.branch_spinner);
        section_spinner = (Spinner) findViewById(R.id.section_spinner);

        setspinnerApdaterAndData();

        course_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    ucourse = parent.getItemAtPosition(position).toString(); //this is your selected item

                    flag5 = true;
                    fbLoginButtonEnabler();
                }else {flag5=false;
                    fbLoginButtonEnabler();
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                fbLoginButtonEnabler();

            }
        });


        year_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    uyear = parent.getItemAtPosition(position).toString(); //this is your selected item
                    flag1 = true;
                    fbLoginButtonEnabler();
                }else {flag1=false;
                    fbLoginButtonEnabler();
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                fbLoginButtonEnabler();

            }
        });
        branch_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    ubranch = parent.getItemAtPosition(position).toString(); //this is your selected item
                    flag2 = true;
                    fbLoginButtonEnabler();
                }else {flag2=false;
                    fbLoginButtonEnabler();
                }
            }


            public void onNothingSelected(AdapterView<?> parent) {
                fbLoginButtonEnabler();

            }
        });
        section_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    usection = parent.getItemAtPosition(position).toString(); //this is your selected item
                    flag4 = true;
                    fbLoginButtonEnabler();
                }else {flag4=false;

                    fbLoginButtonEnabler();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                fbLoginButtonEnabler();

            }
        });



        // Initialize Facebook Login button


        loginButton = findViewById(R.id.login_button);
        loginButton.setEnabled(false);
        mCallbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email", "public_profile");

        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                mprogressbar.dismiss();
                Toast.makeText(LG.this, "Facebook Login Cancelled", Toast.LENGTH_SHORT).show();

                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                mprogressbar.dismiss();
                Toast.makeText(LG.this, "Facebook Login Error Try Again", Toast.LENGTH_SHORT).show();
                // ...
            }
        });






    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    void setspinnerApdaterAndData() {


        branch_array.add("Select Branch");
        year_array.add("Select Year");
        section_array.add("Select Section");
        course_array.add("Select Course");
        course_spinner.setSelection(0);
        branch_spinner.setSelection(0);
        year_spinner.setSelection(0);
        section_spinner.setSelection(0);

        mRootRef.child("app_data").child("college_structure").child(collegePOJO_ob.getCid()).child("courses").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                course_array.add(dataSnapshot.getValue(String.class));
                progressBar.setVisibility(View.INVISIBLE);
                //relativeLayout1.setVisibility(View.VISIBLE);

                relativeLayout2.setVisibility(View.VISIBLE);

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
        mRootRef.child("app_data").child("college_structure").child(collegePOJO_ob.getCid()).child("branches").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                branch_array.add(dataSnapshot.getValue(String.class));
                progressBar.setVisibility(View.INVISIBLE);
               // relativeLayout1.setVisibility(View.VISIBLE);

                relativeLayout2.setVisibility(View.VISIBLE);
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
        mRootRef.child("app_data").child("topics").child(collegePOJO_ob.getCollege_name()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String topicName  = dataSnapshot.getValue(String.class);
                collegeTopic_array.add(topicName);
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



        year_array.add("First");
        year_array.add("Second");
        year_array.add("Third");
        year_array.add("Fourth");
        section_array.add("A");
        section_array.add("B");
        section_array.add("C");
        section_array.add("D");
        section_array.add("E");
        section_array.add("F");
        section_array.add("G");
        section_array.add("H");


        ArrayAdapter<String> adapter_branch = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, branch_array);

        ArrayAdapter<String> adapter_year = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, year_array);
        ArrayAdapter<String> adapter_section = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, section_array);
        ArrayAdapter<String> adapter_course = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, course_array);

        Log.d("--------------", branch_array + " " + year_array);
        // branch_spinner.setD
        course_spinner.setAdapter(adapter_course);
        branch_spinner.setAdapter(adapter_branch);
        year_spinner.setAdapter(adapter_year);
        section_spinner.setAdapter(adapter_section);

    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                           // mprogressbar.show();
                            if (checkBox.isChecked()){
                                mprogressbar.setMessage("Checking your account...");

                                FirebaseDatabase.getInstance().getReference().child(collegePOJO_ob.getCollege_name()).child("V_Faculty").child(FirebaseAuth.getInstance().getCurrentUser().getUid() ).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){
                                            user temp  = dataSnapshot.getValue(user.class);

                                            if (temp.getBranch().equals(ubranch)){

                                                setUpUserData("faculty");
                                            }else{
                                                if (AccessToken.getCurrentAccessToken()!=null)
                                                    LoginManager.getInstance().logOut();
                                                Toast.makeText(LG.this, "This account is attached with "+temp.getBranch(), Toast.LENGTH_LONG).show();
                                                //setUpUserData("faculty");
                                                mprogressbar.dismiss();

                                            }




                                        }
                                        else {
                                            FirebaseUser firebaseUser1 = FirebaseAuth.getInstance().getCurrentUser();
                                            user userOB  = new user();
                                            userOB.setName(firebaseUser1.getDisplayName());
                                            userOB.setEmail(firebaseUser1.getEmail());
                                            userOB.setBranch(ubranch);
                                            userOB.setPhotoId(firebaseUser1.getPhotoUrl().toString());
                                            FirebaseDatabase.getInstance().getReference().child(collegePOJO_ob.getCollege_name()).child("Faculty").child(FirebaseAuth.getInstance().getCurrentUser().getUid() ).setValue(userOB).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                       /* if (AccessToken.getCurrentAccessToken()!=null)
                                                            LoginManager.getInstance().logOut();
                                                          mprogressbar.dismiss();
                                                          Toast.makeText(LG.this, cd.getResource("$^&t&z&%&f&w&j&%&s&t&y&%&{&j&w&n&k&n&j&i&%&f&x&%&f&%&k&f&h&z&q&y&~&%"), Toast.LENGTH_LONG).show();
*/
                                                       toVerifyActivity =true;
                                                       setUpUserData("faculty");
verifyActivity();
                                                    }
                                                    else
                                                    {

                                                        mprogressbar.dismiss();
                                                        Toast.makeText(LG.this, cd.getResource("$^&t&z&%&f&w&j&%&s&t&y&%&{&j&w&n&k&n&j&i&%&f&x&%&f&%&k&f&h&z&q&y&~&%"), Toast.LENGTH_LONG).show();




                                                    }
                                                }
                                            });








                                           // FirebaseAuth.getInstance().signOut();



                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });



                            }
                            else
                                setUpUserData("student");
















                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LG.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            mprogressbar.dismiss();
                          //  updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mprogressbar.setMessage("Signing in...");
        mprogressbar.setCancelable(false);
        mprogressbar.show();

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show();

                // ...
                mprogressbar.dismiss();
            }
        }
        else {

            super.onActivityResult(requestCode, resultCode, data);

            // Pass the activity result back to the Facebook SDK
            mCallbackManager.onActivityResult(requestCode, resultCode, data);



        }
    }

    @Override
    public void onBackPressed() {
         finish();


    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            if (checkBox.isChecked()){
                                mprogressbar.setMessage("Checking your account...");

                                FirebaseDatabase.getInstance().getReference().child(collegePOJO_ob.getCollege_name()).child("V_Faculty").child(FirebaseAuth.getInstance().getCurrentUser().getUid() ).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){
                                            user temp  = dataSnapshot.getValue(user.class);

                                            if (temp.getBranch().equals(ubranch)){

                                                setUpUserData("faculty");
                                            }else{
                                                Toast.makeText(LG.this, "This account is attached with "+temp.getBranch(), Toast.LENGTH_LONG).show();
                                             //  setUpUserData("faculty");
                                                mprogressbar.dismiss();
                                                FirebaseAuth.getInstance().signOut();

                                            }




                                        }
                                        else {
                                            FirebaseUser firebaseUser1 = FirebaseAuth.getInstance().getCurrentUser();
                                            user userOB  = new user();
                                            userOB.setName(firebaseUser1.getDisplayName());
                                            userOB.setEmail(firebaseUser1.getEmail());
                                            userOB.setBranch(ubranch);
                                            userOB.setPhotoId(firebaseUser1.getPhotoUrl().toString());

                                            FirebaseDatabase.getInstance().getReference().child(collegePOJO_ob.getCollege_name()).child("Faculty").child(FirebaseAuth.getInstance().getCurrentUser().getUid() ).setValue(userOB).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                      //  FirebaseAuth.getInstance().signOut();


                                                        //Toast.makeText(LG.this, cd.getResource("$^&t&z&%&f&w&j&%&s&t&y&%&{&j&w&n&k&n&j&i&%&f&x&%&f&%&k&f&h&z&q&y&~&%"), Toast.LENGTH_LONG).show();
                                                        toVerifyActivity =true;
                                                        setUpUserData("faculty");
                                                        verifyActivity();

                                                    }
                                                    else
                                                    {
                                                        FirebaseAuth.getInstance().signOut();
                                                        mprogressbar.dismiss();
                                                        Toast.makeText(LG.this, "Something went wrong . Try again", Toast.LENGTH_LONG).show();




                                                    }
                                                }
                                            });








                                          //  FirebaseAuth.getInstance().signOut();



                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });



                               }
                            else
                                setUpUserData("student");


                           /* Thread thread2 = new Thread(new thread());
                            thread2.start();*/
                            /*mRootRef.child("users").child(uid).child("branch").setValue(ubranch);
                            mRootRef.child("users").child(uid).child("year").setValue(uyear);
                            mRootRef.child("users").child(uid).child("photoId").setValue("demophotoId");*/


                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "**************************************signInWithCredential:failure", task.getException());

                            Toast.makeText(LG.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();


                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void verifyActivity() {

        SharedPreferences pref= getSharedPreferences("v", Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor edt = pref.edit();

        edt.putBoolean("f",true);
         if (edt.commit()) {
             Intent intent = new Intent(LG.this, V.class);
             intent.putExtra("flag",true);
             startActivity(intent);
             finish();
         }


    }
/*
    class thread implements Runnable {
        @Override
        public void run() {



            Log.d("image", Photouri + "");

            byte data[] = Photouri.toString().getBytes();
            URL url = null;
            ByteArrayOutputStream baos = null;
            InputStream is = null;
            String s = "";

            try {
                url = new URL(Photouri.toString());
                URLConnection urlConnection = url.openConnection();
                is = urlConnection.getInputStream();

*//*
                int nRead;

                while ((nRead = is.read()) != -1) {
                    s = s + (char) nRead;
                }

                // baos = new ByteArrayOutputStream();
                data = s.getBytes();*//*


            } catch (IOException e) {

                System.err.printf("Failed while reading bytes from %s: %s", url.toExternalForm(), e.getMessage());
                e.printStackTrace();
                // Perform any other exception handling that's appropriate.
            } finally {

            }


        *//*Uri tempuri = Uri.parse(Photouri.toString());
        File file  =new File(tempuri.getPath());
        Toast.makeText(this, ""+file, Toast.LENGTH_SHORT).show();
        Bitmap original = null;
        try {
            original = BitmapFactory.decodeStream(getAssets().open(Photouri.toString()));
        } catch (IOException e) {
            Toast.makeText(this, "Error in catch", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        original.compress(Bitmap.CompressFormat.JPEG, 100, out);
        Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
        byte[] data = out.toByteArray();*//*
       *//* Bitmap compressed_bitmap=null;
        try {
           Compressor t = new Compressor(LG.this);
                  //  t.setMaxWidth(200);
                  //  t.setMaxHeight(200);
                  //  t.setQuality(75);
                  compressed_bitmap = t.compressToBitmap(file);
            Toast.makeText(this, ""+compressed_bitmap, Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }

       // assert compressed_bitmap != null;
     try {
         boolean compress = compressed_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
     }catch (Exception e){
         Log.d("asad", "compresss vlalue");
     }*//*
            //ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // byte[] data = baos.toByteArray();

            dataStorageRef = rootStorageRef.child("profiles_images").child(uid + ".jpg");

            final UploadTask uploadTask = dataStorageRef.putStream(is);

            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    String image_download_url = uploadTask.getResult().getDownloadUrl().toString();
                    if (uploadTask.isSuccessful()) {










                    }
                }
            });

        }*/

    public void setUpUserData(String userType){
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        final String uname = firebaseUser.getDisplayName();
        final String uemail = firebaseUser.getEmail();
        final String uid = firebaseUser.getUid();
        Uri Photouri = firebaseUser.getPhotoUrl();
        final user u2 = new user();
        u2.setName(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        u2.setUserType(userType);
        u2.setEmail(uemail);
        u2.setPhotoId(Photouri.toString());
        u2.setBranch(ubranch);
        u2.setCollege(collegePOJO_ob.getCollege_name());
        u2.setToken(collegePOJO_ob.getWebsite());
        u2.setCid(collegePOJO_ob.getCid());
        String token = FirebaseInstanceId.getInstance().getToken();

        if (userType.equals("student")) {


            u2.setSection(usection);
          u2.setYear(uyear);
          u2.setCourse(ucourse);

          sharedData sharedData_ob = new sharedData();
          sharedData_ob.saveUserData(u2, LG.this);

          UnsuscribeToAllTopics();
          FirebaseMessaging messaging = FirebaseMessaging.getInstance();
          messaging.subscribeToTopic("ALL");
          messaging.subscribeToTopic(ucourse);
          messaging.subscribeToTopic(collegePOJO_ob.getTopicName());

          messaging.subscribeToTopic(ubranch);

          messaging.subscribeToTopic(uyear);
          messaging.subscribeToTopic(usection);

          user u = new user(uname, ubranch, uyear, Photouri.toString(), token, usection, "SVVV");
          u.setCourse(ucourse);
          u.setCollege(collegePOJO_ob.getCollege_name());
          u.setUserType(userType);
          u.setEmail(uemail);
          mRootRef.child("users").child(uid).setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
              @Override
              public void onComplete(@NonNull Task<Void> task) {
                  if (task.isSuccessful()) {

                      mprogressbar.dismiss();
                      Intent intent = new Intent(LG.this, MA.class);
                      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                      startActivity(intent);

                      finish();
                  } else {
                      Toast.makeText(LG.this, "Error try again", Toast.LENGTH_SHORT).show();

                  }
              }
          });
      }
      else{  user Fuser  = new user();
      Fuser.setName(uname);
      Fuser.setEmail(uemail);
      Fuser.setPhotoId(Photouri.toString());
      Fuser.setBranch(ubranch);
      Fuser.setUserType("faculty");
      Fuser.setToken(token);
      Fuser.setCollege(collegePOJO_ob.getCollege_name());
            mRootRef.child("users").child(uid).setValue(Fuser).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        sharedData sharedData_ob = new sharedData();
                        sharedData_ob.saveUserData(u2, LG.this);
                        mprogressbar.dismiss();
                      if (!toVerifyActivity) {
                          Intent intent = new Intent(LG.this, MA.class);
                          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                          startActivity(intent);

                          finish();
                      }

                    }else {
                        Toast.makeText(LG.this, "Error try again", Toast.LENGTH_SHORT).show();


                    }
                }
            });



      }

    }


    public void UnsuscribeToAllTopics(){
        FirebaseMessaging messaging= FirebaseMessaging.getInstance();


        for (String branch:branch_array){
            if (!branch.equals("Select Branch"))
            messaging.unsubscribeFromTopic(branch);

        }

        for (String year :year_array){
            if (!year.equals("Select Year"))
            messaging.unsubscribeFromTopic(year);
        }
        for (String course :course_array){
            if (!course.equals("Select Course"))
            messaging.unsubscribeFromTopic(course);
        }
        for (String section :section_array){
            if (!section.equals("Select Section"))
            messaging.unsubscribeFromTopic(section);

        }
        for (String cllgTopic :collegeTopic_array){
            messaging.unsubscribeFromTopic(cllgTopic);

        }

    }
    void fbLoginButtonEnabler (){





        if (checkBox.isChecked()){
         if (flag2 && flag3){
             loginButton.setEnabled(true);
             signInButton.setEnabled(true);


         } else {
             loginButton.setEnabled(false);
             signInButton.setEnabled(false);


         }


        }
        else {
            if (flag1 && flag2 && flag4 && flag5 && flag3) {
                loginButton.setEnabled(true);
                signInButton.setEnabled(true);

            } else {
                loginButton.setEnabled(false);
                signInButton.setEnabled(false);
            }
        }
    }

}












