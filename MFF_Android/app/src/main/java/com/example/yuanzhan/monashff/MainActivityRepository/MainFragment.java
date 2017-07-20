package com.example.yuanzhan.monashff.MainActivityRepository;


import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yuanzhan.monashff.APIMethods.APIMethods;
import com.example.yuanzhan.monashff.EntityRepository.Student;
import com.example.yuanzhan.monashff.R;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by YuanZhan on 22/04/2017.
 */

public class MainFragment extends Fragment {

    View vMain;

    double latitude;
    double longitude;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vMain = inflater.inflate(R.layout.fragment_main, container, false);
        //set the temperature for the home page
        // get the tempTV
        final TextView tempTV = (TextView) vMain.findViewById(R.id.tempTV);

        // get the name TV
        final TextView welcomNameTV = (TextView) vMain.findViewById(R.id.welcomNameTV);
        Student currStd = (Student) getActivity().getIntent().getParcelableExtra("loggedStd");
        welcomNameTV.setText(currStd.getFname() + " " + currStd.getLname());


        // get current date and time
        final TextView currDateTime = (TextView) vMain.findViewById(R.id.currDateTime);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        currDateTime.setText("Current Time is: " + dateFormat.format(date));


        // get initial location manager
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000, 0, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                System.out.println(latitude + " " + longitude + " " + "in frage");
            } else {
                System.out.println("null location");
            }
        }


        // create an anonymous AsyncTask
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                // need to get the longitude and latitude of the user
                String temp = APIMethods.getTemp(latitude + "", longitude + ""); // need to change the parameters here
                return temp;
            }

            @Override
            protected void onPostExecute(String temp) {
                if (temp.isEmpty()) {
                    tempTV.setText("No Weather Info.");
                } else {
                    tempTV.setText("Current Temperature: "+ temp + "°C");
                }
            }
        }.execute();

        return vMain;
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


}
