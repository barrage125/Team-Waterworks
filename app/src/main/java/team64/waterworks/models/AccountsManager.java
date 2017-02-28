package team64.waterworks.models;

import android.util.Log;

import team64.waterworks.controllers.DBHelper;
/**
 * Edited by anna on 2/22/2017.
 */

public class AccountsManager {
    private static DBHelper dbHelper;
    private static Account activeAccount;

    /**
     * Create a new User and add it to the ratchet database
     *
     * @param name the User's name
     * @param username the User's unique username
     * @param pw the User's password
     * @return the newly created User object
     */
    public static User newUser(String name, String username, String pw) {
        if (getAccountByUsername(username) != null) {
            throw new IllegalArgumentException("This username already exists");
        }
        User newAccount = new User(name, username, pw);
        dbHelper.addUser(name, username, pw);
        return newAccount;
    }

    /**
     * Create a new Admin and add it to the ratchet database
     *
     * @param name the Admin's name
     * @param username the Admin's unique username
     * @param pw the Admin's password
     * @return the newly created Admin object
     */
    public static Admin newAdmin(String name, String username, String pw) {
        if (getAccountByUsername(username) != null) {
            throw new IllegalArgumentException("This username already exists");
        }
        Admin newAccount = new Admin(name, username, pw);
        dbHelper.addUser(name, username, pw);
        return newAccount;
    }

    /**
     * Create a new Worker and add it to the ratchet database
     *
     * @param name the Worker's name
     * @param username the Worker's unique username
     * @param pw the Worker's password
     * @return the newly created Admin object
     */
    public static User newWorker(String name, String username, String pw) {
        if (getAccountByUsername(username) != null) {
            throw new IllegalArgumentException("This username already exists");
        }
        Worker newAccount = new Worker(name, username, pw);
        dbHelper.addUser(name, username, pw);
        return newAccount;
    }


    /**
     * Create a new Manager and add it to the ratchet database
     *
     * @param name the Manager's name
     * @param username the Manager's unique username
     * @param pw the Manager's password
     * @return the newly created Manager object
     */
    public static User newManager(String name, String username, String pw) {
        if (getAccountByUsername(username) != null) {
            throw new IllegalArgumentException("This username already exists");
        }
        Manager newAccount = new Manager(name, username, pw);
        dbHelper.addUser(name, username, pw);
        return newAccount;
    }

    public static void setActiveAccount(Account new_account) {
        if (activeAccount == null) {
            Log.d("AccountsManager", "attempting to set NULL user");
        }
        activeAccount = new_account;
    }

    public static Account getActiveAccount() {
        return activeAccount;
    }

    public static void clearActiveAccount() {
        activeAccount = null;
    }

}
