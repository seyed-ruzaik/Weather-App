package com.tutorial6;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

    public class DatabaseHelper extends SQLiteOpenHelper {
        public static final String DATABASE_NAME = "weatherInformation27.db";
        public static final String TABLE_NAME = "weather_table";
        public static final String COL_1 = "DATE";
        public static final String COL_2 = "TIME";
        public static final String COL_3 = "WEATHER";

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table " + TABLE_NAME + " (DATE TEXT," +
                    "TIME TEXT,WEATHER TEXT)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }

        public boolean insertData(String date,String time, String weather) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_1, date);
            contentValues.put(COL_2, time);
            contentValues.put(COL_3,weather);
            long result = db.insert(TABLE_NAME, null, contentValues);
            if (result == -1)
                return false;
            else
                return true;
        }

        public Cursor getAllData() {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
            return res;
        }




    }

