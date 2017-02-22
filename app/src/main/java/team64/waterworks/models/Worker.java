package team64.waterworks.models;

/**
 * anna
 * 2/22/17
 */

class Worker extends User {

    /**
     * Constructor that takes all three required fields for any Account.
     *
     * @param name the Worker's name
     * @param username the Worker's UNIQUE username
     * @param pw the Worker's password
     */
    public Worker(String name, String username, String pw) {
        super(name, username, pw);
    }

}
