package com.example.admin.mysvvvappbeta.O;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.admin.mysvvvappbeta.J.cd;
import com.example.admin.mysvvvappbeta.R;
import com.example.admin.mysvvvappbeta.models.post;
import com.example.admin.mysvvvappbeta.models.user;
import com.example.admin.mysvvvappbeta.UI.sharedData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.soundcloud.android.crop.Crop;

import java.io.File;

/**
 * Created by Asad Mirza on 23-12-2017.
 */

public class PDA extends AppCompatActivity {


    private ProgressDialog progressDialog;
    private Button imageButton;
    private EditText title, dis, link1, link2, link3;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private boolean connected = false;
    private ImageView imageView;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mRootRef, mDataRef;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private user userOB;

    private static final int Gallery_request = 1;
    private Uri image_uri;
    private Uri downloadUrl;
    private post p;
    private String p_title, p_dis, p_sender_uid, p_time, p_sendername, p_senderdp;
    private boolean isUp = false;

    //private CheckBox checkBox;
    //private Button dateButton,timeButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_to_dashboard);

        imageButton = findViewById(R.id.image_btn);
        title = (EditText) findViewById(R.id.title);
        dis = (EditText) findViewById(R.id.dis);
        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle(cd.getResource("$F&i&i&%&J&{&j&s&y&%"));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //  checkBox = findViewById(R.id.checkbox);
       // dateButton = findViewById(R.id.dateButton);
       // timeButton = findViewById(R.id.timeButton);

        link1 = (EditText) findViewById(R.id.link1);
        imageView = findViewById(R.id.imageView);
//        link2 = (EditText) findViewById(R.id.link2);
//        link3 = (EditText) findViewById(R.id.link3);

        p = new post();
        sharedData sharedData_ob = new sharedData();
        userOB =sharedData_ob.getUserData(PDA.this);



        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                connected = snapshot.getValue(Boolean.class).booleanValue();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();

        mRootRef = firebaseDatabase.getReference().child(userOB.getCollege()).child("D");



        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, Gallery_request);*/
                Crop.pickImage(PDA.this);

            }
        });
        /*checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkBox.isChecked()) {
                    dateButton.setVisibility(View.VISIBLE);
                    timeButton.setVisibility(View.VISIBLE);
                }
                else
                {
                    dateButton.setVisibility(View.INVISIBLE);
                    timeButton.setVisibility(View.INVISIBLE);


                }
            }
        });*/

     /*   dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(),"Date Picker");

            }
        });
*/


    }
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.post_to_dashboard_menu, menu);

    return  true;
    }
    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.submit_icon) {

            progressDialog = new ProgressDialog(PDA.this);
            progressDialog.setMessage("Uploading Event...");

            progressDialog.show();
            boolean URLisValid = true;
            if (!isUp)
            {isUp = true;
            if (connected) {
                String p_link1 = link1.getText().toString();
                if (TextUtils.isEmpty(p_link1)) {
                    p_link1 = null;

                }
                if (p_link1 != null)
                    URLisValid = Patterns.WEB_URL.matcher(p_link1).matches();


                firebaseAuth = FirebaseAuth.getInstance();
                firebaseUser = firebaseAuth.getCurrentUser();

                if (firebaseUser != null) {


                    firebaseStorage = FirebaseStorage.getInstance();

                    p_sender_uid = firebaseUser.getUid();
                    p_sendername = firebaseUser.getDisplayName();
                     p_senderdp = firebaseUser.getPhotoUrl().toString();
                    p.setSender_dp(p_senderdp);


                    /*     firebaseStorage.getReference().child("profiles_images").child(p_sender_uid + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            p_senderdp = uri.toString();
                            p.setSender_dp(p_senderdp);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PDA.this, "Please try again", Toast.LENGTH_SHORT).show();
                        }
                    });*/


                    //   final String  p_link2 = link2.getText().toString();
                    //  final String p_link3 = link3.getText().toString();


                    p_title = title.getText().toString();
                    p_dis = dis.getText().toString();
                    if (!TextUtils.isEmpty(p_title) && !TextUtils.isEmpty(p_dis) && image_uri != null) {
                        if (URLisValid) {
                            p.setTitle(p_title);
                            p.setDis(p_dis);
                            p.setLink1(p_link1);
                            // p.setLink2(p_link2);
                            // p.setLink3(p_link3);
                            p.setSender_uid(p_sender_uid);

                            p.setSender_name(p_sendername);


                            //


                            final String key = mRootRef.child("temp").push().getKey();
                            p.setP_id(key);
                            //mRootRef.child("posts_times").child(key).setValue(ServerValue.TIMESTAMP);

                            p.setTime(ServerValue.TIMESTAMP);

                            storageReference = firebaseStorage.getReference().child(userOB.getCollege()).child("dashboard").child("E").child(key + image_uri.getLastPathSegment());
                            UploadTask uploadTask = storageReference.putFile(image_uri);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    progressDialog.dismiss();
                                    Toast.makeText(PDA.this, "Please try again", Toast.LENGTH_SHORT).show();
                                }
                            })
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                            downloadUrl = taskSnapshot.getDownloadUrl();
                                            p.setImage_uri(downloadUrl.toString());
                                            p.setStatus("no");


                                            mRootRef.child("SE").child(p_sender_uid).child(key).setValue(p).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {

                                                        mRootRef.child("E").child(key).setValue(p).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                             if (task.isSuccessful()){
                                                                 progressDialog.dismiss();
                                                                 AlertDialog.Builder builder = new AlertDialog.Builder(PDA.this);
                                                                 builder.setCancelable(false);
                                                                 builder.setTitle("Event Successfully Uploaded ");
                                                                 builder.setMessage("It will be verified by us before publishing.This may take some time");
                                                                 builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                     @Override
                                                                     public void onClick(DialogInterface dialog, int which) {
                                                                         finish();
                                                                     }
                                                                 });
                                                                 AlertDialog alertDialog = builder.create();
                                                                 alertDialog.show();
                                                                 isUp = false;



                                                             }   else {
                                                                 isUp = false;
                                                                 progressDialog.dismiss();
                                                                 Toast.makeText(PDA.this, "Error please try again", Toast.LENGTH_SHORT).show();


                                                             }
                                                            }
                                                        });





                                                    } else {
                                                        isUp = false;
                                                        progressDialog.dismiss();
                                                        Toast.makeText(PDA.this, "Error please try again", Toast.LENGTH_SHORT).show();
                                                    }


                                                }
                                            });
                                        }
                                    });
                        } else {
                            link1.setError("Invalid URL");
                            progressDialog.dismiss();
                            isUp = false;
                        }


                    }//empty
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(PDA.this, "Event Name,Description and Image are Mandatory", Toast.LENGTH_SHORT).show();
                        isUp = false;
                    }


                }//fu
                else {
                    progressDialog.dismiss();
                    Toast.makeText(PDA.this, "Error try again ", Toast.LENGTH_SHORT).show();
                    isUp = false;
                }


            }//c


            else {
                progressDialog.dismiss();
                Toast.makeText(PDA.this, "No internet connection", Toast.LENGTH_SHORT).show();
                isUp = false;
            }
            }
            else // isup
            {      progressDialog.dismiss();
                Toast.makeText(PDA.this, "One Event is Already uploading...", Toast.LENGTH_SHORT).show();


            }


        }
        return  true;

    }

 /*   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gallery_request && resultCode == RESULT_OK) {

            image_uri = data.getData();
            CropImage();
            File f = new File(image_uri + "");

            imageButton.setText("1 image selected");
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageURI(image_uri);

        } else {
            image_uri = null;
            imageButton.setText("Upload Image");
            Toast.makeText(PDA.this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }


    }*/



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            if(requestCode ==Crop.REQUEST_PICK){
                Uri uri=data.getData();
                Uri duri=Uri.fromFile(new File(getCacheDir(),"cropped"));

              //  Crop.of(uri,duri).asSquare().start(this);
                Crop.of(uri,duri).withAspect(4,5).start(this);
             image_uri= Crop.getOutput(data);
                imageView.setImageURI(Crop.getOutput(data));
            }
            else if (requestCode==Crop.REQUEST_CROP){
                handle_crop(resultCode,data);
            }
        }
    }

    private void handle_crop(int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            imageView.setVisibility(View.VISIBLE);
            image_uri=Crop.getOutput(data);
            imageView.setImageURI(Crop.getOutput(data));
        }
        else if(resultCode==Crop.RESULT_ERROR){
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }
    }



    /*public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
             {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            DatePickerFragment fragment  = new DatePickerFragment();
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it

            return new DatePickerDialog(getActivity(),fragment, year, month, day);
        }


                 @Override
                 public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                     Toast.makeText(getActivity(), i+i1+i2, Toast.LENGTH_SHORT).show();
                 }
             }
*/
}