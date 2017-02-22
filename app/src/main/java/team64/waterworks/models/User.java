package team64.waterworks.models;

/**
 * anna
 * 2/22/17
 */

class User extends Account {

    /**
     * Constructor that takes all three required fields for any Account.
     *
     * @param name the User's name
     * @param username the User's UNIQUE username
     * @param pw the User's password
     */
    public User(String name, String username, String pw) {
        super(name, username, pw);
    }

}
