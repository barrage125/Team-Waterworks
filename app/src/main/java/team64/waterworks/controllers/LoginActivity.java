package team64.waterworks.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import team64.waterworks.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button login,cancel;
    EditText pass,user;
    TextView error;
    int counter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        login = (Button) findViewById(R.id.login_button);
        pass = (EditText) findViewById(R.id.password);
        user = (EditText) findViewById(R.id.username);
        cancel = (Button) findViewById(R.id.cancel_button);
        error = (TextView) findViewById(R.id.error_message);
        error.setVisibility(View.GONE);

        // Button listeners
        login.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.login_button:
                // Declare username and password vars
                String username = user.getText().toString();
                String password = pass.getText().toString();

                // Prevents fields from being empty
                if (TextUtils.isEmpty(username) | TextUtils.isEmpty(password)) {
                    if(TextUtils.isEmpty(username)) {
                        user.setError("Username cannot be blank");
                    }

                    if(TextUtils.isEmpty(password)) {
                        pass.setError("Password cannot be blank");
                    }
                }

                // username/password check
                else if (password.equals("pass") && username.equals("user")) {
                    Toast.makeText(getApplicationContext(), "Login Successful",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);

                // Incorrect username/password error message
                } else {
                    Toast.makeText(getApplicationContext(), "Incorrect username/password combination",Toast.LENGTH_SHORT).show();

                    /** Show login attempts left */
                    error.setVisibility(View.VISIBLE);
                    counter--;
                    error.setText(Integer.toString(counter) + " login attempt(s) left");
                    if (counter == 0) {
                        login.setEnabled(false);
                    }
                }
                break;

            case R.id.cancel_button:
                Toast.makeText(getApplicationContext(), "Login cancelled",Toast.LENGTH_SHORT).show();
                finish();
                break;

            default:
                break;
        }
    }
}
