package team64.waterworks.models;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;


@SuppressWarnings({"DanglingJavadoc", "UnusedParameters", "SameParameterValue"})
class DBHelper extends SQLiteOpenHelper {

    /**** CLASS VARIABLES ****/
    // AllUsers DB strings
    private static final String ACCOUNTS_DB_NAME = "AllAccounts";
    private static final String ACCOUNTS_DB_CREATE = "CREATE TABLE AllAccounts " +
                                                     "( _id INTEGER PRIMARY KEY, name TEXT, " +
                                                     "username TEXT, password TEXT, profile TEXT, " +
                                                     "auth_level TEXT )";
    private static final String ACCOUNTS_DB_SALT = "!*aS{f8t8$5)9asf(l";

    // AllSourceReports DB Strings
    private static final String SRC_REPORT_DB_NAME = "AllSourceReports";
    private static final String SRC_REPORT_DB_CREATE = "CREATE TABLE AllSourceReports " +
                                                       "( _id INTEGER PRIMARY KEY, location TEXT, " +
                                                       "author TEXT, type TEXT, condition TEXT, " +
                                                       "user_rating TEXT, date TEXT )";

    // AllPurityReports DB Strings
    private static final String PUR_REPORT_DB_NAME = "AllPurityReports";
    private static final String PUR_REPORT_DB_CREATE = "CREATE TABLE AllPurityReports " +
                                                       "( _id INTEGER PRIMARY KEY, location TEXT, " +
                                                       "author TEXT, type TEXT, condition TEXT, " +
                                                       "virus_ppm TEXT, contam_ppm TEXT, date TEXT )";
    private final boolean src_report_db;
    private final boolean pur_report_db;


       /******************/
      /** CONSTRUCTORS **/
     /******************/
    /**
     * Constructor for AllAccounts DB Helper
     * Creates a SQLiteOpenHelper for an account DB if it doesn't already exist
     * @param context context of caller, used in SQLiteOpenHelper constructor
     *
     */
    DBHelper(Context context) {
        super(context, ACCOUNTS_DB_NAME, null, 1);
        this.src_report_db = false;
        this.pur_report_db = false;
    }

    /**
     * Constructor for Water Source Reports DB Helper
     * Creates a SQLiteOpenHelper for a water source report DB if it doesn't already exist
     * @param context context of caller, used in SQLite parent constructor
     *
     */
    DBHelper(Context context, int switch1) {
        super(context, SRC_REPORT_DB_NAME, null, 1);
        this.src_report_db = true;
        this.pur_report_db = false;
    }

    /**
     * Constructor for Water Purity Reports DB Helper
     * Creates a SQLiteOpenHelper for a water purity report DB if it doesn't already exist
     * @param context context of caller, used in SQLite parent constructor
     *
     */
    DBHelper(Context context, int switch1, int switch2) {
        super(context, PUR_REPORT_DB_NAME, null, 1);
        this.src_report_db = false;
        this.pur_report_db = true;
    }


       /*************/
      /** METHODS **/
     /*************/
    /**
     * Executes SQL that writes passed in SQLiteDatabase to disk. Methods executes when
     * getWriteableDatabase() or getReadableDatabase() methods are called & db doesn't already exist
     * @param database database to write to disk, could be a users db or reports db
     */
    @Override
    public void onCreate(SQLiteDatabase database) {
        if (this.src_report_db) {
            database.execSQL(SRC_REPORT_DB_CREATE);
        } else if (this.pur_report_db) {
            database.execSQL(PUR_REPORT_DB_CREATE);
        } else {
            database.execSQL(ACCOUNTS_DB_CREATE);
        }
    }

    /**
     * Called when SQLite db needs to be upgraded
     * @param db db to be upgraded
     * @param oldVersion old db version
     * @param newVersion new db version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}




       /*************************/
      /** ACCOUNTS DB METHODS **/
     /*************************/
    /**
     * Adds a new account to AllAccounts SQLite DB
     * @throws NoSuchAlgorithmException if Base64 algo for hashPassword() not found
     */
    void addAccount(Account account) throws NoSuchAlgorithmException {
        SQLiteDatabase db = getWritableDatabase();

        // Create a new set of values for new account row
        ContentValues values = new ContentValues();
        values.put("name", account.getName());
        values.put("username", account.getUsername());
        values.put("password", hashPassword(account.getPassword()));
        values.put("profile", "");
        values.put("auth_level", account.getAuthLevel());

        // Insert the new account (row), returns primary key value of new row
        db.insert("AllAccounts", null, values);
    }

    /**
     * Hashes an account's password
     * @param password password to be hashed
     * @return hashed password
     * @throws NoSuchAlgorithmException if the Base64 algo cannot be found
     */
    private static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update((ACCOUNTS_DB_SALT + password).getBytes());
        byte[] digest = md.digest();
        return Base64.encodeToString(digest, Base64.DEFAULT);
    }

    /**
     * Updates an already existing account in AllAccounts SQLite DB
     * For every changed field, new values need to be set on account in editAccount activity
     * @param account Account to update
     * @throws IOException IO error occurs while writing stream header in getProfile().storeLocation()
     */
    void updateAccount(Account account) throws IOException {
        String username = account.getUsername();
        SQLiteDatabase db = getReadableDatabase();

        // New values for columns (user attributes)
        ContentValues values = new ContentValues();
        values.put("name", account.getName());
        values.put("profile", Profile.serialize(account.getProfile()));

        // Query string for db, find row that matches passed in username
        String selection = "username = ?";
        String[] selectionArgs = { username };

        // update the values for the row that matches the passed in username
        db.update("AllAccounts", values, selection, selectionArgs);
    }

    /**
     * Finds account in the AllAccounts SQLite DB with the corresponding username/password combination
     * @param username username of account
     * @param password of account
     * @return Account matching the corresponding username/password combo
     * @throws NoSuchAlgorithmException if Base64 algo cannot be found in hashPassword()
     * @throws ClassNotFoundException if Class of serialized object cannot be found in loadLocation()
     * @throws IOException if IO error occurs while writing stream header in loadLocation()
     * @throws NoSuchElementException if the proper authority level hasn't been set (probs = account)
     */
    Account AccountWithCreds(String username, String password) throws NoSuchAlgorithmException,
                                                                      ClassNotFoundException,
                                                                      IOException, NoSuchElementException {
        SQLiteDatabase db = getReadableDatabase();

        // Info we want from account that matches passed in username/password
        String[] columns = { "name", "username", "password", "profile", "auth_level" };

        // Query string we pass to db, selectionArgs replaces ? in selection String
        String selection = "username = ? AND password = ?";
        String[] selectionArgs = { username, hashPassword(password) };

        // Query db, creates cursor object that points at result set (matching db entries)
        Cursor cursor = db.query("AllAccounts", columns, selection, selectionArgs,
                                  null, null, null);

        // If cursor is empty (no account was found) throw error
        if (!(cursor.moveToFirst())) {
            cursor.close();
            throw new NoSuchElementException();
        } else {
            // create String values found from account with matching creds
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String profile = cursor.getString(cursor.getColumnIndexOrThrow("profile"));
            String auth_level = cursor.getString(cursor.getColumnIndexOrThrow("auth_level"));
            cursor.close();

            switch (auth_level) {
                case "user":
                    return new User(name, username, password, Profile.deserialize(profile));
                case "worker":
                    return new Worker(name, username, password, Profile.deserialize(profile));
                case "manager":
                    return new Manager(name, username, password, Profile.deserialize(profile));
                case "admin":
                    return new Admin(name, username, password, Profile.deserialize(profile));
                default:
                    throw new NoSuchElementException();
            }
        }
    }

    /**
     * Determines if an account with the passed in username exists in the AllAccounts SQLite DB
     * @param username username to search for
     * @return if account with corresponding username exists
     */
    boolean isAccount(String username) {
        SQLiteDatabase db = getReadableDatabase();
        boolean answer;

        // Info we want from account that matches passed in username
        String[] columns = {"username"};

        // Query string we pass to db, selectionArgs replaces ? in selection String
        String selection = "username = ?";
        String[] selectionArgs = { username };

        // Query db, creates cursor object that points at result set (matching db entries)
        Cursor cursor = db.query("AllAccounts", columns, selection, selectionArgs,
                                  null, null, null);

        // If result set in cursor has 0 entries, then the account doesn't exist
        answer = (cursor.getCount() > 0);
        cursor.close();
        return answer;
    }

    void deleteAllAccounts() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM "+ ACCOUNTS_DB_NAME);
    }


       /*********************************/
      /** AllSourceReports DB METHODS **/
     /*********************************/
    /**
     * Adds a new Water Source Report to AllSourceReports SQLite DB
     * @param report new Water Source Report to be added
     */
    void addSourceReport(WaterSourceReport report) {
        SQLiteDatabase db = getWritableDatabase();

        // Create a new set of values for the new report row
        ContentValues values = new ContentValues();
        values.put("location", WaterSourceReport.storeLocation(report.getLatitude(), report.getLongitude()));
        values.put("author", report.getAuthor());
        values.put("type", report.getType());
        values.put("condition", report.getCondition());
        values.put("user_rating", report.getRating());
        values.put("date", report.getDate());

        // Insert the new report (row), returns primary key value of new row
        db.insert("AllSourceReports", null, values);
    }

    /**
     * Updates an already existing report in AllSourceReports SQLite DB
     * For every changed field, new values need to be set on source report in editSourceReport activity
     * @param report water source report to update
     */
//    void updateSourceReport(WaterSourceReport report) {
//        String id = Long.toString(report.getId());
//        SQLiteDatabase db = getReadableDatabase();
//
//        // New values for columns (report attributes). id, author, type, and date not included
//        // bc they're final attributes and shouldn't be changeable
//        ContentValues values = new ContentValues();
//        values.put("location", WaterSourceReport.storeLocation(report.getLatitude(), report.getLongitude()));
//        values.put("condition", report.getCondition());
//        values.put("user_rating", report.getRating());
//
//        // Query string for db, find row matching passed in id, selectionArgs replaces ?
//        String selection = "_id = ?";
//        String[] selectionArgs = { id };
//
//        // update the values for the row that matches the passed in id
//        db.update("AllSourceReports", values, selection, selectionArgs);
//    }

    /**
     * Returns source report with the corresponding ID
     * @param ID id of the water source report
     * @return Water source report object with the corresponding ID
     */
    WaterSourceReport getSourceReportByID(long ID) {
        String id = Long.toString(ID);
        SQLiteDatabase db = getReadableDatabase();

        // Info we want from report that matches passed in ID
        String[] columns = { "location", "author", "type", "condition", "user_rating", "date" };

        // Query string we pass to db, selectionArgs replaces ? in selection String
        String selection = "_id = ?";
        String[] selectionArgs = { id };

        // Query db, creates cursor object that points at result set (matching db entries)
        Cursor cursor = db.query( "AllSourceReports", columns, selection, selectionArgs,
                                   null, null, null );

        // If cursor is empty (no report was found) throw error
        if (!(cursor.moveToFirst())) {
            cursor.close();
            throw new NoSuchElementException();
        } else {
            // create String values found from report that matched ID in db
            String location = cursor.getString(cursor.getColumnIndexOrThrow("location"));
            String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
            String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
            String condition = cursor.getString(cursor.getColumnIndexOrThrow("condition"));
            int user_rating = cursor.getInt(cursor.getColumnIndexOrThrow("user_rating"));
            String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
            cursor.close();

            return new WaterSourceReport(ID, WaterSourceReport.loadLatitude(location),
                                         WaterSourceReport.loadLongitude(location), author, type,
                                         condition, user_rating, date);
        }
    }

    /**
     * Checks if water source report with same location already exists
     * @return if water source report already exists
     */
    boolean isLocation(double latitude, double longitude) {
        SQLiteDatabase db = getReadableDatabase();
        String location = WaterSourceReport.storeLocation(latitude, longitude);

        // Info we want from source report that matches passed in ID
        String[] columns = {"location"};

        // Query string we pass to db, selectionArgs replaces ? in selection String
        String selection = "location = ?";
        String[] selectionArgs = {location};

        // Query db, creates cursor object that points at result set (matching db entries)
        Cursor cursor = db.query( "AllSourceReports", columns, selection, selectionArgs,
                                   null, null, null );

        // If cursor has any rows in it,
        boolean answer = (cursor.getCount() > 0);
        cursor.close();

        return answer;
    }

    /**
     * Creates array list of water source reports that match corresponding Location
     * @return array list of water source reports in specified location
     */
//    ArrayList<WaterSourceReport> getSourceReportsByLocation(double latitude, double longitude) {
//        SQLiteDatabase db = getReadableDatabase();
//        ArrayList<WaterSourceReport> matching_entries = new ArrayList<>();
//        String location = WaterSourceReport.storeLocation(latitude, longitude);
//
//        // Query string we pass to db, selectionArgs replaces ? in selection String
//        String selection = "location = ?";
//        String[] selectionArgs = {location};
//
//        // Query db, creates cursor object that points at result set (matching db entries)
//        // Passing null into columns bc we want all report(s) attributes
//        Cursor cursor = db.query( "AllSourceReports", null, selection, selectionArgs,
//                                   null, null, null );
//
//        // No reports with that location were found
//        if (!(cursor.moveToFirst())) {
//            cursor.close();
//            throw new NoSuchElementException();
//        } else {
//            cursor.moveToLast();
//
//            // Recursively add reports to array list until at the end of cursor's result set
//            while (cursor.moveToNext()) {
//                // Set values of report that matched location
//                long id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
//                String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
//                String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
//                String condition = cursor.getString(cursor.getColumnIndexOrThrow("condition"));
//                int user_rating = cursor.getInt(cursor.getColumnIndexOrThrow("user_rating"));
//                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
//
//                WaterSourceReport report = new WaterSourceReport(id,latitude, longitude, author,
//                                                                 type, condition, user_rating, date);
//                matching_entries.add(report);
//            }
//
//            cursor.close();
//            return matching_entries;
//        }
//    }

    /**
     * Creates array list of water source reports that were submitted by the corresponding user
     * @param author user we're searching for
     * @return array list of water source reports written by specified user
     */
//    ArrayList<WaterSourceReport> getSourceReportsByAuthor(String author) {
//        SQLiteDatabase db = getReadableDatabase();
//        ArrayList<WaterSourceReport> matching_entries = new ArrayList<>();
//
//        // Query string we pass to db, selectionArgs replaces ? in selection String
//        String selection = "author = ?";
//        String[] selectionArgs = {author};
//
//        // Query db, creates cursor object that points at result set (matching db entries)
//        // passing null into columns bc need all attributes from report(s) written by passed in user
//        Cursor cursor = db.query( "AllSourceReports", null, selection, selectionArgs,
//                                  null, null, null );
//
//        // No reports written by that user were found
//        if (!(cursor.moveToFirst())) {
//            cursor.close();
//            throw new NoSuchElementException();
//        } else {
//            cursor.moveToLast();
//
//            // Recursively add reports to array list until at the end of cursor's result set
//            while (cursor.moveToNext()) {
//                // values of report written by user
//                long id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
//                String loc = cursor.getString(cursor.getColumnIndexOrThrow("location"));
//                String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
//                String condition = cursor.getString(cursor.getColumnIndexOrThrow("condition"));
//                int user_rating = cursor.getInt(cursor.getColumnIndexOrThrow("user_rating"));
//                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
//
//                WaterSourceReport report = new WaterSourceReport(id, WaterSourceReport.loadLatitude(loc),
//                                                                 WaterSourceReport.loadLongitude(loc),
//                                                                 author, type, condition, user_rating, date);
//                matching_entries.add(report);
//            }
//
//            cursor.close();
//            return matching_entries;
//        }
//    }

    /**
     * Creates array list of all source water reports in AllSourceReports SQLite DB
     * @return array list of all water source reports
     */
    ArrayList<String> getAllSourceReports() {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<String> all_entries = new ArrayList<>();

        // Query string we pass to db
        String selectQuery = "SELECT * FROM AllSourceReports";

        // Query db, creates cursor object that points at result set (matching db entries)
        // passing null into columns bc need all attributes from report(s)
        Cursor cursor = db.rawQuery(selectQuery, null);

        // No reports were found
        if (!(cursor.moveToFirst())) {
            cursor.close();
            throw new NoSuchElementException();
        } else {
            // Recursively add reports to array list until at the end of cursor's result set
            while (!cursor.isAfterLast()) {
                // set values of report
                long id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
                String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
                String loc = cursor.getString(cursor.getColumnIndexOrThrow("location"));
                String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
                String condition = cursor.getString(cursor.getColumnIndexOrThrow("condition"));
                int user_rating = cursor.getInt(cursor.getColumnIndexOrThrow("user_rating"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));

                // Put them in String
                String report = '(' + Long.toString(id) + ')' + ' ' + '(' + loc + ')' + ' ' +
                        '(' + author + ')' + ' ' + '(' + type + ')' + ' ' + '(' +
                        condition + ')' + ' ' + '(' + Integer.toString(user_rating) + ')'
                        + ' ' + '(' + date + ')';

                all_entries.add(report);
                cursor.moveToNext();
            }

            cursor.close();
            return all_entries;
        }
    }

    void deleteAllWSR() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM "+ SRC_REPORT_DB_NAME);
    }





       /*********************************/
      /** AllPurityReports DB METHODS **/
     /*********************************/
    /**
     * Adds a new Water Purity Report to AllPurityReports SQLite DB
     * @param report new Water Purity Report to be added
     */
    void addPurityReport(WaterPurityReport report) {
        SQLiteDatabase db = getWritableDatabase();

        // Create a new set of values for the new report row
        ContentValues values = new ContentValues();
        values.put("location", WaterPurityReport.storeLocation(report.getLatitude(), report.getLongitude()));
        values.put("author", report.getAuthor());
        values.put("condition", report.getCondition());
        values.put("virus_ppm", report.getVirusPPM());
        values.put("contam_ppm", report.getContamPPM());
        values.put("date", report.getDate());

        // Insert the new report (row), returns primary key value of new row
        db.insert("AllPurityReports", null, values);
    }


    /**
     * Returns purity report with the corresponding ID
     * @param ID id of the water purity report
     * @return Water purity report object with the corresponding ID
     */
    WaterPurityReport getPurityReportByID(long ID) {
        String id = Long.toString(ID);
        SQLiteDatabase db = getReadableDatabase();

        // Info we want from report that matches passed in ID
        String[] columns = { "location", "author", "condition", "virus_ppm", "contam_ppm", "date" };

        // Query string we pass to db, selectionArgs replaces ? in selection String
        String selection = "_id = ?";
        String[] selectionArgs = { id };

        // Query db, creates cursor object that points at result set (matching db entries)
        Cursor cursor = db.query( "AllPurityReports", columns, selection, selectionArgs,
                null, null, null );

        // If cursor is empty (no report was found) throw error
        if (!(cursor.moveToFirst())) {
            cursor.close();
            throw new NoSuchElementException();
        } else {
            // create String values found from report that matched ID in db
            String location = cursor.getString(cursor.getColumnIndexOrThrow("location"));
            String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
            String condition = cursor.getString(cursor.getColumnIndexOrThrow("condition"));
            int virus_ppm = cursor.getInt(cursor.getColumnIndexOrThrow("virus_ppm"));
            int contam_ppm = cursor.getInt(cursor.getColumnIndexOrThrow("contam_ppm"));
            String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
            cursor.close();

            return new WaterPurityReport(ID, WaterPurityReport.loadLatitude(location),
                                         WaterPurityReport.loadLongitude(location), author,
                                         condition, virus_ppm, contam_ppm, date);
        }
    }


    /**
     * Filters the purity reports based on passed in params
     * @param start_date the earliest date to search by
     * @param end_date the latest date to search by
     * @param longitude the longitude you want to search for
     * @param latitude the latitude you want to search for
     * @param radius the radius you want to search within
     * @return an array list of purity reports that match the criteria
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public ArrayList<String> getPurityReportsByLocationAndDate(String start_date, String end_date, double longitude, double latitude, double radius) {
        ArrayList<String> matching_entries = getAllPurityReports();
        ArrayList<String> answer = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.US);
        Date sDate;
        Date eDate;
        Date rDate;
        String rDateString;
        boolean matches_date = false;

        String[] report_data;
        String loc;
        double lat;
        double lon;
        int virus_ppm;
        int contam_ppm;

        // If there are existing purity reports in the db
        if (matching_entries != null && matching_entries.size() != 0) {
            for (int i = 0; i < matching_entries.size(); i++) {
                // for each report in the db, make a string array of all the report's values
                report_data = matching_entries.get(i).split("\\)\\s\\(");
                loc = report_data[1];
                lat = WaterPurityReport.loadLatitude(loc);
                lon = WaterPurityReport.loadLongitude(loc);
                //virus_ppm = Integer.parseInt(report_data[4]);
                //contam_ppm = Integer.parseInt(report_data[5]);
                rDateString = report_data[6];

                // Evaluate the date of the report and find if it's within the requested range of time
                try {
                    sDate = dateFormat.parse(start_date);
                    rDate = dateFormat.parse(rDateString);
                    eDate = dateFormat.parse(end_date);

                    // only start date, only end date, start and end date
                    // Evaluate start date first, user can put in a start date without an end date
                    if ((start_date != null) && (!Objects.equals(start_date, ""))) {
                        // compareTo -> 0 equal dates
                        // compareTo -> 1 this date after date arg
                        // compareTo -> -1 this date before date arg
                        // If the report date is after or the same as the start date, add it
                        if (rDate.compareTo(sDate) >= 0) {
                            if ((end_date != null) && (!Objects.equals(end_date, ""))) {
                                if (rDate.compareTo(eDate) <= 0) {
                                    matches_date = true;
                                }
                            } else {
                                matches_date = true;
                            }
                        }

                    } else if ((end_date != null) && (!Objects.equals(end_date, ""))) {
                        // If the report date is before or the same as the end date, add it
                        if (rDate.compareTo(eDate) <= 0) {
                            matches_date = true;
                        }
                    }

                } catch (Exception e) {
                    Log.e("Can't parse date", "the date string couldn't be converted to date obj");
                }

                // Evaluate if report is also within the requested location range
                if (matches_date && longitude != 0 && latitude != 0) {
                    float[] results = new float[1];
                    Location.distanceBetween(latitude, longitude, lat, lon, results);
                    // If the distance between the passed in point and the report's point is
                    // within the range of the radius in meters, add the report
                    if (results[0] <= (radius*1609.34)) {
                        answer.add(matching_entries.get(i));
                    }
                }

            }
        }

        return answer;
    }


    /**
     * Creates array list of all water purity reports in AllPurityReports SQLite DB
     * @return array list of all water purity reports
     */
    ArrayList<String> getAllPurityReports() {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<String> all_entries = new ArrayList<>();

        // Query string we pass to db
        String selectQuery = "SELECT * FROM AllPurityReports";

        // Query db, creates cursor object that points at result set (matching db entries)
        // passing null into columns bc need all attributes from report(s)
        Cursor cursor = db.rawQuery( selectQuery, null );

        // No reports were found
        if (!(cursor.moveToFirst())) {
            cursor.close();
            throw new NoSuchElementException();
        } else {
            // Recursively add reports to array list until at the end of cursor's result set
            while (!cursor.isAfterLast()) {
                // set values of report
                long id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
                String loc = cursor.getString(cursor.getColumnIndexOrThrow("location"));
                String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
                String condition = cursor.getString(cursor.getColumnIndexOrThrow("condition"));
                int virus_ppm = cursor.getInt(cursor.getColumnIndexOrThrow("virus_ppm"));
                int contam_ppm = cursor.getInt(cursor.getColumnIndexOrThrow("contam_ppm"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));

                // Put them in String
                String report = '(' + Long.toString(id) + ')' + ' ' + '(' + loc + ')' + ' ' +
                        '(' + author + ')' + ' ' + '(' +
                        condition + ')' + ' ' + '(' + Integer.toString(virus_ppm) + ')' +
                        ' ' + '(' + Integer.toString(contam_ppm) + ')' + ' ' + '(' + date + ')';

                all_entries.add(report);
                cursor.moveToNext();
            }

            cursor.close();
            return all_entries;
        }
    }

    void deleteAllWPR() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM "+ PUR_REPORT_DB_NAME);
    }


    /**
     //         * Creates array list of water purity reports that match corresponding Location
     //         * @return array list of water purity reports in specified location
     //         */
//    ArrayList<WaterPurityReport> getPurityReportsByLocation(double latitude, double longitude) {
//        SQLiteDatabase db = getReadableDatabase();
//        ArrayList<WaterPurityReport> matching_entries = new ArrayList<>();
//        String location = WaterPurityReport.storeLocation(latitude, longitude);
//
//        // Query string we pass to db, selectionArgs replaces ? in selection String
//        String selection = "location = ?";
//        String[] selectionArgs = {location};
//
//        // Query db, creates cursor object that points at result set (matching db entries)
//        // Passing null into columns bc we want all report(s) attributes
//        Cursor cursor = db.query( "AllPurityReports", null, selection, selectionArgs,
//                null, null, null );
//
//        // No reports with that location were found
//        if (!(cursor.moveToFirst())) {
//            cursor.close();
//            throw new NoSuchElementException();
//        } else {
//            cursor.moveToLast();
//
//            // Recursively add reports to array list until at the end of cursor's result set
//            while (cursor.moveToNext()) {
//                // Set values of report that matched location
//                long id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
//                String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
//                String condition = cursor.getString(cursor.getColumnIndexOrThrow("condition"));
//                int virus_ppm = cursor.getInt(cursor.getColumnIndexOrThrow("virus_ppm"));
//                int contam_ppm = cursor.getInt(cursor.getColumnIndexOrThrow("contam_ppm"));
//                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
//
//                WaterPurityReport report = new WaterPurityReport(id, latitude, longitude, author,
//                                                                 condition, virus_ppm, contam_ppm,
//                                                                 date);
//                matching_entries.add(report);
//            }
//
//            cursor.close();
//            return matching_entries;
//        }
//    }
//
//    /**
//     * Creates array list of water purity reports that were submitted by the corresponding worker
//     * @param author worker we're searching for
//     * @return array list of water purity reports written by specified worker
//     */
//    ArrayList<WaterPurityReport> getPurityReportsByAuthor(String author) {
//        SQLiteDatabase db = getReadableDatabase();
//        ArrayList<WaterPurityReport> matching_entries = new ArrayList<>();
//
//        // Query string we pass to db, selectionArgs replaces ? in selection String
//        String selection = "author = ?";
//        String[] selectionArgs = {author};
//
//        // Query db, creates cursor object that points at result set (matching db entries)
//        // passing null into columns bc need all attributes from report(s) written by passed in worker
//        // query(tableName, tableColumns, whereClause, whereArgs, groupBy, having, orderBy);
//        // http://stackoverflow.com/questions/10600670/sqlitedatabase-query-method
//        Cursor cursor = db.query( "AllPurityReports", null, selection, selectionArgs,
//                                   null, null, null );
//
//        // No reports written by that user were found
//        if (!(cursor.moveToFirst())) {
//            cursor.close();
//            throw new NoSuchElementException();
//        } else {
//            cursor.moveToLast();
//
//            // Recursively add reports to array list until at the end of cursor's result set
//            while (cursor.moveToNext()) {
//                // values of report written by worker
//                long id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
//                String loc = cursor.getString(cursor.getColumnIndexOrThrow("location"));
//                String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
//                String condition = cursor.getString(cursor.getColumnIndexOrThrow("condition"));
//                int virus_ppm = cursor.getInt(cursor.getColumnIndexOrThrow("virus_ppm"));
//                int contam_ppm = cursor.getInt(cursor.getColumnIndexOrThrow("contam_ppm"));
//                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
//
//                WaterPurityReport report = new WaterPurityReport(id, WaterPurityReport.loadLatitude(loc),
//                                                                 WaterPurityReport.loadLongitude(loc),
//                                                                 author, condition, virus_ppm,
//                                                                 contam_ppm, date);
//                matching_entries.add(report);
//            }
//
//            cursor.close();
//            return matching_entries;
//        }
//    }
    /**
     //     * Updates an already existing purity report in AllPurityReports SQLite DB
     //     * For every changed field, new values need to be set on purity report in editPurityReport activity
     //     * @param report water purity report to update
     //     */
//    void updatePurityReport(WaterPurityReport report) {
//        String id = Long.toString(report.getId());
//        SQLiteDatabase db = getReadableDatabase();
//
//        // New values for columns (report attributes). id, author, type, and date not included
//        // bc they're final attributes and shouldn't be changeable
//        ContentValues values = new ContentValues();
//        values.put("location", WaterPurityReport.storeLocation(report.getLatitude(), report.getLongitude()));
//        values.put("condition", report.getCondition());
//        values.put("virus_ppm", report.getVirusPPM());
//        values.put("contam_ppm", report.getContamPPM());
//
//        // Query string for db, find row matching passed in id, selectionArgs replaces ?
//        String selection = "_id = ?";
//        String[] selectionArgs = { id };
//
//        // update the values for the row that matches the passed in id
//        db.update("AllPurityReports", values, selection, selectionArgs);
//    }
}