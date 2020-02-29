package com.example.admin.mysvvvappbeta.T;

/**
 * Created by Asad Mirza on 02-03-2018.
 */import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.mysvvvappbeta.R;
import com.example.admin.mysvvvappbeta.models.sectionPOJO;

import java.util.ArrayList;

/**
 * Created by CrYsIs on 29-01-2018.
 */

public class sections_adapter extends RecyclerView.Adapter<sections_adapter.myviewholder> {
    LayoutInflater layoutInflater;
    private ArrayList<sectionPOJO> data;
    private Context context;

 public    sections_adapter(ArrayList<sectionPOJO> data, Context context) {
        this.data = data;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(this.context);

    }

    @Override
    public myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.section_row_layout, null);
        myviewholder viewholder = new myviewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(myviewholder holder, int position) {
        sectionPOJO s = data.get(position);
        holder.branch.setText(s.getBranch());
        holder.section.setText(s.getSection());
        holder.course.setText(s.getCourse());
        holder.year.setText(s.getYear());


    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class myviewholder extends RecyclerView.ViewHolder {
        private TextView course, year, branch, section;

        public myviewholder(View itemView) {
            super(itemView);
            course = itemView.findViewById(R.id.course);
            branch = itemView.findViewById(R.id.branch);
            year = itemView.findViewById(R.id.year);
            section = itemView.findViewById(R.id.section);

        }
    }
}
