package com.bongobondhuparishad.bloodbank;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Bloodbond";
    public static final String DONORS_TABLE_NAME = "DonorDetails";
    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME,null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table "+ DONORS_TABLE_NAME +"(id integer primary key, details text)"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+DONORS_TABLE_NAME);
        onCreate(db);
    }
    public boolean insert(String s) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("details", s);
        db.insert(DONORS_TABLE_NAME, null, contentValues);
        return true;
    }

    public String get() {
        String result = "";
        try (SQLiteDatabase db = this.getReadableDatabase()) {
            Cursor cursorDonors = db.rawQuery("SELECT * FROM " + DONORS_TABLE_NAME, null);
            if (cursorDonors.moveToFirst()) {
                do {
                    // on below line we are adding the data from cursor to our array list.
                    result = cursorDonors.getString(1);
                } while (cursorDonors.moveToNext());
                // moving our cursor to next.
            }
            cursorDonors.close();

        }
        return result;
    }

}