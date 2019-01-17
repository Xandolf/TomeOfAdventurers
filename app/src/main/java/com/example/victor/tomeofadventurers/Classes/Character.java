package com.example.victor.tomeofadventurers.Classes;

public class Character {
    //******STARTING DEFINING VARIABLES********
    //Imported Classes variables classes


    //Stat Variables
    private String name;
    private String className;
    private String raceName;

    private int currentHitPoints;

    private int maxHitPoints;

    private int armorClass;
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
    private boolean skillProficiencies[] ={ true,false,true,false,false,true,true,true,false,true,false,true,false,true,true,false,true,false};
    private int skills[] = new int[18];
    //Char Creation Variables

    private int mySpellBookTable;
    private int myInventoryTable;

    public Character(String name, int abilityScores[], String myRace, String myClass, int passSpeed, int passHP, String passHitDice)
    {
        this.name = name;
        this.abilityScores = abilityScores;
        this.className=myClass;
        this.raceName=myRace;
        armorClass = 13;
        //mySpeed = 30;
        //maxHitPoints=currentHitPoints=10;

        //Alex Code
        this.mySpeed = passSpeed;
        maxHitPoints = currentHitPoints = passHP;
        this.myHitDice = passHitDice;

        statProficiencyBonus=2;
    }

    //Getters................................................................................
    public String getName()
    {
        return name;
    }

    public int getArmorClass()
    {
        return armorClass;
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

    public boolean [] getAllSkillProficiencies(){return skillProficiencies;}

    public int [] getAllSkillModifiers()
    {
        int [] abilityScoresModifiers = getAllAbilityScoreModifiers();
        skills[3]=abilityScoresModifiers[0];
        skills[0]=skills[15]=skills[16]=abilityScoresModifiers[1];
        skills[2]=skills[5]=skills[8]=skills[10]=skills[14]=abilityScoresModifiers[3];
        skills[1]=skills[6]=skills[9]=skills[11]=skills[17]=abilityScoresModifiers[4];
        skills[4]=skills[7]=skills[12]=skills[13]=abilityScoresModifiers[5];

        for (int i=0;i<18; i++)
        {
            if (skillProficiencies[i]) skills[i]+=statProficiencyBonus;
        }
        return skills;
    }

    public int [] getAllAbilityScoreModifiers()
    {
        int abilityScoreModifiers[]= new int [6];
        for (int i=0; i<6;i++)
        {
            abilityScoreModifiers[i] = (abilityScores[i]-10)/2;
        }
        return abilityScoreModifiers;
    }

    public String getClassName()
    {return className;}

    public String getRaceName()
    {return raceName;}


    //Alex Code
    public int getSpeed(){return mySpeed;}
    public String getMyHitDice(){return myHitDice;}
    //Setters................................................................................




    //Functional.............................................................................
    public void increaseCurrentHealth(int healthGained)
    {
        currentHitPoints += healthGained;
        if (currentHitPoints > maxHitPoints) currentHitPoints = maxHitPoints;
    }
    public void decreaseCurrentHealth(int healthLost)
    {
        currentHitPoints -= healthLost;{
        if (currentHitPoints <0) currentHitPoints =0;
        //TODO Add death handler
        }

    }
}
