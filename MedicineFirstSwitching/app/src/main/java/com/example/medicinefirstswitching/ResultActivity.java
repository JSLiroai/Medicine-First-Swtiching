package com.example.medicinefirstswitching;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicinefirstswitching.RecyclerView.Item;
import com.example.medicinefirstswitching.RecyclerView.ResultAdapter;
import com.example.medicinefirstswitching.Searching.SearchActivity;
import com.google.common.io.LineReader;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    //VIEW COMPONENT
    private RecyclerView recyclerView;
    private ResultAdapter adapter;
    private LinearLayoutManager layoutManager;
    private ImageButton homeButton;
    private ImageButton closeButton;

    private View expandMedicineTranslate;
    private View expandMedicineExplain;
    private View expandMedicineReview;
    private View expandSimilarMedicine;

    private LinearLayout medicineTranslate;
    private LinearLayout medicineReview;
    private LinearLayout similarMedicine;
    private LinearLayout medicineExplain;

    private ImageView toggleTranslate;
    private ImageView toggleExplain;
    private ImageView toggleReview;
    private ImageView toggleSimilar;


    //TEST ARRAY
    public ArrayList<Item> testList = new ArrayList<Item>() {{
        add(new Item("Tylenol","aaa.jpg"));
        add(new Item("Zicam","aaa.jpg"));
        add(new Item("Aspirin","aaa.jpg"));

    }};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        init();

        //EXPAND MEDICINE EXPLAIN
        expandMedicineExplain.setOnClickListener(new ExpandContainerListener(medicineExplain, toggleExplain));
        expandSimilarMedicine.setOnClickListener(new ExpandContainerListener(similarMedicine, toggleSimilar));
        expandMedicineReview.setOnClickListener(new ExpandContainerListener(medicineReview, toggleReview));
        expandMedicineTranslate.setOnClickListener(new ExpandContainerListener(medicineTranslate, toggleTranslate));

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

    public void init(){
        //VIEW MATCHING
        recyclerView = (RecyclerView)findViewById(R.id.result_recyclerview);
        homeButton = (ImageButton)findViewById(R.id.result_btn_home);
        closeButton = (ImageButton)findViewById(R.id.result_btn_close);

        expandMedicineExplain = findViewById(R.id.result_container_explain);
        expandMedicineReview = findViewById(R.id.result_container_review);
        expandSimilarMedicine = findViewById(R.id.result_container_similar);
        expandMedicineTranslate = findViewById(R.id.result_container_translate);

        medicineReview = (LinearLayout) findViewById(R.id.medicine_review);
        similarMedicine = (LinearLayout)findViewById(R.id.similar_medicine);
        medicineExplain = (LinearLayout)findViewById(R.id.medicine_explain);
        medicineTranslate = (LinearLayout)findViewById(R.id.medicine_translate);


        toggleExplain = findViewById(R.id.result_view_medicineExplainToggle);
        toggleReview = findViewById(R.id.result_view_reviewToggle);
        toggleSimilar = findViewById(R.id.result_view_similarMedicineToggle);
        toggleTranslate = findViewById(R.id.result_view_medicineTranslateToggle);

    }

    //EXPAND 시 발생하는 이벤트 리스너
    public class ExpandContainerListener implements View.OnClickListener {

        View expandContainer;  //Expand될 컨테이너
        View toggle;           // 화살표 토글

        public ExpandContainerListener(View expandContainer, View toggle){
            this.expandContainer = expandContainer;
            this.toggle = toggle;
        }

        public void onClick(View view) {
            if(expandContainer.getVisibility()==View.VISIBLE){
                expandContainer.setVisibility(View.GONE);
                toggle.animate().setDuration(200).rotation(180f);
            }
            else{
                expandContainer.setVisibility(View.VISIBLE);
                toggle.animate().setDuration(200).rotation(0f);
            }
        }
    }
}
