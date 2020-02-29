package com.example.admin.mysvvvappbeta.ADA;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.admin.mysvvvappbeta.GlideApp;
import com.example.admin.mysvvvappbeta.R;
import com.example.admin.mysvvvappbeta.models.comment;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 26-12-2017.
 */

public class cmtadapter extends RecyclerView.Adapter<cmtadapter.MyViewHolder> {
    LayoutInflater layoutInflater;

    private ArrayList<comment> data;
    private Context mContext;

    public  cmtadapter(ArrayList<comment> data, Context mContext) {
        this.mContext = mContext;
        this.data = data;
        this.layoutInflater  = LayoutInflater.from(this.mContext);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =layoutInflater.inflate(R.layout.comment_layout, null);
        cmtadapter.MyViewHolder viewHolder = new cmtadapter.MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        comment current   = data.get(position);

        if (!TextUtils.isEmpty(current.getSenderImageURL())){

            if (!TextUtils.isEmpty(current.getSenderImageURL())){
                GlideApp.with(mContext)
                        .load(current.getSenderImageURL())
                        .placeholder(R.drawable.pro_pic)
                          .diskCacheStrategy(DiskCacheStrategy.NONE)

                        .error(R.drawable.pro_pic)
                        .into(holder.circleImageView);
            }
        }
        holder.sender_name.setText(current.getSender());
        holder.time.setText((String)current.getTime());
        holder.commentText.setText(current.getCommenttext());


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends  RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView sender_name,time,commentText;

        public MyViewHolder(View itemView) {
            super(itemView);
            circleImageView  = itemView.findViewById(R.id.hang_dp);
            sender_name  =itemView.findViewById(R.id.hang_name);
            commentText = itemView.findViewById(R.id.hang_comment);
            time = itemView.findViewById(R.id.hang_time);
        }
    }
}
