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
        dbHelper = new DBHelper(c);
    }

    public static boolean newReport(Location location, String author, String type, String condition) {
        WaterReport report = new WaterReport(location, author, type, condition);
        //todo: dbHelper.addReport(report);
        return true;
    }

    public static boolean editReport(int id, Location location, String author, String type, String condition) {
        //todo: dbHelper.updateReport(...)
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
}
