package team64.waterworks.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import team64.waterworks.R;
import team64.waterworks.models.AllUsers;
import team64.waterworks.models.Profile;
import team64.waterworks.models.User;

/**
 * Created by Alexander on 2/24/2017.
 */

public class ProfileActivity extends AppCompatActivity {

    Button done;
    EditText txtBDay, txtAddress, txtEmail, txtTitle;
    private User userInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        userInstance = AllUsers.getUserInstance();

        done = (Button) findViewById(R.id.profDoneBTN);
        txtAddress = (EditText) findViewById(R.id.profAddress);
        txtBDay = (EditText) findViewById(R.id.profBDay);
        txtEmail = (EditText) findViewById(R.id.profEmail);
        txtTitle = (EditText) findViewById(R.id.profTitle);


        Profile profile = userInstance.getProfile();

        if (profile != null) {
            txtAddress.setText(profile.getAddress());
            txtBDay.setText(profile.getBirthday());
            txtEmail.setText(profile.getEmail());
            txtTitle.setText(profile.getTitle());
        }

        /** Button handler for returning to home from ProfileActivity page*/
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInstance.createProfile(txtTitle.getText().toString(),
                                            txtAddress.getText().toString(),
                                            txtEmail.getText().toString(),
                                            txtBDay.getText().toString());
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}
