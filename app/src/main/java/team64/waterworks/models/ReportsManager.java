package team64.waterworks.models;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

public class ReportsManager {
    private static DBHelper dbHelper;


    /**
     * set a new DBHelper to manage
     * @param c Context of the caller
     */
    public static void setDBHelper(Context c) {
        dbHelper = new DBHelper(c, 1);
    }


    /**
     * Create a new Water Report
     * @param location location of water you're reporting
     * @param author user creating the report
     * @param type type of water report (historical, location, or purity)
     * @param condition condition of the water on a scale of 1-5 (health risk, poor, ok, great, pristine)
     * @return if the report was created successfully or not
     */
    public static boolean newReport(Location location, String author, String type, String condition) {
        WaterReport report = new WaterReport(location, author, type, condition);

        // First check if water location report already exists
//        if (type.equals("location")) {
            try {
                if (isValidReport(report)) {
                    Log.e("Report Exists", "A water location report for that location already exists!");
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Loc Serialize Error", "Could not get location as a String when checking isValidReport");
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Unknown Error", "Failed to check if water location report already exists");
                return false;
            }
//        }

        // Add the new report
        try {
            dbHelper.addReport(report);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Location invalid", "Could not retrieve location string");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Unknown error", "Report may or may not be saved");
            return false;
        }
    }


    /**
     * Edit the passed in report values and save into SQLite
     * @param report to be edited and saved
     * @return if the report was successfully edited and saved in SQLite
     */
    public static boolean editReport(WaterReport report) {
        try {
            dbHelper.updateReport(report);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Location invalid", "Could not retrieve location string");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Unknown error", "Could not edit report, report may or may not be updated");
        }

        return true;
    }


    /**
     * Lookup report that corresponds to the passed in ID
     * @param id id of the report looking up
     * @return report that corresponds to the passed in ID
     */
    public static WaterReport getReportByID(long id) {
        try {
            return dbHelper.getReportByID(id);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Loc Deserialize Error", "Location for found report couldn't be deserialized");
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.e("Loc Deserialize Error", "Location for found report couldn't be deserialized");
            return null;
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            Log.e("Report Not Found", "No report could be found by that ID");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Unknown error", "Could not retrieve report with that ID");
            return null;
        }
    }


    /**
     * Create ArrayList of all reports in SQLite that correspond to the passed in location object
     * @param location location of the reports needed
     * @return ArrayList of reports that correspond to passed in location
     * @throws IOException when location object passed in
     * @throws NoSuchElementException
     * @throws Exception
     */
    public static ArrayList<WaterReport> getReportsByLocation(Location location) throws IOException,
                                                                                        NoSuchElementException,
                                                                                        Exception {
        try {
            return dbHelper.getReportsByLocation(location);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Unknown error", "Could not convert location to string for querying db");
            return null;
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            Log.e("No Reports Found", "No reports matched that location");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Unknown Error", "Could not retrieve report(s) with that location");
            return null;
        }
    }


    // return all reports that were written by passed in author
    public static ArrayList<WaterReport> getReportsByAuthor(String author) throws Exception {
        try {
            return dbHelper.getReportsByAuthor(author);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            Log.e("No Reports Found", "No reports have been written by that user");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Unknown Error", "Could not retrieve report(s) written by that user");
            return null;
        }
    }


    // Returns true if the water location report already exists
    public static boolean isValidReport(WaterReport report) throws Exception {
        return dbHelper.isReport(report);
    }
}
