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

    /**
     * Initializes all variables needed for home activity
     * @param savedInstanceState data passed into home activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Account account = AccountsManager.getActiveAccount();

        // Initialize views
        Button logout = (Button) findViewById(R.id.logout_btn);
        Button myProfile = (Button) findViewById(R.id.view_profile_btn);
        Button viewReports = (Button) findViewById(R.id.create_report_btn);
        Button createReport = (Button) findViewById(R.id.homeViewRepBTN);
        TextView welcome = (TextView) findViewById(R.id.welcome_message);
        welcome.setText("Welcome to Waterworks " + account.getUsername() + "!");

        // Listeners for all buttons
        logout.setOnClickListener(this);
        myProfile.setOnClickListener(this);
        viewReports.setOnClickListener(this);
        createReport.setOnClickListener(this);
    }

    /**
     * Called when any button on home view is clicked
     * @param v the home view
     */
    @Override
    public void onClick(View v) {
            switch(v.getId()) {
            /*case R.id.homeViewRepBTN: {
                Intent intent = new Intent(HomeActivity.this, **View Report Activity**.class);
                startActivity(intent);
                break;
            }*/
                case R.id.create_report_btn: {
                    Intent intent = new Intent(HomeActivity.this, WaterReportActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.logout_btn: {
                    Intent intent = new Intent(HomeActivity.this, SplashActivity.class);
                    startActivity(intent);
                    AccountsManager.clearActiveAccount();
                    Toast.makeText(getApplicationContext(), "Logout successful", Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.view_profile_btn: {
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