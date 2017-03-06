package team64.waterworks.models;


class User extends Account {

    /**
     * Constructor that takes all three required fields for any Account.
     * @param name the User's name
     * @param username the User's UNIQUE username
     * @param pw the User's password
     */
    User(String name, String username, String pw)  {
        super(name, username, pw);
    }

    /**
     * User Constructor that can additionally take in a profile paramater
     * @param name user's name
     * @param username user's username
     * @param pw user's password
     * @param profile user's profile
     */
    User(String name, String username, String pw, Profile profile)  {
        this(name, username, pw);
        setProfile(profile);
    }
}
