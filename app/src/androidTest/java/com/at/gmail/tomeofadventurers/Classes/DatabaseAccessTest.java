package com.at.gmail.tomeofadventurers.Classes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DatabaseAccessTest {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private DatabaseAccess myDatabaseAccess;

    private void initializeDB()
    {
        if (openHelper == null) {
            Context context = InstrumentationRegistry.getTargetContext();
            openHelper = new DatabaseHelper(context);
            database = openHelper.getReadableDatabase();
            myDatabaseAccess = DatabaseAccess.getInstance(context);
            myDatabaseAccess.open();
        }
    }

    @Test
    public void getSpellNames() {
        initializeDB();
        List<String> expected = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT name FROM spells", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            expected.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        assertEquals(expected, myDatabaseAccess.getSpellNames());
    }

    @Test
    public void getSpellSlugSpellsExists() {
        initializeDB();
        Cursor cursor = database.rawQuery("SELECT slug FROM spells WHERE name = 'Beacon of Hope'", null);
        List<String> expected = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            expected.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor = myDatabaseAccess.getSpellSlugSpells("Beacon of Hope");
        List<String> result = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            result.add(cursor.getString(0));
            cursor.moveToNext();
        }
        assertEquals(expected, result);
    }

    @Test
    public void getSpellSlugSpellsNotExists() {
        initializeDB();
        Cursor cursor = database.rawQuery("SELECT slug FROM spells WHERE name = 'Wrath of Liu'", null);
        List<String> expected = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            expected.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor = myDatabaseAccess.getSpellSlugSpells("Wrath of Liu");
        List<String> result = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            result.add(cursor.getString(0));
            cursor.moveToNext();
        }
        assertEquals(expected, result);
    }

    @Test
    public void getSpellsData() {
        initializeDB();
        Cursor cursor = database.rawQuery("SELECT * FROM spells", null);
        List<String> expected = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            expected.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor = myDatabaseAccess.getSpellsData();
        List<String> result = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            result.add(cursor.getString(0));
            cursor.moveToNext();
        }
        assertEquals(expected, result);
    }

    @Test
    public void searchSort() {
        initializeDB();
        Cursor cursor = database.rawQuery("SELECT * FROM spells WHERE name LIKE '%' AND (class1 LIKE 'http://www.dnd5eapi.co/api/classes/4' OR class2 LIKE 'http://www.dnd5eapi.co/api/classes/4' OR class3 LIKE 'http://www.dnd5eapi.co/api/classes/4' OR class4 LIKE 'http://www.dnd5eapi.co/api/classes/4' OR class5 LIKE 'http://www.dnd5eapi.co/api/classes/4' OR class6 LIKE 'http://www.dnd5eapi.co/api/classes/4' OR class7 LIKE 'http://www.dnd5eapi.co/api/classes/4') AND spell_level LIKE '2' AND school LIKE 'http://www.dnd5eapi.co/api/magic-schools/1' ORDER BY name", null);
        List<String> expected = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            expected.add(cursor.getString(1));
            cursor.moveToNext();
        }
        List<String> result = myDatabaseAccess.searchSort("%","http://www.dnd5eapi.co/api/classes/4", "2", "http://www.dnd5eapi.co/api/magic-schools/1", "name");
        assertEquals(expected, result);
    }

    @Test
    public void allItemSearchSort() {
        initializeDB();
        Cursor cursor = database.rawQuery("SELECT * FROM items WHERE (name LIKE 'ax' COLLATE NOCASE AND equipment_category LIKE '%') ORDER BY weight DESC", null);
        List<String> expected = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            expected.add(cursor.getString(1));
            cursor.moveToNext();
        }
        List<String> result = myDatabaseAccess.allItemSearchSort("ax", "%", "weight DESC");
        assertEquals(expected, result);
    }
}