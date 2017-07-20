package com.example.yuanzhan.monashff.MainActivityRepository.ReportActivities;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.yuanzhan.monashff.Constants.PercentFormatter;
import com.example.yuanzhan.monashff.R;
import com.example.yuanzhan.monashff.RESTClients.RestClient;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by YuanZhan on 2/05/2017.
 */

public class PieChartActivity extends AppCompatActivity {

    private static String TAG = "PieChartActivity";


    PieChart pieChart;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piechart);
        Log.d(TAG, "onCreate: starting to create chart");


        pieChart = (PieChart) findViewById(R.id.idPieChart);

        pieChart.setRotationEnabled(true);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("Fav-Unit-Freq");
        pieChart.setCenterTextSize(10);
        pieChart.setDrawEntryLabels(false);
        pieChart.setHoleRadius(28f);
        pieChart.setUsePercentValues(true);


        // get frequence and unit name by RestClient
        new AsyncTask<Void, Void, JSONArray>() {

            @Override
            protected JSONArray doInBackground(Void... params) {
                try {

                    JSONArray freqUnitJsonArray = RestClient.getResultStr("student", "/findFavUnitAndFre", "");

                    int[] frequenceArray = new int[freqUnitJsonArray.length()];
                    String[] unitArray = new String[freqUnitJsonArray.length()];
                    for (int i = 0; i < freqUnitJsonArray.length(); i++) {
                        frequenceArray[i] = freqUnitJsonArray.getJSONObject(i).getInt("fre");
                        unitArray[i] = freqUnitJsonArray.getJSONObject(i).getString("unit");
                    }


                    addDataSet(frequenceArray, unitArray);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(JSONArray js) {

            }
        }.execute();


    }

    private void addDataSet(int[] yData, String[] xData) {
        Log.d(TAG, "addDataSet started");

        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for (int i = 0; i < yData.length; i++) {
            yEntrys.add(new PieEntry(yData[i], xData[i]));
        }

        for (int i = 0; i < xData.length; i++) {
            xEntrys.add(xData[i]);
        }

        PieDataSet pieDataSet = new PieDataSet(yEntrys, "");
        pieDataSet.setValueFormatter(new PercentFormatter());
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);


        // adding color for dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.GRAY);
        colors.add(Color.GREEN);
        colors.add(Color.CYAN);
        colors.add(Color.RED);
        colors.add(Color.YELLOW);
        colors.add(Color.MAGENTA);

        pieDataSet.setColors(colors);

        //add legend to chart
        Legend legend = pieChart.getLegend();
        legend.setEnabled(true);
        legend.setFormSize(20);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(15);
        legend.setForm(Legend.LegendForm.SQUARE);


        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(20f);
        pieData.setDrawValues(true);
        pieChart.setData(pieData);
        pieChart.invalidate();

    }
}
