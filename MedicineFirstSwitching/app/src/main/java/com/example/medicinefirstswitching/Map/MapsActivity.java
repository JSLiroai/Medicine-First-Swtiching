package com.example.medicinefirstswitching.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.content.DialogInterface;
import android.location.Location;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;

import com.example.medicinefirstswitching.R;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private final String MAPS_API_KEY = "AIzaSyB6y2LYcOW1m44uiEGWSWOB5xKtA3xyALw";
    private GoogleMap mMap;
    private CameraPosition cameraPosition;
    private List<HashMap<String,String>> placeList;

    //default location (Sydney)
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 16;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private Location lastKnownLocation;

    private ConstraintLayout mapInfoRoot;
    private LayoutInflater minflater;
    private ConstraintLayout mapinfoCard;

    @Override
    public void onBackPressed() {
        if(mapInfoRoot.getVisibility() == View.VISIBLE) {
            mapInfoRoot.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_down));
            mapInfoRoot.setVisibility(View.INVISIBLE);
        }
        else super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        setContentView(R.layout.activity_maps);
        mapInfoRoot = findViewById(R.id.map_info_root);
        minflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        minflater.inflate(R.layout.map_info_card, mapInfoRoot,true);
        mapInfoRoot.setVisibility(View.INVISIBLE);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Places.initialize(getApplicationContext(), getResources().getString(R.string.map_api_key));

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        this.mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.map_info_contents,
                        (FrameLayout) findViewById(R.id.map), false);

                TextView title = infoWindow.findViewById(R.id.map_title);
                title.setText(marker.getTitle());

                TextView snippet = infoWindow.findViewById(R.id.map_snippet);
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        marker.getPosition(), DEFAULT_ZOOM));
                showInfo(marker);
                return true;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                          @NonNull String[] permissions,
                                          @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        if(requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
            else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
            updateLocationUI();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        }
        else {
            int permissionsCode = 42;
            String[] permissions = {android.Manifest.permission.CAMERA, android.Manifest.permission.ACCESS_FINE_LOCATION};

            ActivityCompat.requestPermissions(this, permissions, permissionsCode);
        }
    }

    private void updateLocationUI() {
        if(mMap == null) {
            return;
        }
        try {
            if(locationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            }
            else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        }
        catch (SecurityException e) {
            Log.e("Exception: %s",e.getMessage());
        }
    }

    private void getDeviceLocation() {
        try {
            if(locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if(task.isSuccessful()) {
                            lastKnownLocation = task.getResult();
                            if(lastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                showCurrentPlace();
                            }
                        }
                        else {
                            Log.d("MAP", "Current location is null. Using defaults.");
                            Log.e("MAP", "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    private void showCurrentPlace() {
        if(mMap == null) {
            return;
        }

        if(locationPermissionGranted) {
            String  url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json"
                    + "?location=" + lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude()
                    + "&radius=5000" + "&types=" + "pharmacy" + "&sensor=ture"
                    + "&key=" + getResources().getString(R.string.map_api_key);
            Log.v("dd",url);
            new PlaceTask().execute(url);
        }
        else {
            Log.i("debug", "The user did not grant location permission.");

            mMap.addMarker(new MarkerOptions()
                    .title(getString(R.string.default_info_title))
                    .position(defaultLocation)
                    .snippet(getString(R.string.default_info_snippet)));

            getLocationPermission();
        }
    }

    private void showInfo(Marker marker) {
        for(HashMap<String, String> item : placeList) {
            if(marker.getTag().equals(item.get("tag"))) {
                ((TextView) findViewById(R.id.map_place_name)).setText(item.get("name"));
                ((TextView) findViewById(R.id.map_place_type)).setText(item.get("type"));
                ((TextView) findViewById(R.id.map_place_address)).setText(item.get("address"));
                ((TextView) findViewById(R.id.map_place_status)).setText(item.get("status"));
                if(!item.get("photo_url").equals("")) {
                    Picasso.get().load(item.get("photo_url")).into((ImageView) findViewById(R.id.map_place_photo));
                    ((ImageView) findViewById(R.id.map_place_photo)).setVisibility(View.VISIBLE);
                }
                else ((ImageView) findViewById(R.id.map_place_photo)).setVisibility(View.INVISIBLE);
            }
        }
        if(mapInfoRoot.getVisibility() == View.INVISIBLE) mapInfoRoot.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up));
        mapInfoRoot.setVisibility(View.VISIBLE);
    }

    private class PlaceTask extends AsyncTask<String, Integer,String> {
        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            try {
                data = downloadUrl(strings[0]);
                Log.v("debug", "data : " + data);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            new ParserTask().execute(s);
        }
    }

    private String downloadUrl(String string) throws IOException {
        URL url = new URL(string);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        InputStream stream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();
        String line = "";

        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        String data = builder.toString();
        reader.close();;

        return  data;
    }

    private class ParserTask extends AsyncTask<String,Integer,List<HashMap<String,String>>>{
        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            JsonParser jsonParser = new JsonParser();
            List<HashMap<String,String>> mapList = null;
            JSONObject object = null;
            try {
                object = new JSONObject(strings[0]);
                mapList = jsonParser.parseResult(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mapList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String,String>> hashMaps) {
            mMap.clear();
            placeList = hashMaps;

            for(int i=0; i<hashMaps.size();i++) {
                HashMap<String,String> hashMapList = hashMaps.get(i);

                double lat = Double.parseDouble(hashMapList.get("lat"));
                double lng = Double.parseDouble(hashMapList.get("lng"));
                String name = hashMapList.get("name");

                LatLng latLng = new LatLng(lat,lng);
                MarkerOptions options =new MarkerOptions();
                options.position(latLng);
                options.title(name);

                mMap.addMarker(options).setTag("tag" + i);
            }
        }
    }
}