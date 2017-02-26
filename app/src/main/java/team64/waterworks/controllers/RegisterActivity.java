package team64.waterworks.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import team64.waterworks.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    Button reg,regcanc;
    EditText reguser, regpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Views
        reg = (Button) findViewById(R.id.reg_button);
        reguser = (EditText) findViewById(R.id.reg_user);
        regpass = (EditText) findViewById(R.id.reg_password);
        regcanc = (Button) findViewById(R.id.reg_cancel);

        // Listeners for all buttons
        reg.setOnClickListener(this);
        regcanc.setOnClickListener(this);
    }

    private void registerUser() {

    }

    @Override
    public void onClick(View v) {
        // Switch cases for each button, this way we're not declaring a new View.OnclickListener
        // and writing our own onClick method every time we need a new button
        switch(v.getId()) {
            case R.id.reg_button:
                registerUser();
                break;

            case R.id.reg_cancel:
                // do this
                break;

            default:
                break;
        }
    }
}
