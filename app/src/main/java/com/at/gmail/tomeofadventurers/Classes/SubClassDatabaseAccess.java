package com.at.gmail.tomeofadventurers.Classes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class SubClassDatabaseAccess
{

    private static SubClassDatabaseAccess instance;
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;

    //General database functions -----------------------------------------------------------------
    public SubClassDatabaseAccess(Context context)
    {
        this.openHelper = new DatabaseHelper(context);
    }

    public static SubClassDatabaseAccess getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new SubClassDatabaseAccess(context);
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

    //SubRace database functions -----------------------------------------------------------------
    public Cursor getAllData()
    {
        String query = "SELECT * FROM subclasses";
        Cursor data  = database.rawQuery(query, null);
        return data;
    }

    //getRaceNames function takes in an array of Strings containg race id's
    // and returns an array of strings of race names
    // where each index corresponds to the given id's name
    public String[] getSubClassNames(String[] ids)
    {
        String[] subClassNames = new String[ids.length];
        for (int i = 0; i < ids.length; i++)
        {
            String query  = "SELECT name FROM subclasses WHERE id='" + ids[i] + "'";
            Cursor cursor = database.rawQuery(query, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                subClassNames[i] = (cursor.getString(0));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return subClassNames;
    }

    public String[] getSubClassIdsFor(String primaryClassId)
    {
        String       query  = "SELECT id FROM subclasses WHERE class_id = '" + primaryClassId + "'";
        List<String> ids    = new ArrayList<String>();
        Cursor       cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            ids.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        String[] stringKeys = ids.toArray(new String[ids.size()]);
        return stringKeys;
    }



    public String getDescriptionOfRace(String id)
    {
        String raceDescription = "";
        String query           = "SELECT alignment FROM races WHERE id='" + id + "'";
        Cursor cursor          = database.rawQuery(query, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast())
        {
            raceDescription = cursor.getString(0);
        }
        return raceDescription;
    }


    public String getBaseRaceIdFor(String subClass_id)
    {
        String baseClassId = null;
        String query      = "SELECT class_id FROM subclasses WHERE id='" + subClass_id + "'";
        Cursor cursor     = database.rawQuery(query, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast())
        {
            baseClassId = (cursor.getString(0));
        } else
        {
            Log.d(TAG, "getBaseRaceIdFor: " + subClass_id + " cursor not found.");
        }
        cursor.close();

        return baseClassId;
    }

    public int getHitDie (String id)
    {
        int hitdie = -1;
        String baseClassId=getBaseRaceIdFor(id);
        String query = "SELECT hit_die FROM classes WHERE id='"+ baseClassId+"'";
        Cursor cursor = database.rawQuery(query,null);
        cursor.moveToFirst();
        if(!cursor.isAfterLast())
        {
            hitdie = cursor.getInt(0);
        }
        else
        {
            Log.d(TAG,"getHitDie:"+id+"cursor not found.");
        }

        return hitdie;
    }

}
