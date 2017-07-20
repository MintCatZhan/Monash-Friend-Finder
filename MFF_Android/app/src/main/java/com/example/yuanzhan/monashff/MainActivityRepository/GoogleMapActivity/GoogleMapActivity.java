package com.example.yuanzhan.monashff.MainActivityRepository.GoogleMapActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.yuanzhan.monashff.Constants.Constants;
import com.example.yuanzhan.monashff.EntityRepository.Student;
import com.example.yuanzhan.monashff.R;
import com.example.yuanzhan.monashff.RESTClients.RestClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GoogleMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Student[] allStudents;
    private Student[] restrictStudents;
    private final LatLng MONASH_CAUL = new LatLng(-37.8702, 145.0645);

    final ArrayList<Marker> myMarkers = new ArrayList<>();

    TextView selectedDistanceTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        // get matching students regardless of distance
        Parcelable[] allParcelables = getIntent().getParcelableArrayExtra("matchingStudents");
        final Student[] matchingStudent = new Student[allParcelables.length];
        for (int i = 0; i < allParcelables.length; i++) {
            matchingStudent[i] = (Student) allParcelables[i];
        }
        System.out.println("matching students nums--->" + matchingStudent.length);
        allStudents = matchingStudent;
        restrictStudents = matchingStudent;


        Button restrictBtn = (Button) findViewById(R.id.selectDistanceToShowBtn);
        restrictBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the distance restricted
                double distRestrict = getDistRestrict();
                System.out.println("MarkerOptions loop --> distRestrict----->>>>>>" + distRestrict);
                for (Marker marker : myMarkers) {
                    System.out.println("MarkerOptions loop -->");
                    double dist = computeDistance(marker.getPosition().latitude, marker.getPosition().longitude);
                    System.out.println("MarkerOptions loop --> the distance is " + dist);


                    if (dist < distRestrict) {
                        System.out.println("MarkerOptions loop -->  should be visiable");
//                        mo.visible(true);
                        marker.setVisible(true);
                    } else {
                        System.out.println("MarkerOptions loop -->  should be invisiable");
                        marker.setVisible(false);
                    }
                }
            }
        });


        // set the spinner adapter
        selectedDistanceTV = (TextView) findViewById(R.id.distanceToShowTV);
        setSpinnerSelectedListener((Spinner) findViewById(R.id.distanceToShowSpinner),
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Constants.DISTANCE),
                selectedDistanceTV);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    // this method is used to compute the distance
    public double computeDistance(double latitude, double longtitude) {
        double dist = 0;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000, 0, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                double currLatitude = location.getLatitude();
                double currLongitude = location.getLongitude();
                System.out.println(currLatitude + " " + currLongitude + " " + "in googlemap compute distance");

                double earthRadius = 6371000; //meters
                double dLat = Math.toRadians(latitude - currLatitude);
                double dLng = Math.toRadians(longtitude - currLongitude);
                double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                        + Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(currLatitude))
                        * Math.sin(dLng / 2) * Math.sin(dLng / 2);
                double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                dist = earthRadius * c;
                System.out.println("distance is --->>>>>>" + dist);
            }
        }
        return dist;
    }


    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // log it when the location changes
            if (location != null) {
                Log.i("SuperMap", "Location changed : Lat: "
                        + location.getLatitude() + " Lng: "
                        + location.getLongitude());
            }
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    public double getDistRestrict() {
        Spinner disSpinner = (Spinner) findViewById(R.id.distanceToShowSpinner);
        String disStr = disSpinner.getSelectedItem().toString();
        System.out.println("distance btn clicked, disstr is -->>>" + disStr);
        double distRestrict = 0;

        switch (disStr.toLowerCase()) {
            case "1 km":
                System.out.println("1 km--->>>>>>>");
                distRestrict = 1000;
                break;
            case "2 km":
                System.out.println("2 km--->>>>>>>");
                distRestrict = 2000;
                break;
            case "5 km":
                System.out.println("5 km--->>>>>>>");
                distRestrict = 5000;
                break;
            default:
                System.out.println("No distance selected, show all");
                distRestrict = 999999999;
                break;
        }

        return distRestrict;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // for current student
        final LatLng currStdLL;

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000, 0, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();


                System.out.println(latitude + " " + longitude + " " + "in googlemap");

                currStdLL = new LatLng(latitude, longitude);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MarkerOptions defaultMO = new MarkerOptions();
                        defaultMO.position(currStdLL);
                        defaultMO.title("Me");
                        defaultMO.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                        mMap.addMarker(defaultMO);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(MONASH_CAUL));
                    }
                });
            } else {
                System.out.println("null location");
            }
        }

        // for all friends
        for (int i = 0; i < restrictStudents.length; i++) {
            final int finalI = i;
            new AsyncTask<Void, Void, JSONObject>() {

                @Override
                protected JSONObject doInBackground(Void... params) {
                    try {
                        System.out.println("getting latest location");
                        // for future friends
                        if (restrictStudents[finalI] == null) {
                            return null;
                        } else {
                            final JSONObject latestLocation = RestClient.getLatestLocation(restrictStudents[finalI].getStdId());
                            if (latestLocation != null) {
                                System.out.println(latestLocation.getDouble("latitude") + "*&*^&*^^&^*&^*^" + latestLocation.getDouble("longitude"));
                                final LatLng currLL = new LatLng(latestLocation.getDouble("latitude"), latestLocation.getDouble("longitude"));

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mMap.moveCamera(CameraUpdateFactory.newLatLng(MONASH_CAUL));

                                        Marker theMarker = mMap.addMarker(new MarkerOptions()
                                                .position(currLL).title(restrictStudents[finalI].getFname() + " " + restrictStudents[finalI].getLname())
                                                .snippet(restrictStudents[finalI].getNationality() + " " + restrictStudents[finalI].getCourse()));

                                        myMarkers.add(theMarker);

                                    }
                                });

                            }
                            return latestLocation;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }.execute();
        }

    }


    // this method is used to set onSelectedLister to spinner
    public void setSpinnerSelectedListener(Spinner dropdownPara, ArrayAdapter<String> adapterPara, TextView tvPara) {
        final Spinner dropdown = dropdownPara;
        final TextView tv = tvPara;
        dropdown.setAdapter(adapterPara);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (dropdown.getSelectedItemId() > 0) {
                    tv.setText(dropdown.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
