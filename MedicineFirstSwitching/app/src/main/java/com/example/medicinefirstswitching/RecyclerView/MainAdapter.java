package com.example.medicinefirstswitching.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.example.medicinefirstswitching.R;
import com.example.medicinefirstswitching.Searching.SearchActivity;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {

    Context context;
    ArrayList<Item> list;
    String country;
    int countryFlagId;


    public MainAdapter(Context context, ArrayList<Item> list) {
        super();
        this.context = context;
        this.list = list;
    }

    public void setCountry(String country){
        this.country = country;
    }

    public void setCountryFlagId(int countryFlagId){
        this.countryFlagId = countryFlagId;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
            Item item = list.get(position);
            holder.button.setText(item.name);
            holder.button.setTextColor(Color.BLACK);
            holder.button.setTextSize(20);

            switch (item.getName()){
                case "감기약":
                    holder.button.setBackgroundResource(R.drawable.cold);
                    break;
                case "진통제":
                    holder.button.setBackgroundResource(R.drawable.painrelief);
                    break;
                case "소화제":
                    holder.button.setBackgroundResource(R.drawable.idigestion);
                    break;
                case "멀미약":
                    holder.button.setBackgroundResource(R.drawable.motionsick);
                    break;
                case "지사제":
                    holder.button.setBackgroundResource(R.drawable.diarrhea);
                    break;
                case "치질약":
                    holder.button.setBackgroundResource(R.drawable.hemorrhoids);
                    break;
                case "관절염":
                    holder.button.setBackgroundResource(R.drawable.joint);
                    break;
                case "수면제":
                    holder.button.setBackgroundResource(R.drawable.sleeping);
                    break;
            }
            holder.button.setAlpha(0.5f);
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SearchActivity.class);
                    intent.putExtra("symptom", holder.button.getText());
                    intent.putExtra("country", country);
                    intent.putExtra("countryFlagId", countryFlagId);
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

