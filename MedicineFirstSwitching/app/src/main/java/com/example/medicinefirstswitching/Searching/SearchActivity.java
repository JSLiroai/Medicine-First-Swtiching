package com.example.medicinefirstswitching.Searching;

import android.content.Intent;
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

import com.example.medicinefirstswitching.DBConnection;
import com.example.medicinefirstswitching.R;
import com.example.medicinefirstswitching.ResultActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class SearchActivity extends AppCompatActivity {
    private static final String SEARCH_HISTORY = "search_history";

    private RecyclerView searchRecyclerView;
    private RecyclerView.LayoutManager searchLayoutManager;
    private SearchAdapter searchAdapter;

    private DBConnection db;
    private ArrayList<SearchItem> symptomDataList;
    private ArrayList<SearchItem> searchDataList;
    private ArrayList<RecentItem> recentDataList;
    private String tCountry = "대한민국";
    private int countryFlagId;

    private ImageButton searchBtn;
    private EditText editText;
    private ImageButton btnDelText;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DBConnection("대한민국", SearchActivity.this);
        setContentView(R.layout.activity_search);
        
        //카테고리 버튼 클릭 시
        Intent intent = getIntent();
        String symptom = intent.getStringExtra("symptom");
        tCountry = intent.getStringExtra("country");
        countryFlagId = intent.getIntExtra("countryFlagId", R.drawable.unitedstates);

        editText = findViewById(R.id.search_edit_searchText);
        editText.setText(symptom);

        Log.d("[SYMPTOM]", symptom);
        Log.d("[COUNTRY]", tCountry);
        Log.d("[COUNTRYFLAGID]", countryFlagId+"");


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
        symptomDataList = new ArrayList<SearchItem>();
        searchDataList = new ArrayList<SearchItem>();

        symptomDataList.add(new SearchItem("진통제","증상"));
        symptomDataList.add(new SearchItem("감기약","증상"));
        symptomDataList.add(new SearchItem("소화제","증상"));
        symptomDataList.add(new SearchItem("변비약","증상"));
        symptomDataList.add(new SearchItem("지사제","증상"));
        symptomDataList.add(new SearchItem("치질약","증상"));
        symptomDataList.add(new SearchItem("멀미약","증상"));
        symptomDataList.add(new SearchItem("관절염","증상"));
        symptomDataList.add(new SearchItem("수면제","증상"));
        symptomDataList.add(new SearchItem("속쓰림","증상"));

        ArrayList<HashMap<String, String>> dbList = db.getmArrayList();

        for(HashMap<String, String> item : dbList) {
            searchDataList.add(new SearchItem(item.get("Product"), item.get("Category")));
        }
    }

    public void updateData(ArrayList<HashMap<String, String>> dbList){
        searchDataList.clear();
        for(HashMap<String, String> item : dbList) {
            searchDataList.add(new SearchItem(item.get("Product"), item.get("Category")));
        }
        filter(editText.getText().toString());
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
        searchAdapter = new SearchAdapter(this, recentDataList, symptomDataList, searchDataList);

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
        ArrayList<SearchItem> symList = new ArrayList<SearchItem>();
        ArrayList<SearchItem> searchList = new ArrayList<SearchItem>();
        if(text.equals("")) {
            btnDelText.setVisibility(View.INVISIBLE);
            ((LinearLayoutManager) searchLayoutManager).setReverseLayout(true);
            ((LinearLayoutManager) searchLayoutManager).setStackFromEnd(true);

            searchAdapter.setHistory(recentDataList);
        }
        else {
            btnDelText.setVisibility(View.VISIBLE);

            ArrayList<RecentItem> history = new ArrayList<RecentItem>();
            ((LinearLayoutManager) searchLayoutManager).setReverseLayout(false);
            ((LinearLayoutManager) searchLayoutManager).setStackFromEnd(false);

            searchAdapter.setHistory(history);

            SearchItem symptom = null;
            for(SearchItem item : symptomDataList) {
                if(item.getItem().equals(text)) symptom = item;
            }

            if(symptom != null) {
                Log.v("Debug",symptom.getExplain());
                String searchSym = symptom.getItem();

                for(SearchItem item : searchDataList) {
                    if(item.getExplain().equals(searchSym)) searchList.add(item);
                }
            }
            else {
                for(SearchItem item : symptomDataList) {
                    if(item.getItem().toLowerCase().contains(text.toLowerCase())) symList.add(item);
                }

                for(SearchItem item : searchDataList) {
                    if(item.getItem().toLowerCase().contains(text.toLowerCase())) searchList.add(item);
                }
            }
        }

        searchAdapter.filterList(symList, searchList);
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
            intent.putExtra("String-SearchedItem",checkTarget.getItem());
            intent.putExtra("String-Symptom",checkTarget.getExplain());
            intent.putExtra("String-Country",tCountry);
            intent.putExtra("Int-CountryFlagId", countryFlagId);
            startActivity(intent);
        }
    }

}
