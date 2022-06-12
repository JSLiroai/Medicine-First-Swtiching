package com.example.medicinefirstswitching.Review;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicinefirstswitching.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ReviewActivity extends AppCompatActivity {
    private String product;
    private ReviewConnection rc;

    private LayoutInflater minflater;
    private ConstraintLayout reviewStatusRoot;
    private ListView reviewListView;
    private ImageButton backbtn;

    private EditText editID;
    private EditText editContext;
    private RatingBar editRate;
    private Button commitBtn;

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
        backbtn = (ImageButton) findViewById(R.id.map_back);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editID = findViewById(R.id.review_edit_id);
        editContext = findViewById(R.id.review_edit_context);
        editRate = findViewById(R.id.review_edit_star);
        commitBtn = findViewById(R.id.review_commit);
        commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editID.getText().equals("") && editRate.getRating() != 0)
                    commitReview(editID.getText().toString(), editContext.getText().toString(), editRate.getRating());
            }
        });
    }

    private void commitReview(String id, String review, float rate) {
        InsertData task = new InsertData();
        task.execute("http://61.102.138.116/reviewUpload.php", id, review, Float.toString(rate));

        editID.setText("");
        editContext.setText("");
        editRate.setRating(0);
        Toast.makeText(getApplicationContext(), "리뷰를 작성 완료했습니다.", Toast.LENGTH_LONG).show();
        rc = new ReviewConnection(product, ReviewActivity.this);
    }

    public void updateData(ArrayList<HashMap<String, String>> dbList){
        ArrayList<ReviewItem> list = new ArrayList<ReviewItem>();
        float r1 = 0, r2 = 0, r3 = 0, r4 = 0, r5 = 0;
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
        float rAvg = (r1*1+r2*2+r3*3+r4*4+r5*5)/(r1+r2+r3+r4+r5);

        if(Float.isNaN(rAvg)) rAvg = 0;
        ((RatingBar) findViewById(R.id.review_rate_avg_star)).setRating(rAvg);
        ((TextView) findViewById(R.id.review_rate_avg)).setText(String.format("%.1f",rAvg));
        ((ProgressBar)  findViewById(R.id.review_rate_5)).setProgress(Math.round(r5/(r1+r2+r3+r4+r5)*100));
        ((ProgressBar)  findViewById(R.id.review_rate_4)).setProgress(Math.round(r4/(r1+r2+r3+r4+r5)*100));
        ((ProgressBar)  findViewById(R.id.review_rate_3)).setProgress(Math.round(r3/(r1+r2+r3+r4+r5)*100));
        ((ProgressBar)  findViewById(R.id.review_rate_2)).setProgress(Math.round(r2/(r1+r2+r3+r4+r5)*100));
        ((ProgressBar)  findViewById(R.id.review_rate_1)).setProgress(Math.round(r1/(r1+r2+r3+r4+r5)*100));

        ReviewAdapter rAdapter = new ReviewAdapter(this, list);
        reviewListView.setAdapter(rAdapter);
    }

    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(ReviewActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
        }


        @Override
        protected String doInBackground(String... params) {

            String id = (String)params[1];
            String review = (String)params[2];
            String rate = (String)params[3];

            String serverURL = (String)params[0];
            String postParameters = "Product=" + product + "&Id=" + id +"&Context=" + review + "&Rate=" + rate;


            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d("", "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString();
            } catch (Exception e) {
                Log.d("", "InsertData: Error ", e);
                return new String("Error: " + e.getMessage());
            }

        }
    }

}