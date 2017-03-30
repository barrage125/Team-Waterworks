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
    private final String author;
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
    public WaterPurityReport(double latitude, double longitude, String condition, String author, long vppm, long cppm) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.condition = condition;
        this.author = author;
        this.virusPPM = vppm;
        this.contamPPM = cppm;

        this.id = 0;
        this.date = df.format(Calendar.getInstance().getTime());
    }

    /**
     * Second constructor takes in all instance vars in this order:
     * WaterPurityReport(ID, latitude, longitude, author, type, condition, virus_ppm, contam_ppm, date)
     */
    public WaterPurityReport(long id, double latitude, double longitude,
                             String author, String condition,
                             long virusPPM, long contamPPM, String date) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.condition = condition;
        this.author = author;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WaterPurityReport that = (WaterPurityReport) o;

        if (Double.compare(that.latitude, latitude) != 0) return false;
        if (Double.compare(that.longitude, longitude) != 0) return false;
        if (virusPPM != that.virusPPM) return false;
        if (contamPPM != that.contamPPM) return false;
        if (condition != null ? !condition.equals(that.condition) : that.condition != null)
            return false;
        if (author != null ? !author.equals(that.author) : that.author != null) return false;
        return date != null ? date.equals(that.date) : that.date == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(latitude);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (condition != null ? condition.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (int) (virusPPM ^ (virusPPM >>> 32));
        result = 31 * result + (int) (contamPPM ^ (contamPPM >>> 32));
        return result;
    }
}
