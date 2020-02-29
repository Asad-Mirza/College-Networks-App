package com.example.admin.mysvvvappbeta.O;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.SortedList;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.admin.mysvvvappbeta.GlideApp;
import com.example.admin.mysvvvappbeta.R;
import com.example.admin.mysvvvappbeta.UI.HF;
import com.example.admin.mysvvvappbeta.UI.timeAgo;
import com.example.admin.mysvvvappbeta.models.post;
import com.example.admin.mysvvvappbeta.models.reportPOJO;
import com.example.admin.mysvvvappbeta.models.user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;


import de.hdodenhof.circleimageview.CircleImageView;

import static android.util.Log.d;

/**
 * Created by Asad Mirza on 24-12-2017.
 */

public class hang_adapter extends RecyclerView.Adapter<hang_adapter.myviewholder> {
    LayoutInflater layoutInflater;

    private SortedList<post> data;
    private Context mContext;
    private user userOB;



   public hang_adapter(SortedList<post> data, Context mContext,user userOB) {
        this.mContext = mContext;
        this.data = data;
        this.layoutInflater  = LayoutInflater.from(this.mContext);
         this.userOB = userOB;



    }


    @Override
    public myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view =layoutInflater.inflate(R.layout.hanger_item, null);


        myviewholder viewHolder = new myviewholder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final myviewholder holder, final int position) {


        final post current=data.get(position);
        holder.progressBar.setVisibility(View.VISIBLE);


          if (!TextUtils.isEmpty(current.getSender_dp())) {
              GlideApp.with(mContext)

                      .load(current.getSender_dp())


                      .error(R.drawable.pro_pic)
                      .into(holder.circleImageView);
          }


          if (!TextUtils.isEmpty(current.getImage_uri())) {
              GlideApp.with(mContext)

                      .load(current.getImage_uri())
                      .diskCacheStrategy(DiskCacheStrategy.ALL)

                    //  .override(1600,900)
                      //.error(R.drawable.defaultimg)
                      //.thumbnail(0.1f)

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
                      .into(holder.hang_image);
          }


          //.placeholder(R.drawable.loading_image)


          holder.hang_name.setText(current.getSender_name());
          holder.hang_time.setText(timeAgo.getTimeAgo(Long.parseLong(current.getTime().toString())));
          holder.hang_title.setText(current.getTitle());
          holder.hang_dis.setText(current.getDis());

          final post tobeshared = data.get(position);

            final String  str = tobeshared.getTitle()+"\n"+tobeshared.getDis()+"\n"+"You can registor to "+tobeshared.getTitle()+" in Colleg Neworks App \n Contact us through College Networks App";

        holder.shareButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent sendIntent = new Intent();
                  sendIntent.setAction(Intent.ACTION_SEND);
                  sendIntent.putExtra(Intent.EXTRA_TEXT, str);
                  sendIntent.setType("text/plain");
                  mContext.startActivity(sendIntent);

              }
          });
          holder.commentbutton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent = new Intent(mContext, PopupActivity.class);
                  intent.putExtra("PID", tobeshared.getP_id());
                  intent.putExtra("name",tobeshared.getTitle());
                  mContext.startActivity(intent);

              }
          });
          holder.readMoreButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent = new Intent(mContext, PD.class);
                  intent.putExtra("post", tobeshared);
                  mContext.startActivity(intent);

              }
          });
          holder.hang_image.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  Intent intent = new Intent(mContext, PD.class);
                  //  intent.putExtra(post_details.EXTRA_CONTACT, contact);
                 /* Pair<View, String> p1 = Pair.create((View) holder.hang_image, "trans_image");
                  Pair<View, String> p2 = Pair.create((View) holder.hang_title, "trans_title");
                  Pair<View, String> p3 = Pair.create((View) holder.hang_dis, "trans_dis");
                  Pair<View, String> p4 = Pair.create((View) holder.ProfilerelativeLayout, "profile");


                  ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext, p1, p2, p3, p4);
*/
                  //options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext,holder.hang_image,"trans");
                  intent.putExtra("post", tobeshared);
                  mContext.startActivity(intent);









            /*    Intent intent=new Intent(mContext,post_details.class);

                mContext.startActivity(intent);*/

              }
          });
          holder.optionsMenuTextView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  PopupMenu popup = new PopupMenu(mContext, holder.optionsMenuTextView);
                  //inflating menu from xml resource
                  popup.inflate(R.menu.report_menu);
                  //adding click listener
                  popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                      @Override
                      public boolean onMenuItemClick(MenuItem item) {
                          switch (item.getItemId()) {
                              case R.id.menu1:
                                  reportPOJO pojo = new reportPOJO();
                                  pojo.setAddress("Events > " + userOB.getCollege());
                                  pojo.setId(tobeshared.getP_id());
                                  pojo.setSenderName(userOB.getName());
                                  pojo.setTime(ServerValue.TIMESTAMP);
                                  pojo.setSenderUID(FirebaseAuth.getInstance().getCurrentUser().getUid());


                                  FirebaseDatabase.getInstance().getReference().child("feedback").child("reports").push().setValue(pojo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                      @Override
                                      public void onComplete(@NonNull Task<Void> task) {
                                          if (task.isSuccessful()){

                                              Toast.makeText(mContext, "Thanks for your Feedback !", Toast.LENGTH_SHORT).show();
                                          }
                                          else
                                              Toast.makeText(mContext, "Error in sending Feedback Try again", Toast.LENGTH_SHORT).show();
                                      }
                                  });



                                  break;
                              case R.id.menu2:
                                  mContext.startActivity(new Intent(mContext,HF.class));
                          }
                          return false;
                      }
                  });
                  //displaying the popup
                  popup.show();
              }
          });




       /*
        Glide.with(mContext)
             .using(new FirebaseImageLoader())
              .load(FirebaseStorage.getInstance().getReference().child("posts"))
                   .into(holder.circleImageView);
        */



    }

    @Override
    public int getItemCount() {
        return (data.size());
    }

    class myviewholder extends RecyclerView.ViewHolder {
        CardView cardView;
        ProgressBar progressBar;
        RelativeLayout ProfilerelativeLayout;
        LinearLayout linearLayoutButtons;
        CircleImageView circleImageView;
        ImageView hang_image;
        TextView hang_name, hang_time,hang_title,hang_dis;
        Button commentbutton,shareButton,readMoreButton;
        TextView optionsMenuTextView;

        myviewholder(View view) {
            super(view);
            ProfilerelativeLayout = (RelativeLayout)view.findViewById(R.id.nameNdp);
            circleImageView=(CircleImageView)view.findViewById(R.id.hang_dp);
            linearLayoutButtons =view.findViewById(R.id.buttons);
             hang_image=(ImageView)view.findViewById(R.id.hang_image);
            hang_name=(TextView)view.findViewById(R.id.hang_name);
            hang_time=(TextView)view.findViewById(R.id.hang_time);
         cardView  = view.findViewById(R.id.cardview);
                      hang_title=(TextView)view.findViewById(R.id.hang_title);
            hang_dis=(TextView)view.findViewById(R.id.hang_dis);
           commentbutton =view.findViewById(R.id.comment_button);
           shareButton = view.findViewById(R.id.share_button);
           readMoreButton = view.findViewById(R.id.readmore_button);
           optionsMenuTextView = view.findViewById(R.id.textViewOptions);
           progressBar = view.findViewById(R.id.progress_bar);




        }
    }
}
