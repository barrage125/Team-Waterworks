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
import team64.waterworks.models.*;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button login;
    private EditText pass,user;
    private TextView error;
    int counter = 3;

    /**
     * Initializes all variables needed for login activity
     * @param savedInstanceState data passed into login activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        login = (Button) findViewById(R.id.login_button);
        pass = (EditText) findViewById(R.id.password);
        user = (EditText) findViewById(R.id.username);
        Button cancel = (Button) findViewById(R.id.cancel_button);
        error = (TextView) findViewById(R.id.error_message);
        error.setVisibility(View.GONE);

        // Button listeners
        login.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    /**
     * Called when Login button is clicked
     * @param v the login view
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.login_button:
                // Declare username and password vars
                String username = user.getText().toString();
                String password = pass.getText().toString();
                Account account = AccountsManager.getAccountWithCreds(username, password);

                // Prevents fields from being empty
                if (TextUtils.isEmpty(username) | TextUtils.isEmpty(password)) {
                    if(TextUtils.isEmpty(username)) {
                        user.setError("Username cannot be blank");
                    }

                    if(TextUtils.isEmpty(password)) {
                        pass.setError("Password cannot be blank");
                    }
                } else if ( account != null) {
                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    AccountsManager.setActiveAccount(account);
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//                    intent.putExtra("USER", users_db.AccountWithCreds(username, password));
                    startActivity(intent);
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
