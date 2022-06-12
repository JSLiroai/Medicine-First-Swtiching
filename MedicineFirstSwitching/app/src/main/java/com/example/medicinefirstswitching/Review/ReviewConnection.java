package com.example.medicinefirstswitching.Review;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medicinefirstswitching.ResultActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ReviewConnection {
    private static final String TAG_JSON = "webnautes";
    private static final String TAG_ID = "Id";
    private static final String TAG_CONTEXT = "Context";
    private static final String TAG_RATE = "Rate";


    protected String mJsonString;
    protected ArrayList<HashMap<String, String>> mArrayList;

    protected AppCompatActivity context;

    public ReviewConnection(String product, AppCompatActivity context) {
        this.context = context;
        mArrayList = new ArrayList<>();
        GetData task = new GetData();
        task.execute("http://61.102.138.116/reviewDownload.php" + "?Product=\"" + product + "\"");
    }

    public ArrayList<HashMap<String, String>> getmArrayList() {
        return mArrayList;
    }

    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(context,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            mJsonString = result;
            setResult();
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                int responseStatusCode = httpURLConnection.getResponseCode();
                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString().trim();
            } catch (Exception e) {
                errorString = e.toString();
                return null;
            }
        }
    }

    private void setResult() {
        try{
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                String id = item.getString(TAG_ID);
                String context1 = item.getString(TAG_CONTEXT);
                String rate = item.getString(TAG_RATE);

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(TAG_ID, id);
                hashMap.put(TAG_CONTEXT, context1);
                hashMap.put(TAG_RATE, rate);

                mArrayList.add(hashMap);
            }

            if(context instanceof ResultActivity) ((ResultActivity) this.context).updateReviewData(mArrayList);
            if(context instanceof ReviewActivity) ((ReviewActivity) this.context).updateData(mArrayList);
        } catch (JSONException e) {
            Log.d("DBdebug", "showResult : ", e);
        }

    }
}
