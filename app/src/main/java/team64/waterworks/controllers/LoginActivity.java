package team64.waterworks.controllers;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

import team64.waterworks.R;
import team64.waterworks.models.*;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button login;
    private EditText pass,user;
    private TextView error;
    private int counter = 3;

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
        Button recovery = (Button) findViewById(R.id.recovery_button);
        error = (TextView) findViewById(R.id.error_message);
        error.setVisibility(View.GONE);

        // Button listeners
        login.setOnClickListener(this);
        cancel.setOnClickListener(this);
        recovery.setOnClickListener(this);
    }

    /**
     * Called when any button on login view is clicked
     * @param v the login view
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.recovery_button:

                final String recoveryName = user.getText().toString();
                final int code = (int) Math.floor(Math.random() * 10000);
                if(TextUtils.isEmpty(recoveryName)) {
                    user.setError("Username cannot be blank");
                    SecurityLog.appendLog("Failed login recovery attempt: blank username", this);
                } else {
                    Account recoveryAccount = AccountsManager.findAccount(recoveryName);
                    if(recoveryAccount == null) {
                        user.setError("Account does not exist");
                        SecurityLog.appendLog("Failed login recovery attempt: no account found", this);
                    } else {
                        String recoveryAddress = recoveryAccount.getProfile().getEmail();
                        if (recoveryAddress.equals("")) {
                            user.setError("Recovery impossible. No associated email.");
                            SecurityLog.appendLog("Failed login recovery attempt: no email found", this);
                        }
                        new RecoveryEmailTask(LoginActivity.this).execute(recoveryAddress, code);
                        SecurityLog.appendLog("Login recovery email sent to "+recoveryAddress+" for "+recoveryName, this);
                        //Toast.makeText(getApplicationContext(), "Email Sent", Toast.LENGTH_SHORT).show();
                    }
                }

                View view = (LayoutInflater.from(LoginActivity.this)).inflate(R.layout.dialog_verification, (ViewGroup) null);

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(LoginActivity.this);
                alertBuilder.setView(view);
                alertBuilder.setCancelable(true);

                final EditText verification = (EditText) view.findViewById(R.id.editText_verification);

                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int inputCode = Integer.parseInt(verification.getText().toString());
                        if (inputCode == code) {
                            View view = (LayoutInflater.from(LoginActivity.this)).inflate(R.layout.dialog_new_password, (ViewGroup) null);

                            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(LoginActivity.this);
                            alertBuilder.setView(view);
                            alertBuilder.setCancelable(true);

                            final EditText newPassword = (EditText) view.findViewById(R.id.editText_new_password);

                            alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String inputPassword = newPassword.getText().toString();
                                    if (!inputPassword.equals("")) {
                                        Account currentAccount = AccountsManager.findAccount(recoveryName);
                                        currentAccount.setPassword(inputPassword);
                                        AccountsManager.editAccount(currentAccount);
                                    }
                                }
                            });

                            alertBuilder.setNegativeButton("Cancel", null);

                            Dialog dialogPassword = alertBuilder.create();
                            dialogPassword.show();
                        }
                    }
                });

                alertBuilder.setNegativeButton("Cancel", null);

                Dialog dialogVerify = alertBuilder.create();
                dialogVerify.show();
                break;

            case R.id.login_button:
                // Declare username and password vars
                String username = user.getText().toString();
                String password = pass.getText().toString();
                Account account = AccountsManager.getAccountWithCreds(username, password);

                // Prevents fields from being empty
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    if(TextUtils.isEmpty(username)) {
                        user.setError("Username cannot be blank");
                        SecurityLog.appendLog("Failed login attempt: blank username", this);
                    }

                    if(TextUtils.isEmpty(password)) {
                        pass.setError("Password cannot be blank");
                        SecurityLog.appendLog("Failed login attempt: blank password", this);
                    }
                } else if ( account != null) {
                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    SecurityLog.appendLog("Successful login by "+account.getName()+" with authority level "+account.getAuthLevel(), this);
                    AccountsManager.setActiveAccount(account);
                    Log.d("Authority Level", "Auth level of logged in account is: " + account.getAuthLevel());
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    finish();
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Incorrect username/password combination",Toast.LENGTH_SHORT).show();

                    /* Show login attempts left */
                    error.setVisibility(View.VISIBLE);
                    counter--;

                    SecurityLog.appendLog("Failed login attempt: invalid password. "+counter+" attempts remaining.", this);

                    error.setText(String.format("%1$d" + R.string.attempts, counter));
                    if (counter == 0) {
                        login.setEnabled(false);
                        SecurityLog.appendLog("Login disabled for too many attempts", this);
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
