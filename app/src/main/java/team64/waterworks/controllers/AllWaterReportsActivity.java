package team64.waterworks.controllers;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collection;

import team64.waterworks.R;
import team64.waterworks.models.*;


public class AllWaterReportsActivity extends AppCompatActivity{
    private TextView allWaterReportsText;
    private ListView allWaterReportsList;

    private ArrayAdapter<WaterReport> reportsArrayAdapter;
    private ArrayList<WaterReport> reportsArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_water_reports);

        allWaterReportsText = (TextView) findViewById(R.id.all_reports_text);
        allWaterReportsList = (ListView) findViewById(R.id.all_reports_list);

        reportsArrayList = ReportsManager.viewAllReports();

        reportsArrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.activity_all_water_reports, reportsArrayList);

        allWaterReportsList.setAdapter(reportsArrayAdapter);
    }
}
