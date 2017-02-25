package team64.waterworks.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import team64.waterworks.R;

public class HomeActivity extends AppCompatActivity {

    Button logout;
    Button myProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        logout = (Button) findViewById(R.id.home_logout);
        myProfile = (Button) findViewById(R.id.homeProfileBTN);

        /** Button handler for logging in from SplashActivity page*/
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SplashActivity.class);
                startActivity(intent);
            }
        });

        /** Button handler for accessing profile from HomeActivity page*/
        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}