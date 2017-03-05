package team64.waterworks.models;

import android.location.Location;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
/**
 * anna
 * 3/2/17
 */

public class WaterReport {
    private Date date;
    private int id;
    private Location location;
    private String author;
    private String type;
    private String condition;

    /**
     * Create a new water report.
     * @param location location of report
     * @param author author of report
     * @param type type of water
     * @param condition condition of water
     */
    public WaterReport(Location location, String author, String type, String condition) {
        this(location, new Date(), author, type, condition);
    }


    private WaterReport(Location location, Date date, String author, String type, String condition) {
        this.location = location;
        this.date = date;
        this.id = 0;
        this.author = author;
        this.type = type;
        this.condition = condition;
    }


    /**
     * get location
     * @return location
     */
    public Location getLocation() {
        return this.location;
    }

    /**
     * get location as a string
     * @return location string
     * Should be called on report object, ex:
     * WaterReport report = new WaterReport();
     * report.getLocationString();
     */
    public String getLocationAsString() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(this.location);
        oos.close();
        return Base64.encodeToString(baos.toByteArray(),0);
    }


    // Turn string into Location object
    public static Location deserialize(String s) throws IOException, ClassNotFoundException {
        byte [] data = Base64.decode(s ,0);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        Object o  = ois.readObject();
        ois.close();
        return (Location) o;
    }


    /**
     * set location
     * @param location new location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * @return date
     */
    public Date getDate() { return this.date; }

    /**
     * Set date
     * @param date new date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * get ID
     * @return id
     */
    public int getId() {
        return this.id;
    }

    /**
     * set ID
     * @param id new id
     */
    public void setId(int id) { this.id = id; }

    /**
     * get author
     * @return author
     */
    public String getAuthor() {
        return this.author;
    }

    /**
     * set author
     * @param author new author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * get type
     * @return type
     */
    public String getType() {
        return this.type;
    }

    /**
     * set type
     * @param type new type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * get condition
     * @return condition
     */
    public String getCondition() {
        return this.condition;
    }

    /**
     * set condition
     * @param condition new condition
     */
    public void setCondition(String condition) {
        this.condition = condition;
    }
}
