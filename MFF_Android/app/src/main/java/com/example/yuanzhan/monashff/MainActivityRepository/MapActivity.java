package com.example.yuanzhan.monashff.MainActivityRepository;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;

import com.example.yuanzhan.monashff.EntityRepository.Student;
import com.example.yuanzhan.monashff.R;
import com.example.yuanzhan.monashff.RESTClients.RestClient;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapquest.mapping.maps.OnMapReadyCallback;
import com.mapquest.mapping.maps.MapboxMap;
import com.mapquest.mapping.maps.MapView;


import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by YuanZhan on 1/05/2017.
 */

public class MapActivity extends AppCompatActivity {

    private MapboxMap mMapboxMap;
    private MapView mMapView;

    private final LatLng MONASH_CAUL = new LatLng(-37.8702, 145.0645);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapboxAccountManager.start(getApplicationContext());
        setContentView(R.layout.activity_map);

        // get matching students
        Parcelable[] allParcelables = getIntent().getParcelableArrayExtra("matchingStudents");
        final Student[] matchingStudent = new Student[allParcelables.length];
        for (int i = 0; i < allParcelables.length; i++) {
            matchingStudent[i] = (Student) allParcelables[i];
        }
        System.out.println("matching students nums--->" + matchingStudent.length);


        mMapView = (MapView) findViewById(R.id.mapquestMapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {
                mMapboxMap = mapboxMap;
                mMapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MONASH_CAUL, 11));
                for (int i = 0; i < matchingStudent.length; i++) {

                    //get the latlng object first
                    JSONObject fff;

                    final int finalI = i;
                    new AsyncTask<Void, Void, JSONObject>() {

                        @Override
                        protected JSONObject doInBackground(Void... params) {
                            try {
                                System.out.println("getting latest location");
                                JSONObject latestLocation = RestClient.getLatestLocation(matchingStudent[finalI].getStdId());
                                if (latestLocation != null){
                                    System.out.println(latestLocation.getDouble("latitude") +"*&*^&*^^&^*&^*^"  + latestLocation.getDouble("longitude"));
                                    final MarkerOptions markerOptions = new MarkerOptions();
                                    markerOptions.position(new LatLng(latestLocation.getDouble("latitude"), latestLocation.getDouble("longitude")));
                                    markerOptions.title(matchingStudent[finalI].getFname() + " " + matchingStudent[finalI].getLname());
                                    markerOptions.snippet(matchingStudent[finalI].getNationality() + " " + matchingStudent[finalI].getCourse());


                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mapboxMap.addMarker(markerOptions);
                                        }
                                    });

                                }
                                return latestLocation;
                            } catch (JSONException e) {
                                e.printStackTrace();
                                return null;
                            }
                        }
                    }.execute();




                }
            }
        });
    }


    // ge the latest latlng object of a given student
//    private void getLatLngOfStd(final Student student) {
//
//        new AsyncTask<Void, Void, JSONObject>() {
//
//            @Override
//            protected JSONObject doInBackground(Void... params) {
//                try {
//                    System.out.println("getting latest location");
//                    JSONObject latestLocation = RestClient.getLatestLocation(student.getStdId());
//                    System.out.println(latestLocation.getDouble("latitude") +"*&*^&*^^&^*&^*^"  + latestLocation.getDouble("longitude"));
//                    return latestLocation;
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    return null;
//                }
//            }
//
//            @Override
//            protected void onPostExecute(JSONObject response) {
//            }
//        }.execute();
//
//    }
//
//    private void addMarker(MapboxMap mapboxMap, LatLng position, String fullName, String details) {
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(position);
//        markerOptions.title(fullName);
//        markerOptions.snippet(details);
//        mapboxMap.addMarker(markerOptions);
//    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }
}
