package com.example.medicinefirstswitching;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.medicinefirstswitching.Map.MapsActivity;
import com.example.medicinefirstswitching.RecyclerView.Item;
import com.example.medicinefirstswitching.RecyclerView.MainAdapter;
import com.example.medicinefirstswitching.Searching.SearchActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private Spinner spinner;
    private RecyclerView recyclerView;
    private MainAdapter adapter;
    private GridLayoutManager layoutManager;
    private Button searchButton;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private ImageButton slideHomeBtn;
    private ImageButton slideSearchBtn;
    private FloatingActionButton mapBtn;

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
        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.main_slideLayout_main);
        slideHomeBtn = findViewById(R.id.main_btn_slideHome);
        slideSearchBtn = findViewById(R.id.main_btn_slideSearch);
        searchButton = (Button) findViewById(R.id.main_btn_search);
        mapBtn = (FloatingActionButton) findViewById(R.id.main_fbn_location);



        //SEARCH BUTTON
        searchButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        //SLIDE SEARCH BUTTON
        slideSearchBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        //SLIDE HOME BUTTON
        slideHomeBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
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

        //SLIDING UP PANEL
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            //패널이 슬라이드 중일 때
            public void onPanelSlide(View panel, float slideOffset) {

                slideHomeBtn.setAlpha(slideOffset);
                slideSearchBtn.setAlpha(slideOffset);

            }

            @Override
            //패널의 상태가 변했을 때
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

            }
        });

        //Map Button
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });


        //RECYCLERVIEW
        adapter = new MainAdapter(getApplicationContext(), testList);
        layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

}