package michal.waterworks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class register extends AppCompatActivity {

    Button reg,regcanc;
    EditText reguser, regpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        reg = (Button) findViewById(R.id.reg_button);
        reguser = (EditText) findViewById(R.id.reg_user);
        regpass = (EditText) findViewById(R.id.reg_password);
        regcanc = (Button) findViewById(R.id.reg_cancel);

        /** Button handler for registering a user */
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(register.this, home.class);
                startActivity(intent);
            }
        });

        /** Button handler for canceling registration */
        regcanc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
