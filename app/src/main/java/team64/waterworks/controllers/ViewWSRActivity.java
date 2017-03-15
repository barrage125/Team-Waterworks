package team64.waterworks.controllers;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;

import android.widget.Toast;

import team64.waterworks.R;
import team64.waterworks.models.*;


public class ViewWSRActivity extends AppCompatActivity {
    private TextView allWaterReportsText;
    private TextView labels;

    private ListView listView;
    private ArrayList<String> reports;
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_water_reports);
        allWaterReportsText = (TextView) findViewById(R.id.all_reports_text);
        labels = (TextView) findViewById(R.id.labels);

        listView = (ListView) findViewById(R.id.all_reports_list);
        reports = WSRManager.viewAllSourceReports();

        // If array list of all reports isn't empty, populate listview
        if (reports != null && WSRManager.viewAllSourceReports().size() > 0) {
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, reports);
            listView.setAdapter(adapter);
            labels.setText("     (id) (location) (auth) (type) (cond) (rating) (date)");
        } else {
            Toast.makeText(getApplicationContext(), "No reports have been submitted yet!",
                           Toast.LENGTH_SHORT).show();
        }
    }
}
