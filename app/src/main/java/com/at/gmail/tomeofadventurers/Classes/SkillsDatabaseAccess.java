package com.at.gmail.tomeofadventurers.Classes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SkillsDatabaseAccess {
    private static SkillsDatabaseAccess instance;
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;

    //General database functions -----------------------------------------------------------------
    public SkillsDatabaseAccess(Context context)
    {
        this.openHelper = new DatabaseHelper(context);
    }

    public static SkillsDatabaseAccess getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new SkillsDatabaseAccess(context);
        }
        return instance;
    }

    public void open()
    {
        this.database = openHelper.getWritableDatabase();
    }

    public void close()
    {
        if (database != null)
        {
            this.database.close();
        }
    }
}
