package team64.waterworks.controllers;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import team64.waterworks.R;
import team64.waterworks.models.AccountsManager;
import team64.waterworks.models.ReportsManager;


public class SplashActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Initializes all variables needed for splash activity
     * @param savedInstanceState data passed into splash activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Initialize views
        Button wlogin = (Button) findViewById(R.id.welcome_login);
        Button wreg = (Button) findViewById(R.id.register_login);

        // Listeners for all buttons
        wlogin.setOnClickListener(this);
        wreg.setOnClickListener(this);

        // Set up database handler
        AccountsManager.setDBHelper(this);
        ReportsManager.setDBHelper(this);
    }

    /**
     * Called when any button is clicked
     * @param v splash view
     */
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