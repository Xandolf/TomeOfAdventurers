package com.at.gmail.tomeofadventurers.Classes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.support.constraint.Constraints.TAG;

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


    public String getBaseRaceIdFor(String subrace_id)
    {
        String baseRaceId = null;
        String query      = "SELECT race_id FROM subraces WHERE id='" + subrace_id + "'";
        Cursor cursor     = database.rawQuery(query, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast())
        {
            baseRaceId = (cursor.getString(0));
        } else
        {
            Log.d(TAG, "getBaseRaceIdFor: " + subrace_id + " cursor not found.");
        }
        cursor.close();

        return baseRaceId;
    }

}
