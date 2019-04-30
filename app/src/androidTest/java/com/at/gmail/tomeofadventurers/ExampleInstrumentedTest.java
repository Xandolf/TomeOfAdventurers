package com.at.gmail.tomeofadventurers;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.at.gmail.tomeofadventurers.Classes.CharacterDBAccess;
import com.at.gmail.tomeofadventurers.Classes.DatabaseAccess;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest
{
    public DatabaseAccess myDatabaseAccess;
    public CharacterDBAccess characterDBAccess;

    @Test
    public void useAppContext_isCorrect() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.at.gmail.tomeofadventurers", appContext.getPackageName());
    }

    @Test
    public void getCurrencyAmount_isCorrect()
    {
        Context appContext = InstrumentationRegistry.getTargetContext();
        myDatabaseAccess = DatabaseAccess.getInstance(appContext);
        myDatabaseAccess.open();

        assertEquals(50, myDatabaseAccess.getCurrencyAmount("gold"));

        myDatabaseAccess.close();
    }

    @Test
    public void getIDFromItembook_isCorrect()
    {
        Context appContext = InstrumentationRegistry.getTargetContext();
        myDatabaseAccess = DatabaseAccess.getInstance(appContext);
        myDatabaseAccess.open();

        assertEquals("http://www.dnd5eapi.co/api/equipment/7", myDatabaseAccess.getIDFromItembook("Mace"));

        myDatabaseAccess.close();
    }

    @Test
    public void addItemToItembook_isCorrect()
    {
        Context appContext = InstrumentationRegistry.getTargetContext();
        myDatabaseAccess = DatabaseAccess.getInstance(appContext);
        myDatabaseAccess.open();

        assertTrue(myDatabaseAccess.addItemToItembook("Holy Hand Grenade","Repel Evil","Martial","Ranged"));

        myDatabaseAccess.close();
    }

    @Test
    public void addToInventories_isCorrect()
    {
        Context appContext = InstrumentationRegistry.getTargetContext();
        myDatabaseAccess = DatabaseAccess.getInstance(appContext);
        myDatabaseAccess.open();

        assertTrue(myDatabaseAccess.addToInventories("1a","http://www.dnd5eapi.co/api/equipment/7",1,0));

        myDatabaseAccess.close();
    }

    @Test
    public void initializeCharacter_isCorrect()
    {
        Context appContext = InstrumentationRegistry.getTargetContext();
        characterDBAccess = CharacterDBAccess.getInstance(appContext);
        characterDBAccess.open();

        assertTrue(characterDBAccess.initializeCharacter());

        characterDBAccess.close();
    }

}
