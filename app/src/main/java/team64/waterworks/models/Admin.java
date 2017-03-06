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
    }

}
