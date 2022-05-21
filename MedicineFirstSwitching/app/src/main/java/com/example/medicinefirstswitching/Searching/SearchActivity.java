package com.example.medicinefirstswitching.Searching;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.medicinefirstswitching.ResultActivity;
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
    private EditText editText;
    private ImageButton btnDelText;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        editText = findViewById(R.id.search_edit_searchText);
        searchBtn = findViewById(R.id.search_btn_seearch);
        btnDelText = findViewById(R.id.search_btn_delText);
        btnBack = findViewById(R.id.search_btn_back);

        createDataList();

        buildButtons();
        buildRecyclerView();
        buildEditText();
    }

    private void createDataList() {
        recentDataList = readHistory();

        searchDataList = new ArrayList<SearchItem>();
        searchDataList.add(new SearchItem("asd"));
        searchDataList.add(new SearchItem("asde"));
        searchDataList.add(new SearchItem("감기"));
    }

    private void buildButtons() {
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startResult(editText.getText().toString());
            }
        });
        btnDelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("");
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void buildRecyclerView() {
        searchRecyclerView = findViewById(R.id.search_recycler);
        searchRecyclerView.setHasFixedSize(true);
        searchLayoutManager = new LinearLayoutManager(this);
        searchAdapter = new SearchAdapter(this, recentDataList, searchDataList);

        searchRecyclerView.setLayoutManager(searchLayoutManager);
        searchRecyclerView.setAdapter(searchAdapter);
    }

    private void buildEditText() {
        editText.requestFocus();
        filter(editText.getText().toString());

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    startResult(editText.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    private void filter(String text) {
        //TODO:증상추가
        ArrayList<SearchItem> searchList = new ArrayList<>();
        if(text.equals("")) {
            btnDelText.setVisibility(View.INVISIBLE);

            ((LinearLayoutManager) searchLayoutManager).setReverseLayout(true);
            ((LinearLayoutManager) searchLayoutManager).setStackFromEnd(true);

            searchAdapter.setHistory(recentDataList);
        }
        else {
            btnDelText.setVisibility(View.VISIBLE);

            ArrayList<RecentItem> history = new ArrayList<RecentItem>();
            searchAdapter.setHistory(history);

            ((LinearLayoutManager) searchLayoutManager).setReverseLayout(false);
            ((LinearLayoutManager) searchLayoutManager).setStackFromEnd(false);

            for(SearchItem item : searchDataList) {
                if(item.getItem().toLowerCase().contains(text.toLowerCase())) {
                    searchList.add(item);
                }
            }
        }
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
        for(RecentItem history : recentDataList) {
            if(history.getItem().equals(item)) {
                recentDataList.remove(history);
                break;
            }
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd", java.util.Locale.getDefault());
        String date = dateFormat.format(new Date());

        RecentItem data = new RecentItem(item, date);
        recentDataList.add(data);
        saveHistory();
    }

    private void saveHistory() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(recentDataList);
        editor.putString(SEARCH_HISTORY, json);
        editor.commit();
    }

    public void delHistory(String item) {
        for(RecentItem history : recentDataList) {
            if(history.getItem().equals(item)) {
                recentDataList.remove(history);
                break;
            }
        }

        searchAdapter.setHistory(recentDataList);
        saveHistory();
    }
    //TODO:전체삭제추가
    private void resetHistory() {
        recentDataList.clear();
        searchAdapter.setHistory(recentDataList);
        saveHistory();
    }

    public EditText getEditText() {
        return editText;
    }

    private void startResult(String target) {
        SearchItem checkTarget = null;
        for(SearchItem item : searchDataList) {
            if(item.getItem().equals(target)) {
                checkTarget = item;
                break;
            }
        }

        if(checkTarget == null) {
            //TODO:notify wrong approach
        }
        else {
            addHistoryData(target);

            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
            intent.putExtra("String-SearchedItem",target);
            startActivity(intent);
        }
    }

}
