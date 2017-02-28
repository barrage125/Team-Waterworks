package team64.waterworks.controllers;
import team64.waterworks.models.User;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import team64.waterworks.models.Profile;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "AllUsers";
    private static final String DATABASE_CREATE = "CREATE TABLE AllUsers ( _id INTEGER PRIMARY KEY, name TEXT, username TEXT, password TEXT, profile TEXT )";
    private static final String SALT = "!*aS{f8t8$5)9asf(l";


    // Constructor
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
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

        int count = db.update("AllUsers",
                               values,
                               selection,
                               selectionArgs);
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
        String[] projection = { "name",
                                "username",
                                "password",
                                "profile"
                              };

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

}