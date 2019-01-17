package com.at.gmail.tomeofadventurers.Classes;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;


public class DatabaseHelper extends SQLiteAssetHelper {

    private static final String DB_NAME = "dnd.db";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }
}