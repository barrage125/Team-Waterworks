package team64.waterworks.models;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class getReportByIDTest {

    /**
     * Tests the database method "getPurityReportByID"
     * @throws Exception if method fails
     */
    @Test(expected = NoSuchElementException.class)
    public void testGetPurityReportByID() throws Exception {
        //setup
        Context appContext = InstrumentationRegistry.getTargetContext();
        DBHelper dbHelper = new DBHelper(appContext, 0, 0);
        dbHelper.deleteAllWPR();

        // if there are no WPReports:
        assertEquals(null, dbHelper.getPurityReportByID(0));

        //depends on addPurityReport working
        WaterPurityReport test = new WaterPurityReport(50, 50, "bottled", "michal", 50, 50);
        dbHelper.addPurityReport(test);

        //there are WPReports in the db
        assertEquals(test, dbHelper.getPurityReportByID(0));
        assertEquals(null, dbHelper.getPurityReportByID(1));

        // the WPReport has been deleted:
        dbHelper.deleteAllWPR();
        assertEquals(null, dbHelper.getPurityReportByID(0));
    }

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("team64.waterworks", appContext.getPackageName());
    }
}
