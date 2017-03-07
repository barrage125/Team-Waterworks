package team64.waterworks.models;
import android.content.Context;
import android.location.Location;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;


public class ReportsManager {
    private static DBHelper dbHelper;

    /**
     * set a new DBHelper for AllReports SQLite DB
     * @param c Context of the caller
     */
    public static void setDBHelper(Context c) {
        dbHelper = new DBHelper(c, 1);
    }


    /**
     * Create a new Water Report and add it to AllReports SQLite DB
     * @param location location of water you're reporting
     * @param author user creating the report
     * @param type type of water report (historical, location, or purity)
     * @param condition condition of the water on a scale of health risk, poor, ok, great, pristine
     * @return if the report was created successfully or not
     */
    public static boolean newReport(Location location, String author, String type, String condition) {
        WaterReport report = new WaterReport(, location, author, type, condition);

        // Checks if water location report already exists
//        if (type.equals("location")) {
            try {
                if (location != null) {
                    if (isValidReport(report)) {
                        Log.e("Report Exists", "A water location report for that location already exists!");
                        return false;
                    }
                }
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
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Unknown error", "Report may or may not be saved");
        }
        return false;
    }

    /**
     * Edit the passed in report and saves its new values into SQLite
     * @param report the report to be edited and saved
     * @return if the report was successfully edited and saved in SQLite
     */
    public static boolean editReport(WaterReport report) {
        try {
            dbHelper.updateReport(report);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Location invalid", "Could not retrieve location string");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Unknown error", "Could not edit report, report may or may not be updated");
        }
        return false;
    }

    /**
     * Lookup report that corresponds to the passed in ID
     * @param id id of the report looking up
     * @return report that corresponds to the passed in ID
     */
    public static WaterReport getReportByID(long id) {
        try {
            return dbHelper.getReportByID(id);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            Log.e("Loc Deserialize Error", "Location for found report couldn't be deserialized");
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            Log.e("Report Not Found", "No report could be found by that ID");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.e("Illegal ConstructorCall", "An illegal class called the WaterReport constructor");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Unknown error", "Could not retrieve report with that ID");
        }
        return null;
    }

    /**
     * Create ArrayList of all reports in SQLite that correspond to the passed in location object
     * @param location location of the reports needed
     * @return ArrayList of reports that correspond to passed in location
     * @throws IOException when location object passed in
     * @throws NoSuchElementException
     * @throws Exception
     */
    public static ArrayList<WaterReport> getReportsByLocation(Location location) throws Exception {
        try {
            return dbHelper.getReportsByLocation(location);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Loc Serialize Error", "Could not convert location to string for querying db");
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            Log.e("No Reports Found", "No reports matched that location");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Unknown Error", "Could not retrieve report(s) with that location");
        }
        return null;
    }

    /**
     * Creates array list of reports written by the corresponding author
     * @param author author searching for
     * @return array list of reports written by the passed in author
     */
    public static ArrayList<WaterReport> getReportsByAuthor(String author) {
        try {
            return dbHelper.getReportsByAuthor(author);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            Log.e("No Reports Found", "No reports have been written by that user");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Unknown Error", "Could not retrieve report(s) written by that user");
        }
        return null;
    }

    /**
     * Creates array list of all reports stored in AllReports SQLite DB
     * @return array list of all reports
     */
    public static ArrayList<WaterReport> viewAllReports() {
        try {
            return dbHelper.getAllReports();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            Log.e("No Reports Found", "No reports have been submitted yet");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Unknown Error", "Could not retrieve report(s)");
        }
        return null;
    }

    /**
     * Checks to see if passed in report already exists
     * @param report report to check
     * @return if the water location report already exists
     */
    private static boolean isValidReport(WaterReport report) {
        try {
            return dbHelper.isReport(report);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Loc Serialize Error", "Could not convert location to string for querying db");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Unknown Error", "Could not retrieve report(s) written by that user");
        }
        return true;
    }
}
