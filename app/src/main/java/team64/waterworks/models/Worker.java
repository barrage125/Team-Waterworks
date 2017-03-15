package team64.waterworks.models;


class Worker extends User {

    /**
     * Constructor that takes all three required fields for any Account.
     * @param name the Worker's name
     * @param username the Worker's UNIQUE username
     * @param pw the Worker's password
     */
    Worker(String name, String username, String pw) {
        super(name, username, pw);
        this.setAuthLevel("worker");
    }

    /**
     * Worker Constructor that can take in additional profile parameter
     * @param name worker's name
     * @param username worker's username
     * @param pw worker's password
     * @param profile worker's profile
     */
    Worker(String name, String username, String pw, Profile profile)  {
        this(name, username, pw);
        setProfile(profile);
    }

}
