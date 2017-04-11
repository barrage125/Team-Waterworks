package team64.waterworks.models;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import static org.junit.Assert.*;

public class DBHelperTest {


    /**
     * Tests the database method "isAccount"
     *
     * @throws Exception
     */
    @Test
    public void testIsAccount() throws Exception {
        //setup
        Context appContext = InstrumentationRegistry.getTargetContext();
        DBHelper dbHelper = new DBHelper(appContext);
        dbHelper.deleteAllAccounts();

        //if there are no accounts:
        assertFalse(dbHelper.isAccount("anna"));

        //depends on addAccount working
        dbHelper.addAccount(new User("Anna K. Klaussen", "anna", "pass"));

        //there are accounts in the db
        assertTrue(dbHelper.isAccount("anna"));
        assertFalse(dbHelper.isAccount("admin"));

        //an account has been deleted:
        dbHelper.deleteAllAccounts();
        assertFalse(dbHelper.isAccount("anna"));
    }

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("team64.waterworks", appContext.getPackageName());
    }

}