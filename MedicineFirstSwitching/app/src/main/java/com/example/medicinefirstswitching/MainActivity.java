package com.example.medicinefirstswitching;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.medicinefirstswitching.RecyclerView.Item;
import com.example.medicinefirstswitching.RecyclerView.RecyclerViewAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private Spinner spinner;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private GridLayoutManager layoutManager;
    public ArrayList<Item> testList = new ArrayList<Item>() {{
        add(new Item("감기약",1));
        add(new Item("속쓰림",2));
        add(new Item("변비약",3));
        add(new Item("지사제",4));
        add(new Item("치질약",5));
        add(new Item("멀미약",6));
        add(new Item("관절염",7));
        add(new Item("가스릴리프",8));
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //VIEW MATCHING
        recyclerView = (RecyclerView)findViewById(R.id.grid_recyclerview);
        spinner = (Spinner)findViewById(R.id.main_spn_travel);


        //SPINNER
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //RECYCLERVIEW
        adapter = new RecyclerViewAdapter(getApplicationContext(), testList);
        layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}