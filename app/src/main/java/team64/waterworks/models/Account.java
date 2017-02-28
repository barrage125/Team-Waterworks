package team64.waterworks.models;

/**
 * Created by michal on 2/13/17.
 *
 * Edited by anna on 2/20/2017.
 */

public abstract class Account {
    private String name, username, password;
    private Profile profile;
    private int ID;

    /**
     * Constructor that takes all three required fields for any Account.
     *
     * @param name the Account's name
     * @param username the Account's UNIQUE username
     * @param pw the Account's password
     */
    Account(String name, String username, String pw) {
        this.name = name;
        this.username = username;
        this.password = pw;
    }

    /**
     * Get the Profile associated with this Account, or null if there is none.
     *
     * @return the Account's Profile
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * Add or replace the Profile associated with an Account
     *
     * @param profile
     */
    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    /**
     * Get the name associated with this Account
     *
     * @return the Account's given name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the username associated with this Account
     *
     * @return the Account's unique username
     */
    public String getUsername() {
        return username;
    }

    public Profile createProfile(String title, String address, String email, String birthday) {
        profile = new Profile(title, address, email, birthday);
        return profile;
    }

    /**
     * Generate a hashcode for this Account based on the Account's unique
     * username.
     *
     * @return the Account's hashCode
     */
    @Override
    public int hashCode() {
        return this.username.hashCode();
    }
}
