package com.example.medicinefirstswitching;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicinefirstswitching.RecyclerView.Item;
import com.example.medicinefirstswitching.RecyclerView.ResultAdapter;
import com.example.medicinefirstswitching.Searching.SearchActivity;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

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

    private Button moreReviewBtn;


    //입력될 값
    private TextView titleMedicineName;
    private CircleImageView countryFlag;
    private TextView countryName;
    private TextView koreanMedicineName;
    private TextView foreignMedicineName;
    private ImageView medicineImage;
    private TextView medicineName;
    private TextView medicineCompany;
    private TextView medicineForm;
    private TextView medicineIngredient;
    private TextView medicineEffectiveness;
    private TextView medicineWarning;


    //Intent 가져올 값
    String koreanMedicine;
    String country;
    String symptom;
    int countryFlagId;

    //DataList
    private ArrayList<ResultItem> resultDataList;

    //DB
    private DBConnection db;

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

        Intent intent = getIntent();
        koreanMedicine = intent.getStringExtra("String-SearchedItem");
        symptom = intent.getStringExtra("String-Symptom");
        country = intent.getStringExtra("String-Country");
        countryFlagId = intent.getIntExtra("Int-CountryFlagId", R.drawable.unitedstates);

        db = new DBConnection(country,symptom, ResultActivity.this);

        Log.d("[SYMPTOM]", symptom);
        Log.d("[COUNTRY]", country);
        Log.d("[COUNTRYFLAGID]", countryFlagId+"");

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

        //REVIEW
        moreReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
                intent.putExtra("Sting-item", testList.get(0).name);
                startActivity(intent);
            }
        });
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

        titleMedicineName = findViewById(R.id.result_tv_title);
        countryFlag = findViewById(R.id.result_iv_countryFlag);
        countryName = findViewById(R.id.result_tv_countryName);
        koreanMedicineName = findViewById(R.id.result_tv_medicineNameKr);
        foreignMedicineName = findViewById(R.id.result_tv_medicineNameForeign);
        medicineImage = findViewById(R.id.result_iv_medicineImage);
        medicineName = findViewById(R.id.result_tv_name);
        medicineCompany = findViewById(R.id.result_tv_company);
        medicineForm = findViewById(R.id.result_tv_form);
        medicineIngredient = findViewById(R.id.result_tv_ingredient);
        medicineEffectiveness = findViewById(R.id.result_tv_effectiveness);
        medicineWarning = findViewById(R.id.result_tv_warning);

        moreReviewBtn = findViewById(R.id.result_review_more);
    }
    private void createDataList() {
        resultDataList = new ArrayList<ResultItem>();
        ArrayList<HashMap<String, String>> dbList = db.getmArrayList();

        for(HashMap<String, String> item : dbList) {
            ResultItem data = new ResultItem(item.get("Product"), item.get("Company"), item.get("Form"),item.get("Ingredient"), item.get("Effectiveness"),item.get("Warning"));
            resultDataList.add(data);
            Log.d("[DATA INSERT]", data.toString());
        }
    }

    public void updateData(ArrayList<HashMap<String, String>> dbList){
        resultDataList.clear();
        for(HashMap<String, String> item : dbList) {
            ResultItem data = new ResultItem(item.get("Product"), item.get("Company"), item.get("Form"),item.get("Ingredient"), item.get("Effectiveness"),item.get("Warning"));
            resultDataList.add(data);
            Log.d("[DATA INSERT]", data.toString());
        }
    }


    public void setData(){
        ResultItem settingItem = resultDataList.get(0);

        //약 이름 제목 설정
        titleMedicineName.setText(settingItem.getProduct());

        //약품 번역 창 설정
        koreanMedicineName.setText(koreanMedicine);
        countryName.setText(country);
        countryFlag.setImageResource(countryFlagId);
        foreignMedicineName.setText(settingItem.getProduct());

        //약품 이미지 설정
        String imageURL = "https://mfs-bucket.s3.ap-northeast-2.amazonaws.com/" + settingItem.getProduct() + "jpg";

        //Picasso.get().load(imageURL).into(medicineImage);

        //약품 설명 설정
        medicineName.setText(settingItem.getProduct());
        medicineCompany.setText(settingItem.getCompany());
        medicineForm.setText(settingItem.getForm());

        //약품 주요 성분 설정
        medicineIngredient.setText(settingItem.getIngredient());

        //약품 주요 효능
        medicineEffectiveness.setText(settingItem.getEffectiveness());

        //약품 주의사항
        medicineWarning.setText(settingItem.getWarning());
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
