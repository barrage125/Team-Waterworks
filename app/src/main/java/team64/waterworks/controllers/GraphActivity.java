package team64.waterworks.controllers;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

import team64.waterworks.R;
import team64.waterworks.models.WPRManager;


public class GraphActivity extends AppCompatActivity implements View.OnClickListener {

    private GraphView graph;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purity_graph_report);

        // Initialize buttons
        graph = (GraphView) findViewById(R.id.graph);
        Button filter_btn = (Button) findViewById(R.id.filter_graph_btn);
        filter_btn.setOnClickListener(this);

        // Get an arraylist of all purity reports to display on graph, and draw the graph
        ArrayList<String> purityReports = WPRManager.viewAllPurityReports();
        try {
            if (purityReports != null && WPRManager.viewAllPurityReports().size() > 0) {
                drawGraph(purityReports);
            }
        } catch (NullPointerException e) {
            Log.e("size called on null", "size() is called on null obj from WPRManager.viewAllPurityReports");
        }
    }

    private void drawGraph(ArrayList<String> reports) {
        // Initialize graph and the data
        LineGraphSeries<DataPoint> series;

        // Instance data
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.US);
        Date date;
        String dateString;
        double dateDoub;
        double minDate = System.currentTimeMillis();
        double maxDate = 0;

        DataPoint tempPoint;
        String[] reportData;
        int ppm;
        DataPoint[] dataPoints = new DataPoint[reports.size()];


        // Fill in the ppm data points array from the purity reports arraylist
        for (int i = 0; i < reports.size(); i++) {
            // Get the ppm from purityReports arrayList and remove ()
            reportData = reports.get(i).split("\\)\\s\\(");
            ppm = Integer.parseInt(reportData[4]);

            // Get the date string from purity reports array list and convert to double for graph
            // Record most current and oldest date along the way
            dateString = reportData[6];
            try {
                date = dateFormat.parse(dateString);
                dateDoub = date.getTime();
                if (dateDoub < minDate) {
                    minDate = dateDoub;
                } else if (dateDoub > maxDate) {
                    maxDate = dateDoub;
                }

                // Make a new data point and put it in the DataPoints array
                tempPoint = new DataPoint(date, ppm);
                dataPoints[i] = tempPoint;

            } catch (Exception e) {
                Log.e("Can't parse date", "the date string couldn't be converted to date obj");
            }
        }

        series = new LineGraphSeries<>(dataPoints);
        graph.addSeries(series);

        // Set the x and y axis labels
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getApplicationContext()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3);

        // Set the min and max x axis values
        graph.getViewport().setMinX(minDate);
        graph.getViewport().setMaxX(maxDate);
        graph.getViewport().setXAxisBoundsManual(true);

        // To avoid java rounding the dates to arbitrary numbers
        graph.getGridLabelRenderer().setHumanRounding(false);

        series.setDrawDataPoints(true);

        // enables horizontal zooming and scrolling
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.filter_graph_btn: {
                View view = (LayoutInflater.from(GraphActivity.this)).inflate(R.layout.dialog_graph_filter, null);

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(GraphActivity.this);
                alertBuilder.setView(view);
                alertBuilder.setCancelable(true);

                final EditText startDate = (EditText) view.findViewById(R.id.editText_Start_Date);
                final EditText endDate = (EditText) view.findViewById(R.id.editText_End_Date);
                final EditText longitude = (EditText) view.findViewById(R.id.editText_Longitude);
                final EditText latitude = (EditText) view.findViewById(R.id.editText_Latitude);
                final EditText radius = (EditText) view.findViewById(R.id.editText_Radius);

                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String sDate = (startDate.getText().toString()) + " 00:00";
                        String eDate = (endDate.getText().toString()) + " 00:00";
                        graph.removeAllSeries();
                        drawGraph(WPRManager.getPurityReportsByLocationAndDate(sDate, eDate, Double.parseDouble(longitude.getText().toString()), Double.parseDouble(latitude.getText().toString()), Double.parseDouble(radius.getText().toString())));
                    }
                });

                alertBuilder.setNegativeButton("Cancel", null);

                Dialog dialog = alertBuilder.create();
                dialog.show();
            }
        }
    }
}
