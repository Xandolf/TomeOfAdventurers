package com.at.gmail.tomeofadventurers.Classes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.at.gmail.tomeofadventurers.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassDatabaseAccess
{

    private static ClassDatabaseAccess instance;
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;

    //General database functions -----------------------------------------------------------------
    public ClassDatabaseAccess(Context context)
    {
        this.openHelper = new DatabaseHelper(context);
    }

    public static ClassDatabaseAccess getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new ClassDatabaseAccess(context);
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
        String query = "SELECT * FROM classes";
        Cursor data  = database.rawQuery(query, null);
        return data;
    }

    //getRaceNames function takes in an array of Strings containg race id's
    // and returns an array of strings of race names
    // where each index corresponds to the given id's name
    public String[] getClassNames(String[] ids)
    {
        String[] classNames = new String[ids.length];
        for (int i = 0; i < ids.length; i++)
        {
            String query  = "SELECT name FROM classes WHERE id='" + ids[i] + "'";
            Cursor cursor = database.rawQuery(query, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                classNames[i] = (cursor.getString(0));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return classNames;
    }

    public String[] getClassIds()
    {

        String       query  = "SELECT id FROM classes";
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

    public String getDescriptionOfClass (String id)
    {
        String raceDescription = "";
        String query           = "SELECT alignment FROM classes WHERE id='" + id + "'";
        Cursor cursor          = database.rawQuery(query, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast())
        {
            raceDescription = cursor.getString(0);
        }

        return raceDescription;
    }

    public boolean[] getClassOptionProficiencies(Context myContext, String className){
        String [] skillNames = myContext.getResources().getStringArray(R.array.Skills);
        boolean[] proficiencyOptions = {false, false, false, false, false, false, false, false,
                false, false, false, false, false, false, false, false, false, false};
        String [] queryData    ={"","","","","",""};

        String query           = "select proficiency_choices0,proficiency_choices1," +
                "proficiency_choices2,proficiency_choices3,proficiency_choices4," +
                "proficiency_choices5 from classes where name = '" + className + "'";
        Cursor cursor          = database.rawQuery(query, null);

        while(cursor.moveToNext()){
//            for(int i = 0; i<5; i++){
//                queryData[i]=cursor.getString(i);
//            }
            queryData[0]=cursor.getString(0);
            queryData[1]=cursor.getString(1);
            queryData[2]=cursor.getString(2);
            queryData[3]=cursor.getString(3);
            queryData[4]=cursor.getString(4);
            queryData[5]=cursor.getString(5);
        }
        cursor.close();
        for(int i =0; i<5; i++){
            if(Arrays.asList(skillNames).contains(queryData[i])) {
                proficiencyOptions[i]=true;
            }
        }

        return proficiencyOptions;
    }

}
