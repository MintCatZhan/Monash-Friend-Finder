package com.example.yuanzhan.monashff.MainActivityRepository;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.yuanzhan.monashff.MainActivityRepository.ReportActivities.BarChartActivity;
import com.example.yuanzhan.monashff.MainActivityRepository.ReportActivities.PieChartActivity;
import com.example.yuanzhan.monashff.R;

/**
 * Created by YuanZhan on 2/05/2017.
 */

public class ReportFragment extends Fragment {
    View vReport;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vReport = inflater.inflate(R.layout.fragment_report, container, false);


        Button viewPieChartBtn = (Button)vReport.findViewById(R.id.getPieChartBtn);
        viewPieChartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PieChartActivity.class);
                startActivity(intent);
            }
        });



        Button viewBarChartBtn = (Button)vReport.findViewById(R.id.getBarChartBtn);
        viewBarChartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BarChartActivity.class);
                startActivity(intent);
            }
        });


        return vReport;
    }
}
