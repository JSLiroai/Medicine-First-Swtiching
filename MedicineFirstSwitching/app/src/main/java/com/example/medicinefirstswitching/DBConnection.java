package com.example.medicinefirstswitching;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medicinefirstswitching.Searching.SearchActivity;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class DBConnection {
    private static final String TAG_JSON = "webnautes";
    private static final String TAG_NATION = "nation";
    private static final String TAG_PRODUCT = "product";
    private static final String TAG_CATEGORY1 = "category1";
    private static final String TAG_CATEGORY2 = "category2";

    AppCompatActivity context;

    private ArrayList<HashMap<String, String>> mArrayList;
    String mJsonString;

    public DBConnection(String country, String symptom, AppCompatActivity context) {
        this.context = context;
        mArrayList = new ArrayList<>();
        GetData task = new GetData();
        task.execute("http://61.102.138.116/getData.php" + "?nation=\"" + country + "\"&category1=\"" + symptom + "\"");
    }

    public DBConnection(String country, AppCompatActivity context) {
        this.context = context;
        mArrayList = new ArrayList<>();
        GetData task = new GetData();
        task.execute("http://61.102.138.116/getDataCountry.php" + "?nation=\"" + country + "\"");
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

    void setResult() {
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                String nation = item.getString(TAG_NATION);
                String product = item.getString(TAG_PRODUCT);
                String category1 = item.getString(TAG_CATEGORY1);
                String category2 = item.getString(TAG_CATEGORY2);

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(TAG_NATION, nation);
                hashMap.put(TAG_PRODUCT, product);
                hashMap.put(TAG_CATEGORY1, category1);
                hashMap.put(TAG_CATEGORY2, category2);

                mArrayList.add(hashMap);
                if(context instanceof SearchActivity) ((SearchActivity) this.context).updateData(mArrayList);
                if(context instanceof ResultActivity) {/*result 업데이트 실행*/};
            }
        } catch (JSONException e) {
            Log.d("DBdebug", "showResult : ", e);
        }
    }

}
