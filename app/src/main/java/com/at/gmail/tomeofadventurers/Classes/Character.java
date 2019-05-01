package com.at.gmail.tomeofadventurers.Classes;

import android.widget.Switch;

public class Character
{
    private String name;
    private DnDClass dnDClass;
    private Race race;

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

    public Character(String name, int abilityScores[], String myRace, String myClass, int passSpeed, int passHP, int[] passHitDice)
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

    public DnDClass getDnDClass()
    {
        return dnDClass;
    }

    public Race getRace()
    {
        return race;
    }

    public String getAlignment()
    {
        return alignment;
    }

    public String getPersonalityTraits()
    {
        return personalityTraits;
    }

    public String getIdeals()
    {
        return ideals;
    }

    public String getBonds()
    {
        return bonds;
    }

    public String getFlaws()
    {
        return flaws;
    }

    public int getCurrentSpellSlots()
    {
        return currentSpellSlots;
    }

    public int[] getMaxSpellSlots()
    {
        return maxSpellSlots;
    }




    public int getSpellBookTable()
    {
        return spellBookTable;
    }

    public int getInventoryTable()
    {
        return inventoryTable;
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

    public int getAbilityScoreModifier(String abilityScoreName)
    {
        int abilityScoreModifier = 0;
        int [] abilityScoreModifiers = getAllAbilityScoreModifiers();
        switch (abilityScoreName){
            case "Strength":
                abilityScoreModifier=abilityScoreModifiers[0];
                break;
            case "Dexterity":
                abilityScoreModifier=abilityScoreModifiers[1];
                break;
            case "Constitution":
                abilityScoreModifier=abilityScoreModifiers[2];
                break;
            case "Intelligence":
                abilityScoreModifier=abilityScoreModifiers[3];
                break;
            case "Wisdom":
                abilityScoreModifier=abilityScoreModifiers[4];
                break;
            case "Charisma":
                abilityScoreModifier=abilityScoreModifiers[5];
                break;
        }
        return abilityScoreModifier;
    }

    //Skill proficiencies and modifiers

    public boolean[] getSkillProficiencies()
    {
        return skillProficiencies;
    }

    public int[] getSkills()
    {
        return skills;
    }

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

    public int getCurrentLevel()
    {
        return currentLevel;
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

    //Setters................................................................................


    public void setName(String name)
    {
        this.name = name;
    }

    public void setClassName(String className)
    {
        this.className = className;
    }

    public void setRaceName(String raceName)
    {
        this.raceName = raceName;
    }

    public void setBackground(Background background)
    {
        this.background = background;
    }

    public void setCurrentLevel(int currentLevel)
    {
        this.currentLevel = currentLevel;
    }

    public void setCurrentExperiencePoints(int currentExperiencePoints)
    {
        this.currentExperiencePoints = currentExperiencePoints;
    }

    public void setProficiencyBonus(int proficiencyBonus)
    {
        this.proficiencyBonus = proficiencyBonus;
    }

    public void setArmorClass(int armorClass)
    {
        this.armorClass = armorClass;
    }

    public void setInitiative(int initiative)
    {
        this.initiative = initiative;
    }

    public void setSpeed(int speed)
    {
        this.speed = speed;
    }

    public void setCurrentDeathSaveSuccesses(int currentDeathSaveSuccesses)
    {
        this.currentDeathSaveSuccesses = currentDeathSaveSuccesses;
    }

    public void setCurrentDeathSaveFailures(int currentDeathSaveFailures)
    {
        this.currentDeathSaveFailures = currentDeathSaveFailures;
    }

    public void setCurrentHitDice(int[] currentHitDice)
    {
        this.currentHitDice = currentHitDice;
    }

    public void setMaxHitDice(int[] maxHitDice)
    {
        this.maxHitDice = maxHitDice;
    }

    public void setAlignment(String alignment)
    {
        this.alignment = alignment;
    }

    public void setPersonalityTraits(String personalityTraits)
    {
        this.personalityTraits = personalityTraits;
    }

    public void setIdeals(String ideals)
    {
        this.ideals = ideals;
    }

    public void setBonds(String bonds)
    {
        this.bonds = bonds;
    }

    public void setFlaws(String flaws)
    {
        this.flaws = flaws;
    }

    public void setCurrentSpellSlots(int currentSpellSlots)
    {
        this.currentSpellSlots = currentSpellSlots;
    }

    public void setMaxSpellSlots(int[] maxSpellSlots)
    {
        this.maxSpellSlots = maxSpellSlots;
    }

    public void setCurrentHitPoints(int currentHitPoints)
    {
        this.currentHitPoints = currentHitPoints;
    }

    public void setMaxHitPoints(int maxHitPoints)
    {
        this.maxHitPoints = maxHitPoints;
    }

    public void setTemporaryHitPoints(int temporaryHitPoints)
    {
        this.temporaryHitPoints = temporaryHitPoints;
    }

    public void setHasInspiration(boolean hasInspiration)
    {
        this.hasInspiration = hasInspiration;
    }

    public void setAbilityScores(int[] abilityScores)
    {
        this.abilityScores = abilityScores;
    }

    public void setSavingThrowProficiences(boolean[] savingThrowProficiences)
    {
        this.savingThrowProficiences = savingThrowProficiences;
    }

    public void setSavingThrows(int[] savingThrows)
    {
        this.savingThrows = savingThrows;
    }

    public void setSkillProficiencies(boolean[] skillProficiencies)
    {
        this.skillProficiencies = skillProficiencies;
    }

    public void setSkills(int[] skills)
    {
        this.skills = skills;
    }

    public void setSpellBookTable(int spellBookTable)
    {
        this.spellBookTable = spellBookTable;
    }

    public void setInventoryTable(int inventoryTable)
    {
        this.inventoryTable = inventoryTable;
    }

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
    public void increaseMaxHitPoints(int hitPointsGained)
    {
        this.maxHitPoints+=hitPointsGained;
    }
    public void dencreaseMaxHitPoints(int hitPointsLost)
    {
        this.maxHitPoints-=hitPointsLost;
    }

}
