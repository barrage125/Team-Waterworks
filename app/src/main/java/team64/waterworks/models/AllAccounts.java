package michal.waterworks.models;

import java.util.ArrayList;
/**
 * Created by michal on 2/13/17.
 *
 * Edited by anna on 2/22/2017.
 */

public class AllAccounts {
    private ArrayList<Account> allAccounts;

    public User newUser(String name, String username, String pw) {
        User newAccount = new User(name, username, pw);
        allAccounts.add(newAccount);
        return newAccount;
    }

    public Admin newAdmin(String name, String username, String pw) {
        Admin newAccount = new Admin(name, username, pw);
        allAccounts.add(newAccount);
        return newAccount;
    }

    public User newWorker(String name, String username, String pw) {
        Worker newAccount = new Worker(name, username, pw);
        allAccounts.add(newAccount);
        return newAccount;
    }

    public User newManager(String name, String username, String pw) {
        Manager newAccount = new Manager(name, username, pw);
        allAccounts.add(newAccount);
        return newAccount;
    }

    public Account getAccountByUsername(String username) {
        //using streams, iterate allAccounts until the account has been found or not
        return null;
    }

}
