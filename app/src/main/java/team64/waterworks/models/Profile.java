package team64.waterworks.models;
import android.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


@SuppressWarnings("SameParameterValue")
public class Profile implements Serializable {
    private String title, address, email, birthday;

    /**
     * Empty Profile constructor, creates an empty profile
     */
    private Profile() {
        title = "";
        address = "";
        email = "";
        birthday = "";
    }

    /**
     * Profile constructor
     * @param title title of user (Mrs., Ms., etc)
     * @param address address of user
     * @param email user's email
     * @param birthday user's birthday
     */
    Profile(String title, String address, String email, String birthday) {
        this.title = title;
        this.address = address;
        this.email = email;
        this.birthday = birthday;
    }


    /**
     * Converts string into a Profile object
     * @param prof profile string to convert
     * @return Profile object
     * @throws IOException if IO error occurs while writing stream header
     * @throws ClassNotFoundException if Class of serialized object cannot be found
     */
    static Profile deserialize(String prof) throws IOException, ClassNotFoundException {
        if (prof.equals("")) {
            return new Profile();
        }

        byte [] data = Base64.decode(prof ,0);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        Object o  = ois.readObject();
        ois.close();
        return (Profile) o;
    }

    /**
     * Turns profile object into a string
     * @return String representation of Profile object
     * @throws IOException if IO error occurs while writing stream header
     */
    static String serialize(Profile profile) throws IOException {
        if (profile == null) {
            return "";
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(profile);
        oos.close();
        return Base64.encodeToString(baos.toByteArray(),0);
    }

    /**
     * Gets profile title
     * @return profile title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets user's address in profile
     * @return user's address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Gets user's email in profile
     * @return user's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets user's birthday in profile
     * @return user's birthday
     */
    public String getBirthday() {
        return birthday;
    }

    /**
     * Sets new title for account's profile
     * @param title new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets new address for account's profile
     * @param address new address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Sets new email for account's profile
     * @param email new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets new birthday for account's profile
     * @param birthday new birthday
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
