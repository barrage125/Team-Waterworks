package team64.waterworks.models;

/**
 * anna
 * 2/22/17
 */

public class Profile {
    private String title, address, email, birthday;

    Profile() {
        title = "";
        address = "";
        email = "";
        birthday = "";
    }

    Profile(String title, String address, String email, String birthday) {
        this.title = title;
        this.address = address;
        this.email = email;
        this.birthday = birthday;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
