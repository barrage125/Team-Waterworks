package team64.waterworks.models;
import android.content.Context;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;


public class WSRManager {
    private static DBHelper dbHelper;

    /**
     * set a new DBHelper for AllSourceReports SQLite DB
     * @param c Context of the caller
     */
    public static void setDBHelper(Context c) {
        dbHelper = new DBHelper(c, 0);
    }


    /**
     * Create a new Water Source Report and add it to AllSourceReports SQLite DB
     * @param author user creating the source report
     * @param type type of water source report
     * @param condition condition of the water on a scale of ...
     * @return if the source report was created successfully or not
     */
    public static boolean newSourceReport(double latitude, double longitude, String author,
                                          String type, String condition) {
        WaterSourceReport report = new WaterSourceReport(latitude, longitude, author, type, condition);

        // Checks if water source report already exists
//        if (type.equals("location")) {
            try {
                if (locationTaken(latitude, longitude)) {
                    Log.e("Source Report Exists", "A water source report for that location already exists!");
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Unknown Error", "Failed to check if water source report already exists");
                return false;
            }
//        }

        // Add the new source report
        try {
            dbHelper.addSourceReport(report);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Unknown error", "Source report may or may not be saved");
        }
        return false;
    }

    /**
     * Edits the passed in source report and saves its new values into SQLite
     * @param report the source report to be edited and saved
     * @return if the source report was successfully edited and saved in SQLite
     */
    public static boolean editSourceReport(WaterSourceReport report) {
        try {
            dbHelper.updateSourceReport(report);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Unknown error", "Could not edit source report, it may or may not be updated");
        }
        return false;
    }

    /**
     * Lookup source report that corresponds to the passed in ID
     * @param id id of the source report looking up
     * @return source report that corresponds to the passed in ID
     */
    public static WaterSourceReport getSourceReportByID(long id) {
        try {
            return dbHelper.getSourceReportByID(id);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            Log.e("Source report Not Found", "No source report could be found by that ID");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Unknown error", "Could not retrieve source report with that ID");
        }
        return null;
    }

    /**
     * Create ArrayList of all source reports in SQLite that correspond to the passed in location object
     * @return ArrayList of source reports that correspond to passed in location
     * @throws IOException when location object passed in
     * @throws NoSuchElementException
     * @throws Exception
     */
    public static ArrayList<WaterSourceReport> getSourceReportsByLocation(double latitude, double longitude) {
        try {
            return dbHelper.getSourceReportsByLocation(latitude, longitude);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            Log.e("No Source Reports Found", "No source reports matched that location");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Unknown Error", "Could not retrieve source report(s) with that location");
        }
        return null;
    }

    /**
     * Creates array list of source reports written by the corresponding author
     * @param author author searching for
     * @return array list of source reports written by the passed in author
     */
    public static ArrayList<WaterSourceReport> getSourceReportsByAuthor(String author) {
        try {
            return dbHelper.getSourceReportsByAuthor(author);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            Log.e("No Source Reports Found", "No source reports have been written by that user");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Unknown Error", "Could not retrieve source report(s) written by that user");
        }
        return null;
    }

    /**
     * Creates array list of all water source reports stored in AllSourceReports SQLite DB
     * @return array list of all water source reports
     */
    public static ArrayList<String> viewAllSourceReports() {
        try {
            return dbHelper.getAllSourceReports();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            Log.e("No Source Reports Found", "No source reports have been submitted yet");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Unknown Error", "Could not retrieve source report(s)");
        }
        return null;
    }

    public static void clearWSRDB() {
        dbHelper.deleteAllWSR();
    }

    /**
     * Checks to see if a source report already exists in location
     * @param latitude latitude of location
     * @param longitude longitude of location
     * @return if the water location is already taken
     */
    private static boolean locationTaken(double latitude, double longitude) {
        try {
            return dbHelper.isLocation(latitude, longitude);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Unknown Error", "Could not retrieve source report(s) written by that user");
        }
        return true;
    }
}
