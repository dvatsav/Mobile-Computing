package com.example.brucewayne.a3_2016030;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


//Referred developers documentation
public class A3_2016030_DbManagement extends SQLiteOpenHelper {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "quiz.db";
    public static final String TABLE_NAME = "questions";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
            "_ID INTEGER PRIMARY KEY AUTOINCREMENT" +"," +
                    "QUESTION TEXT" + "," +
                    "ANSWER NUMERIC" + "," +
                    "ATTEMPTED NUMERIC DEFAULT 0" + "," +
                    "USERANS NUMERIC DEFAULT null" + ")";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public A3_2016030_DbManagement(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
