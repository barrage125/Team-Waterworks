package team64.waterworks.models;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import team64.waterworks.controllers.DBHelper;

public class AllUsers {
    public static DBHelper dbHelper;
    private SQLiteDatabase database;


    public final static String USR_TABLE="AllUsers";
    public final static String USR_ID="_id";
    public final static String NAME="name";
    public final static String USRNAME="usrname";

    public AllUsers(Context context){

    }

    public long createRecords(String id, String name, String username){
        ContentValues values = new ContentValues();
        values.put(USR_ID, id);
        values.put(NAME, name);
        values.put(USRNAME, username);
        return database.insert(USR_TABLE, null, values);
    }

    public Cursor selectRecords() {
        String[] cols = new String[] {USR_ID, NAME, USRNAME};
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
}
