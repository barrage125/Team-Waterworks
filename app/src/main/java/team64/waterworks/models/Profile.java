package team64.waterworks.models;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * anna
 * 2/22/17
 */

public class Profile implements Serializable {
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
    public static Profile deserialize( String s ) throws IOException ,
            ClassNotFoundException {
        byte [] data = Base64.decode( s ,0);
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(  data ) );
        Object o  = ois.readObject();
        ois.close();
        return (Profile)o;
    }

    /** Write the object to a Base64 string. */
    public String serialize() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject(this);
        oos.close();
        return Base64.encodeToString(baos.toByteArray(),0);
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
