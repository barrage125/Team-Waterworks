package team64.waterworks.models;


public abstract class Account {
    private String name, username, password;
    private Profile profile;

    /**
     * Constructor that takes all three required fields for any Account.
     * @param name the Account's name
     * @param username the Account's UNIQUE username
     * @param pw the Account's password
     */
    Account(String name, String username, String pw) {
        this.name = name;
        this.username = username;
        this.password = pw;
        this.profile = new Profile("", "", "", "");
    }

    /**
     * Get the Profile associated with this Account, or null if there is none.
     * @return the Account's Profile
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * Creates a new profile
     * @param title title of profile
     * @param address account's address
     * @param email account's email
     * @param birthday account's birthday
     * @return new Profile object created
     */
    public Profile createProfile(String title, String address, String email, String birthday) {
        profile = new Profile(title, address, email, birthday);
        return profile;
    }

    /**
     * Add or replace the Profile associated with an Account
     * @param profile new profile
     */
    void setProfile(Profile profile) {
        this.profile = profile;
    }

    /**
     * Get the name associated with this Account
     * @return the Account's given name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the username associated with this Account
     * @return the Account's unique username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get password associated with this account
     * @return the account's password
     */
    public String getPassword() { return password; }

    /**
     * Generate a hashcode for this Account based on the Account's unique
     * username.
     * @return the Account's hashCode
     */
    @Override
    public int hashCode() {
        return this.username.hashCode();
    }
}
