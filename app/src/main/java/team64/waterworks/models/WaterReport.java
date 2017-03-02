package team64.waterworks.models;

import android.location.Location;

import java.util.Date;
/**
 * anna
 * 3/2/17
 */

public class WaterReport {
    private static int numReports;

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
        this(new Date(), location author, type, condition);
    }


    private WaterReport(Location location, Date date, String author, String type, String condition) {
        this.location = location;
        this.date = date;
        this.id = numReports++;
        this.author = author;
        this.type = type;
        this.condition = condition;
    }

    /**
     * get location
     * @return location
     */
    public Location getLocation() {
        return location;
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
    public Date getDate() {
        return date;
    }

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
        return id;
    }

    /**
     * set ID
     * @param id new id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * get author
     * @return author
     */
    public String getAuthor() {
        return author;
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
        return type;
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
        return condition;
    }

    /**
     * set condition
     * @param condition new condition
     */
    public void setCondition(String condition) {
        this.condition = condition;
    }
}
