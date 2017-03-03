package team64.waterworks.models;

import android.content.Context;
import android.location.Location;

import java.util.Collection;
/**
 * anna
 * 3/2/17
 */

public class ReportsManager {
    private static DBHelper dbHelper;

    public static void setDBHelper(Context c) {
        dbHelper = new DBHelper(c, 1);
    }

    public static boolean newReport(Location location, String author, String type, String condition) {
        WaterReport report = new WaterReport(location, author, type, condition);
        if (isValidReport(report)) {
            throw new IllegalArgumentException("This report already exists");
        }

        if (dbHelper.addReport(report)) {
            return true;
        }

        return false;
    }


    public static boolean editReport(WaterReport report, Location location, String author, String type, String condition) {
        dbHelper.updateReport(...)
        return true;
    }


    public static WaterReport getReportByID(int id) {
        //todo: dbHelper.getReportByID(id)
        return null;
    }

    public static Collection<WaterReport> getReportsByLocation(Location location) {
        //todo: dbHelper.getReportsByLocation(location);
        return null;
    }

    public static Collection<WaterReport> getReportsByAuthor(String username) {
        //todo: dbHelper.getReportsByAuthor(username);
        return null;
    }

    public static boolean isValidReport(WaterReport report) {
        return dbHelper.isReport(report);
    }
}
