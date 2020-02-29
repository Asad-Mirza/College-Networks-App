package com.example.admin.mysvvvappbeta.ADA;


        import android.content.Context;
        import android.content.Intent;
        import android.graphics.drawable.Drawable;
        import android.support.annotation.Nullable;
        import android.support.v7.widget.RecyclerView;
        import android.text.TextUtils;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.ProgressBar;
        import android.widget.TextView;

        import com.bumptech.glide.load.DataSource;
        import com.bumptech.glide.load.engine.GlideException;
        import com.bumptech.glide.request.RequestListener;
        import com.bumptech.glide.request.target.Target;
        import com.example.admin.mysvvvappbeta.GlideApp;
        import com.example.admin.mysvvvappbeta.R;
        import com.example.admin.mysvvvappbeta.T.F;
        import com.example.admin.mysvvvappbeta.UI.timeAgo;
        import com.example.admin.mysvvvappbeta.models.noticePOJO;


        import java.util.ArrayList;

/**
 * Created by CrYsIs on 31-01-2018.
 */

public class post_chat_adapter extends RecyclerView.Adapter<post_chat_adapter.myadapter> {
    private LayoutInflater layoutInflater;
    private ArrayList<noticePOJO> data;
    private Context context;
    // public ViewGroup.LayoutParams params;

   public post_chat_adapter(ArrayList<noticePOJO> data, Context context) {
        this.data = data;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);

    }


    @Override
    public myadapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.post_chat_adapter, null);
        myadapter holder = new myadapter(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final myadapter holder, int position) {
        // holder.getView().setAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom_in));
        holder.imageView.setVisibility(View.VISIBLE);
        holder.progressBar.setVisibility(View.VISIBLE);

        final noticePOJO n = data.get(position);
        if (!TextUtils.isEmpty(n.getImage_url())) {
           /* holder.imageView.setVisibility(View.VISIBLE);
            holder.progressBar.setVisibility(View.VISIBLE);*/

           /*  params = holder.card.getLayoutParams();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            holder.card.setLayoutParams(params);
            Glide.with(context)
                    .load(n.getImage_url())
                    .override(1200,0)
                    .into(holder.imageView);

*/


            GlideApp.with(context.getApplicationContext())

                    .load(n.getThumb())

                    //.diskCacheStrategy(DiskCacheStrategy.ALL)

                    //.placeholder(R.drawable.loading_image)
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




                    //.placeholder(R.drawable.loading_image)

                    .into(holder.imageView);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,F.class);
                    intent.putExtra("image",n.getImage_url());
                    context.startActivity(intent);
                }
            });










          /*  Glide.with(context.getApplicationContext())
                    .load(n.getImage_url())
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                            return false;
                        }
                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.imageView);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,F.class);
                    intent.putExtra("image",n.getImage_url());
                    context.startActivity(intent);
                }
            });*/
        }else{
            holder.imageView.setVisibility(View.GONE);
            holder.progressBar.setVisibility(View.GONE);

        }


        holder.name.setText(n.getSender_name());
        holder.dis.setText(n.getDis());
        holder.time.setText(timeAgo.getTimeAgo(Long.parseLong(n.getTime().toString())));
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    class myadapter extends RecyclerView.ViewHolder {
        private ImageView imageView;
        // private CardView card;
        private ProgressBar progressBar;
        //   public RelativeLayout.LayoutParams params;
        private TextView dis, time, name;
        private View view;

        public myadapter(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progress_bar);
            imageView = itemView.findViewById(R.id.notice_image);
            dis = itemView.findViewById(R.id.notice_dis);
            time = itemView.findViewById(R.id.time);
            name = itemView.findViewById(R.id.name);
            this.view = itemView;


        }

        public View getView() {
            return view;
        }

    }


}
