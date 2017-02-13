package team64.waterworks.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import team64.waterworks.R;

public class SplashActivity extends AppCompatActivity {

    Button wlogin, wreg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        wlogin = (Button) findViewById(R.id.welcome_login);
        wreg = (Button) findViewById(R.id.register_login);

        /** Button handler for logging in from SplashActivity page*/
        wlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        /** Button handler for registering from SplashActivity page*/
        wreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}