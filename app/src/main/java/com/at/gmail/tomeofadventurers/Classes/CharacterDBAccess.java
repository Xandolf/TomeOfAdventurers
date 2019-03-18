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

//Below functions mainly used to fill character sheet fragment
    public String loadCharacterName()
    {
        String query = "SELECT Name FROM characters WHERE Selected = 1";
        Cursor data = database.rawQuery(query, null);

        String name = "_";

        while (data.moveToNext()) {
            name = data.getString(0);
        }

        data.close();

        return name;
    }

    public String loadCharacterClass()
    {
        String query = "SELECT Class FROM characters WHERE Selected = 1";
        Cursor data = database.rawQuery(query, null);

        String dclass = "_";

        while (data.moveToNext()) {
            dclass = data.getString(0);
        }

        data.close();

        return dclass;
    }

    public String loadCharacterRace()
    {
        String query = "SELECT Race FROM characters WHERE Selected = 1";
        Cursor data = database.rawQuery(query, null);

        String race = "_";

        while (data.moveToNext()) {
            race = data.getString(0);
        }

        data.close();

        return race;
    }

    public String loadCharacterHitDice()
    {
        String query = "SELECT HitDice FROM characters WHERE Selected = 1";
        Cursor data = database.rawQuery(query, null);

        String hitDice = "_";

        while (data.moveToNext()) {
            hitDice = data.getString(0);
        }

        data.close();

        return hitDice;
    }

    public String loadCharacterArmorClass()
    {
        String query = "SELECT AC FROM characters WHERE Selected = 1";
        Cursor data = database.rawQuery(query, null);

        String armorClass = "_";

        while (data.moveToNext()) {
            armorClass = data.getString(0);
        }

        data.close();

        return armorClass;
    }

    public String loadCharacterSpeed()
    {
        String query = "SELECT Speed FROM characters WHERE Selected = 1";
        Cursor data = database.rawQuery(query, null);

        String speed = "_";

        while (data.moveToNext()) {
            speed = data.getString(0);
        }

        data.close();

        return speed;
    }

    public int[] loadAbilityScores()
    {
        int[] abilityScores = {0,0,0,0,0,0};

        String query = "SELECT Str, Dex, Con, Int, Wis, Cha FROM characters WHERE Selected = 1";
        Cursor data = database.rawQuery(query, null);

        String str = "_";
        String dex = "_";
        String con = "_";
        String intell = "_";
        String wis = "_";
        String cha = "_";

        while (data.moveToNext()) {
            str = data.getString(0);
            dex = data.getString(1);
            con = data.getString(2);
            intell = data.getString(3);
            wis = data.getString(4);
            cha = data.getString(5);
        }

        data.close();

        abilityScores[0] = Integer.parseInt(str);
        abilityScores[1] = Integer.parseInt(dex);
        abilityScores[2] = Integer.parseInt(con);
        abilityScores[3] = Integer.parseInt(intell);
        abilityScores[4] = Integer.parseInt(wis);
        abilityScores[5] = Integer.parseInt(cha);

        return abilityScores;
    }

    public int[] loadAbilityScoresModifiers()
    {
        int[] abilityScoresModifiers = {0,0,0,0,0,0};

        String query = "SELECT StrMod, DexMod, ConMod, IntMod, WisMod, ChaMod FROM characters WHERE Selected = 1";
        Cursor data = database.rawQuery(query, null);

        //Modifiers for ability scores

        String str = "_";
        String dex = "_";
        String con = "_";
        String intell = "_";
        String wis = "_";
        String cha = "_";

        while (data.moveToNext()) {
            str = data.getString(0);
            dex = data.getString(1);
            con = data.getString(2);
            intell = data.getString(3);
            wis = data.getString(4);
            cha = data.getString(5);
        }

        data.close();

        abilityScoresModifiers[0] = Integer.parseInt(str);
        abilityScoresModifiers[1] = Integer.parseInt(dex);
        abilityScoresModifiers[2] = Integer.parseInt(con);
        abilityScoresModifiers[3] = Integer.parseInt(intell);
        abilityScoresModifiers[4] = Integer.parseInt(wis);
        abilityScoresModifiers[5] = Integer.parseInt(cha);

        return abilityScoresModifiers;
    }

    public int[] loadSkillModifiers()
    {
        int[] skillModifiers = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}; //18 total

        String query = "SELECT Acrobatics, AnimalHandling, Arcana, Athletics, Deception, History, Insight," +
                " Intimidation, Investigation, Medicine, Nature, Perception, Performance, " +
                "Persuasion, Religion, SleightOfHand, Stealth, Survival FROM characters WHERE Selected = 1";
        Cursor data = database.rawQuery(query, null);

        //Modifiers for skills

        String ACR = "_", AH = "_", ARC = "_", ATH = "_", DEC = "_", HIST = "_", INS = "_";
        String INTI = "_", INV = "_", MED = "_", NAT = "_", PERC = "_", PERF = "_";
        String PERS = "_", REL = "_", SOH = "_", STLH = "_", SURV = "_";

        while (data.moveToNext()) {
            ACR = data.getString(0);
            AH = data.getString(1);
            ARC = data.getString(2);
            ATH = data.getString(3);
            DEC = data.getString(4);
            HIST = data.getString(5);
            INS = data.getString(6);
            INTI = data.getString(7);
            INV = data.getString(8);
            MED = data.getString(9);
            NAT = data.getString(10);
            PERC = data.getString(11);
            PERF = data.getString(12);
            PERS = data.getString(13);
            REL = data.getString(14);
            SOH = data.getString(15);
            STLH = data.getString(16);
            SURV = data.getString(17);
        }

        data.close();

        skillModifiers[0] = Integer.parseInt(ACR);
        skillModifiers[1] = Integer.parseInt(AH);
        skillModifiers[2] = Integer.parseInt(ARC);
        skillModifiers[3] = Integer.parseInt(ATH);
        skillModifiers[4] = Integer.parseInt(DEC);
        skillModifiers[5] = Integer.parseInt(HIST);
        skillModifiers[6] = Integer.parseInt(INS);
        skillModifiers[7] = Integer.parseInt(INTI);
        skillModifiers[8] = Integer.parseInt(INV);
        skillModifiers[9] = Integer.parseInt(MED);
        skillModifiers[10] = Integer.parseInt(NAT);
        skillModifiers[11] = Integer.parseInt(PERC);
        skillModifiers[12] = Integer.parseInt(PERF);
        skillModifiers[13] = Integer.parseInt(PERS);
        skillModifiers[14] = Integer.parseInt(REL);
        skillModifiers[15] = Integer.parseInt(SOH);
        skillModifiers[16] = Integer.parseInt(STLH);
        skillModifiers[17] = Integer.parseInt(SURV);


        return skillModifiers;
    }

    public int loadCharacterMaxHP()
    {
        String query = "SELECT MaxHitPoints FROM characters WHERE Selected = 1";
        Cursor data = database.rawQuery(query, null);

        String maxHP = "_";

        while (data.moveToNext()) {
            maxHP = data.getString(0);
        }

        data.close();

        return Integer.parseInt(maxHP);
    }

    public int loadCharacterCurrentHP()
    {
        String query = "SELECT CurrentHitPoints FROM characters WHERE Selected = 1";
        Cursor data = database.rawQuery(query, null);

        String hp = "_";

        while (data.moveToNext()) {
            hp = data.getString(0);
        }

        data.close();

        return Integer.parseInt(hp);
    }

    public int loadCharacterProfBonus()
    {
        String query = "SELECT ProfBonus FROM characters WHERE Selected = 1";
        Cursor data = database.rawQuery(query, null);

        String pB = "_";

        while (data.moveToNext()) {
            pB = data.getString(0);
        }

        data.close();

        return Integer.parseInt(pB);
    }

    public String loadCharacterPassivePerception()
    {
        String query = "SELECT PassivePerception FROM characters WHERE Selected = 1";
        Cursor data = database.rawQuery(query, null);

        String pP = "_";

        while (data.moveToNext()) {
            pP = data.getString(0);
        }

        data.close();

        return pP;
    }

    public void saveCurrentHP(int currentHP)
    {
        String query = "UPDATE characters SET CurrentHitPoints = '"+ currentHP +"' WHERE Selected = 1";
        database.execSQL(query);
    }
}
