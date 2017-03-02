package team64.waterworks.models;

import java.io.Serializable;

/**
 * anna
 * 2/22/17
 */

public class User extends Account implements Serializable {

    /**
     * Constructor that takes all three required fields for any Account.
     *
     * @param name the User's name
     * @param username the User's UNIQUE username
     * @param pw the User's password
     */
    private Profile profile;

    public User(String name, String username, String pw)  {
        super(name, username, pw);
    }

    public User(String name, String username, String pw, Profile profile)  {
        this(name, username, pw);
        setProfile(profile);
    }

    public Profile createProfile(String title, String address, String email, String birthday) {
        profile = new Profile(title, address, email, birthday);
        return profile;
    }
}
