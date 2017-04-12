package team64.waterworks;

import org.junit.Test;

import team64.waterworks.models.AccountsManager;

import static org.junit.Assert.*;

/**
 * Created by Alexander on 4/12/2017.
 */

public class IsValidPasswordUnitTest {
    @Test
    public void standardPasswordIsCorrect() throws Exception {
        assertEquals(true, AccountsManager.isValidPassword("GPBurdell1927"));
    }

    @Test
    public void noUpperPaswordIsIncorrect() throws Exception {
        assertEquals(false, AccountsManager.isValidPassword("gpburdell1927"));
    }

    @Test
    public void noLowerPaswordIsIncorrect() throws Exception {
        assertEquals(false, AccountsManager.isValidPassword("GPBURDELL1927"));
    }

    @Test
    public void noNumberPaswordIsIncorrect() throws Exception {
        assertEquals(false, AccountsManager.isValidPassword("GPBurdell"));
    }
}
