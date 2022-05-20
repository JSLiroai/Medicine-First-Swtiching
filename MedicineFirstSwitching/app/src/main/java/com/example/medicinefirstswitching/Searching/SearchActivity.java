package com.example.medicinefirstswitching.Searching;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicinefirstswitching.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SearchActivity extends AppCompatActivity {
    private static final String SEARCH_HISTORY = "search_history";

    private RecyclerView searchRecyclerView;
    private RecyclerView.LayoutManager searchLayoutManager;
    private SearchAdapter searchAdapter;

    private ArrayList<SearchItem> searchDataList;
    private ArrayList<RecentItem> recentDataList;

    private ImageButton searchBtn;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        editText = findViewById(R.id.search_edit_searchText);
        searchBtn = findViewById(R.id.search_btn_seearch);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addHistoryData(editText.getText().toString());
            }
        });

        createDataList();
        buildRecyclerView();
        buildEditText();
    }

    private void createDataList() {
        recentDataList = readHistory();

        searchDataList = new ArrayList<SearchItem>();
        searchDataList.add(new SearchItem("asd"));
        searchDataList.add(new SearchItem("asde"));
    }

    private void buildRecyclerView() {
        searchRecyclerView = findViewById(R.id.search_recycler);
        searchRecyclerView.setHasFixedSize(true);
        searchLayoutManager = new LinearLayoutManager(this);
        searchAdapter = new SearchAdapter(recentDataList, searchDataList);//Early recycleAdapter setting

        searchRecyclerView.setLayoutManager(searchLayoutManager);
        searchRecyclerView.setAdapter(searchAdapter);
    }

    private void buildEditText() {
        filter(editText.getText().toString());

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
                    addHistoryData(textView.getText().toString());
                    //start result activity

                    return true;
                }
                return false;
            }
        });
    }

    private void filter(String text) {
        //add symptom check
        ArrayList<SearchItem> searchList = new ArrayList<>();
        if(text.equals("")) {
            searchAdapter.setHistory(recentDataList);
        }
        else {
            ArrayList<RecentItem> history = new ArrayList<RecentItem>();
            searchAdapter.setHistory(history);

            for(SearchItem item : searchDataList) {
                if(item.getItem().toLowerCase().contains(text.toLowerCase())) {
                    searchList.add(item);
                }
            }
        }

        Log.v("debug", searchList.toString());
        searchAdapter.filterList(searchList);
    }

    private ArrayList<RecentItem> readHistory() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Gson gson = new Gson();
        String json = sharedPrefs.getString(SEARCH_HISTORY, null);
        Type type = new TypeToken<ArrayList<RecentItem>>() {
        }.getType();
        if(json == null) return new ArrayList<RecentItem>();
        ArrayList<RecentItem> history = gson.fromJson(json, type);

        return history;
    }

    private void addHistoryData(String item) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd", java.util.Locale.getDefault());
        String date = dateFormat.format(new Date());

        RecentItem data = new RecentItem(item, date);
        recentDataList.add(data);
    }

}
