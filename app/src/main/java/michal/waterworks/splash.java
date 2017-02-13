package michal.waterworks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class splash extends AppCompatActivity {

    Button wlogin, wreg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        wlogin = (Button) findViewById(R.id.welcome_login);
        wreg = (Button) findViewById(R.id.register_login);

        /** Button handler for logging in from splash page*/
        wlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(splash.this, login.class);
                startActivity(intent);
            }
        });

        /** Button handler for registering from splash page*/
        wreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(splash.this, register.class);
                startActivity(intent);
            }
        });
    }
}