package com.example.medicinefirstswitching.Searching;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicinefirstswitching.R;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView searchRecyclerView;
    private RecyclerView.LayoutManager searchLayoutManager;
    private SearchAdapter searchAdapter;

    private ArrayList<SearchItem> searchDataList;

    RecyclerView latestRecyclerView;
    RecentSearchAdapter recentSearchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        createDataList();
        buildRecyclerView();
        buildEditText();

    }

    private void filter(String text) {
        //if edittext == "" -> history else autocombo
        ArrayList<SearchItem> filteredList = new ArrayList<>();

        for(SearchItem item : searchDataList) {
            if(item.getText().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            } //filtering
        }

        searchAdapter.filterList(filteredList);
    }

    private void createDataList() {
        //get datalist from DB
    }

    private void buildRecyclerView() {
        searchRecyclerView = findViewById(R.id.search_recycler);
        searchRecyclerView.setHasFixedSize(true);
        searchLayoutManager = new LinearLayoutManager(this);
        searchAdapter = new SearchAdapter(searchDataList);//Early recycleAdapter setting

        searchRecyclerView.setLayoutManager(searchLayoutManager);
        searchRecyclerView.setAdapter(searchAdapter);

        /*
        latestRecyclerView = (RecyclerView) findViewById(R.id.search_recycler);
        latestRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        latestSearchAdapter = new LatestSearchAdapter("sample");

        latestRecyclerView.setAdapter(latestSearchAdapter);
        */
    }

    private void buildEditText() {
        EditText editText = findViewById(R.id.search_edit_searchText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //start result activity
                    return true;
                }
                return false;
            }
        });
    }
}
