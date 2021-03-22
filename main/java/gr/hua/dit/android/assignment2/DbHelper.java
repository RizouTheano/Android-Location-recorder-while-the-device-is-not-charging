package gr.hua.dit.android.assignment2;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    public static String DB_NAME = "LocationsDatabase";
    public static String TABLE_NAME = "LocationTable";
    public DbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    //Database Fields
    public static String longitude = "longitude";
    public static String latitude = "latitude";
    public static String timestamp = "timestamp";
    private String SQL_QUERY = " CREATE TABLE " + TABLE_NAME + " ( " + timestamp + " TEXT, " + latitude + " TEXT, " + longitude + " TEXT ) ";





    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long CreateEntry(ContactContract contact){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(timestamp,contact.getTimestamp());
        values.put(latitude, contact.getLatitude());
        values.put(longitude, contact.getLongitude());
        long insertId = db.insert(TABLE_NAME, null, values);
        return insertId;
    }
}
