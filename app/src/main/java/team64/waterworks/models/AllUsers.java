package team64.waterworks.models;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;

import team64.waterworks.controllers.DBHelper;

public class AllUsers {
    private static DBHelper dbHelper;
    private static User user;

    private SQLiteDatabase database;

    public final static String USR_TABLE = "AllUsers";
    public final static String USR_ID = "_id";
    public final static String NAME = "name";
    public final static String USRNAME = "usrname";
    public final static String PROFILE = "profile";

    public AllUsers(Context context) {
    }

    public long createRecords(String id, String name, String username, Profile profile) {
        ContentValues values = new ContentValues();
        values.put(USR_ID, id);
        values.put(NAME, name);
        values.put(USRNAME, username);
        try {
            values.put(PROFILE, profile.serialize());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return database.insert(USR_TABLE, null, values);
    }

    public Cursor selectRecords() {
        String[] cols = new String[]{USR_ID, NAME, USRNAME};
        Cursor mCursor = database.query(true, USR_TABLE, cols, null, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public static DBHelper getInstance(Context context) {
        dbHelper = new DBHelper(context);
        return dbHelper;
    }

    public static void setUserInstance(User new_user) {
        if (user == null) {
            Log.d("Jake", "attempting to set NULL user");
        }
        user = new_user;
    }

    public static User getUserInstance() {
        return user;
    }

    public static void clearUserInstance() {
        user = null;
    }

}