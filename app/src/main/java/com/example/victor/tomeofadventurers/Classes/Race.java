package com.example.victor.tomeofadventurers.Classes;

public class Race {
    private String raceName;

    //Alex Code
    private int speed=30;

    public void setRaceName(String raceName)
    {
        this.raceName=raceName;
    }

    public String getRaceName()
    {
        return this.raceName;
    }
    //Alex code
    public void setSpeed(int passSpeed){this.speed = passSpeed;}
    public int getSpeed(){return this.speed;}
}
