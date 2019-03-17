package com.at.gmail.tomeofadventurers.Classes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class CharacterDBAccess {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static CharacterDBAccess instance;

    //General database functions -----------------------------------------------------------------
    private CharacterDBAccess(Context context) {
        this.openHelper = new DatabaseHelper(context);
    }

    public static CharacterDBAccess getInstance(Context context) {
        if (instance == null) {
            instance = new CharacterDBAccess(context);
        }
        return instance;
    }

    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public List<String> getCharacterNames() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT Name FROM characters", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public String getIDFromCharacters(String charName){
        String query = "SELECT id FROM characters WHERE Name = '" + charName + "'";
        Cursor data = database.rawQuery(query, null);

        String charID = "_";

        while (data.moveToNext()) {
            charID = data.getString(0);
        }

        data.close();

        return charID;
    }

    public void chooseCharacter(String charID)
    {
        String query1 = "UPDATE characters SET Selected = 0";
        database.execSQL(query1);

        String query2 = "UPDATE characters SET Selected = 1 WHERE id = '"+ charID +"'";
        database.execSQL(query2);
    }

    public String findSelectedCharacter()
    {
        String query = "SELECT id FROM characters WHERE Selected = 1";
        Cursor data = database.rawQuery(query, null);

        String charID = "_";

        while (data.moveToNext()) {
            charID = data.getString(0);
        }

        data.close();

        return charID;
    }
}
