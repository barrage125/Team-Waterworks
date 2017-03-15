package team64.waterworks.models;


class Admin extends Account {

    /**
     * Constructor that takes all three required fields for any Account.
     * @param name the Admin's name
     * @param username the Admin's UNIQUE username
     * @param pw the Admin's password
     */
    Admin(String name, String username, String pw) {
        super(name, username, pw);
        this.setAuthLevel("admin");
    }

    /**
     * Admin Constructor that can take in additional profile parameter
     * @param name admin's name
     * @param username admin's username
     * @param pw admin's password
     * @param profile admin's profile
     */
    Admin(String name, String username, String pw, Profile profile)  {
        this(name, username, pw);
        setProfile(profile);
    }

}
