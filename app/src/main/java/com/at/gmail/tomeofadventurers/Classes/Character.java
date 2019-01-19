package com.at.gmail.tomeofadventurers.Classes;

public class Character
{
    private String name;
    private String className;
    private String raceName;
    private Background background;

    private int currentLevel, currentExperiencePoints, proficiencyBonus, armorClass, initiative,
            speed, currentDeathSaveSuccesses, currentDeathSaveFailures, currentHitDice[],
            maxHitDice[];


    private String alignment, personalityTraits, ideals, bonds, flaws;

    private int currentSpellSlots, maxSpellSlots[];

    private int currentHitPoints, maxHitPoints, temporaryHitPoints;

    private boolean hasInspiration;


    //Ability Scores
    /*Array Legend
    0 - Strength
    1 - Dexterity
    2 - Constitution
    3 - intelligence
    4 - Wisdom
    5 - Charisma*/
    private int abilityScores[] = new int[6];


    //Saving Throw Variables
    /* Array Legend
    0 - Strength
    1 - Dexterity
    2 - Constitution
    3 - intelligence
    4 - Wisdom
    5 - Charisma*/
    private boolean savingThrowProficiences[] = new boolean[6];
    private int savingThrows[] = new int[6];


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

    //TODO make these values change based on character creation
    private boolean skillProficiencies[] = {true, false, true, false, false, true, true, true,
            false, true, false, true, false, true, true, false, true, false};
    private int skills[] = new int[18];


    //Database Keys
    private int spellBookTable;
    private int inventoryTable;

    public Character(String name, int abilityScores[], String myRace, String myClass,
            int passSpeed, int passHP, int[] passHitDice)
    {
        this.name = name;
        this.abilityScores = abilityScores;
        this.className = myClass;
        this.raceName = myRace;
        armorClass = 13;
        //mySpeed = 30;
        //maxHitPoints=currentHitPoints=10;

        //Alex Code
        this.speed = passSpeed;
        maxHitPoints = currentHitPoints = passHP;
        this.currentHitDice = passHitDice;

        proficiencyBonus = 2;
    }

    //Getters................................................................................
    public String getName()
    {
        return name;
    }

    public String getClassName()
    {
        return className;
    }

    public String getRaceName()
    {
        return raceName;
    }

    public int getArmorClass()
    {
        return armorClass;
    }

    public int getInitiative()
    {
        return initiative;
    }

    public int getSpeed()
    {
        return speed;
    }

    //HitPoints

    public int getCurrentHitPoints()
    {
        return currentHitPoints;
    }

    public int getTemporaryHitPoints()
    {
        return temporaryHitPoints;
    }

    public int getMaxHitPoints()
    {
        return maxHitPoints;
    }

    public int[] getCurrentHitDice()
    {
        return currentHitDice;
    }

    public int[] getMaxHitDice()
    {
        return maxHitDice;
    }

    //Ability Scores

    public int[] getAbilityScores()
    {
        return abilityScores;
    }

    public int[] getAllAbilityScoreModifiers()
    {
        int abilityScoreModifiers[] = new int[6];
        for (int i = 0; i < 6; i++)
        {
            abilityScoreModifiers[i] = (abilityScores[i] - 10) / 2;
        }
        return abilityScoreModifiers;
    }

    //Skill proficiencies and modifiers

    public boolean[] getAllSkillProficiencies()
    {
        return skillProficiencies;
    }

    public int[] getAllSkillModifiers()
    {
        int[] abilityScoresModifiers = getAllAbilityScoreModifiers();
        skills[3] = abilityScoresModifiers[0];
        skills[0] = skills[15] = skills[16] = abilityScoresModifiers[1];
        skills[2] = skills[5] = skills[8] = skills[10] = skills[14] = abilityScoresModifiers[3];
        skills[1] = skills[6] = skills[9] = skills[11] = skills[17] = abilityScoresModifiers[4];
        skills[4] = skills[7] = skills[12] = skills[13] = abilityScoresModifiers[5];

        for (int i = 0; i < 18; i++)
        {
            if (skillProficiencies[i]) skills[i] += proficiencyBonus;
        }
        return skills;
    }

    public Background getBackground()
    {
        return background;
    }

    public int getCurrentExperiencePoints()
    {
        return currentExperiencePoints;
    }

    public boolean getHasInspiration()
    {
        return hasInspiration;
    }

    public int getProficiencyBonus()
    {
        return proficiencyBonus;
    }

    public int getCurrentDeathSaveSuccesses()
    {
        return currentDeathSaveSuccesses;
    }

    public int getCurrentDeathSaveFailures()
    {
        return currentDeathSaveFailures;
    }

    public boolean[] getSavingThrowProficiences()
    {
        return savingThrowProficiences;
    }

    public int[] getSavingThrows()
    {
        return savingThrows;
    }

    public int getCurrentLevel()
    {
        return currentLevel;
    }
    //Setters................................................................................


    //Functional.............................................................................
    public void increaseCurrentHitPoints(int hitPointsGained)
    {
        currentHitPoints += hitPointsGained;
        if (currentHitPoints > maxHitPoints) currentHitPoints = maxHitPoints;
    }

    public void decreaseCurrentHitPoints(int hitPointsLost)
    {
        currentHitPoints -= hitPointsLost;
        if (currentHitPoints <= 0)
        {

            currentHitPoints = 0;
            //TODO Add death handler
        }
    }


}
