package team64.waterworks.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import team64.waterworks.R;
import team64.waterworks.models.AllUsers;
import team64.waterworks.models.User;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button logout, myProfile;
    private TextView welcome;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        user = AllUsers.getUserInstance();

        // Initialize views
        logout = (Button) findViewById(R.id.home_logout);
        myProfile = (Button) findViewById(R.id.homeProfileBTN);
        welcome = (TextView) findViewById(R.id.welcome_message);
        welcome.setText("Welcome to Waterworks!");


        // Listeners for all buttons
        logout.setOnClickListener(this);
        myProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.home_logout: {
                Intent intent = new Intent(HomeActivity.this, SplashActivity.class);
                startActivity(intent);
                AllUsers.clearUserInstance();
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