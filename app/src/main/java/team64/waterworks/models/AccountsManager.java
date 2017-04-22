package team64.waterworks.models;
import android.content.Context;
import android.util.Log;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class AccountsManager {
    private static DBHelper dbHelper;
    private static Account activeAccount;

    /**
     * Set a new DBHelper for AllAccounts SQLite DB
     * @param c Context of the caller
     */
    public static void setDBHelper(Context c) {
        dbHelper = new DBHelper(c);
    }

    /**
     * Create a new account and add it to AllAccounts SQLite DB
     * @param username account's unique username
     * @param pw account's password
     * @param auth_level account's authority level (user, worker, manager, admin)
     * @return if account was successfully added or not
     */
    public static boolean newAccount(String username, String pw, String auth_level) {
        // Checks if username is available
        if (isValidAccount(username)) {
            Log.e("Account exists", "An account with that username already exists!");
            return false;
        }

        // Create account object for corresponding authority level
        Account account;
        switch (auth_level) {
            case "user":
                account = new User("", username, pw);
                break;
            case "worker":
                account = new Worker("", username, pw);
                break;
            case "manager":
                account = new Manager("", username, pw);
                break;
            case "admin":
                account = new Admin("", username, pw);
                break;
            default:
                Log.e("Invalid Auth Level", "The passed in authority level isn't a valid auth level!");
                return false;
        }

        // Add the new account
        try {
            dbHelper.addAccount(account);
            setActiveAccount(account);
            return true;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Log.e("Unable to Hash Password", "Algorithm for hashing password not found!");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Unknown Error", "Account may or may not be saved.");
            return false;
        }
    }

    /*
      Create a new Admin and add it to AllAccounts SQLite DB
      @param name the Admin's name
     * @param username the Admin's unique username
     * @param pw the Admin's password
     * @return if admin was added successfully or not
     */
//    public static boolean newAdmin(String name, String username, String pw) {
//        // Checks if username is available
//        if (isValidAccount(username)) {
//            Log.e("Account exists", "An account with that username already exists!");
//            return false;
//        }
//
//        // Add the new admin
//        Admin admin = new Admin(name, username, pw);
//        try {
//            dbHelper.addAccount(admin);
//            setActiveAccount(admin);
//            return true;
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//            Log.e("Unable to Hash Password", "Algorithm for hashing password not found!");
//            return false;
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e("Unknown Error", "Admin may or may not be saved.");
//            return false;
//        }
//    }

    /*
      Create a new Worker and add it to AllAccounts SQLite DB
      @param name the Worker's name
     * @param username the Worker's unique username
     * @param pw the Worker's password
     * @return if worker was successfully added or not
     */
//    public static boolean newWorker(String name, String username, String pw) {
//        // Checks if username is available
//        if (isValidAccount(username)) {
//            Log.e("Account exists", "An account with that username already exists!");
//            return false;
//        }
//
//        Worker worker = new Worker(name, username, pw);
//        try {
//            dbHelper.addAccount(worker);
//            setActiveAccount(worker);
//            return true;
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//            Log.e("Unable to Hash Password", "Algorithm for hashing password not found!");
//            return false;
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e("Unknown Error", "Worker may or may not be saved.");
//            return false;
//        }
//    }

    /*
      Create a new Manager and add it to AllAccounts SQLite DB
      @param name the Manager's name
     * @param username the Manager's unique username
     * @param pw the Manager's password
     * @return if manager was successfully added or not
     */
//    public static boolean newManager(String name, String username, String pw) {
//        // Checks if username is available
//        if (isValidAccount(username)) {
//            Log.e("Account exists", "An account with that username already exists!");
//            return false;
//        }
//
//        Manager manager = new Manager(name, username, pw);
//        try {
//            dbHelper.addAccount(manager);
//            setActiveAccount(manager);
//            return true;
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//            Log.e("Unable to Hash Password", "Algorithm for hashing password not found!");
//            return false;
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e("Unknown Error", "Manager may or may not be saved.");
//            return false;
//        }
//    }

    /**
     * Finds account that corresponds to passed in username/password combo
     * @param username username looking for
     * @param password password looking for
     * @return Account with specified username/password combo
     */
    public static Account getAccountWithCreds(String username, String password) {
        try {
            setActiveAccount(dbHelper.AccountWithCreds(username, password));
            return dbHelper.AccountWithCreds(username, password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Log.e("Unable to Hash Password", "Algorithm for hashing password not found!");
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            Log.e("Profile Serialize Error", "Failed to loadLocation profile");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Unknown Error", "Couldn't find an account with those credentials!");
        }
        clearActiveAccount();
        return null;
    }

    public static Account findAccount(String username) {
        try {
            return dbHelper.findAccount(username);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Log.e("Unable to Hash Password", "Algorithm for hashing password not found!");
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            Log.e("Profile Serialize Error", "Failed to loadLocation profile");
            Log.e("Profile Serialize Error", "Failed to loadLocation profile");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Unknown Error", "Couldn't find an account with those credentials!");
        }
        return null;
    }

    /**
     * Edits passed in account and saves its new values to AllAccounts SQLite DB
     * @param account the account to be edited and saved
     * @return if the account was successfully edited or not
     */
    public static boolean editAccount(Account account) {
        try {
            dbHelper.updateAccount(account);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Profile Serialize Error", "Failed to storeLocation profile");
        }
        return false;
    }

    /**
     * Sets an instance of the current active/logged in account
     * @param new_account newly active account
     */
    public static void setActiveAccount(Account new_account) {
        if (new_account == null) {
            Log.e("Null Active Account", "Can't set the currently logged in account to null!");
        }
        activeAccount = new_account;
    }

    /**
     * Gets currently logged in account
     * @return account currently active
     */
    public static Account getActiveAccount() {
        return activeAccount;
    }

    /**
     * Clears the currently logged in account (when logging account)
     */
    public static void clearActiveAccount() {
        activeAccount = null;
    }

    /**
     * Checks if an account with the corresponding username exists
     * @param username username of account
     * @return if account with passed in username exists or not
     */
    public static boolean isValidAccount(String username) {
        return dbHelper.isAccount(username);
    }

}
