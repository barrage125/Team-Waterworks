package team64.waterworks.models;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class WaterPurityReport {

    /**** INSTANCE VARIABLES ****/
    private long id;
    private double latitude, longitude;
    private String condition;
    private final String author, type;
    private final String date;

    private long virusPPM, contamPPM;



    /**** CLASS VARIABLES ****/
    private static final DateFormat df = new SimpleDateFormat("MM/dd/yyy HH:mm");


       /******************/
      /** CONSTRUCTORS **/
     /******************/
    /**
    * First constructor takes in latitude, longitude, condition, author, type
    */
    public WaterPurityReport(double latitude, double longitude, String condition, String author, String type) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.condition = condition;
        this.author = author;
        this.type = type;

        this.id = 0;
        this.date = df.format(Calendar.getInstance().getTime());
    }

    /**
     * Second constructor takes in all instance vars in this order:
     * WaterPurityReport(ID, latitude, longitude, author, type, condition, virus_ppm, contam_ppm, date)
     */
    public WaterPurityReport(long id, double latitude, double longitude,
                             String author, String type, String condition,
                             long virusPPM, long contamPPM, String date) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.condition = condition;
        this.author = author;
        this.type = type;
        this.date = date;
        this.virusPPM = virusPPM;
        this.contamPPM = contamPPM;
    }

       /*************/
      /** METHODS **/
     /*************/
    /**
     * Get location as a string for storing in the AllSourceReports db
     * @return location as string "latitude:longitude"
     */
    static String storeLocation(double latitude, double longitude) {
        String location = Double.toString(latitude) + ":" + Double.toString(longitude);
        return location;
    }

    /**
     * Grab latitude from location string in AllSourceReports DB
     * used when creating a report object from AllSourceReports DB
     * @param loc report's location as a string
     * @return a double array of the latitude and longitude coordinates
     */
    static double loadLatitude(String loc) {
        String[] coords = loc.split(":");
        return Double.parseDouble(coords[0]);
    }

    /**
     * Grab longitude from location string in AllSourceReports DB
     * used when creating a report object from AllSourceReports DB
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
     * For every instance var
     */

    /**
     * get a report's ID
     * @return ID
     */
    public long getId() {
        return id;
    }

    /**
     * get a report's latitude
     * @return latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * get a report's longitude
     * @return longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * get a report's condition
     * @return condition
     */
    public String getCondition() {
        return condition;
    }

    /**
     * get a report's author
     * @return author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * get a report's type
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * get a report's date
     * @return date
     */
    public String getDate() {
        return date;
    }

    /**
     * get a report's virus ppm
     * @return virus ppm
     */
    public long getVirusPPM() {
        return virusPPM;
    }

    /**
     * get a report's contaminant ppm
     * @return contaminant ppm
     */
    public long getContamPPM() {
        return contamPPM;
    }

    /*************/
      /** SETTERS **/
     /*************/
    /**
     * For every instance var
     */

    /**
     * set a report's id
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * set a report's latitude
     * @param latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * set a report's longitude
     * @param longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * set a report's condition
     * @param condition
     */
    public void setCondition(String condition) {
        this.condition = condition;
    }

    /**
     * set a report's virus ppm
     * @param virusPPM
     */
    public void setVirusPPM(long virusPPM) {
        this.virusPPM = virusPPM;
    }

    /**
     * set a report's contaminant ppm
     * @param contamPPM
     */
    public void setContamPPM(long contamPPM) {
        this.contamPPM = contamPPM;
    }
}
