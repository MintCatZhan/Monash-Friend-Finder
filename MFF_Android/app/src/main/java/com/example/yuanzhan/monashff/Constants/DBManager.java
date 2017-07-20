package com.example.yuanzhan.monashff.Constants;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by YuanZhan on 24/04/2017.
 */

public class DBManager {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "CountryLang.db";
    private final Context context;
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBStructure.tableEntry.TABLE_NAME + " (" +
                    DBStructure.tableEntry._ID + " INTEGER PRIMARY KEY," +
                    DBStructure.tableEntry.COLUMN_COUNTRY_ABBR + TEXT_TYPE +
                    COMMA_SEP +
                    DBStructure.tableEntry.COLUMN_COUNTRY + TEXT_TYPE +
                    COMMA_SEP +
                    DBStructure.tableEntry.COLUMN_LANGUAGE + TEXT_TYPE +
                    " );";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DBStructure.tableEntry.TABLE_NAME;

    private String[] projection = {
            DBStructure.tableEntry.COLUMN_COUNTRY_ABBR,
            DBStructure.tableEntry.COLUMN_COUNTRY,
            DBStructure.tableEntry.COLUMN_LANGUAGE};


    private MySQLiteOpenHelper myDBHelper;
    private SQLiteDatabase db;

    public DBManager(Context ctx) {
        this.context = ctx;
        myDBHelper = new MySQLiteOpenHelper(context);
    }

    private static class MySQLiteOpenHelper extends SQLiteOpenHelper {
        public MySQLiteOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    public DBManager open() throws SQLException {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        myDBHelper.close();
    }


    public long insertCountry(String id, String country, String language) {
        ContentValues values = new ContentValues();
        values.put(DBStructure.tableEntry.COLUMN_COUNTRY_ABBR, id);
        values.put(DBStructure.tableEntry.COLUMN_COUNTRY, country);
        values.put(DBStructure.tableEntry.COLUMN_LANGUAGE, language);
        return db.insert(DBStructure.tableEntry.TABLE_NAME, null, values);
    }

    public Cursor getAllCountry() {
        return db.query(DBStructure.tableEntry.TABLE_NAME, projection, null, null,
                null, null, null);
    }

    public int deleteCountry(String rowId) {
        String[] selectionArgs = {String.valueOf(rowId)};
        String selection = DBStructure.tableEntry.COLUMN_COUNTRY_ABBR + " LIKE ?";
        return db.delete(DBStructure.tableEntry.TABLE_NAME, selection, selectionArgs);
    }

    // delete all record
    public void deleteAllCountry() {
        db.execSQL("delete from " + DBStructure.tableEntry.TABLE_NAME);
    }
}
