package com.at.gmail.tomeofadventurers.Classes;

public class CharacterDataPasser {
    //******STARTING DEFINING VARIABLES********
    //Imported Classes variables classes


    //myRace.setRace();

    //Stat Variables
    private String myName;
    private int currentHitPoints;

    private int maxHitPoints;

    private int myArmorClass;
    private String myHitDice;
    private int myInitiative;
    private int mySpeed;
    private int currentDeathSaveSuccesses;
    private int currentDeathSaveFailures;
    //Ability Scores (SDCIWC) Stats

    /*Array Legend
    0 - Strength
    1 - Dexterity
    2 - Constitution
    3 - intelligence
    4 - Wisdom
    5 - Charisma*/
    private int abilityScores[] = new int[6];
    //Other Stat Variables

    private boolean statInspiration;
    private int statProficiencyBonus;
    //Saving Throw Variables

    /* Array Legend
    0 - Strength
    1 - Dexterity
    2 - Constitution
    3 - intelligence
    4 - Wisdom
    5 - Charisma*/
    private boolean savingThrowProficiences[] = new boolean[6];
    private int savingThrow[] = new int[6];
    //Skills Variable

    /* Array Legend
    0 - Acrobatics (Dex)
    1 - Animal Handling (Wis)
    2 - Arcana (int)
    3 - Athletics (Str)
    4 - Deception (Cha)
    5 - History (int)
    6 - Insight (Wis)
    7 - intimidation (Cha)
    8 - Investigation (int)
    9 - Medicine (Wis)
    10 - Nature (int)
    11 - Perception (Wis)
    12 - Performance (Cha)
    13 - Persuasion (Cha)
    14 - Religion (int)
    15 - Sleight of Hand (Dex)
    16 - Stealth (Dex)
    17 - Survival (Wis)
    */
    private boolean skillProficiencies[] = new boolean[18];
    private int skills[] = new int[18];
    //Char Creation Variables

    private int mySpellBookTable;
    private int myInventoryTable;

    public CharacterDataPasser(String myName, int myMaxHealth, int abilityScores[],
                     boolean skillProficiencies[],boolean savingThrowProfs [])
    {
        this.myName = myName;
        this.currentHitPoints=this.maxHitPoints=myMaxHealth;
        this.abilityScores=abilityScores;
        this.skillProficiencies =skillProficiencies;
        this.savingThrowProficiences=savingThrowProfs;
        myArmorClass=13;
        mySpeed=30;


    }

    //Getters................................................................................
    public String getMyName()
    {
        return myName;
    }

    public int getMyArmorClass()
    {
        return myArmorClass;
    }
    public int[] getAbilityScores()
    {
        return abilityScores;
    }

    public int getCurrentHitPoints()
    {
        return currentHitPoints;
    }

    public int getMaxHitPoints()
    {
        return maxHitPoints;
    }

    public boolean [] getSkillProficiencies(){return skillProficiencies;}

    public int [] getAllAbilityScoreModifiers()
    {
        int abilityScoreModifiers[]=new int [6];
        for (int i=0; i<6;i++)
        {
            abilityScoreModifiers[i] = (abilityScores[i]-10)/2;
        }


        return abilityScoreModifiers;
    }

    //Setters................................................................................
    public void setMyName(String passName){
        myName = passName;
    }



    //Functional.............................................................................
    public void increaseCurrentHealth(int healthGained)
    {
        currentHitPoints +=healthGained;
        if (currentHitPoints > maxHitPoints) currentHitPoints = maxHitPoints;
    }
    public void decreaseCurrentHealth(int healthLost)
    {
        currentHitPoints -=healthLost;{
        if (currentHitPoints <0) currentHitPoints =0;
    }

    }
}

