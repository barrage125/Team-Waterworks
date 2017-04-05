package team64.waterworks.controllers;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import team64.waterworks.R;
import team64.waterworks.models.WPRManager;
import team64.waterworks.models.WaterPurityReport;

public class GraphActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Initializes all variables needed for graph activity
     * @param savedInstanceState data passed into graph activity
     */
    Double filterLong;
    Double filterLat;
    int filterRadius;
    String filterStartDate;
    String filterEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purity_graph_report);
        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> virusSeries = null;
        LineGraphSeries<DataPoint> contamSeries = null;

        Date min = null;
        Date max = null;

        if (filterLat == null && filterLat == null) {
            ArrayList<String> purityReports = WPRManager.viewAllPurityReports();
            DataPoint[] virusDataPoints = new DataPoint[purityReports.size()];
            DataPoint[] contamDataPoints = new DataPoint[purityReports.size()];
            if (purityReports != null) {
                for (int i = 0; i < purityReports.size(); i++) {
                    String[] reportsData = purityReports.get(i).split(" ", 0);
                    String idString = reportsData[0].replace("(", "").replace(")", "");
                    Long idLong = Long.parseLong(idString);
                    double virusPPM = (double) WPRManager.getPurityReportByID(idLong).getVirusPPM();
                    double contamPPM = (double) WPRManager.getPurityReportByID(idLong).getContamPPM();
                    String[] dateInString = WPRManager.getPurityReportByID(idLong).getDate().split(" ");
                    System.out.println(dateInString[0]);
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = null;

                    try {

                        date = formatter.parse(dateInString[0]);
                        if (min == null) {
                            min = date;
                            max = date;
                        }

                        if (min.compareTo(date) < 0) {
                            min = date;
                        }

                        if (max.compareTo(date) > 0) {
                            max = date;
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (date != null) {
                        virusDataPoints[i] = new DataPoint(date, virusPPM);
                        contamDataPoints[i] = new DataPoint(date, contamPPM);
                    }
                }
            }
            if (virusDataPoints != null) {
                virusSeries = new LineGraphSeries<>(virusDataPoints);
            }

            if (contamDataPoints != null) {
                contamSeries = new LineGraphSeries<>(contamDataPoints);
            }
        } else {
            ArrayList<WaterPurityReport> purityReports = WPRManager.getPurityReportsByLocationAndDate(filterLat, filterLong, filterStartDate, filterEndDate);
            DataPoint[] virusDataPoints = null;
            DataPoint[] contamDataPoints = null;
            if (purityReports != null) {
                contamDataPoints = new DataPoint[purityReports.size()];
                virusDataPoints = new DataPoint[purityReports.size()];
                for (int i = 0; i < purityReports.size(); i++) {
                    double virusPPM = (double) purityReports.get(i).getVirusPPM();
                    double contamPPM = (double) purityReports.get(i).getContamPPM();
                    String dateInString = purityReports.get(i).getDate();

                    SimpleDateFormat formatter = new SimpleDateFormat("(dd/MM/yyyy");
                    Date date = null;

                    try {

                        date = formatter.parse(dateInString);

                        if (min.compareTo(date) < 0) {
                            min = date;
                        }

                        if (max.compareTo(date) > 0) {
                            max = date;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (date != null) {
                        virusDataPoints[i] = new DataPoint(date, virusPPM);
                        contamDataPoints[i] = new DataPoint(date, contamPPM);
                    }
                }
            }
            if (virusDataPoints != null) {
                virusSeries = new LineGraphSeries<>(virusDataPoints);
            }

            if (contamDataPoints != null) {
                contamSeries = new LineGraphSeries<>(contamDataPoints);
            }
        }

        if (graph != null && virusSeries != null) {
            graph.addSeries(virusSeries);
        }

        if (graph != null && contamSeries != null) {
            graph.getSecondScale().addSeries(contamSeries);
        }

        if (graph != null && virusSeries == null && contamSeries == null) {
            graph.addSeries(new LineGraphSeries<>());
        }

        graph.getSecondScale().setMinY(0);
        graph.getSecondScale().setMaxY(100);
        contamSeries.setColor(Color.RED);

        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getApplicationContext()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3);

        graph.getViewport().setMinX(min.getTime());
        graph.getViewport().setMaxX(max.getTime());
        graph.getViewport().setXAxisBoundsManual(true);

        graph.getGridLabelRenderer().setHumanRounding(false);

        graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
        graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling

        virusSeries.setDrawDataPoints(true);
        virusSeries.setDataPointsRadius(10);

        contamSeries.setDrawDataPoints(true);
        contamSeries.setDataPointsRadius(10);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.filter_graph_btn:
            View view = (LayoutInflater.from(GraphActivity.this)).inflate(R.layout.dialog_graph_filter, null);

            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(GraphActivity.this);
            alertBuilder.setView(view);

            final EditText startDate = (EditText) view.findViewById(R.id.editText_Start_Date);
            final EditText endDate = (EditText) view.findViewById(R.id.editText_End_Date);
            final EditText longitude = (EditText) view.findViewById(R.id.editText_Longitude);
            final EditText latitude = (EditText) view.findViewById(R.id.editText_Latitude);
            final EditText radius = (EditText) view.findViewById(R.id.editText_Radius);

            alertBuilder.setCancelable(true);
            alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    filterLong = Double.parseDouble(longitude.getText().toString());
                    filterLat = Double.parseDouble(latitude.getText().toString());
                    filterRadius = Integer.parseInt(radius.getText().toString());
                    filterStartDate = startDate.getText().toString();
                    filterEndDate = endDate.getText().toString();
                }
            });
            alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //Cancelled
                }
            });
            Dialog dialog = alertBuilder.create();
            dialog.show();
        }
    }
}
