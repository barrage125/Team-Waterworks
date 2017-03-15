package team64.waterworks.models;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.NoSuchElementException;


public class WPRManager {
    private static DBHelper dbHelper;

    /**
     * set a new DBHelper for AllPurityReports SQLite DB
     * @param c Context of the caller
     */
    public static void setDBHelper(Context c) {
        dbHelper = new DBHelper(c, 1, 1);
    }


    /**
     * Create a new Water Purity Report and add it to AllPurityReports SQLite DB
     */
    public static boolean newPurityReport(double latitude, double longitude, String author,
                                          String type, String condition) {
        WaterPurityReport report = new WaterPurityReport(latitude, longitude, condition, author, type);

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
            dbHelper.addPurityReport(report);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Unknown error", "Source report may or may not be saved");
        }
        return false;
    }

    /**
     * Edits the passed in purity report and saves its new values into SQLite
     */
    public static boolean editPurityReport(WaterPurityReport report) {
        try {
            dbHelper.updatePurityReport(report);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Unknown error", "Could not edit source report, it may or may not be updated");
        }
        return false;
    }

    /**
     * Lookup purity report that corresponds to the passed in ID
     */
    public static WaterPurityReport getPurityReportByID(long id) {
        try {
            return dbHelper.getPurityReportByID(id);
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
     * Create ArrayList of all purity reports in SQLite that correspond to the passed in location object
     */
    public static ArrayList<WaterPurityReport> getPurityReportsByLocation(double latitude, double longitude) throws Exception {
        try {
            return dbHelper.getPurityReportsByLocation(latitude, longitude);
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
     * Creates array list of purity reports written by the corresponding author
     */
    public static ArrayList<WaterPurityReport> getPurityReportsByAuthor(String author) {
        try {
            return dbHelper.getPurityReportsByAuthor(author);
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
     * Creates array list of all water purity reports stored in AllPurityReports SQLite DB
     * @return array list of all water purity reports
     */
    public static ArrayList<String> viewAllPurityReports() {
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

    public static void clearWPRDB() {
        dbHelper.deleteAllWPR();
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
