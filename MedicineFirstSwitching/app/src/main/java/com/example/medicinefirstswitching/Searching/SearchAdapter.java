package com.example.medicinefirstswitching.Searching;

import android.util.Log;
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
    final int VIEW_TYPE_SYMPTOM = 1;
    final int ViEW_TYPE_COMPLETE = 2;

    protected static SearchActivity activity;
    protected ArrayList<RecentItem> recentDataSet;
    protected ArrayList<SearchItem> symptomDataSet;
    protected ArrayList<SearchItem> mediDataSet;

    //ViewAdapter
    SearchAdapter(SearchActivity activity, ArrayList<RecentItem> recentData, ArrayList<SearchItem> symData, ArrayList<SearchItem> mediData) {
        this.activity = activity;
        recentDataSet = recentData;
        symptomDataSet = symData;
        mediDataSet = mediData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if(viewType == VIEW_TYPE_RECENT) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_recentitem, viewGroup, false);
            return new RecentViewHolder(itemView);
        }
        if(viewType == VIEW_TYPE_SYMPTOM) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_item, viewGroup, false);
            return new SymptomViewHolder(itemView);
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
        if(holder instanceof SymptomViewHolder) {
            ((SymptomViewHolder) holder).populate(symptomDataSet.get(position - recentDataSet.size()));
        }
        if(holder instanceof SearchViewHolder) {
            ((SearchViewHolder) holder).populate(mediDataSet.get(position - recentDataSet.size() - symptomDataSet.size()));
        }
    }

    @Override
    public int getItemCount() {
        return recentDataSet.size() + symptomDataSet.size() + mediDataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position < recentDataSet.size()) {
            return VIEW_TYPE_RECENT;
        }

        if(position - recentDataSet.size() < symptomDataSet.size()) {
            return VIEW_TYPE_SYMPTOM;
        }

        if(position - recentDataSet.size() - symptomDataSet.size() < mediDataSet.size()) {
            return ViEW_TYPE_COMPLETE;
        }

        return -1;
    }

    public void filterList(ArrayList<SearchItem> symList, ArrayList<SearchItem> filteredList) {
        symptomDataSet = symList;
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
                        EditText edit = activity.getEditText();
                        edit.setText(textItem.getText().toString());
                        edit.setSelection(edit.length());
                    }
                }
            });

            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        activity.delHistory(textItem.getText().toString());
                    }
                }
            });
        }

        public void populate(RecentItem recentItem) {
            textItem.setText(recentItem.getItem());
            textDate.setText(recentItem.getDate());
        }

        public TextView getTextView() { return textItem; }
    }

    //SymptomViewHolder
    public static class SymptomViewHolder extends RecyclerView.ViewHolder {
        private final TextView textItem;
        private final TextView textExplain;
        private final ImageButton btn1;

        public SymptomViewHolder(View view) {
            super(view);

            textItem = (TextView) view.findViewById(R.id.search_text_searchItem);
            textExplain = (TextView) view.findViewById(R.id.search_text_explain);
            btn1 = (ImageButton) view.findViewById(R.id.search_btn_loadItem);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        EditText edit = activity.getEditText();
                        edit.setText(textItem.getText().toString());
                        edit.setSelection(edit.length());
                    }
                }
            });

            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText edit = activity.getEditText();
                    edit.setText(textItem.getText().toString());
                    edit.setSelection(edit.length());
                }
            });
        }

        public void populate(SearchItem searchItem) {
            textItem.setText(searchItem.getItem());
            textExplain.setText(searchItem.getExplain());
        }

        public TextView getTextView() { return textItem; }
    }

    //MedicineViewHolder
    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        private final TextView textItem;
        private final TextView textExplain;
        private final ImageButton btn1;

        public SearchViewHolder(View view) {
            super(view);

            textItem = (TextView) view.findViewById(R.id.search_text_searchItem);
            textExplain = (TextView) view.findViewById(R.id.search_text_explain);
            btn1 = (ImageButton) view.findViewById(R.id.search_btn_loadItem);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        EditText edit = activity.getEditText();
                        edit.setText(textItem.getText().toString());
                        edit.setSelection(edit.length());
                    }
                }
            });

            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText edit = activity.getEditText();
                    edit.setText(textItem.getText().toString());
                    edit.setSelection(edit.length());
                }
            });
        }

        public void populate(SearchItem searchItem) {
            textItem.setText(searchItem.getItem());
            textExplain.setText(searchItem.getExplain());
        }

        public TextView getTextView() { return textItem; }
    }
}
