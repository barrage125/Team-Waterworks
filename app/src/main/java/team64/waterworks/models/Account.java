package team64.waterworks.models;

/**
 * Created by michal on 2/13/17.
 */

abstract class Account {
    private String name, username, password;

    Account(String name, String username, String pw) {
        this.name = name;
        this.username = username;
        this.password = pw;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }
}
