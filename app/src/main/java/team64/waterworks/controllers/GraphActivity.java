package team64.waterworks.controllers;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Date;

import team64.waterworks.R;

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
        setContentView(R.layout.activity_home);
        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
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
