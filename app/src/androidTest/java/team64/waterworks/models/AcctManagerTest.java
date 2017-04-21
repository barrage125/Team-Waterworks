package team64.waterworks.models;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Created by lindsayknapp on 4/11/17.
 */

public class AcctManagerTest {

    /**
     * Tests the database method "newAccount"
     *
     * @throws Exception if addAccount method fails
     */
    @Test
    public void testNewAccount() throws Exception {
            //setup
        Context appContext = InstrumentationRegistry.getTargetContext();
        DBHelper dbHelper = new DBHelper(appContext);
        AccountsManager acctManager = new AccountsManager();
        acctManager.clearActiveAccount();

        dbHelper.deleteAllAccounts();

        //if there are no accounts:
        assertFalse(acctManager.newAccount("lck12", "pass", "user"));

        dbHelper.addAccount(new User("lck12", "pass", "user"));


            //depends on addAccount working
        acctManager.newAccount("lck12", "pass", "user");

            //there are accounts in the db
        assertTrue(acctManager.newAccount("lck12", "pass", "user"));
        assertFalse(acctManager.newAccount("lindsay", "password", "admin"));

            //an account has been deleted:
        acctManager.clearActiveAccount();
        assertFalse(acctManager.newAccount("lck12", "pass", "user"));
        }

        @Test
        public void useAppContext() throws Exception {
            // Context of the app under test.
            Context appContext = InstrumentationRegistry.getTargetContext();

            assertEquals("team64.waterworks", appContext.getPackageName());
    }

}
