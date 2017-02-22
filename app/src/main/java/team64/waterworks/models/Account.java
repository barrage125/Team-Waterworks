package team64.waterworks.models;

/**
 * Created by michal on 2/13/17.
 *
 * Edited by anna on 2/20/2017.
 */

abstract class Account {
    private String name, username, password;
    private Profile profile;

    Account(String name, String username, String pw) {
        this.name = name;
        this.username = username;
        this.password = pw;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }
}
