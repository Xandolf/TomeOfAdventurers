package com.at.gmail.tomeofadventurers.Classes;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class FirebaseUser {

    public String email;
    public String userName;
    public String profilePic;
    public String charName;
    public String charRace;
    public String charClass;
    public int charMaxHP;
    public int charCurrentHP;

    public FirebaseUser(){

        userName = "";
        profilePic = "";
        charName = "";
        charRace = "";
        charClass = "";
        charMaxHP = -1;
        charCurrentHP = -1;

    }

    public FirebaseUser(String email, String userName, String profilePic, String charName, String charRace, String charClass, int charMaxHP, int charCurrentHP){

        this.email = email;
        this.userName = userName;
        this.profilePic = profilePic;
        this.charName = charName;
        this.charRace = charRace;
        this.charClass = charClass;
        this.charMaxHP = charMaxHP;
        this.charCurrentHP = charCurrentHP;

    }

    //Getters
    public String GetEmail(){ return email; }

    public String GetUserName(){ return userName; }

    public String GetProfilePic(){ return profilePic; }

    public String GetCharName(){ return charName; }

    public String GetCharRace(){ return charRace; }

    public String GetCharClass(){ return charClass; }

    public int GetCharMaxHP(){ return charMaxHP; }

    public int GetCurrentHP(){ return charCurrentHP; }

    //Setters


    public void setEmail(String email) { this.email = email; }

    public void setUserName(String userName) { this.userName = userName; }

    public void setProfilePic(String profilePic) { this.profilePic = profilePic; }

    public void setCharName(String charName) { this.charName = charName; }

    public void setCharRace(String charRace) { this .charRace = charRace; }

    public void setCharClass(String charClass) { this.charClass = charClass; }

    public void setCharMaxHP(int charMaxHP) { this.charMaxHP = charMaxHP; }

    public void setCharCurrentHP(int charCurrentHP) { this.charCurrentHP = charCurrentHP; }


}
