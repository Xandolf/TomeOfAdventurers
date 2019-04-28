package com.at.gmail.tomeofadventurers.Classes;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.TextView;

import com.at.gmail.tomeofadventurers.Fragments.MenuFragments.InventoryFragment;
import com.at.gmail.tomeofadventurers.R;

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
        contentValues.put("equipment_category", descr);
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
        String query = "SELECT items.name FROM items, inventories, characters WHERE items.id = inventories.id AND characters.Selected = 1 AND characters.id = inventories.idchar ORDER BY inventories.equip DESC";
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
        String query = "SELECT count FROM items, inventories, characters WHERE items.id = inventories.id AND characters.Selected = 1 AND characters.id = inventories.idchar ORDER BY inventories.equip DESC";
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
        String query = "SELECT equip FROM items, inventories, characters WHERE items.id = inventories.id AND characters.Selected = 1 AND characters.id = inventories.idchar ORDER BY inventories.equip DESC";
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

    public String[] inventoryCurrency()
    {
        String query = "select count from inventories, characters where (inventories.id = 'platinum' or \n" +
                "inventories.id = 'gold' or inventories.id = 'electrum' or inventories.id = 'silver' or \n" +
                "inventories.id = 'copper') AND characters.Selected = 1 AND characters.id = inventories.idchar\n" +
                "order by (case inventories.id when 'platinum' then 1 when 'gold' then 2 when 'electrum' then 3\n" +
                "when 'silver' then 4 when 'copper' then 5 else 100 end)";
        Cursor data = database.rawQuery(query, null);

        String[] currencyValues = {"0","0","0","0","0"};
        int counter = 0;

        while(data.moveToNext()) {
            currencyValues[counter] = data.getString(0);
            counter++;
        }

        data.close();

        return  currencyValues;
    }

    public int getCurrencyAmount(String desiredCurrency)
    {
        String query = "select count from inventories where idchar = (select id from characters where Selected = 1) and id = '"+ desiredCurrency +"'";
        Cursor data = database.rawQuery(query, null);

        String amountString = "_";
        int amount;

        while (data.moveToNext()) {
            amountString = data.getString(0);
        }

        data.close();

        amount = Integer.parseInt(amountString);

        return amount;
    }

    public void convertCurrency(String myCurrencyType, String desiredCurrencyType, int percent)
    {
        int myCurrencyAmt, desiredCurrencyAmt, leftOverCurrencyAmount, myCurrencyAmtPercent;
        double myCurrencyAmtPercentDouble;
        int divisor = 1, multiplier = 1; //initialize

        myCurrencyAmt = getCurrencyAmount(myCurrencyType);
        myCurrencyAmtPercentDouble = myCurrencyAmt * (percent*0.01);
        myCurrencyAmtPercent = (int) Math.round(myCurrencyAmtPercentDouble);
        leftOverCurrencyAmount = myCurrencyAmt - myCurrencyAmtPercent;

        if((myCurrencyType.equals("copper") && desiredCurrencyType.equals("silver")) || (myCurrencyType.equals("silver") && desiredCurrencyType.equals("gold"))
        || (myCurrencyType.equals("gold") && desiredCurrencyType.equals("platinum")))
        {
            divisor = 10;
        }
        else if((myCurrencyType.equals("copper") && desiredCurrencyType.equals("electrum")))
        {
            divisor = 50;
        }
        else if((myCurrencyType.equals("copper") && desiredCurrencyType.equals("gold")) || (myCurrencyType.equals("silver") && desiredCurrencyType.equals("platinum")))
        {
            divisor = 100;
        }
        else if ((myCurrencyType.equals("copper") && desiredCurrencyType.equals("platinum")))
        {
            divisor = 1000;
        }
        else if ((myCurrencyType.equals("silver") && desiredCurrencyType.equals("electrum")))
        {
            divisor = 5;
        }
        else if ((myCurrencyType.equals("electrum") && desiredCurrencyType.equals("gold")))
        {
            divisor = 2;
        }
        else if ((myCurrencyType.equals("electrum") && desiredCurrencyType.equals("platinum")))
        {
            divisor = 20;
        }
        else if ((myCurrencyType.equals("silver") && desiredCurrencyType.equals("copper")) || (myCurrencyType.equals("gold") && desiredCurrencyType.equals("silver"))
        || (myCurrencyType.equals("platinum") && desiredCurrencyType.equals("gold")))
        {
            multiplier = 10;
        }
        else if ((myCurrencyType.equals("electrum") && desiredCurrencyType.equals("copper")))
        {
            multiplier = 50;
        }
        else if ((myCurrencyType.equals("electrum") && desiredCurrencyType.equals("silver")))
        {
            multiplier = 5;
        }
        else if ((myCurrencyType.equals("gold") && desiredCurrencyType.equals("copper")) || (myCurrencyType.equals("platinum") && desiredCurrencyType.equals("silver")))
        {
            multiplier = 100;
        }
        else if ((myCurrencyType.equals("gold") && desiredCurrencyType.equals("electrum")))
        {
            multiplier = 2;
        }
        else if ((myCurrencyType.equals("platinum") && desiredCurrencyType.equals("copper")))
        {
            multiplier = 1000;
        }
        else if ((myCurrencyType.equals("platinum") && desiredCurrencyType.equals("electrum")))
        {
            multiplier = 20;
        }

        //--------------------------------------------------------------- main formula below

        if(!myCurrencyType.equals(desiredCurrencyType))
        {
            desiredCurrencyAmt = ((myCurrencyAmtPercent/divisor)*multiplier) + getCurrencyAmount(desiredCurrencyType);
            myCurrencyAmt = (myCurrencyAmtPercent%divisor) + leftOverCurrencyAmount;  //left over currency remains that currency i.e 55cp/10, 5 cp remains 5 becomes sp
        }
        else //case where currency types match
        {
            desiredCurrencyAmt = myCurrencyAmt;
        }

        String query1 = "UPDATE inventories SET count = '"+ myCurrencyAmt +"' WHERE idchar = (select id from characters where Selected = 1)" +
                "and id = '"+ myCurrencyType +"'";
        database.execSQL(query1);

        String query2 = "UPDATE inventories SET count = '"+ desiredCurrencyAmt +"' WHERE idchar = (select id from characters where Selected = 1)" +
                "and id = '"+ desiredCurrencyType +"'";
        database.execSQL(query2);

    }

    //Spells database functions -----------------------------------------------------------------
    public List<String> getSpellNames() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT name FROM spells", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public Cursor getSpellSlugSpells(String listName){
        String query = "SELECT slug FROM spells WHERE name = '" + listName + "'";
        Cursor data = database.rawQuery(query, null);
        return data;
    }

    public boolean isSpellinSpellbook(String slugToCheck, String charID){

        boolean inSpellbooks = false;
        String slugMatched = "_"; //Dummy initialize value

        String query = "SELECT slug FROM spellbooks WHERE slug = '"+ slugToCheck +"' AND idchar = '"+ charID +"'";
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

    public boolean addToSpellbooks(String charID, String slug, int myCount) {

        ContentValues contentValue = new ContentValues();

        contentValue.put("idchar", charID);
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

    public int getExistingSpellCount(String slugToCheck, String charID){

        int finalCount = -1;

        String query = "SELECT count FROM spellbooks WHERE slug = '"+ slugToCheck +"' and idchar = '"+ charID +"'";

        Cursor data = database.rawQuery(query, null);

        while(data.moveToNext()) {
            finalCount = data.getInt(0);
        }

        data.close();

        return finalCount;
    }

    public void addToSpellbooksCount(String slugToCheck, int myCount, String charID) {

        String newCount = Integer.toString(myCount);;

        String query = "UPDATE spellbooks SET count = '"+ newCount +"' WHERE slug = '"+ slugToCheck +"' AND idchar = '"+ charID +"'";

        database.execSQL(query);
    }

    public boolean addSpellToSpells(String spellName, String descr, String source1, String type1) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("slug", spellName);
        contentValues.put("name", spellName);
        contentValues.put("desc", descr);
        contentValues.put("page", source1);
        contentValues.put("school", type1);

        long result = database.insert("spells", null, contentValues);

        //if data is inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getSpellsData(){
        String query = "SELECT * FROM spells";
        Cursor data = database.rawQuery(query, null);
        return data;
    }

    public void deleteSpellFromSpells(String listSlug){
        String query = "DELETE FROM " + "spells" + " WHERE "
                + "slug" + " = '" + listSlug + "'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + listSlug + " from database.");
        database.execSQL(query);
    }

    public List<String> fillSpellbook() {
        List<String> list = new ArrayList<>();
        String query = "SELECT spells.name FROM spells, spellbooks, characters WHERE spells.slug = spellbooks.slug AND characters.Selected = 1 AND characters.id = spellbooks.idchar";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public void removeFromSpellbooksCount(String slugToCheck, int myCount, String charID) {

        String newCount = Integer.toString(myCount);

        String query = "UPDATE spellbooks SET count = '"+ newCount +"' WHERE slug = '"+ slugToCheck +"' AND idchar = '"+ charID +"'";

        database.execSQL(query);
    }

    public void deleteItemFromSpellbook(String slugToCheck, String charID){
        String query = "DELETE FROM spellbooks WHERE slug = '"+ slugToCheck +"' AND idchar = '"+ charID +"'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + slugToCheck + " from database.");
        database.execSQL(query);
    }
    //filters results by first 3 inputs, then orders it in ascending order by the 4th (ie name from A to Z, level from lowest to highest, etc)
    public List<String> searchSort(String classURL, String level, String school, String order)
    {
        List<String> list = new ArrayList<>();
        String query = "SELECT * FROM spells WHERE (class1 LIKE '" + classURL + "' OR class2 LIKE '" + classURL + "' OR class3 LIKE '" + classURL
            + "' OR class4 LIKE '" + classURL + "' OR class5 LIKE '" + classURL +"' OR class6 LIKE '" + classURL +"' OR class7 LIKE '" + classURL
            + "') AND spell_level LIKE '" + level + "' AND school LIKE '" + school + "' ORDER BY " + order;

        Cursor result = database.rawQuery(query, null);
        result.moveToFirst();
        while (!result.isAfterLast()) {
            list.add(result.getString(1));
            result.moveToNext();
        }
        result.close();
        return list;
    }
    //filtering and sorting for allitems
    public List<String> allItemSearchSort(String name, String equipmentCategory, String order)
    {
        List<String> list = new ArrayList<>();
        String query = "SELECT * FROM items WHERE (name LIKE '" + name + "' AND equipment_category LIKE '" + equipmentCategory + "') ORDER BY " + order;

        Cursor result = database.rawQuery(query, null);
        result.moveToFirst();
        while (!result.isAfterLast()) {
            list.add(result.getString(1));
            result.moveToNext();
        }

        result.close();
        return list;
    }
}
