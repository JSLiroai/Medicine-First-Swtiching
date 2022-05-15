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


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    protected ArrayList<SearchItem> localDataSet;

    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView1;
        private final ImageButton btn1;

        public SearchViewHolder(View view) {
            super(view);

            textView1 = (TextView) view.findViewById(R.id.search_text_recentItem);
            btn1 = (ImageButton) view.findViewById(R.id.search_btn_loadItem);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        //consider go result directly
                        EditText edit = (EditText) view.findViewById(R.id.search_edit_searchText);
                        edit.setText(textView1.getText());
                    }
                }
            });

            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText edit = (EditText) view.findViewById(R.id.search_edit_searchText);
                    edit.setText(textView1.getText());
                }
            });
        }

        public TextView getTextView() { return textView1; }
    }

    SearchAdapter(ArrayList<SearchItem> dataSet) {
        localDataSet = dataSet;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_item, viewGroup, false);

        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder viewHolder, final int position) {
        SearchItem currentItem = localDataSet.get(position);

        viewHolder.getTextView().setText(currentItem.getText());
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public void filterList(ArrayList<SearchItem> filteredList) {
        localDataSet = filteredList;
        notifyDataSetChanged();
    }
}
