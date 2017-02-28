package team64.waterworks.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import team64.waterworks.R;
import team64.waterworks.models.AccountsManager;
import team64.waterworks.models.Profile;
import team64.waterworks.models.Account;

/**
 * Created by Alexander on 2/24/2017.
 */

public class ProfileActivity extends AppCompatActivity {

    Button done;
    EditText txtBDay, txtAddress, txtEmail, txtTitle;
    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        account = AccountsManager.getActiveAccount();

        done = (Button) findViewById(R.id.profDoneBTN);
        txtAddress = (EditText) findViewById(R.id.profAddress);
        txtBDay = (EditText) findViewById(R.id.profBDay);
        txtEmail = (EditText) findViewById(R.id.profEmail);
        txtTitle = (EditText) findViewById(R.id.profTitle);

        if (account.getProfile() != null) {
            Profile profile = account.getProfile();
            txtAddress.setText(profile.getAddress());
            txtBDay.setText(profile.getBirthday());
            txtEmail.setText(profile.getEmail());
            txtTitle.setText(profile.getTitle());
        }

        /** Button handler for returning to home from ProfileActivity page*/
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account.getProfile().setAddress(txtAddress.getText().toString());
                account.getProfile().setBirthday(txtBDay.getText().toString());
                account.getProfile().setEmail(txtEmail.getText().toString());
                account.getProfile().setTitle(txtTitle.getText().toString());

                Profile new_profile = account.createProfile(txtTitle.getText().toString(),
                                                                 txtAddress.getText().toString(),
                                                                 txtEmail.getText().toString(),
                                                                 txtBDay.getText().toString());
                if (AccountsManager.updateAccount(account.getUsername(), new_profile)) {
                    Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Profile Saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to save profile", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
