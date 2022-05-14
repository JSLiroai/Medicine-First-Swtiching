package com.example.medicinefirstswitching.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicinefirstswitching.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<Item> list;
    int count = 0;

    public RecyclerViewAdapter(Context context, ArrayList<Item> list) {
        super();
        this.context = context;
        this.list = list;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams)holder.itemView.getLayoutParams();
        layoutParams.height = layoutParams.width;
        holder.itemView.requestLayout();
        System.out.println("Binderholer Called");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_item_layout, parent, false);
        System.out.println("Viewholer Called");
        return new MyViewHolder(view);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Button button;

        public MyViewHolder(View itemView) {
            super(itemView);
            button= itemView.findViewById(R.id.recylcerview_btn_symptoms);
            button.setText(list.get(count++).symptom);

        }
    }
}

