package team64.waterworks.models;

import android.location.Location;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WaterReport {
    private long id;
    private Location location;
    private String condition;
    private int user_rating;

    // As far as I can tell, there are no cases where these should be changed
    // The report is first submitted by this author, that doesn't change
    // You wouldn't change a report type when editing a report, that would change all of it's data
    // The report is first submitted as a certain date, that doesn't change (shouldn't rewrite history)
    private final String author;
    private final String type;
    private final String date;

    // Date formatting instance data, only used for constructor
    private DateFormat df = new SimpleDateFormat("MM/dd/yyy HH:mm");
    Date today;


    /**
     * Create a new water report.
     * @param location location of report
     * @param author author of report
     * @param type type of water report
     * @param condition condition of water based on water purity report
     */
    public WaterReport(Location location, String author, String type, String condition) {
        this.location = location;
        this.condition = condition;

        this.author = author;
        this.type = type;

        // set to 0 by default bc real values not available yet
        this.id = 0;
        this.user_rating = 0;

        // Set the date
        this.today = Calendar.getInstance().getTime();
        this.date = df.format(today);
    }

    // Constructor used ONLY by DBHelper, SHOULD NOT BE CALLED OTHERWISE

    public WaterReport(long ID, Location location, String author, String type, String condition, int user_rating, String date) throws Exception {
        // condition check to make sure only DBhelper calls this constructor
        // users shouldn't be able to set final attributes like author, type, and date
        if (getClass().equals("class java.team64.waterworks.models.DBHelper")) {
            this.id = ID;
            this.location = location;
            this.author = author;
            this.type = type;
            this.condition = condition;
            this.user_rating = user_rating;
            this.date = date;
        } else {
            throw new IllegalAccessException();
        }
    }


    /**
     * get location as a string
     * @return location string
     */
    public static String getLocationAsString(Location report_location_obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(report_location_obj);
        oos.close();
        return Base64.encodeToString(baos.toByteArray(),0);
    }


    // Turn string into Location object
    // Made serialization and deseralization Base64 because that's the only kind I know how to do
    // Calling convention:
    // WaterReport.deserialize(report_location)
    public static Location deserialize(String s) throws IOException, ClassNotFoundException {
        byte[] data = Base64.decode(s, 0);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        Object o = ois.readObject();
        ois.close();
        return (Location) o;
    }




    /*****************************
     * GETTERS AND SETTERS
     ****************************/

    /**
     * get ID
     * @return id
     */
    public long getId() {
        return this.id;
    }

    /**
     * get location
     * @return location
     */
    public Location getLocation() {
        return this.location;
    }

    /**
     * get condition
     * @return condition
     */
    public String getCondition() {
        return this.condition;
    }

    /**
     * get user rating
     * @return user rating
     */
    public int getRating() { return this.user_rating; }

    /**
     * get author
     * @return author
     */
    public String getAuthor() {
        return this.author;
    }

    /**
     * get type
     * @return type
     */
    public String getType() {
        return this.type;
    }

    /**
     * @return date
     */
    public String getDate() { return this.date; }




    /**
     * set ID
     * @param id new id
     */
    public void setId(long id) { this.id = id; }

    /**
     * set location
     * @param location new location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * set condition
     * @param condition new condition
     */
    public void setCondition(String condition) {
        this.condition = condition;
    }

    /**
     * set user rating
     * @param rating new user rating
     */
    public void setRating(int rating) { this.user_rating = rating; }
}
