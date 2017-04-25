package team64.waterworks.controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import team64.waterworks.R;
import team64.waterworks.models.SecurityLog;
import team64.waterworks.models.WSRManager;

public class SecurityLogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_log);

        // Initialize Buttons
        ListView listView = (ListView) findViewById(R.id.all_logs_list);

        ArrayList<String> logs = new ArrayList<>();
        Collections.addAll(logs, SecurityLog.readLog(this).split("\n"));

        ArrayAdapter<String> adapter;

        // If array list of all reports isn't empty, populate listview
        try {
            //noinspection ConstantConditions
            if (logs != null && logs.size() > 0) {
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, logs);
                listView.setAdapter(adapter);
            } else {
                Toast.makeText(getApplicationContext(), "No logs have been recorded yet!",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (NullPointerException e) {
            Log.e("SecurityLogActivity", "size() is called on null obj");
        }
    }
}
