package com.example.yuanzhan.monashff.MainActivityRepository.ReportActivities;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.example.yuanzhan.monashff.RESTClients.RestClient;
import com.github.mikephil.charting.charts.BarChart;

import com.example.yuanzhan.monashff.R;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

/**
 * Created by YuanZhan on 2/05/2017.
 */

public class BarChartActivity extends AppCompatActivity {

    BarChart chart;


    EditText stdIdET;
    EditText startDateET;
    EditText endDateET;
    Button generateBtn;
    ArrayList<String> BarEntryLabels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barchart);
        chart = (BarChart) findViewById(R.id.idBarChart);
        stdIdET = (EditText) findViewById(R.id.stdIdInputET);
        startDateET = (EditText) findViewById(R.id.startDateInputET);
        endDateET = (EditText) findViewById(R.id.endDateInputET);
        generateBtn = (Button) findViewById(R.id.stdIdInputBtn);


        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String stdIdStr = stdIdET.getText().toString();
                final String startDateStr = startDateET.getText().toString();
                final String endDateStr = endDateET.getText().toString();
                new AsyncTask<Void, Void, JSONArray>() {
                    @Override
                    protected JSONArray doInBackground(Void... params) {
                        JSONArray freqLoc = RestClient.getResultStr("location",
                                "/getLocFrequency", "/" + stdIdStr + "/" + startDateStr + "/" + endDateStr);
                        System.out.println("freqloc length--->>" + freqLoc.length());
//                        getLocFreq(JSONArray freqLocE);
//                        JSONArray freqLoc = getLocFreq(freqLocE);
                        return freqLoc;
                    }

                    @Override
                    protected void onPostExecute(JSONArray freqLoc) {
                        ArrayList<BarEntry> BARENTRY = new ArrayList<>();

                        BarDataSet Bardataset;
                        BarData BARDATA;
                        if (freqLoc != null) {
                            try {
                                // xAxis
                                for (int i = 0; i < freqLoc.length(); i++) {
                                    System.out.println("freqloc the one--->>" + freqLoc.getJSONObject(i).getString("locName"));
                                    BarEntryLabels.add(freqLoc.getJSONObject(i).getString("locName"));
                                }


                                System.out.println("BarEntryLabels size-->" + BarEntryLabels.size());

                                // yAxis
                                for (int i = 0; i < freqLoc.length(); i++) {
                                    System.out.println("freqloc the one times--->>" + freqLoc.getJSONObject(i).getInt("times"));
                                    BARENTRY.add(new BarEntry(i + 1, freqLoc.getJSONObject(i).getInt("times")));
                                }

                                System.out.println("BARENTRY size-->" + BARENTRY.size());


                                // adding color for dataset
                                ArrayList<Integer> colors = new ArrayList<>();
                                colors.add(Color.BLUE);
                                colors.add(Color.GRAY);
                                colors.add(Color.GREEN);
                                colors.add(Color.CYAN);
                                colors.add(Color.RED);
                                colors.add(Color.YELLOW);
                                colors.add(Color.MAGENTA);

                                Bardataset = new BarDataSet(BARENTRY, "Location");
                                Bardataset.setValueTextSize(12);
                                Bardataset.setColors(colors);

                                BARDATA = new BarData(Bardataset);


                                Bardataset.setColors(ColorTemplate.COLORFUL_COLORS);


                                chart.setData(BARDATA);
                                Legend legend = chart.getLegend();
                                legend.setEnabled(true);
                                legend.setFormSize(20);
                                legend.setWordWrapEnabled(true);
                                legend.setTextSize(15);
                                legend.setForm(Legend.LegendForm.SQUARE);

                                chart.animateY(3000);
                                chart.invalidate();
//                                chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                                chart.getXAxis().setEnabled(true);
                                chart.getXAxis().setDrawLabels(true);
                                chart.getXAxis().setDrawGridLines(false);
                                chart.getXAxis().setTextColor(Color.BLACK);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }.execute();
            }
        });
    }



    public JSONArray getLocFreq(JSONArray freqLocE){
        String resultStr = "[{\"locName\":\"";

        ArrayList<String> locNames = null;

        try{
            for (int i = 0; i < freqLocE.length(); i++){
                String addressStr = getAddressByLatLon(freqLocE.getJSONObject(i).getDouble("latitude"),freqLocE.getJSONObject(i).getDouble("longitude") );
                locNames.add(addressStr);
            }

            // use hashset to remove duplicated locnames for the previous arraylist
            HashSet h = new HashSet(locNames);
            locNames.clear();
            locNames.addAll(h);

            // search all
            for (String locName : locNames) {
                int times = 0;
                for (int i = 0; i < freqLocE.length(); i++){
                    if (getAddressByLatLon(freqLocE.getJSONObject(i).getDouble("latitude"),freqLocE.getJSONObject(i).getDouble("longitude")).equals(locName)){
                        times++;
                    }
                }

                resultStr = resultStr + locName +"\",\"times\"" + times + "},";
            }

            resultStr = resultStr.substring(0, resultStr.length()-1);
            resultStr = resultStr + "]";
            JSONArray json = new JSONArray(resultStr);
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }

    /**
     * this method is using google map api to get the address name or even places by given latitude and longitude
     *
     * @return String
     */
    public String getAddressByLatLon(double lat, double lon) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String knownName = addresses.get(0).getFeatureName();
            if (knownName == null) {
                return address;
            } else {
                return knownName;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error Here getAddressByLatLon";
        }
    }


}

//    public void AddValuesToBARENTRY() {

//
//
//        BARENTRY.add(new BarEntry(2f, 0));
//        BARENTRY.add(new BarEntry(4f, 1));
//        BARENTRY.add(new BarEntry(6f, 2));
//        BARENTRY.add(new BarEntry(8f, 3));
//        BARENTRY.add(new BarEntry(7f, 4));
//        BARENTRY.add(new BarEntry(3f, 5));



//    public void AddValuesToBarEntryLabels() {
//
//        BarEntryLabels.add("January");
//        BarEntryLabels.add("February");
//        BarEntryLabels.add("March");
//        BarEntryLabels.add("April");
//        BarEntryLabels.add("May");
//        BarEntryLabels.add("June");
//
//    }


//
//    private BarChart barChart;
//    private HashMap<String, Integer> locationFreqMap;
//
//    @Override
//    protected void onCreate(final Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_barchart);
//
//        barChart = (BarChart) findViewById(R.id.idBarChart);
//
//        final EditText stdIdET = (EditText) findViewById(R.id.stdIdInputET);
//        final EditText startDateET = (EditText) findViewById(R.id.startDateInputET);
//        final EditText endDateET = (EditText) findViewById(R.id.endDateInputET);
//        Button generateBtn = (Button) findViewById(R.id.stdIdInputBtn);
//        generateBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final String stdIdStr = stdIdET.getText().toString();
//                final String startDateStr = startDateET.getText().toString();
//                final String endDateStr = endDateET.getText().toString();
//
//                new AsyncTask<Void, Void, JSONArray>() {
//                    @Override
//                    protected JSONArray doInBackground(Void... params) {
//                        JSONArray freqLoc = RestClient.getResultStr("location",
//                                "/getLocFrequency", "/" + stdIdStr + "/" + startDateStr + "/" + endDateStr);
//                        System.out.println("freqloc length--->>" + freqLoc.length());
//                        return freqLoc;
//                    }
//
//                    @Override
//                    protected void onPostExecute(JSONArray freqLoc) {
//                        if (freqLoc != null) {
//                            try {
//                                // xAxis
//                                ArrayList<String> locName = new ArrayList<>();
//                                for (int i = 0; i < freqLoc.length(); i++) {
//                                    System.out.println("freqloc the one--->>" + freqLoc.getJSONObject(i).getString("locName"));
//                                    locName.add(freqLoc.getJSONObject(i).getString("locName"));
//                                }
//
//                                ArrayList<BarEntry> valueSet = new ArrayList<>();
//                                for (int i = 0; i < freqLoc.length(); i++) {
//                                    System.out.println("freqloc the one times--->>" + freqLoc.getJSONObject(i).getInt("times"));
//                                    valueSet.add(new BarEntry(freqLoc.getJSONObject(i).getInt("times"), i));
//                                }
//
//                                XAxis xAxis = barChart.getXAxis();
//                                xAxis.setDrawAxisLine(true);
//                                xAxis.setDrawGridLines(false);
//                                barChart.getAxisLeft().setDrawAxisLine(false);
//
//                                Legend legend = barChart.getLegend();
//                                legend.setWordWrapEnabled(true);
//                                legend.setEnabled(false);
////                                legend.setForm(Legend.LegendForm.SQUARE);
//
//                                BarDataSet barDataSet = new BarDataSet(valueSet, "Location");
//                                barDataSet.setBarBorderWidth(2f);
//                                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
//                                BarData data = new BarData(barDataSet);
//                                barChart.setData(data);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                    }
//
//                }.execute();
//            }
//        });
//    }
//
//
//    /**
//     * get location record of a certain student
//     * this method is going to be invoke inside AsyncTask
//     * first it get all locations this student has been
//     * then it try to convert the latitude and longitude of each JSONObject into address, even places
//     * finally it return a hashmap<String locationame, int times> --> the locationame can be address or even places
//     **/
//    private HashMap<String, Integer> getLocationRecord(long stdId) {
//        HashMap<String, Integer> resultMap = new HashMap<>();
//
//
//        List<String> locations = new ArrayList<>();
//        List<String> locationsNoDuplication = new ArrayList<>();
//
//        try {
//            JSONArray locationJsonArrayOfStd = RestClient.getResultStr("location", "/findByStdId", "/" + stdId);
//            for (int i = 0; i < locationJsonArrayOfStd.length(); i++) {
//                double lat = locationJsonArrayOfStd.getJSONObject(i).getDouble("latitude");
//                double lon = locationJsonArrayOfStd.getJSONObject(i).getDouble("longitude");
//                locations.add(getAddressByLatLon(lat, lon));
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        // get the no duplciation locations String list
//        locationsNoDuplication = locations;
//        HashSet h = new HashSet(locationsNoDuplication);
//        locationsNoDuplication.clear();
//        locationsNoDuplication.addAll(h);
//
//        for (String locNoDupStr : locationsNoDuplication) {
//            int times = 0;
//            for (String locStr : locations) {
//                if (locStr.equals(locNoDupStr)) {
//                    times++;
//                }
//            }
//            resultMap.put(locNoDupStr, times);
//        }
//        return resultMap;
//    }
//


//}


