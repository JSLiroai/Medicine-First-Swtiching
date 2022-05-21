package com.example.medicinefirstswitching;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.example.medicinefirstswitching.Searching.SearchActivity;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ResultAdapter adapter;
    private LinearLayoutManager layoutManager;
    private ImageButton homeButton;
    private ImageButton closeButton;


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
        homeButton = (ImageButton)findViewById(R.id.result_btn_home);
        closeButton = (ImageButton)findViewById(R.id.result_btn_close);


        //CLOSE BUTTON
        closeButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });


        //HOME BUTTON
        homeButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        //RECYCLERVIEW
        adapter = new ResultAdapter(getApplicationContext(), testList);
        layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
