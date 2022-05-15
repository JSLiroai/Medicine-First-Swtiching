package com.example.medicinefirstswitching.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicinefirstswitching.R;

import java.util.ArrayList;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.MyViewHolder> {

    Context context;
    ArrayList<Item> list;

    public ResultAdapter(Context context, ArrayList<Item> list) {
        super();
        this.context = context;
        this.list = list;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Item item = list.get(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.result_item, parent, false);
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

