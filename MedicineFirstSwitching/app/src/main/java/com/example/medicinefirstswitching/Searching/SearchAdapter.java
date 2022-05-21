package com.example.medicinefirstswitching.Searching;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.medicinefirstswitching.R;

import java.util.ArrayList;


public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final int VIEW_TYPE_RECENT = 0;
    final int ViEW_TYPE_COMPLETE = 1;

    protected ArrayList<RecentItem> recentDataSet;
    protected ArrayList<SearchItem> mediDataSet;

    //ViewAdapter
    SearchAdapter(ArrayList<RecentItem> recentData, ArrayList<SearchItem> mediData) {
        recentDataSet = recentData;
        mediDataSet = mediData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if(viewType == VIEW_TYPE_RECENT) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_recentitem, viewGroup, false);
            return new RecentViewHolder(itemView);
        }
        if(viewType == ViEW_TYPE_COMPLETE) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_item, viewGroup, false);
            return new SearchViewHolder(itemView);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof RecentViewHolder) {
            ((RecentViewHolder) holder).populate(recentDataSet.get(position));
        }
        if(holder instanceof SearchViewHolder) {
            ((SearchViewHolder) holder).populate(mediDataSet.get(position - recentDataSet.size()));
        }
    }

    @Override
    public int getItemCount() {
        return recentDataSet.size() + mediDataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position < recentDataSet.size()) {
            return VIEW_TYPE_RECENT;
        }

        if(position - recentDataSet.size() < mediDataSet.size()) {
            return ViEW_TYPE_COMPLETE;
        }

        return -1;
    }

    public void filterList(ArrayList<SearchItem> filteredList) {
        mediDataSet = filteredList;
        notifyDataSetChanged();
    }

    public void setHistory(ArrayList<RecentItem> historyList) {
        recentDataSet = historyList;
        notifyDataSetChanged();
    }

    //RecentViewHolder
    public static class RecentViewHolder extends RecyclerView.ViewHolder {
        private final TextView textItem;
        private final TextView textDate;
        private final ImageButton btn1;

        public RecentViewHolder(View view) {
            super(view);

            textItem = (TextView) view.findViewById(R.id.search_text_recentItem);
            textDate = (TextView) view.findViewById(R.id.search_text_date);
            btn1 = (ImageButton) view.findViewById(R.id.search_btn_delItem);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        EditText edit = (EditText) view.findViewById(R.id.search_edit_searchText);
                        edit.setText(textItem.getText());
                    }
                }
            });

            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //delete history
                }
            });
        }

        public void populate(RecentItem recentItem) {
            textItem.setText(recentItem.getItem());
            textDate.setText(recentItem.getDate());
        }

        public TextView getTextView() { return textItem; }
    }

    //MedicineViewHolder
    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        private final TextView textItem;
        private final ImageButton btn1;

        public SearchViewHolder(View view) {
            super(view);

            textItem = (TextView) view.findViewById(R.id.search_text_searchItem);
            btn1 = (ImageButton) view.findViewById(R.id.search_btn_loadItem);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        //consider go result directly
                        EditText edit = (EditText) view.findViewById(R.id.search_edit_searchText);
                        edit.setText(textItem.getText());
                    }
                }
            });

            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText edit = (EditText) view.findViewById(R.id.search_edit_searchText);
                    edit.setText(textItem.getText());
                }
            });
        }

        public void populate(SearchItem searchItem) {
            textItem.setText(searchItem.getItem());
        }

        public TextView getTextView() { return textItem; }
    }
}
