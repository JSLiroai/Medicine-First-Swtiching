package com.example.medicinefirstswitching;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicinefirstswitching.DBConnection;
import com.example.medicinefirstswitching.MainActivity;
import com.example.medicinefirstswitching.R;
import com.example.medicinefirstswitching.RecyclerView.Item;
import com.example.medicinefirstswitching.RecyclerView.ResultAdapter;
import com.example.medicinefirstswitching.Review.ReviewActivity;
import com.example.medicinefirstswitching.Review.ReviewConnection;
import com.example.medicinefirstswitching.Review.ReviewItem;
import com.example.medicinefirstswitching.Searching.SearchActivity;
import com.example.medicinefirstswitching.Searching.SearchItem;
import com.squareup.picasso.Picasso;

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

    private ConstraintLayout reviewRoot;
    private Button reviewMoreBtn;
    private LayoutInflater minflater;


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
    int index;

    //DataList
    private ArrayList<ResultItem> resultDataList;
    private ArrayList<ResultItem> similarDataList;


    //DB
    private DBConnection db;
    private ReviewConnection rdb;


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
        index = intent.getIntExtra("Int-Index", 0);

        db = new DBConnection(country,symptom, ResultActivity.this);

        Log.d("[SYMPTOM]", symptom);
        Log.d("[COUNTRY]", country);
        Log.d("[COUNTRYFLAGID]", countryFlagId+"");

        //EXPAND MEDICINE EXPLAIN
        expandMedicineExplain.setOnClickListener(new ExpandContainerListener(medicineExplain, toggleExplain));
        expandSimilarMedicine.setOnClickListener(new ExpandContainerListener(similarMedicine, toggleSimilar));
        expandMedicineReview.setOnClickListener(new ExpandContainerListener(medicineReview, toggleReview));
        expandMedicineTranslate.setOnClickListener(new ExpandContainerListener(medicineTranslate, toggleTranslate));

        createDataList();

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
                intent.putExtra("product",resultDataList.get(index).getProduct());
                startActivity(intent);
            }
        });

        //Review button
        reviewMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
                intent.putExtra("product",resultDataList.get(index).getProduct());
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

        reviewRoot = findViewById(R.id.result_review_root);
        reviewMoreBtn = findViewById(R.id.result_review_more);
        minflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        minflater.inflate(R.layout.review_rate_statis, reviewRoot,true);
    }
    private void createDataList() {

        int count = 0;

        resultDataList = new ArrayList<ResultItem>();
        similarDataList = new ArrayList<ResultItem>();

        ArrayList<HashMap<String, String>> dbList = db.getmArrayList();

        for(HashMap<String, String> item : dbList) {
            ResultItem data = new ResultItem(item.get("Product"), item.get("Company"), item.get("Form"),item.get("Ingredient"), item.get("Effectiveness"),item.get("Warning"));
            data.setIndex(count);
            resultDataList.add(data);

            //약품의 index가 현재 화면의 index와 일치하지 않으면 SimilarDataList에 담긴다
            if(index!=count){
                similarDataList.add(data);
                Log.d("[SIMILARDATALIST]", data.toString());
            }
            count++;

            Log.d("[RESULTDATALIST INSERT]", data.toString());
        }
    }

    public void updateData(ArrayList<HashMap<String, String>> dbList){
        resultDataList.clear();
        similarDataList.clear();

        int count = 0;

        for(HashMap<String, String> item : dbList) {
            ResultItem data = new ResultItem(item.get("Product"), item.get("Company"), item.get("Form"),item.get("Ingredient"), item.get("Effectiveness"),item.get("Warning"));
            resultDataList.add(data);
            data.setIndex(count);

            //약품의 index가 현재 화면의 index와 일치하지 않으면 SimilarDataList에 담긴다
            if(index!=count){
                similarDataList.add(data);
                Log.d("[SIMILARDATALIST]", data.toString());
            }
            count++;

            Log.d("[RESULTDATALIST INSERT]", data.toString());
        }

        setData();
        rdb = new ReviewConnection(resultDataList.get(index).getProduct(), ResultActivity.this);

        //RECYCLERVIEW
        adapter = new ResultAdapter(getApplicationContext(), similarDataList, koreanMedicine, country, symptom, countryFlagId);
        layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void updateReviewData(ArrayList<HashMap<String, String>> dbList) {
        int r1 = 0, r2 = 0, r3 = 0, r4 = 0, r5 = 0;
        for(HashMap<String, String> item : dbList) {
            int rate = Integer.parseInt(item.get("Rate"));
            switch (rate) {
                case 1: r1++; break;
                case 2: r2++; break;
                case 3: r3++; break;
                case 4: r4++; break;
                case 5: r5++; break; }
        }
        float rAvg = ((float) (r1*1+r2*2+r3*3+r4*4+r5*5))/(r1+r2+r3+r4+r5);

        ((RatingBar) findViewById(R.id.review_rate_avg_star)).setRating(rAvg);
        ((TextView) findViewById(R.id.review_rate_avg)).setText(String.format("%.1f",rAvg));
        ((ProgressBar)  findViewById(R.id.review_rate_5)).setProgress(r5/(r1+r2+r3+r4+r5)*100);
        ((ProgressBar)  findViewById(R.id.review_rate_4)).setProgress(r4/(r1+r2+r3+r4+r5)*100);
        ((ProgressBar)  findViewById(R.id.review_rate_3)).setProgress(r3/(r1+r2+r3+r4+r5)*100);
        ((ProgressBar)  findViewById(R.id.review_rate_2)).setProgress(r2/(r1+r2+r3+r4+r5)*100);
        ((ProgressBar)  findViewById(R.id.review_rate_1)).setProgress(r1/(r1+r2+r3+r4+r5)*100);
    }

    public void setData(){
        ResultItem settingItem = resultDataList.get(index);

        //약 이름 제목 설정
        titleMedicineName.setText(settingItem.getProduct());

        //약품 번역 창 설정
        koreanMedicineName.setText(koreanMedicine);
        countryName.setText(country);
        countryFlag.setImageResource(countryFlagId);
        foreignMedicineName.setText(settingItem.getProduct());

        //약품 이미지 설정
        String imageURL = "https://mfs-bucket.s3.ap-northeast-2.amazonaws.com/" + settingItem.getProduct() + ".jpg";

        Picasso.get().load(imageURL).into(medicineImage);

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