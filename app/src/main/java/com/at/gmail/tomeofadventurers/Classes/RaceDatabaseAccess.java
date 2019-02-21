package com.at.gmail.tomeofadventurers.Classes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class RaceDatabaseAccess
{

    private static RaceDatabaseAccess instance;
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;

    //General database functions -----------------------------------------------------------------
    public RaceDatabaseAccess(Context context)
    {
        this.openHelper = new DatabaseHelper(context);
    }

    public static RaceDatabaseAccess getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new RaceDatabaseAccess(context);
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

    //Race database functions -----------------------------------------------------------------
    public Cursor getAllData()
    {
        String query = "SELECT * FROM races";
        Cursor data  = database.rawQuery(query, null);
        return data;
    }

    //getRaceNames function takes in an array of Strings containg race id's
    // and returns an array of strings of race names
    // where each index corresponds to the given id's name
    public String[] getRaceNames(String[] ids)
    {
        String[] raceNames = new String[ids.length];
        for (int i = 0; i < ids.length; i++)
        {
            String query  = "SELECT name FROM races WHERE id='" + ids[i] + "'";
            Cursor cursor = database.rawQuery(query, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                raceNames[i] = (cursor.getString(0));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return raceNames;
    }

    public String[] getRaceKeys()
    {
        String       query  = "SELECT id FROM races";
        List<String> keys   = new ArrayList<String>();
        Cursor       cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            keys.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        String[] stringKeys = keys.toArray(new String[keys.size()]);


        return stringKeys;
    }

    //----- Functions for getting single race's info
    public int[] getAbilityScoreBonuses(String id)
    {
        int[]  abilityScoreBonuses = new int[6];
        String query               = "SELECT str_bonus, dex_bonus, con_bonus, int_bonus, " +
                "wis_bonus, cha_bonus FROM races WHERE id='" + id + "'";
        Cursor cursor              = database.rawQuery(query, null);
        cursor.moveToFirst();
        int i = 0;
        while (!cursor.isAfterLast() && i < 6)
        {
            abilityScoreBonuses[i] = cursor.getInt(i);
            i++;
        }
        cursor.close();
        return abilityScoreBonuses;
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
}
