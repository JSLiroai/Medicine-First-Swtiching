package com.example.medicinefirstswitching.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.example.medicinefirstswitching.R;
import com.example.medicinefirstswitching.ResultActivity;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {

    Context context;
    ArrayList<Item> list;

    public MainAdapter(Context context, ArrayList<Item> list) {
        super();
        this.context = context;
        this.list = list;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
            Item item = list.get(position);
            holder.button.setText(item.name);
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ResultActivity.class);
                    context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));


                }
            });

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Button button;

        public MyViewHolder(View itemView) {
            super(itemView);
            button= itemView.findViewById(R.id.main_btn_symptom);
        }
    }
}

