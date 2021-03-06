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
                                          String condition, long vppm, long cppm) {
        WaterPurityReport report = new WaterPurityReport(latitude, longitude, condition, author, vppm, cppm);

        // Add the new purity report
        try {
            dbHelper.addPurityReport(report);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Unknown error", "Purity report may or may not be saved");
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
            Log.e("Unknown error", "Could not edit purity report, it may or may not be updated");
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
            Log.e("Purity report Not Found", "No purity report could be found by that ID");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Unknown error", "Could not retrieve purity report with that ID");
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
            Log.e("No Purity Reports Found", "No purity reports matched that location");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Unknown Error", "Could not retrieve purity report(s) with that location");
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
            Log.e("No Purity Reports Found", "No purity reports have been written by that user");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Unknown Error", "Could not retrieve purity report(s) written by that user");
        }
        return null;
    }


    public static ArrayList<WaterPurityReport> getPurityReportsByLocationAndDate(double latitude, double longitude, String startDate, String endDate) {
        try {
            return dbHelper.getPurityReportsByLocationAndDate(latitude, longitude, startDate, endDate);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            Log.e("No Purity Reports Found", "No purity reports have been written by that user");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Unknown Error", "Could not retrieve purity report(s) written by that user");
        }
        return null;
    }

    /**
     * Creates array list of all water purity reports stored in AllPurityReports SQLite DB
     * @return array list of all water purity reports
     */
    public static ArrayList<String> viewAllPurityReports() {
        try {
            return dbHelper.getAllPurityReports();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            Log.e("No Purity Reports Found", "No purity reports have been submitted yet");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Unknown Error", "Could not retrieve purity report(s)");
        }
        return null;
    }

    public static void clearWPRDB() {
        dbHelper.deleteAllWPR();
    }
}
