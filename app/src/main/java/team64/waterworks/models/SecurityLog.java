package team64.waterworks.models;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by anna on 4/22/17.
 */

public class SecurityLog {

    public static void appendLog(String text, Context c) {
        String filename = "security_log";
        FileOutputStream outputStream;
        try {
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss", Locale.US).format(new Date());

            outputStream = c.openFileOutput(filename, Context.MODE_APPEND);
            outputStream.write(timeStamp.getBytes());
            outputStream.write("\t".getBytes());
            outputStream.write(text.getBytes());
            outputStream.write("\n".getBytes());
            outputStream.close();

            Log.d("SecurityLog", "Successfully wrote " + (timeStamp + text).length() + " characters to log");
        }
        catch (IOException e)
        {
            Log.w("SecurityLog", "Caught IOException");
        }
    }

    public static String readLog(Context c) {

        String filename = "security_log";
        FileInputStream inputStream;

        try {
            inputStream = c.openFileInput(filename);

            StringBuilder out = new StringBuilder();
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = reader.readLine()) != null) {
                out.append(line + "\n");
            }
            //System.out.println(out.toString());   //Prints the string content read from input stream
            reader.close();

            Log.i("SecurityLog", "Returned " + out.toString().length() + " characters");
            return out.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return "No data found";
        }
    }

}
