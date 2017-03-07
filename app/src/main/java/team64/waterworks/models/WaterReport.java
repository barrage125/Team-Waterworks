package team64.waterworks.models;
import android.location.Location;
import android.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class WaterReport implements Serializable {

     /**** INSTANCE VARIABLES ****/
    private long id;
    private double latitude;
    private double longitude;
    private String condition;
    private int user_rating;

    // Report will always be initially submitted by one author
    // Can't change report type when editing a report, that would change all of it's data
    // Report will always be initially submitted at a certain date
    private final String author;
    private final String type;
    private final String date;


     /**** CLASS VARIABLES ****/
    private static final DateFormat df = new SimpleDateFormat("MM/dd/yyy HH:mm");


       /******************/
      /** CONSTRUCTORS **/
     /******************/
    /**
     * Create a new water report.
     * @param latitude lat of the map pin (water location)
     * @param longitude longitude of the map pin (water location)
     * @param author author of report
     * @param type type of water report
     * @param condition condition of water based on water purity report
     */
    WaterReport(double latitude, double longitude, String author, String type, String condition) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.condition = condition;

        this.author = author;
        this.type = type;

        // set to 0 by default bc real values not available yet
        this.id = 0;
        this.user_rating = 0;

        // Set the date
        Date today = Calendar.getInstance().getTime();
        this.date = df.format(today);
    }

    /**
     * Water Report Constructor, used ONLY by DBHelper, SHOULD NOT BE CALLED OTHERWISE
     * Creates an instance of a Water Report that's already stored in SQLite
     * Not used for creating a new Water Report that isn't already in SQLite
     * Used when looking up a water report directly from SQLite and need and instance of that report
     * as a Water Report object
     * @param ID id of water report from water report found in SQLite
     * @param latitude lat of the map pin (water location)
     *@param longitude longitude of the map pin (water location)
     * @param author user who wrote the water report
     * @param type type of water report (historical, location, or purity)
     * @param condition condition of water
     * @param user_rating user rating of water location
     * @param date when report was originally posted      @throws IllegalAccessException when class other than DBHelper tries to call this constructor
     */
    WaterReport(long ID, double latitude, double longitude, String author, String type, String condition,
                int user_rating, String date) throws IllegalAccessException {
        // condition check to make sure only DBHelper class calls this constructor
        // users shouldn't be able to set SQLite auto generated values like id
        if (getClass().toString().equals("class java.team64.waterworks.models.DBHelper")) {
            this.id = ID;
            this.latitude = latitude;
            this.longitude = longitude;
            this.author = author;
            this.type = type;
            this.condition = condition;
            this.user_rating = user_rating;
            this.date = date;
        } else {
            throw new IllegalAccessException();
        }
    }


       /*************/
      /** METHODS **/
     /*************/
    /**
     * Get location as a string for storing in the AllReports db
     * @return location as string "latitude:longitude"
     * @throws IOException if IO error occurs while writing stream header
     */
    static String storeLocation(Location report_location_obj) throws IOException {
        if (report_location_obj == null) {
            return "";
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(report_location_obj);
        oos.close();
        return Base64.encodeToString(baos.toByteArray(),0);
    }

    /**
     * Turn string into Location object
     * WaterReport.deserialize(report_location_string)
     * @param loc report's location as a string
     * @return Location object
     * @throws IOException if IO error occurs while writing stream header
     * @throws ClassNotFoundException if Class of serialized object cannot be found
     */
    static Location deserialize(String loc) throws IOException, ClassNotFoundException {
        if (loc.equals("")) {
            return new Location("");
        }

        byte[] data = Base64.decode(loc, 0);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        Object o = ois.readObject();
        ois.close();
        return (Location) o;
    }


       /*************/
      /** GETTERS **/
     /*************/
    /**
     * Get report's ID
     * @return id of report
     */
    public long getId() {
        return this.id;
    }

    /**
     * Get latitude of water location in report
     * @return latitude of water location
     */
    double getLatitude() {
        return this.latitude;
    }

    /**
     * Get longitude of water location in report
     * @return longitude of water location
     */
    double getLongitude() {
        return this.longitude;
    }

    /**
     * Get condition of water in report
     * @return condition of water
     */
    String getCondition() {
        return this.condition;
    }

    /**
     * Get user rating of water body in report
     * @return user rating
     */
    int getRating() { return this.user_rating; }

    /**
     * Get user who initially submitted water report
     * @return author of water report
     */
    String getAuthor() {
        return this.author;
    }

    /**
     * Get type of water report (historical, location, purity)
     * @return type of water report
     */
    String getType() {
        return this.type;
    }

    /**
     * Get date water report was initially submitted
     * @return date
     */
    String getDate() { return this.date; }


       /*************/
      /** SETTERS **/
     /*************/
    /**
     * Set latitude of water location in report
     * @param latitude new latitude of water location
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Set latitude of water location in report
     * @param longitude new latitude of water location
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Set condition of water quality
     * @param condition new condition of water
     */
    public void setCondition(String condition) {
        this.condition = condition;
    }

    /**
     * Set user rating of water location
     * @param rating new user rating
     */
    public void setRating(int rating) { this.user_rating = rating; }
}
