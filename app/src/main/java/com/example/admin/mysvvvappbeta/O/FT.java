package com.example.admin.mysvvvappbeta.O;

/**
 * Created by Asad Mirza on 02-03-2018.
 */

        import android.app.Activity;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.graphics.drawable.Drawable;
        import android.net.Uri;
        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.support.annotation.Nullable;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.view.animation.Animation;
        import android.view.animation.AnimationUtils;
        import android.widget.Button;
        import android.widget.ImageButton;
        import android.widget.ImageView;
        import android.widget.ProgressBar;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;
        import android.support.v7.widget.Toolbar;


        import com.bumptech.glide.load.DataSource;
        import com.bumptech.glide.load.engine.GlideException;
        import com.bumptech.glide.request.RequestListener;
        import com.bumptech.glide.request.target.Target;
        import com.example.admin.mysvvvappbeta.GlideApp;
        import com.example.admin.mysvvvappbeta.R;
        import com.example.admin.mysvvvappbeta.models.sectionPOJO;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
        import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.OnProgressListener;
        import com.google.firebase.storage.StorageReference;
        import com.google.firebase.storage.UploadTask;

/**
 * Created by CrYsIs on 10-01-2018.
 */

public class FT extends AppCompatActivity {
    private ImageView imageView;
    private ImageButton imageButton;
    private Button button;

    private Intent intent;
    private Animation b;
    private TextView textView;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private StorageReference storageReference,mref,retrieve;
    private FirebaseStorage firebaseStorage;
    private Uri selectedImage;
    private RelativeLayout lay;
    private ProgressBar progressBar;

    private String clgname, course, branch, year, section, previous;
    private boolean flag1 = false, connected = false;
    private int REQUEST_CODE = 1000;
    private Toolbar toolbar;
    private TextView noTimeTableTextView ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable);
     /*   toolbar = findViewById(R.id.time_bar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbar.setTitle("Time Table");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/
        Toolbar toolbar = findViewById(R.id.toolbar);
        lay = findViewById(R.id.linear2);
        progressBar = findViewById(R.id.progress_bar);
        button = findViewById(R.id.publish);
        progressBar = findViewById(R.id.circularProgressbar);
        textView = findViewById(R.id.texturi);
        imageView = findViewById(R.id.imageview);
        imageButton = findViewById(R.id.time_table_image);
        noTimeTableTextView = findViewById(R.id.wrn);
        SharedPreferences sp = getSharedPreferences("USER_PROFILE", MODE_MULTI_PROCESS);
        clgname = sp.getString("college", null);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        intent = getIntent();
        sectionPOJO s = (sectionPOJO) intent.getSerializableExtra("obj");
        course = s.getCourse();
        branch = s.getBranch();
        year = s.getYear();
        section = s.getSection();
        connecting();
        extract();
        toolbar.setTitle(branch+" "+section+" Time Table");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                b = AnimationUtils.loadAnimation(FT.this, R.anim.fadeout);
                b.reset();
                if (connected) {
                    databaseReference = firebaseDatabase.getReference();
                    storageReference = firebaseStorage.getReference();
                    if (flag1) {
                        final String key = databaseReference.child("fke").push().getKey();
                        mref=storageReference.child(clgname)
                                .child("Time_Table")
                                .child(course)
                                .child(branch)
                                .child(year)
                                .child(section)
                                .child(key + selectedImage.getLastPathSegment());

                        mref.putFile(selectedImage)
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                        lay.setVisibility(View.VISIBLE);
                                        lay.startAnimation(b);
                                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                        progressBar.setProgress((int) progress);
                                        button.setVisibility(View.INVISIBLE);
                                    }
                                })
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        String url = taskSnapshot.getDownloadUrl().toString();
                                        if (url != null) {
                                            databaseReference.child(clgname)
                                                    .child("Time_Table")
                                                    .child(course)
                                                    .child(branch)
                                                    .child(year)
                                                    .child(section)
                                                    .setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        Toast.makeText(FT.this, "Time_Table Sucessfully Uploaded", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    } else {
                                                        Toast.makeText(FT.this, "Please Try Again", Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            });
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(FT.this, "Please Try Again", Toast.LENGTH_SHORT).show();
                            }
                        });


                    } else {
                        Toast.makeText(FT.this, "No Image Selected ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(FT.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }


        });
    /*    if(clgname!=null){
          extract();}
    }*/
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            selectedImage = data.getData();
            imageView.setImageURI(selectedImage);
            textView.setText(selectedImage.toString());

            flag1 = true;

        } else {
            flag1 = false;
        }
    }
    @Override
    public void onBackPressed() {
        finish();
    }
    public void connecting() {
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                connected = snapshot.getValue(Boolean.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });
    }

    public void extract() {
        FirebaseDatabase.getInstance().getReference().child(clgname)
                .child("Time_Table")
                .child(course)
                .child(branch)
                .child(year)
                .child(section).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                previous = dataSnapshot.getValue(String.class);

               if (previous!=null) {
                   GlideApp.with(FT.this)
                           .load(previous)
                           .centerCrop()
                           .listener(new RequestListener<Drawable>() {
                               @Override
                               public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                   progressBar.setVisibility(View.INVISIBLE);
                                   return false;
                               }

                               @Override
                               public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                   progressBar.setVisibility(View.INVISIBLE);
                                   textView.setText("Update Time Table");
                                   return false;
                               }
                           })
                           .into(imageView);
               }
               else{
                   progressBar.setVisibility(View.INVISIBLE);


               }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
