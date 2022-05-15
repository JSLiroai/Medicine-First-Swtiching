package com.example.medicinefirstswitching;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicinefirstswitching.RecyclerView.Item;
import com.example.medicinefirstswitching.RecyclerView.MainAdapter;
import com.example.medicinefirstswitching.RecyclerView.ResultAdapter;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ResultAdapter adapter;
    private LinearLayoutManager layoutManager;
    public ArrayList<Item> testList = new ArrayList<Item>() {{
        add(new Item("Tylenol","aaa.jpg"));
        add(new Item("Zicam","aaa.jpg"));
        add(new Item("Aspirin","aaa.jpg"));

    }};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //VIEW MATCHING
        recyclerView = (RecyclerView)findViewById(R.id.result_recyclerview);

        //RECYCLERVIEW
        adapter = new ResultAdapter(getApplicationContext(), testList);
        layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
