package com.at.gmail.tomeofadventurers.Fragments;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.at.gmail.tomeofadventurers.Activities.MainActivity;
import com.at.gmail.tomeofadventurers.Classes.AbilityScoreSender;
import com.at.gmail.tomeofadventurers.Classes.BusProvider;
import com.at.gmail.tomeofadventurers.Classes.Character;
import com.at.gmail.tomeofadventurers.Classes.CharacterDBAccess;
import com.at.gmail.tomeofadventurers.Classes.DnDClass;
import com.at.gmail.tomeofadventurers.Classes.Race;
import com.at.gmail.tomeofadventurers.R;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

public class SelectNameFragment extends Fragment {

    Button buttonCreateCharacter;
    EditText editTextCharacterName;
    DnDClass dnDClass;
    Race race;
    String name,raceName,className;
    int [] abilityScores = {0,0,0,0,0,0};
    Character newCharacter;
    //AbilityScoreSender abilityScores;
    Bus BUS;

    //Alex Code
    int [] hitDice = {8,8}; //dummy values need to be replaced
    int charSpeed = 20;
    int HP = 16;
    CharacterDBAccess characterDBAccess;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_name, container, false);
        super.onCreate(savedInstanceState);

        raceName="NA";
        className="NA";

        //Get Instance of the BUS
//        BUS = BusProvider.getInstance();
//        BUS.register(this);
        //Define Variables
        editTextCharacterName = view.findViewById(R.id.editTextCharacterName);

        //Button to Create Character (essentially finish the process of creating a character atm)
        buttonCreateCharacter = view.findViewById(R.id.btnCreateCharacter);
        buttonCreateCharacter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v ){
                //Get the name from the editText box
                name = editTextCharacterName.getText().toString();

                characterDBAccess = CharacterDBAccess.getInstance(getContext());
                characterDBAccess.open();

                characterDBAccess.saveName(name);
                characterDBAccess.saveCurrentHP(HP); //full hp to start
                characterDBAccess.saveMaxHp(HP);
                characterDBAccess.saveSpeed(charSpeed);
                characterDBAccess.saveHitDice(hitDice);
                characterDBAccess.savePassivePerception(13);  //hard coded example
                characterDBAccess.saveProficiencyBonus(2); //hard coded example
                characterDBAccess.saveArmorClass(12); //hard coded example

                characterDBAccess.close();
//
//                Character newCharacter = new Character(name, abilityScores, raceName, className, charSpeed, HP, hitDice);
//
//
//                boolean insertCharacter = characterDBAccess.saveCharacter(newCharacter);
//
//                if (insertCharacter) {
//                    toastMessage("Character Successfully Created!");
//                } else {
//                    toastMessage("Something went wrong");
//                }




                //Post the character we just created to the BUS
//                BUS.post(sendCharacter());



                //Direct to Character Sheet fragment
                Intent switcher = new Intent(getActivity(), MainActivity.class);
                startActivity(switcher);

            }
        });

        return view;
    }

    public void toastMessage(String message){
        //Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    //These are subscribe functions.
    //YOU DO NOT NEED TO CALL SUBSCRIBE FUNCTIONS
    //When you register a bus it will call any subscribe functions it needs to.
    //After the name is input and accepted call BUS.register(this)
    //Then create a charater using these variables
    //Then call BUS.post(sendCharacter("CREATED CHARACTER"));
    //BUS.unregister(this);
//    @Subscribe
//    public void getClass(DnDClass dnDClass)
//    {
//        className = dnDClass.getClassName();
//    }
//    @Subscribe
//    public void getRace (Race race)
//    {
//        raceName = race.getRaceName();
//    }
//    @Subscribe
//    public void getAbilityScores(AbilityScoreSender abilityScoreSender)
//    { abilityScores = abilityScoreSender.getAbilityScores();}
//
//    //Alex Code
//    @Subscribe
//    public void getSpeed(Race race){
//        charSpeed = race.getSpeed();
//        toastMessage(String.valueOf(charSpeed));
//    }
//    @Subscribe
//    public void getHP(DnDClass dnDClass){HP = dnDClass.getHP();}
//    @Subscribe
//    public void getHitDice(DnDClass dnDClass){hitDice = dnDClass.getHitDice();}

    //This is the produce function. It takes in an already created charater
    //Make sure you call it while the BUS is registered and inside a BUS.post()
//    @Produce
//    public Character sendCharacter ()
//    {
//        //Create a new instance of a character, using the parameters from the Posterboard on the BUS
//        Character newCharacter = new Character(name, abilityScores, raceName, className, charSpeed, HP,hitDice);
//
//        //Alex Code
//        //toastMessage(String.valueOf(charSpeed));
//
//        return newCharacter;
//    }
}


