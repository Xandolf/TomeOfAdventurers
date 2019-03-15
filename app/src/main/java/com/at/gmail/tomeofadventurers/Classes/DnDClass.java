package com.at.gmail.tomeofadventurers.Classes;

public class DnDClass {
    private String className;

    //Alex Code
    private int HP;
    private String shitDice;
    private int hitDice;
    private String [] equipment;
    private boolean [] skillProficiencies;
    private boolean [] savingThrows;

    public void setClassName(String className)
    {
        this.className=className;
    }
    public String getClassName()
    {
        return this.className;
    }

    //Alex Code
    public void setHP(int HP){this.HP = HP;}
    public int getHP(){return this.HP;}

    public void setHitDice(String hitDice){this.shitDice = hitDice;}
//    public String getHitDice(){return this.shitDice;}
}
