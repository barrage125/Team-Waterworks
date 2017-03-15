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


public class WaterSourceReport implements Serializable {

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
    WaterSourceReport(double latitude, double longitude, String author, String type, String condition) {
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
     * @param date when report was originally posted
     */
    WaterSourceReport(long ID, double latitude, double longitude, String author, String type, String condition,
                      int user_rating, String date) {
        this.id = ID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.author = author;
        this.type = type;
        this.condition = condition;
        this.user_rating = user_rating;
        this.date = date;
    }


       /*************/
      /** METHODS **/
     /*************/
    /**
     * Get location as a string for storing in the AllReports db
     * @return location as string "latitude:longitude"
     */
    static String storeLocation(double latitude, double longitude) {
        String location = Double.toString(latitude) + ":" + Double.toString(longitude);
        return location;
    }

    /**
     * Grab latitude from location string in AllReports DB
     * used when creating a report object from AllReports DB
     * @param loc report's location as a string
     * @return a double array of the latitude and longitude coordinates
     */
    static double loadLatitude(String loc) {
        String[] coords = loc.split(":");
        return Double.parseDouble(coords[0]);
    }

    /**
     * Grab longitude from location string in AllReports DB
     * used when creating a report object from AllReports DB
     * @param loc report's location as a string
     * @return a double array of the latitude and longitude coordinates
     */
    static double loadLongitude(String loc) {
        String[] coords = loc.split(":");
        return Double.parseDouble(coords[1]);
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
    public double getLatitude() {
        return this.latitude;
    }

    /**
     * Get longitude of water location in report
     * @return longitude of water location
     */
    public double getLongitude() {
        return this.longitude;
    }

    /**
     * Get condition of water in report
     * @return condition of water
     */
    public String getCondition() {
        return this.condition;
    }

    /**
     * Get user rating of water body in report
     * @return user rating
     */
    public int getRating() { return this.user_rating; }

    /**
     * Get user who initially submitted water report
     * @return author of water report
     */
    public String getAuthor() {
        return this.author;
    }

    /**
     * Get type of water report (historical, location, purity)
     * @return type of water report
     */
    public String getType() {
        return this.type;
    }

    /**
     * Get date water report was initially submitted
     * @return date
     */
    public String getDate() { return this.date; }


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
