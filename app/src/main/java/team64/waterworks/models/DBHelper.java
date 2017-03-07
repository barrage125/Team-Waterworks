package team64.waterworks.models;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;


class DBHelper extends SQLiteOpenHelper {

    /**** CLASS VARIABLES ****/
    // AllUsers DB strings
    private static final String USERS_DB_NAME = "AllUsers";
    private static final String USERS_DB_CREATE = "CREATE TABLE AllUsers " +
                                                  "( _id INTEGER PRIMARY KEY, name TEXT, " +
                                                  "username TEXT, password TEXT, profile TEXT )";
    private static final String USERS_DB_SALT = "!*aS{f8t8$5)9asf(l";

    // AllReports DB Strings
    private static final String REPORT_DB_NAME = "AllReports";
    private static final String REPORT_DB_CREATE = "CREATE TABLE AllReports " +
                                                   "( _id INTEGER PRIMARY KEY, location TEXT, " +
                                                   "author TEXT, type TEXT, condition TEXT, " +
                                                   "user_rating TEXT, date TEXT )";
    private boolean report_db;


       /******************/
      /** CONSTRUCTORS **/
     /******************/
    /**
     * Constructor for AllUsers DB Helper
     * Creates a SQLiteOpenHelper for a user DB if it doesn't already exist
     * @param context context of caller, used in SQLiteOpenHelper constructor
     *
     */
    DBHelper(Context context) {
        super(context, USERS_DB_NAME, null, 1);
        this.report_db = false;
    }

    /**
     * Constructor for Water Reports DB Helper
     * Creates a SQLiteOpenHelper for a water report DB if it doesn't already exist
     * @param context context of caller, used in SQLite parent constructor
     * @param switchy indicates caller intends to create a DBHelper for a water report db
     */
    DBHelper(Context context, int switchy) {
        super(context, REPORT_DB_NAME, null, 1);
        this.report_db = true;
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
        if (this.report_db) {
            database.execSQL(REPORT_DB_CREATE);
        } else {
            database.execSQL(USERS_DB_CREATE);
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


       /*********************/
      /** USER DB METHODS **/
     /*********************/
    /**
     * Adds a new account to AllUsers SQLite DB
     * @throws NoSuchAlgorithmException if Base64 algo for hashPassword() not found
     * @throws IOException if IO error occurs while writing stream header in getProfile().storeLocation()
     */
    void addAccount(Account account) throws NoSuchAlgorithmException, IOException {
        SQLiteDatabase db = getWritableDatabase();

        // Create a new set of values for new account row
        ContentValues values = new ContentValues();
        values.put("name", account.getName());
        values.put("username", account.getUsername());
        values.put("password", hashPassword(account.getPassword()));
        values.put("profile", "");

        // Insert the new account (row), returns primary key value of new row
        db.insert("AllUsers", null, values);
    }

    /**
     * Hashes an account's password
     * @param password password to be hashed
     * @return hashed password
     * @throws NoSuchAlgorithmException if the Base64 algo cannot be found
     */
    private static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update((USERS_DB_SALT + password).getBytes());
        byte[] digest = md.digest();
        return Base64.encodeToString(digest, Base64.DEFAULT);
    }

    /**
     * Updates an already existing account in AllUsers SQLite DB
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
        db.update("AllUsers", values, selection, selectionArgs);
    }

    /**
     * Finds account in the AllUsers SQLite DB with the corresponding username/password combination
     * @param username username of account
     * @param password of account
     * @return Account matching the corresponding username/password combo
     * @throws NoSuchAlgorithmException if Base64 algo cannot be found in hashPassword()
     * @throws ClassNotFoundException if Class of serialized object cannot be found in loadLocation()
     * @throws IOException if IO error occurs while writing stream header in loadLocation()
     */
    Account AccountWithCreds(String username, String password) throws NoSuchAlgorithmException,
                                                                      ClassNotFoundException,
                                                                      IOException {
        SQLiteDatabase db = getReadableDatabase();

        // Info we want from user that matches passed in username/password
        String[] columns = { "name", "username", "password", "profile" };

        // Query string we pass to db, selectionArgs replaces ? in selection String
        String selection = "username = ? AND password = ?";
        String[] selectionArgs = { username, hashPassword(password) };

        // Query db, creates cursor object that points at result set (matching db entries)
        Cursor cursor = db.query("AllUsers", columns, selection, selectionArgs,
                                  null, null, null);

        // If cursor is empty (no account was found) throw error
        if (!(cursor.moveToFirst())) {
            cursor.close();
            throw new NoSuchElementException();
        } else {
            // create String values found from account with matching creds
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String profile = cursor.getString(cursor.getColumnIndexOrThrow("profile"));
            cursor.close();

            return new User(name, username, password, Profile.deserialize(profile));
        }
    }

    /**
     * Determines if an account with the passed in username exists in the AllUsers SQLite DB
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
        Cursor cursor = db.query("AllUsers", columns, selection, selectionArgs,
                                  null, null, null);

        // If result set in cursor has 0 entries, then the account doesn't exist
        answer = (cursor.getCount() > 0);
        cursor.close();
        return answer;
    }



       /***************************/
      /** AllReports DB METHODS **/
     /***************************/
    /**
     * Adds a new Water Report to AllReports SQLite DB
     * @param report new Water Report to be added
     * @throws IOException if IO error occurs while writing stream header in storeLocation()
     */
    void addReport(WaterReport report) throws IOException {
        SQLiteDatabase db = getWritableDatabase();

        // Create a new set of values for the new report row
        ContentValues values = new ContentValues();
        values.put("location", WaterReport.storeLocation(report.getLatitude(), report.getLongitude()));
        values.put("author", report.getAuthor());
        values.put("type", report.getType());
        values.put("condition", report.getCondition());
        values.put("user_rating", report.getRating());
        values.put("date", report.getDate());

        // Insert the new report (row), returns primary key value of new row
        db.insert("AllReports", null, values);
    }

    /**
     * Updates an already existing report in AllReports SQLite DB
     * For every changed field, new values need to be set on report in editReport activity
     * @param report water report to update
     * @throws IOException if IO error occurs while writing stream header in storeLocation()
     */
    void updateReport(WaterReport report) throws IOException {
        String id = Long.toString(report.getId());
        SQLiteDatabase db = getReadableDatabase();

        // New values for columns (report attributes). id, author, type, and date not included
        // bc they're final attributes and shouldn't be changeable
        ContentValues values = new ContentValues();
        values.put("location", WaterReport.storeLocation(report.getLatitude(), report.getLongitude()));
        values.put("condition", report.getCondition());
        values.put("user_rating", report.getRating());

        // Query string for db, find row matching passed in id, selectionArgs replaces ?
        String selection = "_id = ?";
        String[] selectionArgs = { id };

        // update the values for the row that matches the passed in id
        db.update("AllReports", values, selection, selectionArgs);
    }

    /**
     * Returns report with the corresponding ID
     * @param ID id of the water report
     * @return Water Report object with the corresponding ID
     * @throws IOException if IO error occurs while writing stream header in loadLocation()
     * @throws ClassNotFoundException if Class of serialized object cannot be found in loadLocation()
     * @throws IllegalAccessException if WaterReport constructor called by class other than DBHelper
     */
    WaterReport getReportByID(long ID) throws IOException, ClassNotFoundException,
                                                           IllegalAccessException {
        String id = Long.toString(ID);
        SQLiteDatabase db = getReadableDatabase();

        // Info we want from report that matches passed in ID
        String[] columns = { "location", "author", "type", "condition", "user_rating", "date" };

        // Query string we pass to db, selectionArgs replaces ? in selection String
        String selection = "_id = ?";
        String[] selectionArgs = { id };

        // Query db, creates cursor object that points at result set (matching db entries)
        Cursor cursor = db.query( "AllReports", columns, selection, selectionArgs,
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

            return new WaterReport(ID, WaterReport.loadLatitude(location),
                                   WaterReport.loadLongitude(location), author, type, condition,
                                   user_rating, date);
        }
    }

    /**
     * Checks if water report with same location already exists
     * @return if water report already exists
     * @throws IOException if IO error occurs while writing stream header in storeLocation()
     */
    boolean isLocation(double latitude, double longitude) throws IOException {
        SQLiteDatabase db = getReadableDatabase();
        String location = WaterReport.storeLocation(latitude, longitude);

        // Info we want from report that matches passed in ID
        String[] columns = {"location"};

        // Query string we pass to db, selectionArgs replaces ? in selection String
        String selection = "location = ?";
        String[] selectionArgs = {location};

        // Query db, creates cursor object that points at result set (matching db entries)
        Cursor cursor = db.query( "AllReports", columns, selection, selectionArgs,
                                   null, null, null );

        // If cursor has any rows in it,
        boolean answer = (cursor.getCount() > 0);
        cursor.close();

        return answer;
    }

    /**
     * Creates array list of water reports that match corresponding Location
     * @return array list of water reports in specified location
     * @throws IOException if IO error occurs while writing stream header in storeLocation()
     * @throws IllegalAccessException if WaterReport constructor called by class other than DBHelper
     */
    ArrayList<WaterReport> getReportsByLocation(double latitude, double longitude) throws IOException,
                                                                          IllegalAccessException {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<WaterReport> matching_entries = new ArrayList<>();
        String location = WaterReport.storeLocation(latitude, longitude);

        // Query string we pass to db, selectionArgs replaces ? in selection String
        String selection = "location = ?";
        String[] selectionArgs = {location};

        // Query db, creates cursor object that points at result set (matching db entries)
        // Passing null into columns bc we want all report(s) attributes
        Cursor cursor = db.query( "AllReports", null, selection, selectionArgs,
                                   null, null, null );

        // No reports with that location were found
        if (!(cursor.moveToFirst())) {
            cursor.close();
            throw new NoSuchElementException();
        } else {
            cursor.moveToLast();

            // Recursively add reports to array list until at the end of cursor's result set
            while (cursor.moveToNext()) {
                // Set values of report that matched location
                long id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
                String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
                String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
                String condition = cursor.getString(cursor.getColumnIndexOrThrow("condition"));
                int user_rating = cursor.getInt(cursor.getColumnIndexOrThrow("user_rating"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));

                WaterReport report = new WaterReport(id,latitude, longitude, author, type, condition,
                                                     user_rating, date);
                matching_entries.add(report);
            }

            cursor.close();
            return matching_entries;
        }
    }

    /**
     * Creates array list of water reports that were submitted by the corresponding user
     * @param author user we're searching for
     * @return array list of water reports written by specified user
     * @throws IOException if IO error occurs while writing stream header in loadLocation()
     * @throws ClassNotFoundException if Class of serialized object cannot be found in loadLocation()
     * @throws IllegalAccessException if WaterReport constructor called by class other than DBHelper
     */
    ArrayList<WaterReport> getReportsByAuthor(String author) throws IOException,
                                                                    ClassNotFoundException,
                                                                    IllegalAccessException {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<WaterReport> matching_entries = new ArrayList<>();

        // Query string we pass to db, selectionArgs replaces ? in selection String
        String selection = "author = ?";
        String[] selectionArgs = {author};

        // Query db, creates cursor object that points at result set (matching db entries)
        // passing null into columns bc need all attributes from report(s) written by passed in user
        Cursor cursor = db.query( "AllReports", null, selection, selectionArgs,
                                  null, null, null );

        // No reports written by that user were found
        if (!(cursor.moveToFirst())) {
            cursor.close();
            throw new NoSuchElementException();
        } else {
            cursor.moveToLast();

            // Recursively add reports to array list until at the end of cursor's result set
            while (cursor.moveToNext()) {
                // values of report written by user
                long id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
                String loc = cursor.getString(cursor.getColumnIndexOrThrow("location"));
                String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
                String condition = cursor.getString(cursor.getColumnIndexOrThrow("condition"));
                int user_rating = cursor.getInt(cursor.getColumnIndexOrThrow("user_rating"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));

                WaterReport report = new WaterReport(id, WaterReport.loadLatitude(loc),
                                                     WaterReport.loadLongitude(loc), author, type,
                                                     condition, user_rating, date);
                matching_entries.add(report);
            }

            cursor.close();
            return matching_entries;
        }
    }

    /**
     * Creates array list of all water reports in AllReports SQLite DB
     * @return array list of all water reports
     * @throws IOException if IO error occurs while writing stream header in loadLocation()
     * @throws ClassNotFoundException if Class of serialized object cannot be found in loadLocation()
     * @throws IllegalAccessException if WaterReport constructor called by class other than DBHelper
     */
    ArrayList<String> getAllReports() throws IOException, ClassNotFoundException,
                                                               IllegalAccessException {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> all_entries = new ArrayList<>();

        // Query string we pass to db
        String selectQuery = "SELECT * FROM AllReports";

        // Query db, creates cursor object that points at result set (matching db entries)
        // passing null into columns bc need all attributes from report(s)
        Cursor cursor = db.rawQuery( selectQuery, null );

        // No reports were found
        if (!(cursor.moveToFirst())) {
            cursor.close();
            throw new NoSuchElementException();
        } else {
            cursor.moveToLast();

            // Recursively add reports to array list until at the end of cursor's result set
            while (cursor.moveToNext()) {
                // set values of report
                long id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
                String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
                String loc = cursor.getString(cursor.getColumnIndexOrThrow("location"));
                String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
                String condition = cursor.getString(cursor.getColumnIndexOrThrow("condition"));
                int user_rating = cursor.getInt(cursor.getColumnIndexOrThrow("user_rating"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));

                // Put them in String array
                String[] report = { Long.toString(id), loc, author, type, condition,
                                    Integer.toString(user_rating), date};
                List<String> string_report = Arrays.asList(report);

                all_entries.addAll(string_report);
            }

            cursor.close();
            return all_entries;
        }
    }
}