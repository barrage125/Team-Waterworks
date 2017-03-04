package team64.waterworks.controllers;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import team64.waterworks.R;
import team64.waterworks.models.Account;
import team64.waterworks.models.AccountsManager;
import team64.waterworks.models.ReportsManager;


/**
 * Created by Alexander on 3/3/2017.
 */

public class WaterReportActivity extends AppCompatActivity implements View.OnClickListener {

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
        setContentView(R.layout.activity_water_report);
        account = AccountsManager.getActiveAccount();

        submit = (Button) findViewById(R.id.wrSubmitButton);
        cancel = (Button) findViewById(R.id.wrCancelButton);
        txtLong = (EditText) findViewById(R.id.wrLong);
        txtLat = (EditText) findViewById(R.id.wrLat);
        txtType = (EditText) findViewById(R.id.wrType);
        txtCondition = (EditText) findViewById(R.id.wrCond);

        submit.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.wrSubmitButton: {
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
            case R.id.wrCancelButton: {
                Intent intent = new Intent(WaterReportActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        }
    }

}

