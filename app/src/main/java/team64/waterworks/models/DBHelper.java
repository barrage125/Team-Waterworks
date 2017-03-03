package team64.waterworks.models;
import team64.waterworks.models.User;

import android.accounts.Account;
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
import team64.waterworks.models.Profile;

public class DBHelper extends SQLiteOpenHelper {
    // AllUsers DB strings
    private static final String DATABASE_NAME = "AllUsers";
    private static final String DATABASE_CREATE = "CREATE TABLE AllUsers ( _id INTEGER PRIMARY KEY, name TEXT, username TEXT, password TEXT, profile TEXT )";
    private static final String SALT = "!*aS{f8t8$5)9asf(l";

    // Water Reports DB String
    private static final String REPORT_DATABASE_NAME = "AllReports";
    private static final String REPORT_DATABASE_CREATE = "CREATE TABLE AllReports ( _id INTEGER PRIMARY KEY, location TEXT, author TEXT, type TEXT, condition TEXT )";
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
        String[] selectionArgs = {username};

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
    public boolean addReport(WaterReport report) {
        try {
            SQLiteDatabase db = getWritableDatabase();

            // Create a new set of values for the new report row
            ContentValues values = new ContentValues();
            try {
                values.put("location", report.getLocationString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            values.put("author", report.getAuthor());
            values.put("type", report.getType());
            values.put("condition", report.getCondition());


            // Insert the new report (row), returns primary key value of new row
            long newRowId = db.insert("AllReports", null, values);
            report.setId(getReportID(report));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns reports ID in sqlite, should only be used in add report method
     * other methods should use the getID getter in Water Report class otherwise
     * @param report to find id
     * @return report's id in SQLite
     */
    public boolean int getReportID(WaterReport report) {
        SQLiteDatabase db = getReadableDatabase();
        String location = "";

        try {
            location = report.getLocationString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String author = report.getAuthor();
        String type = report.getType();
        String condition = report.getCondition();

        String[] projection = { "__id__" };

        String selection = "location = ? AND author = ? AND type = ? AND condition = ?";
        String[] selectionArgs = new String[4];
        selectionArgs[0] = location;
        selectionArgs[1] = author;
        selectionArgs[2] = type;
        selectionArgs[3] = condition;

        Cursor cursor = db.query("AllReports",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow("__id__"));
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


    public boolean updateReport(int ID, WaterReport report) {
        try {
            String id = Integer.toString(ID);
            SQLiteDatabase db = getReadableDatabase();

            // New value for one column
            ContentValues values = new ContentValues();
            try {
                values.put("location", report.getLocationString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            values.put("author", report.getAuthor());
            values.put("type", report.getType());
            values.put("condition", report.getCondition());

            // Which row to update, based on the title
            String selection = "__id__ LIKE ?";
            String[] selectionArgs = {id};

            int count = db.update("AllReports", values, selection, selectionArgs);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Location location, String author, String type, String condition
    public User getReport(int ID, Location location, String author, String type, String condition) {
        String id = Integer.toString(ID);
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = { "__id__", "location", "author", "type", "condition" };

        String selection = "__id__ = ?";
        String[] selectionArgs = new String[1];
        selectionArgs[0] = id;

        Cursor cursor = db.query("AllReports",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            id = cursor.getString(cursor.getColumnIndexOrThrow("__id__"));
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
    public boolean isReport(WaterReport report) {
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
}