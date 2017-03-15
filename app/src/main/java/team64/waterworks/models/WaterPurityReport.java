package team64.waterworks.models;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class WaterPurityReport {

    /**** INSTANCE VARIABLES ****/
    // id, latitude, longitude, condition, author, type, date, virus_ppm, contam_ppm


    /**** CLASS VARIABLES ****/
    private static final DateFormat df = new SimpleDateFormat("MM/dd/yyy HH:mm");


       /******************/
      /** CONSTRUCTORS **/
     /******************/
    /**
    * First constructor takes in latitude, longitude, condition, author, type
    */

    /**
     * Second constructor takes in all instance vars in this order:
     * WaterPurityReport(ID, latitude, longitude, author, type, condition, virus_ppm, contam_ppm, date)
     */


       /*************/
      /** METHODS **/
     /*************/
    /**
     * Get location as a string for storing in the AllSourceReports db
     * @return location as string "latitude:longitude"
     */
//    static String storeLocation(double latitude, double longitude) {
//        String location = Double.toString(latitude) + ":" + Double.toString(longitude);
//        return location;
//    }
//
//    /**
//     * Grab latitude from location string in AllSourceReports DB
//     * used when creating a report object from AllSourceReports DB
//     * @param loc report's location as a string
//     * @return a double array of the latitude and longitude coordinates
//     */
//    static double loadLatitude(String loc) {
//        String[] coords = loc.split(":");
//        return Double.parseDouble(coords[0]);
//    }
//
//    /**
//     * Grab longitude from location string in AllSourceReports DB
//     * used when creating a report object from AllSourceReports DB
//     * @param loc report's location as a string
//     * @return a double array of the latitude and longitude coordinates
//     */
//    static double loadLongitude(String loc) {
//        String[] coords = loc.split(":");
//        return Double.parseDouble(coords[1]);
//    }


       /*************/
      /** GETTERS **/
     /*************/
    /**
     * For every instance var
     */

//    public long getContamPPM() { }
//    public long getVirusPPM() { }


       /*************/
      /** SETTERS **/
     /*************/
    /**
     * For every instance var
     */

}
