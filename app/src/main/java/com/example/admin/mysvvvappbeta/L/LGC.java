
package com.example.admin.mysvvvappbeta.L;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.example.admin.mysvvvappbeta.R;
import com.example.admin.mysvvvappbeta.models.collegePOJO;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LGC extends AppCompatActivity {
    private RecyclerView recyclerView;

    private ProgressBar mProgressBar;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private clg_adapter clg_adapter_obj;
    private FirebaseApp department;

    private ArrayList<collegePOJO> data;

    private Button button;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_college_ask);
        button=findViewById(R.id.button);

        mProgressBar = findViewById(R.id.mprogressbar);
        mProgressBar.setVisibility(View.VISIBLE);
        textView  =findViewById(R.id.textview2);
        textView.setVisibility(View.GONE);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setVisibility(View.INVISIBLE);


        data = new ArrayList<>();
        clg_adapter_obj = new clg_adapter(data, LGC.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);


        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(clg_adapter_obj);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.child("app_data").child("college_data").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                collegePOJO c = dataSnapshot.getValue(collegePOJO.class);
                data.add(c);

                clg_adapter_obj.notifyDataSetChanged();
                mProgressBar.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);


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


}

class clg_adapter extends RecyclerView.Adapter<clg_adapter.viewholder> {
    private LayoutInflater layoutInflater;
    private ArrayList<collegePOJO> data;
    private Context context;



    clg_adapter(ArrayList<collegePOJO> data, Context context) {
        this.data = data;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);

    }



    @Override
    public viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.actv_row_layout, null);
        viewholder holder = new viewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(viewholder holder, int position) {
        final collegePOJO c = data.get(position);


        holder.name.setText(c.getCollege_name());
        Glide.with(context)
                .load(c.getCollege_logo())

                .into(holder.imageView);

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,LG.class);
               intent.putExtra("data",c);
                context.startActivity(intent);
               // ((Activity)context).finish();
            }
        });


    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView imageView;
        private Button button;


        public viewholder(View itemView) {
            super(itemView);
            button=itemView.findViewById(R.id.button);
            name = itemView.findViewById(R.id.name);
            imageView = itemView.findViewById(R.id.image);
        }


    }
}