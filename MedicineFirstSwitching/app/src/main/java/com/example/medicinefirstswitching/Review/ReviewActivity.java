package com.example.medicinefirstswitching.Review;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.medicinefirstswitching.R;
import com.example.medicinefirstswitching.Searching.SearchItem;

import java.util.ArrayList;
import java.util.HashMap;

public class ReviewActivity extends AppCompatActivity {
    private String product;
    private ReviewConnection rc;

    private LayoutInflater minflater;
    private ConstraintLayout reviewStatusRoot;
    private ListView reviewListView;
    private ImageButton backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        init();

        Intent intent = getIntent();
        product = intent.getStringExtra("product");

        rc = new ReviewConnection(product, ReviewActivity.this);

        minflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        minflater.inflate(R.layout.review_rate_statis, reviewStatusRoot,true);
    }

    private void init() {
        reviewStatusRoot = (ConstraintLayout) findViewById(R.id.review_rate_root);
        reviewListView = (ListView) findViewById(R.id.review_listview_content);
        backbtn = (ImageButton) findViewById(R.id.review_back);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void updateData(ArrayList<HashMap<String, String>> dbList){
        ArrayList<ReviewItem> list = new ArrayList<ReviewItem>();
        int r1 = 0, r2 = 0, r3 = 0, r4 = 0, r5 = 0;
        for(HashMap<String, String> item : dbList) {
            int rate = Integer.parseInt(item.get("Rate"));
            switch (rate) {
                case 1: r1++; break;
                case 2: r2++; break;
                case 3: r3++; break;
                case 4: r4++; break;
                case 5: r5++; break; }

            list.add(new ReviewItem(item.get("Id"), item.get("Context"), rate));
        }
        float rAvg = ((float) (r1*1+r2*2+r3*3+r4*4+r5*5))/(r1+r2+r3+r4+r5);

        ((RatingBar) findViewById(R.id.review_rate_avg_star)).setRating(rAvg);
        ((TextView) findViewById(R.id.review_rate_avg)).setText(String.format("%.1f",rAvg));
        ((ProgressBar)  findViewById(R.id.review_rate_5)).setProgress(r5/(r1+r2+r3+r4+r5)*100);
        ((ProgressBar)  findViewById(R.id.review_rate_4)).setProgress(r4/(r1+r2+r3+r4+r5)*100);
        ((ProgressBar)  findViewById(R.id.review_rate_3)).setProgress(r3/(r1+r2+r3+r4+r5)*100);
        ((ProgressBar)  findViewById(R.id.review_rate_2)).setProgress(r2/(r1+r2+r3+r4+r5)*100);
        ((ProgressBar)  findViewById(R.id.review_rate_1)).setProgress(r1/(r1+r2+r3+r4+r5)*100);

        ReviewAdapter rAdapter = new ReviewAdapter(this, list);
        reviewListView.setAdapter(rAdapter);
    }
}