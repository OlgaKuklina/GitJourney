package com.oklab.githubjourney.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.oklab.githubjourney.data.ActivityItemsProvider.Tables;

/**
 * Created by olgakuklina on 2017-03-18.
 */

public class ActivityItemsDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "githubjourney.db";
    private static final int DATABASE_VERSION = 2;

    public ActivityItemsDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Tables.ITEMS + " ("
                + ActivityItemsContract.ItemsColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ActivityItemsContract.ItemsColumns.ENTRY_URL + " TEXT,"
                + ActivityItemsContract.ItemsColumns.TITLE + " TEXT NOT NULL,"
                + ActivityItemsContract.ItemsColumns.AUTHOR_ID + " TEXT NOT NULL,"
                + ActivityItemsContract.ItemsColumns.DESCRIPTION + " TEXT,"
                + ActivityItemsContract.ItemsColumns.ACTION_TYPE + " TEXT NOT NULL,"
                 + ActivityItemsContract.ItemsColumns.PUBLISHED_DATE + " INTEGER NOT NULL DEFAULT 0"
                + ")" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Tables.ITEMS);
        onCreate(db);
    }
}


