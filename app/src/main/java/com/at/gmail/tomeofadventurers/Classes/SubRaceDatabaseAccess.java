package com.at.gmail.tomeofadventurers.Classes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class SubRaceDatabaseAccess
{
    private static SubRaceDatabaseAccess instance;
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;

    //General database functions -----------------------------------------------------------------
    public SubRaceDatabaseAccess(Context context)
    {
        this.openHelper = new DatabaseHelper(context);
    }

    public static SubRaceDatabaseAccess getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new SubRaceDatabaseAccess(context);
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
        String query = "SELECT * FROM subraces";
        Cursor data  = database.rawQuery(query, null);
        return data;
    }

    //getRaceNames function takes in an array of Strings containg race id's
    // and returns an array of strings of race names
    // where each index corresponds to the given id's name
    public String[] getSubraceNames(String[] ids)
    {
        String[] raceNames = new String[ids.length];
        for (int i = 0; i < ids.length; i++)
        {
            String query  = "SELECT name FROM subraces WHERE id='" + ids[i] + "'";
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

    public String[] getSubRaceIdsFor(String primaryRaceId)
    {
        String       query  = "SELECT id FROM subraces WHERE race_id = '" + primaryRaceId + "'";
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



    public int [] getTotalAbilityScoreBonusesForSubrace(String subrace_id){
        String race_id = getBaseRaceIdFor(subrace_id);
        int [] baseRaceAbilityScoreBonuses = getBaseRaceAbilityScoreBonuses(race_id);
        int [] subRaceAbiliityScoreBonuses = getSubRaceAbilityScoreBonuses(subrace_id);
        int [] totalAbilityScoreBonuses = new int [6];
        for (int i =0;i<6;i++)
        {
            totalAbilityScoreBonuses[i]=baseRaceAbilityScoreBonuses[i]+subRaceAbiliityScoreBonuses[i];

        }
        return totalAbilityScoreBonuses;
    }

    //----- Functions for getting single race's info
    public int[] getBaseRaceAbilityScoreBonuses(String id)
    {
        int[] abilityScoreBonuses = new int[6];
        String query = "SELECT str_bonus, dex_bonus, con_bonus, int_bonus, " +
                "wis_bonus, cha_bonus FROM races WHERE id='" + id + "'";
        Cursor cursor = database.rawQuery(query, null);
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

    //----- Functions for getting single race's info
    public int[] getSubRaceAbilityScoreBonuses(String id)
    {
        int[] abilityScoreBonuses = new int[6];
        String query = "SELECT str_bonus, dex_bonus, con_bonus, int_bonus, " +
                "wis_bonus, cha_bonus FROM subraces WHERE id='" + id + "'";
        Cursor cursor = database.rawQuery(query, null);
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
