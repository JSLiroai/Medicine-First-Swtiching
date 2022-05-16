package com.example.medicinefirstswitching;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.example.medicinefirstswitching.RecyclerView.Item;
import com.example.medicinefirstswitching.RecyclerView.MainAdapter;
import com.example.medicinefirstswitching.Searching.SearchActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private Spinner spinner;
    private RecyclerView recyclerView;
    private MainAdapter adapter;
    private GridLayoutManager layoutManager;
    private Button button;
    public ArrayList<Item> testList = new ArrayList<Item>() {{
        add(new Item("감기약","aaa.jpg"));
        add(new Item("속쓰림","aaa.jpg"));
        add(new Item("변비약","aaa.jpg"));
        add(new Item("지사제","aaa.jpg"));
        add(new Item("치질약","aaa.jpg"));
        add(new Item("멀미약","aaa.jpg"));
        add(new Item("관절염","aaa.jpg"));
        add(new Item("가스릴리프","aaa.jpg"));
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //VIEW MATCHING
        recyclerView = (RecyclerView)findViewById(R.id.main_recyclerview);
        spinner = (Spinner)findViewById(R.id.main_spn_travel);



        //SEARCH BUTTON
        button = (Button)findViewById(R.id.main_btn_search);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });


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
        adapter = new MainAdapter(getApplicationContext(), testList);
        layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}