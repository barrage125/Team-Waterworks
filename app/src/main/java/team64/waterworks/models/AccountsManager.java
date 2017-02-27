package team64.waterworks.models;

import java.util.HashSet;

public class AccountsManager {
    private HashSet<Account> allAccounts;

    /**
     * Create a new User and add it to the ratchet database
     *
     * @param name the User's name
     * @param username the User's unique username
     * @param pw the User's password
     * @return the newly created User object
     */
    public User newUser(String name, String username, String pw) {
        if (getAccountByUsername(username) != null) {
            throw new IllegalArgumentException("This username already exists");
        }
        User newAccount = new User(name, username, pw);
        allAccounts.add(newAccount);
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
    public Admin newAdmin(String name, String username, String pw) {
        if (getAccountByUsername(username) != null) {
            throw new IllegalArgumentException("This username already exists");
        }
        Admin newAccount = new Admin(name, username, pw);
        allAccounts.add(newAccount);
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
    public User newWorker(String name, String username, String pw) {
        if (getAccountByUsername(username) != null) {
            throw new IllegalArgumentException("This username already exists");
        }
        Worker newAccount = new Worker(name, username, pw);
        allAccounts.add(newAccount);
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
    public User newManager(String name, String username, String pw) {
        if (getAccountByUsername(username) != null) {
            throw new IllegalArgumentException("This username already exists");
        }
        Manager newAccount = new Manager(name, username, pw);
        allAccounts.add(newAccount);
        return newAccount;
    }

    /**
     * Gets an account out of the database with the given username
     *
     * @param username the username to search for
     * @return the Account object
     */
    public Account getAccountByUsername(String username) {

        for (Account account : allAccounts) {
            if (account.getUsername().equals(username)) {
                return account;
            }
        }
        return null;
    }
}
