package com.at.gmail.tomeofadventurers.Classes;

public class DnDClass {
    private String subClassId;

    //Alex Code
    private int HP;
    private String shitDice;
    private int hitDice;
    private String [] equipment;
    private boolean [] skillProficiencies;
    private boolean [] savingThrows;

    public void setSubClassId(String subClassId)
    {
        this.subClassId = subClassId;
    }
    public String getSubClassId()
    {
        return this.subClassId;
    }

    //Alex Code
    public void setHP(int HP){this.HP = HP;}
    public int getHP(){return this.HP;}

    public void setHitDice(String hitDice){this.shitDice = hitDice;}
//    public String getHitDice(){return this.shitDice;}
}
