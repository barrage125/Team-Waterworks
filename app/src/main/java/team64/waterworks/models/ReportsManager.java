package team64.waterworks.models;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import java.io.IOException;
import java.util.Collection;
import java.util.NoSuchElementException;

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

        try {
            dbHelper.addReport(report);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Location invalid", "Could not retrieve location string");
            return false;
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            Log.e("No such report", "Water Report id can't be set, it wasn't found in db");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Unknown error", "Report may or may not be saved");
            return false;
        }
    }


    public static boolean editReport(WaterReport report, Location location, String author, String type, String condition) {
        //dbHelper.updateReport(...)
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
        //return dbHelper.isReport(report);
        return true;
    }
}
