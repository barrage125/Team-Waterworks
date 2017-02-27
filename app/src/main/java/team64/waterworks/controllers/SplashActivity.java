package team64.waterworks.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import team64.waterworks.R;
import team64.waterworks.models.AllUsers;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {

    private Button wlogin, wreg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Initialize views
        wlogin = (Button) findViewById(R.id.welcome_login);
        wreg = (Button) findViewById(R.id.register_login);



        // Listeners for all buttons
        wlogin.setOnClickListener(this);
        wreg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.welcome_login:
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                break;

            case R.id.register_login:
                Intent intent2 = new Intent(SplashActivity.this, RegisterActivity.class);
                startActivity(intent2);
                break;

            default:
                break;
        }
    }
}