package team64.waterworks.controllers;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import team64.waterworks.R;
import team64.waterworks.models.Account;
import team64.waterworks.models.AccountsManager;
import team64.waterworks.models.Profile;
import team64.waterworks.models.ReportsManager;
import team64.waterworks.models.WaterReport;

/**
 * Created by Alexander on 3/3/2017.
 */

public class WaterReportActivity extends AppCompatActivity {

    Button submit, cancel;
    EditText txtLong, txtLat, txtType, txtCondition;

    private Account account;
    private Location location;
    private String author;
    private String type;
    private String condition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        account = AccountsManager.getActiveAccount();

        submit = (Button) findViewById(R.id.profDoneBTN);
        cancel = (Button) findViewById(R.id.profDoneBTN);
        txtLong = (EditText) findViewById(R.id.wrLong);
        txtLat = (EditText) findViewById(R.id.wrLat);
        txtType = (EditText) findViewById(R.id.wrType);
        txtCondition = (EditText) findViewById(R.id.wrCond);


        //** Button handler for returning to home from WaterReportActivity page*//*
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                condition = txtCondition.getText().toString();
                type = txtType.getText().toString();
                author = account.getUsername();
                location = new Location("");
                location.setLongitude(Double.parseDouble(txtLong.getText().toString()));
                location.setLatitude(Double.parseDouble(txtLat.getText().toString()));
                if (ReportsManager.newReport(location, author, type, condition)) {
                    Intent intent = new Intent(WaterReportActivity.this, HomeActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Report submitted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to submit", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //** Button handler for returning to home from WaterReportActivity page w/o saving report*//*
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WaterReportActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}

