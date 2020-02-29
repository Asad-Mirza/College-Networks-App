package com.example.admin.mysvvvappbeta.O;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.util.SortedList;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.admin.mysvvvappbeta.GlideApp;
import com.example.admin.mysvvvappbeta.R;
import com.example.admin.mysvvvappbeta.models.noticePOJO;
import com.example.admin.mysvvvappbeta.T.F;
import com.example.admin.mysvvvappbeta.UI.timeAgo;

/**
 * Created by admin on 04-01-2018.
 */

public class notice_adapter extends RecyclerView.Adapter<notice_adapter.MyViewHolder> {
    LayoutInflater layoutInflater;

    private SortedList<noticePOJO> data;
    private Context mContext;

long lastSeen;

   public notice_adapter(SortedList<noticePOJO> data, Context mContext,long lastSeen) {
        this.mContext = mContext;
        this.data = data;
        this.layoutInflater  = LayoutInflater.from(this.mContext);
        this.lastSeen= lastSeen;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =layoutInflater.inflate(R.layout.notice_row_layout, null);
        notice_adapter.MyViewHolder viewHolder = new notice_adapter.MyViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.ImageAndProgressrelativeLayout.setVisibility(View.VISIBLE);
        final noticePOJO current = data.get(position);
        holder.title.setText(current.getDis());
        String timeSince  =  timeAgo.getTimeAgo(Long.parseLong(current.getTime().toString()));
        holder.time.setText(timeSince);
        if (lastSeen <= Long.parseLong(current.getTime().toString())){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#EEF4FF"));

        }else { holder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));}
         holder.imageView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent = new Intent(mContext,F.class);
                 intent.putExtra("image",current.getImage_url());
                 mContext.startActivity(intent);
             }
         });
        holder.sender_name.setText(current.getSender_name());
        if (!TextUtils.isEmpty(current.getImage_url())){

            GlideApp.with(mContext)
                        .load(current.getThumb())
                        // .error(R.drawable.img_icon)
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
        else
        {
            holder.ImageAndProgressrelativeLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title,sender_name,time;
        ImageView imageView;
         ProgressBar progressBar;
         CardView cardView;
         RelativeLayout ImageAndProgressrelativeLayout;
    public MyViewHolder(View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.title);
        sender_name = itemView.findViewById(R.id.sender_name);
        time = itemView.findViewById(R.id.time);
        imageView = itemView.findViewById(R.id.imageView);
       progressBar = itemView.findViewById(R.id.progress_bar);
     ImageAndProgressrelativeLayout = itemView.findViewById(R.id.imageViewlayout);
       cardView = itemView.findViewById(R.id.cardview);
    }
}

}
