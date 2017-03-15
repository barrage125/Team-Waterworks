package team64.waterworks.controllers;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;

import android.widget.Toast;

import team64.waterworks.R;
import team64.waterworks.models.*;


public class ListAllWSRActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all_wsr);

        // Initialize Buttons
        TextView labels = (TextView) findViewById(R.id.labels);
        ListView listView = (ListView) findViewById(R.id.all_reports_list);
        ArrayList<String> reports = WSRManager.viewAllSourceReports();
        Button clear_db_btn = (Button) findViewById(R.id.clr_WSR_btn);
        ArrayAdapter<String> adapter;

        // If array list of all reports isn't empty, populate listview
        if (reports != null && WSRManager.viewAllSourceReports().size() > 0) {
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, reports);
            listView.setAdapter(adapter);
            labels.setText("     (id) (location) (auth) (type) (cond) (rating) (date)");
        } else {
            Toast.makeText(getApplicationContext(), "No reports have been submitted yet!",
                           Toast.LENGTH_SHORT).show();
        }
        clear_db_btn.setOnClickListener(this);
    }


    /**
     * Called when any button is clicked
     * @param v the current view
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clr_WSR_btn: {
                // Ask for user confirmation before clearing db
                new AlertDialog.Builder(this)
                    .setTitle("Clear all Water Source Reports")
                    .setMessage("Do you really want to clear the water source reports db?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            WSRManager.clearWSRDB();
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
                break;
            }
        }
    }
}
