package team64.waterworks.models;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Base64;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.NoSuchElementException;


public class DBHelper extends SQLiteOpenHelper {
    // AllUsers DB strings
    private static final String DATABASE_NAME = "AllUsers";
    private static final String DATABASE_CREATE = "CREATE TABLE AllUsers ( _id INTEGER PRIMARY KEY, name TEXT, username TEXT, password TEXT, profile TEXT )";
    private static final String SALT = "!*aS{f8t8$5)9asf(l";

    // Water Reports DB String
    private static final String REPORT_DATABASE_NAME = "AllReports";
    private static final String REPORT_DATABASE_CREATE = "CREATE TABLE AllReports ( _id INTEGER PRIMARY KEY, location TEXT, author TEXT, type TEXT, condition TEXT, user_rating TEXT, date TEXT )";
    private static final String REPORT_SALT = "!*aS{f8t8$5)9asf(l";

    private boolean report_db = false;


    // Constructor for AllUsers DB
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    // Constructor for Water Reports DB
    public DBHelper(Context context, int switchy) {
        super(context, REPORT_DATABASE_NAME, null, 1);
        this.report_db = true;
    }


    @Override
    public void onCreate(SQLiteDatabase database) {
        if (this.report_db) {
            database.execSQL(REPORT_DATABASE_CREATE);
        } else {
            database.execSQL(DATABASE_CREATE);
        }
    }


    // Adds user to SQLite
    // returns if the user was successfully added or not
    public boolean addUser(String name, String username, String password) {
        // If user already exists, return false
        if (isUser(username)) {
            return false;
        }

        SQLiteDatabase db = getWritableDatabase();

        // Create a new set of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("username", username);
        try {
            values.put("password", hashPassword(password));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // serialize needs to catch IOException
        try {
            values.put("profile", (new Profile().serialize()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Insert the new user (row), returns primary key value of new row
        long newRowId = db.insert("AllUsers", null, values);
        return true;
    }


    public boolean updateUser(String username, Profile profile) {
        SQLiteDatabase db = getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        try {
            values.put("profile", profile.serialize());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // Which row to update, based on the title
        String selection = "username LIKE ?";
        String[] selectionArgs = { username };

        int count = db.update("AllUsers", values, selection, selectionArgs);
        return true;
    }


    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update((SALT + password).getBytes());
        byte[] digest = md.digest();
        return Base64.encodeToString(digest, Base64.DEFAULT);
    }


    public User getUser(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = { "name", "username", "password", "profile" };

        String selection = "username = ? AND password = ?";
        String[] selectionArgs = new String[2];
        selectionArgs[0] = username;

        try {
            selectionArgs[1] = hashPassword(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        Cursor cursor = db.query("AllUsers",
                                  projection,
                                  selection,
                                  selectionArgs,
                                  null,
                                  null,
                                  null
                                );

        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
            password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            String profile = cursor.getString(cursor.getColumnIndexOrThrow("profile"));

            cursor.close();
            User user = null;
            try {
                user = new User(name, username, password, Profile.deserialize(profile));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return user;
        } else {
            cursor.close();
            return null;
        }
    }


    // Returns if the user exists
    public boolean isUser(String username) {
        SQLiteDatabase db = getReadableDatabase();

        // Declare the values we're looking for in the table
        String[] projection = {"username"};
        String selection = "username = ?";
        String[] selectionArgs = {username};

        // Query db
        Cursor cursor = db.query("AllUsers",
                                  projection,
                                  selection,
                                  selectionArgs,
                                  null,
                                  null,
                                  null
                                );
        return cursor.getCount() > 0;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}



    /**
     *
     * WATER REPORT METHODS
     *
     **/

    // Adds water report to SQLite
    // returns if the report was successfully added or not
    public void addReport(WaterReport report) throws Exception {
            SQLiteDatabase db = getWritableDatabase();

            // Create a new set of values for the new report row
            // IO Exception may occur here
            ContentValues values = new ContentValues();
            values.put("location", WaterReport.getLocationAsString(report.getLocation()));
            values.put("author", report.getAuthor());
            values.put("type", report.getType());
            values.put("condition", report.getCondition());
            values.put("user_rating", report.getRating());
            values.put("date", report.getDate());

            // Insert the new report (row), returns primary key value of new row
            db.insert("AllReports", null, values);

            // Set the passed in report object's ID var to the id set automatically by SQlite
            // NoSuchElement exception may occur here
            // report.setId(newRowId);
    }


    // updates report based on report's known id
    public void updateReport(WaterReport report) throws Exception {
            String id = Long.toString(report.getId());
            SQLiteDatabase db = getReadableDatabase();

            // New values for columns (report attributes). id, author, type, and date not included
            // bc they're final attributes and shouldn't be able to be changed
            ContentValues values = new ContentValues();
            values.put("location", WaterReport.getLocationAsString(report.getLocation()));
            values.put("condition", report.getCondition());
            values.put("user_rating", report.getRating());

            // Query string for db, find row that matches passed in id
            String selection = "_id = ?";
            String[] selectionArgs = { id };

            // update the values for the row that matches the passed in id
            int count = db.update("AllReports", values, selection, selectionArgs);
    }


    // Returns report that matches passed in ID
    public WaterReport getReportByID(long ID) throws Exception {
        String id = Long.toString(ID);
        SQLiteDatabase db = getReadableDatabase();

        // Info we want from report that matches passed in ID
        String[] columns = { "location", "author", "type", "condition", "user_rating", "date" };

        // Query string we pass to db, selectionArgs replaces ? in selection String
        String selection = "_id = ?";
        String[] selectionArgs = { id };

        // Query db, creates cursor object that points at result set (matching entries from db query)
        Cursor cursor = db.query("AllReports",
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        // If cursor is empty (no report was found) throw error
        if (!(cursor.moveToFirst())) {
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

            WaterReport report = new WaterReport(ID, WaterReport.deserialize(location), author, type, condition, user_rating, date);
            return report;
        }
    }


    // If user tries to make a water location report with same location
    public boolean isReport(WaterReport report) throws Exception {
        SQLiteDatabase db = getReadableDatabase();
        String location = WaterReport.getLocationAsString(report.getLocation());

        // Info we want from report that matches passed in ID
        // Ex: query db table to find if there are any rows that match location Starbucks
        // id   location        author
        // 1    Starbucks       mom
        // 2    Target          dad
        // 3    Starbucks       sister

        // We only want to know if there are rows that have a Starbucks location, we don't care
        // about the author or id, so our columns = {"location"}
        String[] columns = {"location"};

        // Query string we pass to db, selectionArgs replaces ? in selection String
        // For our selection string, we want to find places that match starbucks
        // selection = "location = Starbucks";
        String selection = "location = ?";
        String[] selectionArgs = {location};

        // Query db, creates cursor object that points at result set (matching entries from db query)
        // When we do Cursor cursor = db.query(table_name, columns, selection, ....)
        // we will have a cursor that points to a set of all the matching entries in the table:
        // Cursor -> null
        //           Starbucks
        //           Starbucks
        Cursor cursor = db.query("AllReports",
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        // If cursor has any rows in it,
        boolean answer = (cursor.getCount() > 0);
        cursor.close();

        return answer;
    }


    // returns array list of water reports that match passed in Location
    public ArrayList<WaterReport> getReportsByLocation(Location LOCATION) throws Exception {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<WaterReport> matching_entries = new ArrayList<WaterReport>();
        String location = WaterReport.getLocationAsString(LOCATION);

        // Query string we pass to db, selectionArgs replaces ? in selection String
        String selection = "location = ?";
        String[] selectionArgs = {location};

        // Query db, creates cursor object that points at result set (matching entries from db query)
        // passing null into columns bc we want all attributes from report(s) that match passed in location
        Cursor cursor = db.query("AllReports",
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        // No reports with that location were found
        if (!(cursor.moveToFirst())) {
            throw new NoSuchElementException();
        } else {
            cursor.moveToLast();
            while (cursor.moveToNext()) {
                // values of report that matched location
                long id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
                String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
                String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
                String condition = cursor.getString(cursor.getColumnIndexOrThrow("condition"));
                int user_rating = cursor.getInt(cursor.getColumnIndexOrThrow("user_rating"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));

                WaterReport report = new WaterReport(id, LOCATION, author, type, condition, user_rating, date);
                matching_entries.add(report);
            }

            cursor.close();
            return matching_entries;
        }
    }


    // returns array list of water reports that were written by passed in username
    public static ArrayList<WaterReport> getReportsByAuthor(String username) throws Exception {
        ArrayList<WaterReport> matching_entries = new ArrayList<WaterReport>();


        if (matching_entries.isEmpty()) {
            throw new NoSuchElementException();
        }
        return matching_entries;
    }
}