package com.example.admin.mysvvvappbeta.meta;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.SortedList;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.admin.mysvvvappbeta.J.cd;
import com.example.admin.mysvvvappbeta.T.E;
import com.example.admin.mysvvvappbeta.GlideApp;
import com.example.admin.mysvvvappbeta.R;
import com.example.admin.mysvvvappbeta.models.ad;
import com.example.admin.mysvvvappbeta.models.noticePOJO;
import com.example.admin.mysvvvappbeta.models.user;
import com.example.admin.mysvvvappbeta.O.notice_adapter;
import com.example.admin.mysvvvappbeta.UI.sharedData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.Calendar;

/**
 * Created by asad mirza on 03-01-2018.
 */

public class N extends AppCompatActivity {
     private FirebaseDatabase firebaseDatabase;
     private DatabaseReference mRootRef;
     private String UID;
     private user userOB;
     private RecyclerView recyclerView;
     private SortedList<noticePOJO> data;
    private static final int TOTAL_ITEMS_IN_PAGE = 20;
    private int FLAG=0;
    private noticePOJO lastPID;
  private int  loadedItems = 0;

     private notice_adapter notice_adapter_ob;
     private TextView loadMoreTextView;
     private ProgressBar progressBar;
    private  com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout mSwipeRefreshLayout;

  private ImageView adImage;
  private TextView msgTextView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_activity);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(cd.getResource("$S&t&y&n&h&j&%&G&t&f&w&i&%"));
        sharedData sharedData_ob = new sharedData();
        userOB = sharedData_ob.getUserData(N.this);
        recyclerView = findViewById(R.id.recyclerview);
        msgTextView = findViewById(R.id.textview);
        loadMoreTextView  = findViewById(R.id.loadmoreTextView);
     progressBar  = findViewById(R.id.progress_bar);
     adImage  = findViewById(R.id.ad);
        long lastseen = sharedData_ob.getLastSeenNotice(N.this);
        long cur_time = (Calendar.getInstance().getTimeInMillis()) ;
        sharedData_ob.setLastSeenNotice(cur_time,N.this);



        recyclerView.setVisibility(View.INVISIBLE);

        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setEnabled(false);
        mRootRef = FirebaseDatabase.getInstance().getReference();

        mRootRef.child(userOB.getCollege()).child("ads").child("notice").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               final ad adOB = dataSnapshot.getValue(ad.class);

               if (adOB!=null){
                   adImage.setVisibility(View.VISIBLE);

                if (!TextUtils.isEmpty(adOB.getImageURL())) {

                    GlideApp.with(N.this)


                            .load(adOB.getImageURL())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            //.error(R.drawable.defaultimg)

                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                     adImage.setVisibility(View.GONE);


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
                            Intent intent=new Intent(N.this,E.class);
                            intent.putExtra("link",adOB.getUrl());
                            intent.putExtra("toolbar",adOB.getTitle());
                            intent.putExtra("adflag",true);
                            startActivity(intent);
                        }
                    });


                }

               }
                else
                    adImage.setVisibility(View.GONE);








            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                adImage.setVisibility(View.VISIBLE);

            }
        });




        data = new SortedList<noticePOJO>(noticePOJO.class, new SortedList.Callback<noticePOJO>() {
            @Override
            public int compare(noticePOJO o1, noticePOJO o2) {
                Long o1l = Long.parseLong(o1.getTime().toString());
                Long o2l = Long.parseLong(o2.getTime().toString());

                return o2l.compareTo(o1l);
            }

            @Override
            public void onInserted(int position, int count) {
                notice_adapter_ob.notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notice_adapter_ob.notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notice_adapter_ob.notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onChanged(int position, int count) {
                notice_adapter_ob.notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(noticePOJO oldItem, noticePOJO newItem) {
                // return whether the items' visual representations are the same or not.
                return oldItem.getNid().equals(newItem.getNid());
            }

            @Override
            public boolean areItemsTheSame(noticePOJO item1, noticePOJO item2) {
                return item1.getNid().equals(item2.getNid());
            }
        });
        notice_adapter_ob = new notice_adapter(data,N.this,lastseen);

        check();   // check if exist
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
                       // Toast.makeText(N.this, "" + lastPID.getDis(), Toast.LENGTH_SHORT).show();



                        loadMoreData(TOTAL_ITEMS_IN_PAGE);

                        //   recyclerView.scrollToPosition((PAGE_NO-1)*TOTAL_ITEMS_IN_PAGE);
                        mSwipeRefreshLayout.setRefreshing(false);

                    } else {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }
            }
        });



        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(this);


        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(notice_adapter_ob);


        firebaseDatabase =FirebaseDatabase.getInstance();

        UID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        toolbar.setSubtitle(userOB.getBranch()+" "+userOB.getSection());


        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });











    }
      @Override
    public void onBackPressed() {
     finish();
    }


    private void loadData(int limit){



        mRootRef.child(userOB.getCollege()).child("notice_board").child(userOB.getCourse()).child(userOB.getBranch()).child(userOB.getYear()).child(userOB.getSection())
                .limitToLast(limit)
                .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                noticePOJO obj = dataSnapshot.getValue(noticePOJO.class);
                String key = dataSnapshot.getKey();
                obj.setNid(key);
                data.add(obj);
                loadedItems++;
                if (loadedItems == 10)    loadMoreTextView.setVisibility(View.VISIBLE);
                if (loadedItems==1){

                    recyclerView.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setEnabled(true);

                    progressBar.setVisibility(View.GONE);}



                notice_adapter_ob.notifyDataSetChanged();
                if (FLAG == 0){
                    lastPID = obj;
                    FLAG =1;}



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

    private void loadMoreData(int limit){
        mRootRef.child(userOB.getCollege()).child("notice_board").child(userOB.getCourse()).child(userOB.getBranch()).child(userOB.getYear()).child(userOB.getSection())
                .limitToLast(limit)
                .orderByKey()
                .endAt(lastPID.getNid())

                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        noticePOJO obj = dataSnapshot.getValue(noticePOJO.class);
                        String key = dataSnapshot.getKey();
                        obj.setNid(key);
                        data.add(obj);
                        loadedItems++;
                        if (loadedItems == 10)    loadMoreTextView.setVisibility(View.VISIBLE);
                        if (loadedItems==1){
                            //loadMoreTextView.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            mSwipeRefreshLayout.setEnabled(true);

                            progressBar.setVisibility(View.GONE);}



                        notice_adapter_ob.notifyDataSetChanged();
                        if (FLAG == 0){
                            lastPID = obj;
                            FLAG =1;}



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

    public void check(){
        mRootRef.child(userOB.getCollege()).child("notice_board").child(userOB.getCourse()).child(userOB.getBranch()).child(userOB.getYear()).child(userOB.getSection()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    progressBar.setVisibility(View.GONE);
                    msgTextView.setVisibility(View.VISIBLE);
                }else{
                    progressBar.setVisibility(View.GONE);
                    msgTextView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
