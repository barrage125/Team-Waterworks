package team64.waterworks.controllers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import team64.waterworks.R;
import team64.waterworks.models.WPRManager;

public class ListAllWPRActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all_wpr);

        // Initialize Buttons
        TextView labels = (TextView) findViewById(R.id.plabels);
        ListView listView = (ListView) findViewById(R.id.all_preports_list);
        ArrayList<String> reports = WPRManager.viewAllPurityReports();
        Button clear_db_btn = (Button) findViewById(R.id.clr_WPR_btn);
        ArrayAdapter<String> adapter;

        // If array list of all reports isn't empty, populate listview
        if (reports != null && WPRManager.viewAllPurityReports().size() > 0) {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, reports);
            listView.setAdapter(adapter);
            labels.setText(" (id) (loc) (auth) (cond) (v. ppm) (c. ppm) (date)");
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
            case R.id.clr_WPR_btn: {
                // Ask for user confirmation before clearing db
                new AlertDialog.Builder(this)
                        .setTitle("Clear all Water Purity Reports")
                        .setMessage("Do you really want to clear the water purity reports db?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                WPRManager.clearWPRDB();
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
