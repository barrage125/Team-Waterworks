package team64.waterworks.models;
import android.content.Context;
import java.util.ArrayList;


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
    public static boolean newPurityReport() {
        return true;
    }

    /**
     * Edits the passed in purity report and saves its new values into SQLite
     */
    public static boolean editPurityReport(WaterPurityReport report) {
        return true;
    }

    /**
     * Lookup purity report that corresponds to the passed in ID
     */
    public static WaterPurityReport getPurityReportByID(long id) {
        return null;
    }

    /**
     * Create ArrayList of all purity reports in SQLite that correspond to the passed in location object
     */
    public static ArrayList<WaterPurityReport> getPurityReportsByLocation(double latitude, double longitude) throws Exception {
        return null;
    }

    /**
     * Creates array list of purity reports written by the corresponding author
     */
    public static ArrayList<WaterPurityReport> getPurityReportsByAuthor(String author) {
        return null;
    }

    /**
     * Creates array list of all water purity reports stored in AllPurityReports SQLite DB
     * @return array list of all water purity reports
     */
    public static ArrayList<String> viewAllPurityReports() {
        return null;
    }
}
