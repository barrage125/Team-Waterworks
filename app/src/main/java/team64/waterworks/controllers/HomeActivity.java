package team64.waterworks.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import team64.waterworks.R;
import team64.waterworks.models.*;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button logout, myProfile, viewReports, createReport;
    private TextView welcome;
    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        account = AccountsManager.getActiveAccount();

        // Initialize views
        logout = (Button) findViewById(R.id.home_logout);
        myProfile = (Button) findViewById(R.id.homeProfileBTN);
        viewReports = (Button) findViewById(R.id.homeCreateRepBTN);
        createReport = (Button) findViewById(R.id.homeViewRepBTN);
        welcome = (TextView) findViewById(R.id.welcome_message);
        welcome.setText("Welcome to Waterworks!");


        // Listeners for all buttons
        logout.setOnClickListener(this);
        myProfile.setOnClickListener(this);
        viewReports.setOnClickListener(this);
        createReport.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
            switch(v.getId())
            {/*case R.id.homeViewRepBTN: {
                Intent intent = new Intent(HomeActivity.this, **View Report Activity**.class);
                startActivity(intent);
                break;
            }*/case R.id.homeCreateRepBTN: {
                Intent intent = new Intent(HomeActivity.this, WaterReportActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.home_logout: {
                Intent intent = new Intent(HomeActivity.this, SplashActivity.class);
                startActivity(intent);
                AccountsManager.clearActiveAccount();
                Toast.makeText(getApplicationContext(), "Logout successful", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.homeProfileBTN: {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
            }
            default: {
                break;
            }
        }
    }
}