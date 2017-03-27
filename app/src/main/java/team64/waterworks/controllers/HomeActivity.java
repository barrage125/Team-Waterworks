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

        // Initialize buttons
        TextView welcome = (TextView) findViewById(R.id.welcome_message);
        welcome.setText("Welcome to Waterworks " + account.getUsername() + "!");

        Button logout = (Button) findViewById(R.id.logout_btn);
        Button myProfile = (Button) findViewById(R.id.view_profile_btn);
        Button viewMap = (Button) findViewById(R.id.view_map_btn);

        Button createSourceReport = (Button) findViewById(R.id.create_src_report_btn);
        Button listAllSourceReports = (Button) findViewById(R.id.list_all_WSR_btn);

        Button createPurReport = (Button) findViewById(R.id.create_pur_report_btn);
        Button listAllPurReports = (Button) findViewById(R.id.list_all_WPR_btn);

        // Listeners for all buttons
        logout.setOnClickListener(this);
        myProfile.setOnClickListener(this);
        viewMap.setOnClickListener(this);
        createSourceReport.setOnClickListener(this);
        listAllSourceReports.setOnClickListener(this);

        // If user auth level, can't view or add purity reports
        if (account.getAuthLevel().equals("user")) {
            createPurReport.setVisibility(View.GONE);
            listAllPurReports.setVisibility(View.GONE);
        } else {
            createPurReport.setOnClickListener(this);
            listAllPurReports.setOnClickListener(this);
        }
    }

    /**
     * Called when any button on home view is clicked
     * @param v the home view
     */
    @Override
    public void onClick(View v) {
            switch(v.getId()) {
                case R.id.list_all_WSR_btn: {
                    Intent intent = new Intent(HomeActivity.this, ListAllWSRActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.create_src_report_btn: {
                    Intent intent = new Intent(HomeActivity.this, AddWSRActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.list_all_WPR_btn: {
                    Intent intent = new Intent(HomeActivity.this, ListAllWPRActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.create_pur_report_btn: {
                    Intent intent = new Intent(HomeActivity.this, AddWPRActivity.class);
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
                case R.id.view_map_btn: {
                    Intent intent = new Intent(HomeActivity.this, MapActivity.class);
                    startActivity(intent);
                    break;
                }
                default: {
                    break;
                }
        }
    }
}