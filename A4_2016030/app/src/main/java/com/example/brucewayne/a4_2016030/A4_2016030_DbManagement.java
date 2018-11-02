package com.example.brucewayne.a4_2016030;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class A4_2016030_DbManagement extends SQLiteOpenHelper {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "sensor_values.db";
    public static final String TABLE_NAME_S1 = "accelerometer";
    public static final String TABLE_NAME_S2 = "gyroscope";
    public static final String TABLE_NAME_S3 = "orientation";
    public static final String TABLE_NAME_S4 = "gps";
    public static final String TABLE_NAME_S5 = "proximity";

    private static final String SQL_CREATE_ENTRIES_S1 =
            "CREATE TABLE " + TABLE_NAME_S1 + " (" +
                    "_ID INTEGER PRIMARY KEY AUTOINCREMENT" +"," +
                    "TIMESTAMP NUMERIC" + "," +
                    "X_ACCELERATION REAL" + "," +
                    "Y_ACCELERATION REAL" + "," +
                    "Z_ACCELERATION REAL" + ")";

    private static final String SQL_CREATE_ENTRIES_S2 =
            "CREATE TABLE " + TABLE_NAME_S2 + " (" +
                    "_ID INTEGER PRIMARY KEY AUTOINCREMENT" +"," +
                    "TIMESTAMP NUMERIC" + "," +
                    "X_ACCELERATION REAL" + "," +
                    "Y_ACCELERATION REAL" + "," +
                    "Z_ACCELERATION REAL" + ")";

    private static final String SQL_CREATE_ENTRIES_S3 =
            "CREATE TABLE " + TABLE_NAME_S3 + " (" +
                    "_ID INTEGER PRIMARY KEY AUTOINCREMENT" +"," +
                    "TIMESTAMP NUMERIC" + "," +
                    "AZIMUTH REAL" + "," +
                    "PITCH REAL" + "," +
                    "ROLL REAL" + ")";

    private static final String SQL_CREATE_ENTRIES_S4 =
            "CREATE TABLE " + TABLE_NAME_S4 + " (" +
                    "_ID INTEGER PRIMARY KEY AUTOINCREMENT" +"," +
                    "TIMESTAMP NUMERIC" + "," +
                    "LATITUDE REAL" + "," +
                    "LONGITUDE REAL" + ")";



    private static final String SQL_CREATE_ENTRIES_S5 =
            "CREATE TABLE " + TABLE_NAME_S5 + " (" +
                    "_ID INTEGER PRIMARY KEY AUTOINCREMENT" +"," +
                    "TIMESTAMP NUMERIC" + "," +
                    "DISTANCE_FROM_OBJECT REAL" + ")";




    private static final String SQL_DELETE_ENTRIES_S1 =
            "DROP TABLE IF EXISTS " + TABLE_NAME_S1;
    private static final String SQL_DELETE_ENTRIES_S2 =
            "DROP TABLE IF EXISTS " + TABLE_NAME_S2;
    private static final String SQL_DELETE_ENTRIES_S3 =
            "DROP TABLE IF EXISTS " + TABLE_NAME_S3;
    private static final String SQL_DELETE_ENTRIES_S4 =
            "DROP TABLE IF EXISTS " + TABLE_NAME_S4;
    private static final String SQL_DELETE_ENTRIES_S5 =
            "DROP TABLE IF EXISTS " + TABLE_NAME_S5;

    public A4_2016030_DbManagement(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES_S1);
        db.execSQL(SQL_CREATE_ENTRIES_S2);
        db.execSQL(SQL_CREATE_ENTRIES_S3);
        db.execSQL(SQL_CREATE_ENTRIES_S4);
        db.execSQL(SQL_CREATE_ENTRIES_S5);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES_S1);
        db.execSQL(SQL_DELETE_ENTRIES_S2);
        db.execSQL(SQL_DELETE_ENTRIES_S3);
        db.execSQL(SQL_DELETE_ENTRIES_S4);
        db.execSQL(SQL_DELETE_ENTRIES_S5);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
