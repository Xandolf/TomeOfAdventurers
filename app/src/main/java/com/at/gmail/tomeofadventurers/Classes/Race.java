package com.at.gmail.tomeofadventurers.Classes;

public class Race {
    private String raceName;
    private String subRaceId;


    //Alex Code
    private int speed=30;

    public void setRaceName(String raceName)
    {
        this.raceName=raceName;
    }
    public void setSubRaceId (String subRaceId)
    {
        this.subRaceId=subRaceId;
    }

    public String getRaceName()
    {
        return this.raceName;
    }
    //Alex code
    public void setSpeed(int passSpeed){this.speed = passSpeed;}
    public int getSpeed(){return this.speed;}


//    public int [] getAbilityScoreBonuses(){}






}
