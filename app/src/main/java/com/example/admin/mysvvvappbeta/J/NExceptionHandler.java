package com.example.admin.mysvvvappbeta.J;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;

import com.example.admin.mysvvvappbeta.R;
import com.example.admin.mysvvvappbeta.models.noticePOJO;
import com.example.admin.mysvvvappbeta.models.sectionPOJO;
import com.example.admin.mysvvvappbeta.ADA.post_chat_adapter;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Layout;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileDescriptor;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class NExceptionHandler extends AppCompatActivity implements Serializable {

    private FirebaseApp department;
    private Intent intent;
    private StorageTask uploadtask;
    static private final String sharedPreName = "USER_PROFILE";
    private ImageButton imageButton, button, imagemenu, docsmenu, cameramenu, delete;
    private EditText editText;
    private DatabaseReference mrootref, databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FrameLayout framer;
    private TextView uploading, percent, tname, tsize;
    private ImageView imageView;
    private ProgressBar progressBar;
    private String clgname, uid, course, branch, year, section, name, time, key, size;
    private int REQUEST_CODE = 1000, n = 0, REQUEST_CODE_2 = 1001, REQUEST_CODE_3 = 1003;

    private Uri selectedImage, docs;
    private boolean connected = false, flagimage = false, flagdoc = false;
    private RelativeLayout lay;
    private sectionPOJO s;
    private RelativeLayout linear, data_linear;
    private RecyclerView recyclerView;
    private post_chat_adapter post_chat_adapter_obj;
    private ArrayList<noticePOJO> data;
    private ChildEventListener childEventListener;
    private long bytes;
    private RotateAnimation ar, ra,r;
private Toolbar Tbar;

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mrootref.child(clgname)
                .child("notice_board")
                .child(course)
                .child(branch)
                .child(year)
                .child(section).removeEventListener(childEventListener);
    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_nexception_handler);
        intent = getIntent();
        s = (sectionPOJO) intent.getSerializableExtra("obj");

        Tbar = findViewById(R.id.toolbar); // Attaching the layout to the toolbar object

        Tbar.setTitle(s.getBranch()+" "+s.getSection());
        setSupportActionBar(Tbar);

        Tbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //''''''' alertdialog will be generate herer in it . since back button is permanently disabled
                if(lay.getVisibility() ==lay.VISIBLE){
                    final android.support.v7.app.AlertDialog.Builder builder=new android.support.v7.app.AlertDialog.Builder(NExceptionHandler.this);
                    builder.setTitle("Action");
                    builder.setMessage("Want to cancel the uploading?");

                    builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // work regarding the pausing a download??
                            uploadtask.cancel();
                            lay.setVisibility(View.GONE);
                            button.setEnabled(true);
                            imageButton.setEnabled(true);
                            editText.setEnabled(true);
                            Toast.makeText(NExceptionHandler.this, "Uploading Cancelled", Toast.LENGTH_SHORT).show();
                        }
                    });
                 builder.show();
                }else{
                    onBackPressed();
                }

            }
        });


        // Setting toolbar as the ActionBar with setSupportActionBar() call
        imagemenu = findViewById(R.id.menu1);
        docsmenu = findViewById(R.id.menu2);
        cameramenu = findViewById(R.id.menu3);
        delete = findViewById(R.id.delete);
        framer = findViewById(R.id.frame);
        percent = findViewById(R.id.percentage);
        progressBar = findViewById(R.id.progressbar);
        linear = findViewById(R.id.linear);
        recyclerView = findViewById(R.id.recyclerview);
        data = new ArrayList<>();
        post_chat_adapter_obj = new post_chat_adapter(data, NExceptionHandler.this);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(post_chat_adapter_obj);


        data_linear = findViewById(R.id.data);

        button = findViewById(R.id.next);
        uploading = findViewById(R.id.upload);
        lay = findViewById(R.id.linear2);
        imageView = findViewById(R.id.imageView);

        tsize = findViewById(R.id.vsize);
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.circular);
        progressBar = findViewById(R.id.circularProgressbar);
        progressBar.setProgress(0);// Main Progress
        progressBar.setClickable(false);
        progressBar.setMax(100); // Maximum Progress
        progressBar.setProgressDrawable(drawable);
        progressBar.setSecondaryProgress(100);

        editText = findViewById(R.id.notice_dis);

        editText.setVerticalScrollBarEnabled(true);
        editText.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);


      /*  ScrollView scroller = new ScrollView(this);
        scroller.addView(revisedtext);*/


        imageButton = findViewById(R.id.notice_image);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();


        // getting the values in activity====
        course = s.getCourse();
        branch = s.getBranch();
        year = s.getYear();
        section = s.getSection();
        SharedPreferences sp = getSharedPreferences(sharedPreName, MODE_MULTI_PROCESS);
        clgname = sp.getString("college", null);
       name= FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        //====


//======   it is an animation code=========================
        rotater();
//======================================


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete.startAnimation(r);
                selectedImage = null;
                docs = null;

                if(flagimage) {
                    flagimage = false;
                    Toast.makeText(NExceptionHandler.this, "Selected image removed", Toast.LENGTH_SHORT).show();
                }else {
                    flagdoc = false;
                    Toast.makeText(NExceptionHandler.this, "Selected file removed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        connection();
        mrootref = firebaseDatabase.getReference();
        databaseReference = mrootref.child(clgname)
                .child("notice_board")
                .child(course)
                .child(branch)
                .child(year)
                .child(section);
        databaseReference.keepSynced(true);


        meth();   // method represting recyclerview


//attachment button======================
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {     //   providing attachment

                if (framer.getVisibility() == framer.VISIBLE) {
                    imageButton.startAnimation(ar);
//                     imagemenu.startAnimation(r);
//                     cameramenu.startAnimation(r);
//                     docsmenu.startAnimation(r);
                    if (flagimage || flagdoc) {
                        data_linear.setVisibility(View.VISIBLE);
                    }
                    //    framer.setVisibility(View.GONE);
                } else {

                    if (flagimage || flagdoc) {
                        data_linear.setVisibility(View.GONE);
                    }
                    imageButton.startAnimation(ra);
                    //   framer.setVisibility(View.VISIBLE);

                }
            }
        });
        ////////   MY CODE     regarding attachment

        imagemenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE);
            }
        });


        docsmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] mime={"application/msword",
                        "application/vnd.openxmlformats-officedocument.wordprocessing.document",
                        "application/pdf",
                        "text/plain",
                        "application/ppt"};
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType(mime.length==1 ? mime[0]:"*/*");
                if(mime.length>0){
                    intent.putExtra(Intent.EXTRA_MIME_TYPES,mime);
                }

                startActivityForResult(Intent.createChooser(intent, "Select files"), REQUEST_CODE_2);


                //
//                File file=new File("name");
//                intent=new Intent();
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.setAction(Intent.ACTION_VIEW);
//                String type="application/msword/pdf/vnd.ms-powerpoint/vnd.ms-excel";
//                intent.setDataAndType(Uri.fromFile(file),type);
//                startActivityForResult(Intent.createChooser(intent,"Select files"),REQUEST_CODE_2);

            }
        });


        cameramenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, REQUEST_CODE_3);
            }
        });

        /// ???????

///=============================

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connected) {
                    final long val=50000000;
                    final String dis = editText.getText().toString();
                        if (!TextUtils.isEmpty(dis)) {
                            editText.getText().clear();
                            button.setEnabled(false);
                            imageButton.setEnabled(false);
                            editText.setEnabled(false);
                            if (!flagdoc && !flagimage) {
                                text(dis);
                            } else {
                                if (flagdoc && !flagimage) {

                                    if(bytes <= val) {
                                        pdf(docs, dis);
                                    }else{
                                        Toast.makeText(NExceptionHandler.this, "File Size is greater then 50MB", Toast.LENGTH_SHORT).show();
                                        button.setEnabled(true);
                                        imageButton.setEnabled(true);
                                        editText.setEnabled(true);

                                    }


                                }
                                if (!flagdoc && flagimage) {
                                    image(dis);
                                }
                            }


                        } else {
                            Toast.makeText(NExceptionHandler.this, "Description is mandatory", Toast.LENGTH_SHORT).show();
                        }


                        ///  all type of uploading goes their

                   }else{
                    Toast.makeText(NExceptionHandler.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            imageButton.startAnimation(ar);
            selectedImage = data.getData();
            data_linear.setVisibility(View.VISIBLE);
            docs = null;
            flagdoc = false;
            imageView.setImageURI(selectedImage);
            details(selectedImage, tsize);
            if (selectedImage != null) {
                framer.setVisibility(View.GONE);
            }

            flagimage = true;

        }
        if (requestCode == REQUEST_CODE_2 && resultCode == Activity.RESULT_OK) {
            imageButton.startAnimation(ar);
            docs = data.getData();
            data_linear.setVisibility(View.VISIBLE);
            imageView.setImageDrawable(getDrawable(R.drawable.docs));
            selectedImage = null;
            flagimage = false;
            size = details(docs, tsize);
            if (docs != null) {
                framer.setVisibility(View.GONE);
            }
            flagdoc = true;
        }
        if (requestCode == REQUEST_CODE_3 && resultCode == Activity.RESULT_OK) {
            imageButton.startAnimation(ar);
            selectedImage = data.getData();
            data_linear.setVisibility(View.VISIBLE);
            docs = null;
            flagdoc = false;
            details(selectedImage, tsize);
            imageView.setImageURI(selectedImage);
            flagimage = true;

            if (selectedImage != null) {
                framer.setVisibility(View.GONE);
            }
        }


    }


// all the methods to be used in an activity


    public void connection() {
        mrootref = FirebaseDatabase.getInstance().getReference(".info/connected");
        mrootref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                connected = snapshot.getValue(Boolean.class);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Something went wrong");
            }
        });
    }

    public void meth() {

        childEventListener = databaseReference
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        noticePOJO n = dataSnapshot.getValue(noticePOJO.class);
                        data.add(n);
                        post_chat_adapter_obj.notifyDataSetChanged();
                        recyclerView.scrollToPosition(data.size() - 1);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        //                       data.clear();
                        //                     meth();

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }

    public void hidekey() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        assert inputManager != null;
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

    }


    public void pdf(Uri file, final String dis) {


        flagdoc = false;
        key = mrootref.child("fke").push().getKey();
        hidekey();
        data_linear.setVisibility(View.GONE);
        storageReference = firebaseStorage.getReference();
      uploadtask=  storageReference.child(clgname)              // have added uploadtask to check its working
                .child("notice_board")
                .child(key + file.getLastPathSegment())
                .putFile(file)
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        lay.setVisibility(View.VISIBLE);
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressBar.setProgress((int) progress);
                        String per = String.valueOf((int) progress) + "%";
                        percent.setText(per);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(uploadtask.isCanceled()){

                        }else{
                            Toast.makeText(NExceptionHandler.this, "Something went wrong Try again later", Toast.LENGTH_SHORT).show();
                        }

                        lay.setVisibility(View.GONE);
                        button.setEnabled(true);
                        imageButton.setEnabled(true);
                        editText.setEnabled(true);

                    }
                })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
// listener reageding text is  runing
                        String url = taskSnapshot.getDownloadUrl().toString();
                        noticePOJO n = new noticePOJO();
                        n.setDis(dis);
                        n.setSender_name(name);
                        n.setImage_url(url);
                        n.setTime(ServerValue.TIMESTAMP);
                        n.setType("doc");
                        n.setSize(size);
                        mrootref.child(clgname)
                                .child("notice_board")
                                .child(course)
                                .child(branch)
                                .child(year)
                                .child(section)
                                .push()
                                .setValue(n)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            button.setEnabled(true);
                                            imageButton.setEnabled(true);
                                            editText.setEnabled(true);
                                            lay.setVisibility(View.GONE);

                                            Toast.makeText(NExceptionHandler.this, cd.getResource("$S&t&y&n&h&j&%&X&j&s&y&%"), Toast.LENGTH_SHORT).show();
                                        } else {
                                            lay.setVisibility(View.GONE);
                                            button.setEnabled(true);
                                            imageButton.setEnabled(true);
                                            editText.setEnabled(true);
                                            Toast.makeText(NExceptionHandler.this, "Please Try again", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });
    }


    public void text(String dis) {
        noticePOJO n = new noticePOJO();
        n.setDis(dis);
        n.setSender_name(name);
        n.setTime(ServerValue.TIMESTAMP);
        n.setType("text");
        mrootref.child(clgname)
                .child("notice_board")
                .child(course)
                .child(branch)
                .child(year)
                .child(section)
                .push()
                .setValue(n)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            button.setEnabled(true);
                            imageButton.setEnabled(true);
                            editText.setEnabled(true);
                            Toast.makeText(NExceptionHandler.this,cd.getResource("$S&t&y&n&h&j&%&X&j&s&y&%"), Toast.LENGTH_SHORT).show();
                        } else {
                            button.setEnabled(true);
                            imageButton.setEnabled(true);
                            editText.setEnabled(true);
                            Toast.makeText(NExceptionHandler.this, "Please Try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public void image(final String dis) {
        flagimage = false;
//    Animation b = AnimationUtils.loadAnimation(NExceptionHandler.this, R.anim.fadeout);
//    b.reset();
//
//    data_linear.startAnimation(b);
//    linear.startAnimation(b);
        //button.startAnimation(b);
        hidekey();
        data_linear.setVisibility(View.GONE);

        storageReference = firebaseStorage.getReference();
        storageReference.child(clgname)
                .child("notice_board")
                .child(key + selectedImage.getLastPathSegment())
                .putFile(selectedImage)
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        //  lay.startAnimation(a);
                        lay.setVisibility(View.VISIBLE);
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressBar.setProgress((int) progress);

                        String per = String.valueOf((int) progress) + "%";
                        percent.setText(per);

                    }
                })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String url = taskSnapshot.getDownloadUrl().toString();
                        selectedImage = null;
                        noticePOJO n = new noticePOJO();
                        n.setType("image");
                        n.setImage_url(url);
                        n.setSender_name(name);
                        n.setDis(dis);
                        n.setTime(ServerValue.TIMESTAMP);
                        mrootref.child(clgname)
                                .child("notice_board")
                                .child(course)
                                .child(branch)
                                .child(year)
                                .child(section)
                                .push()
                                .setValue(n)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            lay.setVisibility(View.GONE);
                                            button.setEnabled(true);
                                            imageButton.setEnabled(true);
                                            editText.setEnabled(true);
                                            Toast.makeText(NExceptionHandler.this, cd.getResource("$S&t&y&n&h&j&%&X&j&s&y&%"), Toast.LENGTH_SHORT).show();
                                        } else {
                                            lay.setVisibility(View.GONE);
                                            button.setEnabled(true);
                                            imageButton.setEnabled(true);
                                            editText.setEnabled(true);
                                            Toast.makeText(NExceptionHandler.this, "Please try again", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               if(uploadtask.isCanceled()){

               }else{
                   Toast.makeText(NExceptionHandler.this, "Something went wrong Try again later", Toast.LENGTH_SHORT).show();
               }

                lay.setVisibility(View.GONE);
                button.setEnabled(true);
                imageButton.setEnabled(true);
                editText.setEnabled(true);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public String details(Uri file, TextView vsize) {
        Cursor r = getContentResolver().query(file, null, null
                , null, null);
        assert r != null;
        int sizeindex = r.getColumnIndex(OpenableColumns.SIZE);
        r.moveToFirst();
        bytes=r.getLong(sizeindex);
        String[]types={"kb","Mb","GB","TB","PB","EB"};
        int unit=1000;
        if(bytes < unit)
            vsize.setText(bytes +"bytes");
        int exp=(int)(Math.log(bytes)/Math.log(unit));
        String  val=String.format("%.1f",bytes/Math.pow(unit,exp))+types[exp-1];
        vsize.setText(val);

        return Long.toString(r.getLong(sizeindex));
    }

    private void rotater(){
        r = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        r.setFillAfter(true);
        r.setDuration(500);
        r.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                data_linear.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });




        ra = new RotateAnimation(0.0f, 180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setFillAfter(true);
        ra.setDuration(200);
        ar = new RotateAnimation(180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ar.setFillAfter(true);
        ar.setDuration(200);
        ar.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                framer.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ra.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                framer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }




    @Override
    public void onBackPressed() {

        if(lay.getVisibility()==lay.VISIBLE){
            Toast.makeText(this, "Documents are uploading please wait...", Toast.LENGTH_SHORT).show();
        }else{
            super.onBackPressed();
        }

    }
}