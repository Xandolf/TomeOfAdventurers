package com.at.gmail.tomeofadventurers.Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class DatabaseAccess {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

//General database functions -----------------------------------------------------------------
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseHelper(context);
    }

    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
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

    //Itembook database functions -----------------------------------------------------------------
    public Cursor getItemsData(){
        String query = "SELECT * FROM dnditems";
        Cursor data = database.rawQuery(query, null);
        return data;
    }

    public List<String> getItemNames() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM dnditems", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public Cursor getItemSlugitems(String listName){
        String query = "SELECT slug FROM dnditems WHERE name = '" + listName + "'";
        Cursor data = database.rawQuery(query, null);
        return data;
    }

    public void deleteItemFromItembook(String listSlug){
        String query = "DELETE FROM " + "dnditems" + " WHERE "
                + "slug" + " = '" + listSlug + "'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + listSlug + " from database.");
        database.execSQL(query);
    }

    public boolean addItemToItembook(String itemName, String descr, String source1, String type1) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("slug", itemName);
        contentValues.put("name", itemName);
        contentValues.put("desc", descr);
        contentValues.put("document_slug", source1);
        contentValues.put("type", type1);

        long result = database.insert("dnditems", null, contentValues);

        //if data is inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    //Inventory database functions -----------------------------------------------------------------
    public boolean isIteminInventories(String slugToCheck){

        boolean inInventories = false;
        String slugMatched = "_"; //Dummy initialize value

        String query = "SELECT " + "slug" + " FROM " + "inventories" +
                " WHERE " + "slug" + " = '" + slugToCheck + "'";
        Cursor data = database.rawQuery(query, null);

        while(data.moveToNext())
        {
            slugMatched = data.getString(0);
        }

        data.close();

        if(slugMatched != "_")
            inInventories = true;

        return inInventories;
    }

    public List<String> fillInventory() {
        List<String> list = new ArrayList<>();
        String query = "SELECT name FROM dnditems, inventories WHERE dnditems.slug = inventories.slug";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }


    public boolean addToInventories(int idchar, String slug, int myCount) {

        ContentValues contentValue = new ContentValues();

        contentValue.put("idchar", idchar);
        contentValue.put("slug", slug);
        contentValue.put("count", myCount); //hard code to 1 for now need to add to count

        Log.d(TAG, "addData: Adding " + slug + " to " + "inventories");

        long result = database.insert("inventories", null, contentValue);

        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public void addToInventoriesCount(String slugToCheck, int myCount) {

        String newCount = Integer.toString(myCount);

        String query = "UPDATE " + "inventories" + " SET " + "count" +
                " = '" + newCount + "' WHERE " + "slug" + " = '" + slugToCheck + "'";

        database.execSQL(query);
    }

    public void removeFromInventoriesCount(String slugToCheck, int myCount) {

        String newCount = Integer.toString(myCount);

        String query = "UPDATE " + "inventories" + " SET " + "count" +
                " = '" + newCount + "' WHERE " + "slug" + " = '" + slugToCheck + "'";

        database.execSQL(query);
    }

    public void deleteItemFromInv(String slugToCheck){
        String query = "DELETE FROM " + "inventories" + " WHERE "
                + "slug" + " = '" + slugToCheck + "'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + slugToCheck + " from database.");
        database.execSQL(query);
    }

    public int getExistingItemCount(String slugToCheck){

        int finalCount = -1;

        String query = "SELECT " + "count" + " FROM " + "inventories" +
                " WHERE " + "slug" + " = '" + slugToCheck + "'";

        Cursor data = database.rawQuery(query, null);

        while(data.moveToNext()) {
            finalCount = data.getInt(0);
        }

        data.close();

        return finalCount;
    }

    //Spells database functions -----------------------------------------------------------------
    public List<String> getSpellNames() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM dndspells", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public Cursor getSpellSlugSpells(String listName){
        String query = "SELECT slug FROM dndspells WHERE name = '" + listName + "'";
        Cursor data = database.rawQuery(query, null);
        return data;
    }

    public boolean isSpellinSpellbook(String slugToCheck){

        boolean inSpellbooks = false;
        String slugMatched = "_"; //Dummy initialize value

        String query = "SELECT " + "slug" + " FROM " + "spellbooks" +
                " WHERE " + "slug" + " = '" + slugToCheck + "'";
        Cursor data = database.rawQuery(query, null);

        while(data.moveToNext())
        {
            slugMatched = data.getString(0);
        }

        data.close();

        if(slugMatched != "_")
            inSpellbooks = true;

        return inSpellbooks;
    }

    public boolean addToSpellbooks(int idchar, String slug, int myCount) {

        ContentValues contentValue = new ContentValues();

        contentValue.put("idchar", idchar);
        contentValue.put("slug", slug);
        contentValue.put("count", myCount);

        Log.d(TAG, "addData: Adding " + slug + " to " + "spellbooks");

        long result = database.insert("spellbooks", null, contentValue);

        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public int getExistingSpellCount(String slugToCheck){

        int finalCount = -1;

        String query = "SELECT " + "count" + " FROM " + "spellbooks" +
                " WHERE " + "slug" + " = '" + slugToCheck + "'";

        Cursor data = database.rawQuery(query, null);

        while(data.moveToNext()) {
            finalCount = data.getInt(0);
        }

        data.close();

        return finalCount;
    }

    public void addToSpellbooksCount(String slugToCheck, int myCount) {

        String newCount = Integer.toString(myCount);;

        String query = "UPDATE " + "spellbooks" + " SET " + "count" +
                " = '" + newCount + "' WHERE " + "slug" + " = '" + slugToCheck + "'";

        database.execSQL(query);
    }

    public boolean addSpellToSpells(String spellName, String descr, String source1, String type1) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("slug", spellName);
        contentValues.put("name", spellName);
        contentValues.put("desc", descr);
        contentValues.put("page", source1);
        contentValues.put("school", type1);

        long result = database.insert("dndspells", null, contentValues);

        //if data is inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getSpellsData(){
        String query = "SELECT * FROM dndspells";
        Cursor data = database.rawQuery(query, null);
        return data;
    }

    public void deleteSpellFromSpells(String listSlug){
        String query = "DELETE FROM " + "dndspells" + " WHERE "
                + "slug" + " = '" + listSlug + "'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + listSlug + " from database.");
        database.execSQL(query);
    }

    public List<String> fillSpellbook() {
        List<String> list = new ArrayList<>();
        String query = "SELECT name FROM dndspells, spellbooks WHERE dndspells.slug = spellbooks.slug";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public void removeFromSpellbooksCount(String slugToCheck, int myCount) {

        String newCount = Integer.toString(myCount);

        String query = "UPDATE " + "spellbooks" + " SET " + "count" +
                " = '" + newCount + "' WHERE " + "slug" + " = '" + slugToCheck + "'";

        database.execSQL(query);
    }

    public void deleteItemFromSpellbook(String slugToCheck){
        String query = "DELETE FROM " + "spellbooks" + " WHERE "
                + "slug" + " = '" + slugToCheck + "'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + slugToCheck + " from database.");
        database.execSQL(query);
    }


}
