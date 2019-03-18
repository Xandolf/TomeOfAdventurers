package com.at.gmail.tomeofadventurers.Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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
        String query = "SELECT * FROM items";
        Cursor data = database.rawQuery(query, null);
        return data;
    }

    public List<String> getItemNames() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM items", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public String getIDFromItembook(String listName){
        String query = "SELECT id FROM items WHERE name = '" + listName + "'";
        Cursor data = database.rawQuery(query, null);

        String itemID = "_";

        while (data.moveToNext()) {
            itemID = data.getString(0);
        }

        data.close();

        return itemID;
    }

    public void deleteItemFromItembook(String listID){
        String query = "DELETE FROM " + "items" + " WHERE "
                + "id" + " = '" + listID + "'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + listID + " from database.");
        database.execSQL(query);
    }

    public boolean addItemToItembook(String itemName, String descr, String weaponCategory, String weaponRange) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", itemName);  //need to have id for new items later!!
        contentValues.put("name", itemName);
        contentValues.put("desc", descr);
        contentValues.put("weapon_category", weaponCategory);
        contentValues.put("weapon_range", weaponRange);

        long result = database.insert("items", null, contentValues);

        //if data is inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    //Inventory database functions -----------------------------------------------------------------
    public boolean isIteminInventories(String idToCheck, String charID){

        boolean inInventories = false;
        String idMatched = "_"; //Dummy initialize value

        String query = "SELECT id FROM inventories WHERE id = '"+ idToCheck +"' AND idchar = '"+ charID +"'";
        Cursor data = database.rawQuery(query, null);

        while(data.moveToNext())
        {
            idMatched = data.getString(0);
        }

        data.close();

        if(idMatched != "_")
            inInventories = true;

        return inInventories;
    }

    public List<String> fillInventoryNames() {
        List<String> list = new ArrayList<>();
        String query = "SELECT items.name FROM items, inventories, characters WHERE items.id = inventories.id AND characters.Selected = 1 AND characters.id = inventories.idchar ORDER BY inventories.id";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();

        int i = 0;  //iterator
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
            i++;
        }
        cursor.close();
        return list;
    }

    public List<Integer> fillInventoryQty() {
        List<Integer> list = new ArrayList<>();
        String query = "SELECT count FROM inventories, characters WHERE characters.Selected = 1 AND characters.id = inventories.idchar ORDER BY inventories.id";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getInt(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<Integer> fillInventoryEquipped() {
        List<Integer> list = new ArrayList<>();
        String query = "SELECT equip FROM inventories, characters WHERE characters.Selected = 1 AND characters.id = inventories.idchar ORDER BY inventories.id";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getInt(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }


    public boolean addToInventories(String idchar, String id, int myCount, int isEquipped) {

        ContentValues contentValue = new ContentValues();

        contentValue.put("idchar", idchar);
        contentValue.put("id", id);
        contentValue.put("count", myCount);
        contentValue.put("equip", isEquipped);

        Log.d(TAG, "addData: Adding " + id + " to " + "inventories");

        long result = database.insert("inventories", null, contentValue);

        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public void addToInventoriesCount(String idToCheck, int myCount, String charID) {

        String newCount = Integer.toString(myCount);

        String query = "UPDATE inventories SET count = '"+ newCount +"' WHERE id = '"+ idToCheck +"' AND idchar = '"+ charID +"'";

        database.execSQL(query);
    }

    public void removeFromInventoriesCount(String idToCheck, int myCount, String charID) {

        String newCount = Integer.toString(myCount);

        String query = "UPDATE inventories SET count = '"+ newCount +"' WHERE id = '"+ idToCheck +"' AND idchar = '"+ charID +"'";

        database.execSQL(query);
    }

    public void deleteItemFromInv(String idToCheck, String charID){
        String query = "DELETE FROM inventories WHERE id = '"+ idToCheck +"' AND idchar = '"+ charID +"'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + idToCheck + " from database.");
        database.execSQL(query);
    }

    public int getExistingItemCount(String idToCheck, String charID){

        int finalCount = -1;

        String query = "SELECT count FROM inventories WHERE id = '"+ idToCheck +"' and idchar = '"+ charID +"'";

        Cursor data = database.rawQuery(query, null);

        while(data.moveToNext()) {
            finalCount = data.getInt(0);
        }

        data.close();

        return finalCount;
    }

    public boolean isItemEquipped(String idToCheck, String charID) {

        boolean inInventories = false;
        String idMatched = "_"; //Dummy initialize value

        String query = "SELECT equip FROM inventories WHERE id = '"+ idToCheck +"' AND idchar = '"+ charID +"'";
        Cursor data = database.rawQuery(query, null);

        while(data.moveToNext())
        {
            idMatched = data.getString(0);
        }

        data.close();

        if(idMatched != "_")
            inInventories = true;

        return inInventories;
    }

    public void setEquipped(String idToCheck, int isEquipped, String charID) {

        String query = "UPDATE " + "inventories" + " SET " + "equip" +
                " = '" + isEquipped + "' WHERE " + "id" + " = '" + idToCheck + "' AND idchar = '"+ charID +"'";

        database.execSQL(query);
    }

    public String inventoryWeight()
    {
        String totalWeight = "0";
        String query = "SELECT SUM(weight*count) FROM items, inventories, characters WHERE items.id = inventories.id AND characters.Selected = 1 AND characters.id = inventories.idchar";
        Cursor data = database.rawQuery(query, null);

        while(data.moveToNext()) {
            totalWeight = data.getString(0);
        }

        data.close();

        return  totalWeight;
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
