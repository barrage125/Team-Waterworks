package team64.waterworks.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import team64.waterworks.R;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button logout, myProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize views
        logout = (Button) findViewById(R.id.home_logout);
        myProfile = (Button) findViewById(R.id.homeProfileBTN);

        // Listeners for all buttons
        logout.setOnClickListener(this);
        myProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.home_logout:
                Intent intent = new Intent(HomeActivity.this, SplashActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Logout successful",Toast.LENGTH_SHORT).show();
                break;

            case R.id.homeProfileBTN:
                Intent intent2 = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent2);
                break;

            default:
                break;
        }
    }
}