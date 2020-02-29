package com.example.admin.mysvvvappbeta.ADA;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.admin.mysvvvappbeta.O.PD;
import com.example.admin.mysvvvappbeta.T.E;
import com.example.admin.mysvvvappbeta.UI.timeAgo;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.admin.mysvvvappbeta.GlideApp;
import com.example.admin.mysvvvappbeta.R;
import com.example.admin.mysvvvappbeta.models.comment;
import com.example.admin.mysvvvappbeta.models.post;
import com.example.admin.mysvvvappbeta.models.user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 26-12-2017.
 */

public class MyEvents_adapter extends RecyclerView.Adapter<MyEvents_adapter.MyViewHolder> {
    LayoutInflater layoutInflater;
    user userOB;

    private ArrayList<post> data;
    private Context mContext;
    boolean connected = false;

    public  MyEvents_adapter(ArrayList<post> data, Context mContext,user userOB) {
        this.mContext = mContext;
        this.data = data;
        this.layoutInflater  = LayoutInflater.from(this.mContext);
          this.userOB = userOB;
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                connected = snapshot.getValue(Boolean.class);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public MyEvents_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =layoutInflater.inflate(R.layout.myevent_row_layout, null);
        MyEvents_adapter.MyViewHolder viewHolder = new MyEvents_adapter.MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final post current   = data.get(position);

        if (!TextUtils.isEmpty(current.getImage_uri())){

            if (!TextUtils.isEmpty(current.getImage_uri())){
                GlideApp.with(mContext)
                        .load(current.getImage_uri())

                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                holder.progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        
                        .into(holder.imageView);
            }
        }
        holder.hanger_title.setText(current.getTitle());
       String time = timeAgo.getTimeAgo(Long.parseLong(current.getTime().toString()));
        holder.hanger_time.setText(time);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,PD.class);
               intent.putExtra("post",current);
               mContext.startActivity(intent);
            }
        });
        if (current.getStatus().equals("no")){
         holder.verifyTextView.setTextColor(Color.RED);
            holder.verifyTextView.setText("Under Verification");
        }
        else
        {
           // holder.verifyTextView.setTextColor(Color.GREEN);

            holder.verifyTextView.setText("Published");

        }
        holder.options_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(mContext, holder.options_menu);
                //inflating menu from xml resource
                popup.inflate(R.menu.my_events_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                deleteEvent(current);







                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();

            }
        });








    }

    private void deleteEvent(final post p) {

   if (connected){

       AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
       builder.setCancelable(false);
       builder.setTitle("Are you sure you want to delete the Event ?");
       builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               final ProgressDialog progressDialog  = new ProgressDialog(mContext);
               progressDialog.setMessage("Deleting your Event...");
               progressDialog.show();
               final DatabaseReference mRootRef  = FirebaseDatabase.getInstance().getReference();
               final String UID  = FirebaseAuth.getInstance().getCurrentUser().getUid();
               mRootRef.child(userOB.getCollege()).child("D").child("SE").child(UID).child(p.getP_id()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){

                        mRootRef.child(userOB.getCollege()).child("D").child("E").child(p.getP_id()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    if (p.getStatus().equals("yes ")){
                                        mRootRef.child(userOB.getCollege()).child("D").child("VE").child(p.getP_id()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()){
                                                    progressDialog.dismiss();
                                                    Toast.makeText(mContext, "Event Deleted", Toast.LENGTH_SHORT).show();


                                                }

                                            else {   progressDialog.dismiss();
                                                    Toast.makeText(mContext, "Error try again", Toast.LENGTH_SHORT).show();



                                                }}
                                        });


                                    }

                                    else {

                                        progressDialog.dismiss();
                                        Toast.makeText(mContext, "Event Deleted", Toast.LENGTH_SHORT).show();

                                    }
                                }
                                else{
                                    progressDialog.dismiss();
                                    Toast.makeText(mContext, "Error Try again", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });





                    }
                    else{
                       progressDialog.dismiss();
                        Toast.makeText(mContext, "Error Try again", Toast.LENGTH_SHORT).show();
                    }
                   }
               });




           }
       });
    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

        }
    }) ;
    AlertDialog alertDialog = builder.create();
    alertDialog.show();
   }
   else{
       Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();

   }






    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends  RecyclerView.ViewHolder{
      ImageView imageView;
      TextView hanger_title,hanger_time,options_menu,verifyTextView;
  RelativeLayout relativeLayout;
  ProgressBar progressBar;
        MyViewHolder(View itemView) {
            super(itemView);
     progressBar = itemView.findViewById(R.id.progress_bar);
            relativeLayout = itemView.findViewById(R.id.relativelayout);
           imageView = itemView.findViewById(R.id.imageView);
           hanger_title = itemView.findViewById(R.id.hang_name);
           hanger_time = itemView.findViewById(R.id.hang_time);
           verifyTextView = itemView.findViewById(R.id.hang_verify);
           options_menu = itemView.findViewById(R.id.textViewOptions);
        }
    }
}
