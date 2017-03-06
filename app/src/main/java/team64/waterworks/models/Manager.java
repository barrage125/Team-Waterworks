package team64.waterworks.models;


class Manager extends Worker {

    /**
     * Constructor that takes all three required fields for any Account.
     * @param name the Manager's name
     * @param username the Manager's UNIQUE username
     * @param pw the Manager's password
     */
    Manager(String name, String username, String pw) {
        super(name, username, pw);
    }

}
