package team64.waterworks.controllers;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.Arrays;
import java.util.List;
import team64.waterworks.R;
import team64.waterworks.models.AccountsManager;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText reguser, regpass;
    private ProgressDialog progressDialog;

    /**
     * Initializes all variables needed for register activity
     * @param savedInstanceState data passed into register activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Views
        Button reg = (Button) findViewById(R.id.reg_button);
        reguser = (EditText) findViewById(R.id.reg_user);
        regpass = (EditText) findViewById(R.id.reg_password);
        Button regcanc = (Button) findViewById(R.id.reg_cancel);
        Spinner acctType = (Spinner) findViewById(R.id.spinnerAcctType);
        progressDialog = new ProgressDialog(this);

        // Listeners for all buttons
        reg.setOnClickListener(this);
        regcanc.setOnClickListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                                       Arrays.asList("User", "Worker", "Manager", "Admin"));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        acctType.setAdapter(adapter);
    }

    /**
     * Registers a new user
     */
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
        if (AccountsManager.isValidAccount(username)) {
            reguser.setError("Username already taken");
        } else {
            progressDialog.setMessage("Registering user...");
            progressDialog.show();

            if(!AccountsManager.newUser("", username, password)) {
                progressDialog.dismiss();
                Log.d("Sorry","An error occurred");
            } else {
                AccountsManager.setActiveAccount(AccountsManager.getAccountWithCreds(username, password));
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        }
    }

    /**
     * called when any button on register view is clicked
     * @param v register view
     */
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
