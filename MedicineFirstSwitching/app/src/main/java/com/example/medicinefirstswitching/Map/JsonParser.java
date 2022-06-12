package com.example.medicinefirstswitching.Map;

import static com.google.android.libraries.places.api.model.Place.BusinessStatus.OPERATIONAL;

import android.content.res.Resources;

import com.google.android.libraries.places.api.model.Place;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.medicinefirstswitching.R;

public class JsonParser {
    public static final String OPEN = "OPERATIONAL";
    private final String MAPS_API_KEY = "AIzaSyB6y2LYcOW1m44uiEGWSWOB5xKtA3xyALw";

    private HashMap<String,String> parseJsonObject(JSONObject object) {
        HashMap<String,String> dataList = new HashMap<>();
        try {
            String name = object.getString("name");
            String latitude = object.getJSONObject("geometry")
                    .getJSONObject("location").getString("lat");
            String longitude = object.getJSONObject("geometry").
                    getJSONObject("location").getString("lng");
            dataList.put("name",name);
            dataList.put("lat",latitude);
            dataList.put("lng",longitude);
            try {
                String type = object.getJSONArray("types").optString(0);
                dataList.put("type", type);
            } catch (JSONException e) {
                dataList.put("type", "");
            }
            try {
                String address = object.getJSONObject("plus_code").getString("compound_code");
                dataList.put("address", address);
            } catch (JSONException e) {
                dataList.put("address", "");
            }
            try {
                String status = object.getString("business_status") == OPEN ? "영업 중": "영업 종료";
                dataList.put("status", status);
            } catch (JSONException e) {
                dataList.put("status", "");
            }
            try {
                String photo_refer = object.getJSONArray("photos").optJSONObject(0).getString("photo_reference");
                String photo_url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=500&photoreference="
                        + photo_refer + "&key=" + MAPS_API_KEY;

                dataList.put("photo_url", photo_url);
            } catch (JSONException e) {
                dataList.put("photo_url", "");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    private List<HashMap<String,String>> parseJsonArray(JSONArray jsonArray) {
        List<HashMap<String,String>> dataList = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++) {
            try {
                HashMap<String,String> data = parseJsonObject((JSONObject) jsonArray.get(i));
                data.put("tag", "tag" + i);
                dataList.add(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return dataList;
    }

    public List<HashMap<String,String>> parseResult(JSONObject object) {
        JSONArray jsonArray = null;
        try {
            jsonArray = object.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return parseJsonArray(jsonArray);
    }
}
