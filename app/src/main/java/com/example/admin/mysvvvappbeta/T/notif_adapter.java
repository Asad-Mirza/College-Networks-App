package com.example.admin.mysvvvappbeta.T;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.admin.mysvvvappbeta.R;
import com.example.admin.mysvvvappbeta.UI.timeAgo;
import com.example.admin.mysvvvappbeta.models.notif_OBJ;

import java.util.ArrayList;

/**
 * Created by admin on 02-01-2018.
 */


public class notif_adapter extends RecyclerView.Adapter<notif_adapter.myviewholder> {
    LayoutInflater layoutInflater;

    private ArrayList<notif_OBJ> data;
    private Context mContext;
    long lastSeen;



   public notif_adapter(ArrayList<notif_OBJ> data, Context mContext,long lastSeen) {
        this.mContext = mContext;
        this.data = data;
        this.layoutInflater  = LayoutInflater.from(this.mContext);
        this.lastSeen = lastSeen;

    }


    @Override
    public notif_adapter.myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =layoutInflater.inflate(R.layout.notification_row_layout, null);
        notif_adapter.myviewholder viewHolder = new notif_adapter.myviewholder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(notif_adapter.myviewholder holder, int position) {

        final notif_OBJ current=data.get(position);

        holder.textView.setText(current.getNotfText());
        String timeSince = timeAgo.getTimeAgo(Long.parseLong(current.getNotifTime().toString()));
        holder.time.setText(timeSince);

        if (lastSeen <= Long.parseLong(current.getNotifTime().toString())){
            holder.relativeLayout.setBackgroundColor(Color.parseColor("#EEF4FF"));

        }else { holder.relativeLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));}

//
//        if (!TextUtils.isEmpty(current.getImage_uri())){
//            Picasso.with(mContext).load(current.getImage_uri())
//                    .error(R.drawable.defaultimg)
//                    .fit()
//                    .placeholder(R.drawable.defaultimg)
//                    .into(holder.hang_image);
//        }



      }

    @Override
    public int getItemCount() {
        return (data.size());
    }

    class myviewholder extends RecyclerView.ViewHolder {


        TextView textView,time;
        RelativeLayout relativeLayout;

        myviewholder(View view) {
            super(view);
           relativeLayout = view.findViewById(R.id.nameNdp);
           textView = (TextView)view.findViewById(R.id.hang_name);
           time = (TextView)view.findViewById(R.id.hang_time);




        }
    }
}
