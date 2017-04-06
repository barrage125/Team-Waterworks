package team64.waterworks.controllers;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import java.util.Arrays;

import team64.waterworks.R;
import team64.waterworks.models.Account;
import team64.waterworks.models.AccountsManager;
import team64.waterworks.models.WSRManager;


public class AddWSRActivity extends AppCompatActivity implements View.OnClickListener {

    private Button submit;
    private Button cancel;
    private EditText txtLong;
    private EditText txtLat;
    private Spinner _waterType;
    private Spinner waterCond;

    private Account account;
    private ProgressDialog progressDialog;

    /**
     * Initializes all variables needed for water report activity
     * @param savedInstanceState data passed into water report activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wsr);
        account = AccountsManager.getActiveAccount();

        submit = (Button) findViewById(R.id.wrSubmitButton);
        cancel = (Button) findViewById(R.id.wrCancelButton);
        txtLong = (EditText) findViewById(R.id.wrLong);
        txtLat = (EditText) findViewById(R.id.wrLat);
        _waterType = (Spinner) findViewById(R.id.wrType);
        waterCond = (Spinner) findViewById(R.id.wrCond);

        submit.setOnClickListener(this);
        cancel.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                Arrays.asList("Bottled", "Well", "Stream", "Lake", "Spring", "Other"));
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _waterType.setAdapter(typeAdapter);

        ArrayAdapter<String> condAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                Arrays.asList("Waste", "Treatable-Clear", "Treatable-Muddy", "Potable"));
        condAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        waterCond.setAdapter(condAdapter);

    }

    /**
     * Called when any button on water report view is clicked
     * @param v water report view
     */
    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.wrSubmitButton: {

                String author = account.getUsername();
                String latitude = txtLat.getText().toString();
                String longitude = txtLong.getText().toString();
                String waterType = _waterType.getSelectedItem().toString();
                String waterCondition = waterCond.getSelectedItem().toString();

                if (TextUtils.isEmpty(latitude) || TextUtils.isEmpty(longitude)) {
                    txtLat.setError("Coordinates cannot be blank!");
                } else if (Double.parseDouble(latitude) < -90 || Double.parseDouble(latitude) > 90) {
                    txtLat.setError("Latitude must be between -90 and 90");
                } else if (Double.parseDouble(longitude) < -180 || Double.parseDouble(longitude) > 180) {
                    txtLong.setError("Longitude must be between -180 and 180");
                } else {
                    progressDialog.setMessage("Submitting report...");
                    progressDialog.show();

                    if (!(WSRManager.newSourceReport(Double.parseDouble(latitude), Double.parseDouble(longitude), author, waterType, waterCondition))) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Unable to submit", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Intent intent = new Intent(AddWSRActivity.this, HomeActivity.class);
                        finish();
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Report submitted", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;

            case R.id.wrCancelButton: {
                Intent intent = new Intent(AddWSRActivity.this, HomeActivity.class);
                startActivity(intent);
            }
            break;
        }
    }

}

