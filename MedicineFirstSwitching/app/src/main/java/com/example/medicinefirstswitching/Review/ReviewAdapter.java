package com.example.medicinefirstswitching.Review;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.medicinefirstswitching.R;

import com.example.medicinefirstswitching.Searching.SearchItem;

import java.util.ArrayList;

public class ReviewAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater mLayoutInflater = null;
    ArrayList<ReviewItem> mdata;

    public ReviewAdapter(Context context, ArrayList<ReviewItem> data) {
            mContext = context;
            mdata = data;
            mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mdata.size();
    }

    @Override
    public Object getItem(int position) {
        return mdata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.review_item, null);

        TextView textID = (TextView)view.findViewById(R.id.review_id);
        TextView textContext = (TextView)view.findViewById(R.id.review_context);
        RatingBar rateBar = (RatingBar)view.findViewById(R.id.review_rate);

        textID.setText("ID:" + mdata.get(position).getId());
        textContext.setText(mdata.get(position).getContext());
        rateBar.setRating(mdata.get(position).getRate());

        return view;
    }
}
