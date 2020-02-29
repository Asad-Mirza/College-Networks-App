package com.example.admin.mysvvvappbeta;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.util.SortedList;
import android.support.v7.widget.Toolbar;

import android.content.Intent;

import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.admin.mysvvvappbeta.J.ME;
import com.example.admin.mysvvvappbeta.J.cd;
import com.example.admin.mysvvvappbeta.L.LGC;
import com.example.admin.mysvvvappbeta.O.PDA;
import com.example.admin.mysvvvappbeta.O.hang_adapter;
import com.example.admin.mysvvvappbeta.T.E;
import com.example.admin.mysvvvappbeta.T.E2;
import com.example.admin.mysvvvappbeta.T.F;
import com.example.admin.mysvvvappbeta.T.SC;
import com.example.admin.mysvvvappbeta.UI.HF;
import com.example.admin.mysvvvappbeta.UI.NF;

import com.example.admin.mysvvvappbeta.UI.sharedData;
import com.example.admin.mysvvvappbeta.meta.A;
import com.example.admin.mysvvvappbeta.meta.N;
import com.example.admin.mysvvvappbeta.meta.AU;
import com.example.admin.mysvvvappbeta.models.ad;
import com.example.admin.mysvvvappbeta.models.post;
import com.example.admin.mysvvvappbeta.models.user;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;


import de.hdodenhof.circleimageview.CircleImageView;


public class MA extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private boolean connected = false;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    RecyclerView recyclerView;
    private hang_adapter hang_adapter_ob;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mrootref;
    private SortedList<post> data;
    private ProgressBar progressBar;
    private int loadedItems = 0;
    private ImageView adImage;

    private com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout mSwipeRefreshLayout;
    private MenuItem menuItem;
    private boolean erpad = false;
    private TextView username, useremail, userYearandBranch;
    private ImageView imageView;
    private user u;
    private TextView TextViewAddEvent;
    private CircleImageView circleImageView;
    private TextView loadMoreTextView;
    private static final int TOTAL_ITEMS_IN_PAGE = 13;
    private int FLAG = 0;
    private post lastPID;

    private user userOB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.hanger);
        recyclerView.setVisibility(View.INVISIBLE);
        circleImageView = findViewById(R.id.pro_image);
        loadMoreTextView = findViewById(R.id.loadmoreTextView);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh);
        toolbar.setTitle(cd.getResource("$J&{&j&s&y&x&%"));
        adImage = findViewById(R.id.ad);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);


        TextViewAddEvent = findViewById(R.id.add_event);
        setSupportActionBar(toolbar);

        TextViewAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MA.this, PDA.class));
            }
        });
        sharedData sharedData_ob = new sharedData();
        userOB = sharedData_ob.getUserData(MA.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // firebaseDatabase.setPersistenceEnabled(true);

        mrootref = firebaseDatabase.getReference().child(userOB.getCollege());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
         /*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
        username = (TextView) header.findViewById(R.id.username);
        useremail = (TextView) header.findViewById(R.id.useremail);
        userYearandBranch = header.findViewById(R.id.userbranchandyear);
        imageView = header.findViewById(R.id.imageView);


        username.setText(userOB.getName());
        useremail.setText(firebaseUser.getEmail());
        if (userOB.getUserType().equals("student")) {
            userYearandBranch.setText(userOB.getBranch() + " " + userOB.getYear() + " Year");
        } else {
            userYearandBranch.setText(userOB.getBranch());
        }

        //Toast.makeText(this, ob.getName(), Toast.LENGTH_SHORT).show();


   /*         firebaseDatabase.getReference().child("users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    u = new user();
                    u = dataSnapshot.getValue(user.class);

*/
        adon();

        GlideApp.with(MA.this)

                .load(userOB.getPhotoId())
                .error(R.drawable.pro_pic)
                .into(imageView);


        GlideApp.with(MA.this)

                .load(userOB.getPhotoId())
                .error(R.drawable.pro_pic)
                .into(circleImageView);



         /*       }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });*/
        setAd();

        data = new SortedList<post>(post.class, new SortedList.Callback<post>() {
            @Override
            public int compare(post o1, post o2) {
                Long o1l = Long.parseLong(o1.getTime().toString());
                Long o2l = Long.parseLong(o2.getTime().toString());

                return o2l.compareTo(o1l);
            }

            @Override
            public void onInserted(int position, int count) {
                hang_adapter_ob.notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                hang_adapter_ob.notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                hang_adapter_ob.notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onChanged(int position, int count) {
                hang_adapter_ob.notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(post oldItem, post newItem) {
                // return whether the items' visual representations are the same or not.
                return oldItem.getP_id().equals(newItem.getP_id());
            }

            @Override
            public boolean areItemsTheSame(post item1, post item2) {
                return item1.getP_id().equals(item2.getP_id());
            }
        });


        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
     /*linearLayoutManager.setReverseLayout(true);
       linearLayoutManager.setStackFromEnd(true);*/
        linearLayoutManager.setSmoothScrollbarEnabled(true);

        recyclerView.setLayoutManager(linearLayoutManager);

        hang_adapter_ob = new hang_adapter(data, MA.this, userOB);
        recyclerView.setAdapter(hang_adapter_ob);


        loadData(TOTAL_ITEMS_IN_PAGE);
        if (data.size() == 0) {
            mSwipeRefreshLayout.setEnabled(false);
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                if (data.size() != 0) {
                    String dir = direction == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom";
                    if (dir.equals("bottom")) {
                        FLAG = 0;
                        //   Toast.makeText(MA.this, "" + lastPID.getTitle(), Toast.LENGTH_SHORT).show();


                        loadMoreData(TOTAL_ITEMS_IN_PAGE);

                        //   recyclerView.scrollToPosition((PAGE_NO-1)*TOTAL_ITEMS_IN_PAGE);
                        mSwipeRefreshLayout.setRefreshing(false);

                    } else {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(MA.this, PDA.class);
                startActivity(intent);

                // Snackbar.make(view, "Hi " + name + " " + email + " " + t, Snackbar.LENGTH_LONG)
                //.setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        FirebaseDatabase.getInstance().getReference().child("self_notifs").child(firebaseAuth.getCurrentUser().getUid()).child("notif_state").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String t = dataSnapshot.getValue(String.class);
                //Toast.makeText(MA.this, t, Toast.LENGTH_SHORT).show();

                if (t != null) {

                    menuItem = menu.findItem(R.id.notif_icon);


                    if (t.equals("unread")) {
                        menuItem.setIcon(R.drawable.ic_notifications_active_white_24dp);

                    } else {
                        menuItem.setIcon(R.drawable.ic_notifications_white_24dp);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle ♠clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.notif_icon) {

            startActivity(new Intent(MA.this, NF.class));
        }else if (id == R.id.myevents_icon) {

            startActivity(new Intent(MA.this, ME.class));
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        final int id = item.getItemId();
        Intent nav_intent = null;


        if (id == R.id.notice) {
            if (userOB.getUserType().equals("student")) {

                nav_intent = new Intent(MA.this, N.class);
                nav_intent.putExtra("user", u);
                startActivity(nav_intent);

            } else

            {

                nav_intent = new Intent(MA.this, SC.class);
                nav_intent.putExtra("flag", false);
                // nav_intent.putExtra("user",u);
                startActivity(nav_intent);


            }
        } else if (id == R.id.nav_erp) {
           if(erpad){
               Intent intent = new Intent(MA.this, E2.class);

               intent.putExtra("link", userOB.getToken());


               intent.putExtra("toolbar", "College ERP");
               startActivity(intent);


           }
           else{
               Intent intent = new Intent(MA.this, E.class);

               intent.putExtra("link", userOB.getToken());


               intent.putExtra("toolbar", "College ERP");
               startActivity(intent);

           }

        } else if (id == R.id.nav_timetable) {

            if (userOB.getUserType().equals("student")) {

                nav_intent = new Intent(MA.this, F.class);
                nav_intent.putExtra("flag", true);
                startActivity(nav_intent);

            } else {

                nav_intent = new Intent(MA.this, SC.class);
                nav_intent.putExtra("val", true);
                startActivity(nav_intent);

            }

        } else if (id == R.id.nav_signout) {
            final ProgressDialog progressDialog = new ProgressDialog(MA.this);
            progressDialog.setMessage("Signing out...");
            progressDialog.show();


            new Thread(new Runnable() {
                @Override
                public void run() {


                    try {


                        firebaseAuth.signOut();
                        if (AccessToken.getCurrentAccessToken() != null)
                            LoginManager.getInstance().logOut();
                        FirebaseInstanceId.getInstance().deleteInstanceId();
                        Intent nav_intent = new Intent(MA.this, LGC.class);
                        startActivity(nav_intent);

                        finish();

                    } catch (Exception e) {


                    }
                }
            }).start();
            // progressDialog.dismiss();
            // Toast.makeText(MA.this, "Error in Signing out Try Again", Toast.LENGTH_SHORT).show();


        } else if (id == R.id.nav_helpandFeedBack) {

            nav_intent = new Intent(MA.this, HF.class);
            startActivity(nav_intent);


        } else if (id == R.id.about_us) {

            nav_intent = new Intent(MA.this, AU.class);
            startActivity(nav_intent);


        } else if (id == R.id.nav_anonyFeed) {

            nav_intent = new Intent(MA.this, A.class);
            startActivity(nav_intent);


        }
        else if (id == R.id.nav_shareApp) {
String str = "Download College Networks - The All -in -One App for every College Student. Don't miss any College Stuff. \n" +
        "Now info of everything going in your college is in your pocket!\n" +
        "\n" +
        "Don't miss any event of your college or of your city, register to events of your interest, connect easily with Event Organizers \n" +
        "Advertise your own events, users can register within the App, Solve queries of your users.\n" +
        "\n" +
        "College Networks provides easy communication between students and teachers. We provide Notice Board for every class.\n" +
        "Teachers can log in and can send Notice to students\n" +
        "\n" +
        "Students can send Anonymous Feedback to their College Management\n" +
        "\n" +
        "and many more features...   https://college-networks.en.aptoide.com/?store_name=college-networks-team";
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, str);
            sendIntent.setType("text/plain");
           startActivity(sendIntent);


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void loadMoreData(int limit) {
        mrootref.child("dashboard").
                child("post")
                .orderByKey()
                .endAt(lastPID.getP_id())
                .limitToLast(limit)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        mSwipeRefreshLayout.setRefreshing(true);
                        post p = dataSnapshot.getValue(post.class);
                        data.add(p);

                        if (FLAG == 0) {
                            lastPID = p;
                            FLAG = 1;
                        }

                        hang_adapter_ob.notifyDataSetChanged();
                        mSwipeRefreshLayout.setRefreshing(false);

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


    }

    private void loadData(int limit) {


        mrootref.child("dashboard").child("post")
                .limitToLast(limit)


                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        post p = dataSnapshot.getValue(post.class);
                        data.add(p);
                        loadedItems++;
                        //Collections.reverse(data);
                        hang_adapter_ob.notifyDataSetChanged();
                        if (loadedItems == 1) {
                            loadMoreTextView.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            mSwipeRefreshLayout.setEnabled(true);
                            progressBar.setVisibility(View.GONE);
                        }

                        if (FLAG == 0) {
                            lastPID = p;
                            FLAG = 1;
                        }

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


    }

    void setAd() {

        FirebaseDatabase.getInstance().getReference().child(userOB.getCollege()).child("ads").child("dashboard").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ad adOB = dataSnapshot.getValue(ad.class);

                if (adOB != null) {
                    adImage.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(adOB.getImageURL())) {

                        // Toast.makeText(E.this, "In not null561"+adOB.getImageURL(), Toast.LENGTH_SHORT).show();
                        GlideApp.with(MA.this)


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
                                Intent intent = new Intent(MA.this, E.class);
                                intent.putExtra("link", adOB.getUrl());
                                intent.putExtra("toolbar", adOB.getTitle());
                                //intent.putExtra("adflag", true);
                                startActivity(intent);
                            }
                        });


                    }
                } else
                    adImage.setVisibility(View.GONE);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                adImage.setVisibility(View.VISIBLE);

            }
        });


    }

    public void adon() {
        FirebaseDatabase.getInstance().getReference().child(userOB.getCollege()).child("ads").child("erp").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    erpad = true;
                }
                else erpad = false;
               // Toast.makeText(MA.this, ""+erpad, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}



















