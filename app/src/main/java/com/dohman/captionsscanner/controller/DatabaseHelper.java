package com.dohman.captionsscanner.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    private static final String DB_NAME = "Words.db";
    private static final String DB_TABLE = "Words_Table";

    // Columns
    private static final String ID = "ID";
    private static final String WORD = "WORD";
    private static final String TRANSLATED = "TRANSLATED";
    private static final String JSON = "JSON";

    private static final String CREATE_TABLE = "CREATE TABLE " + DB_TABLE + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            WORD + " TEXT, " +
            TRANSLATED + " TEXT, " +
            JSON + " TEXT " + ")";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);

        onCreate(db);
    }

    // Insert data
    public boolean insertData(String word, String translated, String json) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WORD, word);
        contentValues.put(TRANSLATED, translated);
        contentValues.put(JSON, json);

        long result = db.insert(DB_TABLE, null, contentValues);

        return result != -1; // Doesn't insert data if result = -1
    }

    // View data
    public Cursor viewData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + DB_TABLE;

        return db.rawQuery(query, null);
    }

    // View random data
    public Cursor randomData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + DB_TABLE + "\nORDER BY RANDOM()" + "\nLIMIT 1", null);
        if (cursor.moveToFirst()) {
            return cursor;
        }

        return cursor;
    }

    // See if word already is in the database
    public boolean searchWord(String word) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + DB_TABLE + " WHERE " + WORD + " Like '%" + word + "%'";
        Cursor cursor = db.rawQuery(query, null);

        boolean found = cursor != null && cursor.getCount() > 0;
        if (cursor != null) {
            cursor.close();
        }

        return found;
    }

}
