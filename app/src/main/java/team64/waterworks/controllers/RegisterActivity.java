package team64.waterworks.controllers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import team64.waterworks.R;
import team64.waterworks.models.AllUsers;
import team64.waterworks.models.User;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button reg,regcanc;
    private EditText reguser, regpass;
    private ProgressDialog progressDialog;
    private DBHelper users_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Views
        reg = (Button) findViewById(R.id.reg_button);
        reguser = (EditText) findViewById(R.id.reg_user);
        regpass = (EditText) findViewById(R.id.reg_password);
        regcanc = (Button) findViewById(R.id.reg_cancel);
        progressDialog = new ProgressDialog(this);

        // Listeners for all buttons
        reg.setOnClickListener(this);
        regcanc.setOnClickListener(this);

        users_db = AllUsers.getInstance(this);
    }

    private void registerUser() {
        // Declare username and password vars
        String username = reguser.getText().toString();
        String password = regpass.getText().toString();

        // If empty fields
        if (TextUtils.isEmpty(username)) {
            reguser.setError("Username cannot be blank");
        }

        if (TextUtils.isEmpty(password)) {
            regpass.setError("Password cannot be blank");
        }

        // else if username already taken
        if (users_db.isUser(username)) {
            reguser.setError("Username already taken");
        } else {
            progressDialog.setMessage("Registering user...");
            progressDialog.show();
            if(!users_db.addUser("", username, password)) {
                Log.d("HEY","DANGER WILL ROBINSON");
            }
            AllUsers.setUserInstance(users_db.getUser(username, password));
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Registration Successful",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
            startActivity(intent);
        }
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
                Intent intent = new Intent(RegisterActivity.this, SplashActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Registration canceled",Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }
}
