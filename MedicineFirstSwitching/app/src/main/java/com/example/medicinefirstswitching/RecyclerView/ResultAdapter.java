package com.example.medicinefirstswitching.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicinefirstswitching.R;
import com.example.medicinefirstswitching.ResultActivity;
import com.example.medicinefirstswitching.ResultItem;
import com.example.medicinefirstswitching.Searching.SearchActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.MyViewHolder> {

    Context context;
    ArrayList<ResultItem> list;
    String koreanMedicine;
    String country;
    String symptom;
    int countryFlagId;
    public ResultAdapter(Context context, ArrayList<ResultItem> list, String koreanMedicine, String country, String symptom, int countryFlagId) {
        super();
        this.context = context;
        this.list = list;
        this.koreanMedicine = koreanMedicine;
        this.country = country;
        this.symptom = symptom;
        this.countryFlagId = countryFlagId;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ResultItem item = list.get(position);
        String imageURL = "https://mfs-bucket.s3.ap-northeast-2.amazonaws.com/" + item.getProduct() + ".jpg";
        Picasso.get().load(imageURL).into(holder.button);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ResultActivity.class);
                intent.putExtra("String-SearchedItem", koreanMedicine);
                intent.putExtra("String-Symptom", symptom);
                intent.putExtra("String-Country", country);
                intent.putExtra("Int-CountryFlagId", countryFlagId);
                intent.putExtra("Int-Index", item.getIndex());
                Log.d("[MY INDEX]", item.getIndex()+"");
                context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

            }
        });

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
        ImageButton button;

        public MyViewHolder(View itemView) {
            super(itemView);
            button= itemView.findViewById(R.id.result_btn_similar);
        }
    }
}

