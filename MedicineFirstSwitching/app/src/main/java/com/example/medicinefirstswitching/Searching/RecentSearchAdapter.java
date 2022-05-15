package com.example.medicinefirstswitching.Searching;

import com.example.medicinefirstswitching.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class RecentSearchAdapter extends RecyclerView.Adapter<RecentSearchAdapter.ViewHolder> {
    private ArrayList<SearchItem> localDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textItem;
        private final TextView textDate;
        private final ImageButton delBtn;

        public ViewHolder(View view) {
            super(view);

            textItem = (TextView) view.findViewById(R.id.search_text_recentItem);
            textDate = (TextView) view.findViewById(R.id.search_text_date);
            delBtn = (ImageButton) view.findViewById(R.id.search_btn_delItem);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            delBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        public TextView getTextView() {
            return textItem;
        }
    }

    RecentSearchAdapter(ArrayList<SearchItem> dataSet) {
        localDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_recentitem, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getTextView().setText(localDataSet.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public void submitData(ArrayList<SearchItem> historyList) {
        localDataSet = historyList;
    }

}
