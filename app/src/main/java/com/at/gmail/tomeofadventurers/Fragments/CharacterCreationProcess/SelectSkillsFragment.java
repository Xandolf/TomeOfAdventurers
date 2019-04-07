package com.at.gmail.tomeofadventurers.Fragments.CharacterCreationProcess;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.at.gmail.tomeofadventurers.Classes.BusProvider;
import com.at.gmail.tomeofadventurers.Classes.CharacterDBAccess;
import com.at.gmail.tomeofadventurers.Classes.ClassDatabaseAccess;
import com.at.gmail.tomeofadventurers.Classes.DatabaseAccess;
import com.at.gmail.tomeofadventurers.Classes.DnDClass;
import com.at.gmail.tomeofadventurers.Classes.SkillProficiencySender;
import com.at.gmail.tomeofadventurers.R;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

public class SelectSkillsFragment extends Fragment implements View.OnClickListener {

    Button buttonGoToSelectName;
    CheckBox acrobaticsButton, animalHandlingButton, arcanaButton, athleticsButton, deceptionButton,
            historyButton, insightButton, intimidationButton, investigationButton, medicineButton, natureButton,
            perceptionButton, performanceButton, persuasionButton, religionButton, slightOfHandButton,
            stealthButton, survivalButton;
    String className;
    ClassDatabaseAccess myDatabaseAccess;

    Bus BUS;
    //0 = Not Proficient, 1 = Proficient, 2 = Expertise
    int[] skillProficiencies = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; //19 options for all skills
    //Player can select 2 skills from provided skills
    int skillCount = 2;
    int [] skillModifiers = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_skills, container, false);
        super.onCreate(savedInstanceState);

        CharacterDBAccess characterDBAccess;
        characterDBAccess = CharacterDBAccess.getInstance(getContext());
        characterDBAccess.open();
        className = characterDBAccess.loadCharacterClass();      //retrieve class name from db

        //Open Database
        myDatabaseAccess = ClassDatabaseAccess.getInstance(this.getContext());
        myDatabaseAccess.open();

        skillCount = myDatabaseAccess.getProficiencyPointCount(className);

        myDatabaseAccess.close();

        buttonGoToSelectName = view.findViewById(R.id.buttonGoToEnterName);
        //Get the instance of the bus
//        BUS = BusProvider.getInstance();
//        BUS.register(this);


        acrobaticsButton = view.findViewById(R.id.radio_acrobatics);
        acrobaticsButton.setOnClickListener(this);

        animalHandlingButton = view.findViewById(R.id.radio_animalHandling);
        animalHandlingButton.setOnClickListener(this);

        arcanaButton = view.findViewById(R.id.radio_arcana);
        arcanaButton.setOnClickListener(this);

        athleticsButton = view.findViewById(R.id.radio_athletics);
        athleticsButton.setOnClickListener(this);

        deceptionButton = view.findViewById(R.id.radio_deception);
        deceptionButton.setOnClickListener(this);

        historyButton = view.findViewById(R.id.radio_history);
        historyButton.setOnClickListener(this);

        insightButton = view.findViewById(R.id.radio_insight);
        insightButton.setOnClickListener(this);

        intimidationButton = view.findViewById(R.id.radio_intimidation);
        intimidationButton.setOnClickListener(this);

        investigationButton = view.findViewById(R.id.radio_investigation);
        investigationButton.setOnClickListener(this);

        medicineButton = view.findViewById(R.id.radio_medicine);
        medicineButton.setOnClickListener(this);

        natureButton = view.findViewById(R.id.radio_nature);
        natureButton.setOnClickListener(this);

        perceptionButton = view.findViewById(R.id.radio_perception);
        perceptionButton.setOnClickListener(this);

        performanceButton = view.findViewById(R.id.radio_performance);
        performanceButton.setOnClickListener(this);

        persuasionButton = view.findViewById(R.id.radio_persuasion);
        persuasionButton.setOnClickListener(this);

        religionButton = view.findViewById(R.id.radio_religion);
        religionButton.setOnClickListener(this);

        slightOfHandButton = view.findViewById(R.id.radio_slightOfHand);
        slightOfHandButton.setOnClickListener(this);

        stealthButton = view.findViewById(R.id.radio_stealth);
        stealthButton.setOnClickListener(this);

        survivalButton = view.findViewById(R.id.radio_survival);
        survivalButton.setOnClickListener(this);

        buttonGoToSelectName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Send the ability scores to the BUS
//                BUS.post(proficiencySender());

                //Unregister the BUS
//                BUS.unregister(this);
                calculateSkillModifiers(); //sample oversimplified function, needs to be enhanced, just add +2 to prof skills for modifier for now

                characterDBAccess.saveSkillProficiencies(skillProficiencies);
                characterDBAccess.saveSkillModifiers(skillModifiers);

                characterDBAccess.close();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragTrans = fragmentManager.beginTransaction();
                SelectNameFragment frag = new SelectNameFragment();
                fragTrans.replace(R.id.fragment_container, frag);
                fragTrans.commit();
            }

        });
        //disableButton(buttonGoToSelectName);

        selectableProficiencies(getContext());
        //preDeterminedProficiencies();
        //nonSelectableProficiencies();


        return view;
    }

    public void preDeterminedProficiencies() {
        acrobaticsButton.setChecked(true);
        acrobaticsButton.setEnabled(false);

    }

    public void selectableProficiencies(Context myContext) {
        myDatabaseAccess = ClassDatabaseAccess.getInstance(myContext);
        myDatabaseAccess.open();
        boolean[] proficiencyOptions = myDatabaseAccess.getClassOptionProficiencies(myContext,className);
        CheckBox[] skillArray = {acrobaticsButton, animalHandlingButton, arcanaButton, athleticsButton, deceptionButton,
                historyButton, insightButton, intimidationButton, investigationButton, medicineButton, natureButton,
                perceptionButton, performanceButton, persuasionButton, religionButton, slightOfHandButton,
                stealthButton, survivalButton};
        for(int i=0; i<18;i++){
            skillArray[i].setEnabled(false);
        }

        for(int i=0; i<18;i++) {
            if(proficiencyOptions[i]){
                skillArray[i].setEnabled(true);
            }
        }
        myDatabaseAccess.close();
    }

    public void checkExpendedPoints() {
        CheckBox[] skillArray = {acrobaticsButton, animalHandlingButton, arcanaButton, athleticsButton, deceptionButton,
                historyButton, insightButton, intimidationButton, investigationButton, medicineButton, natureButton,
                perceptionButton, performanceButton, persuasionButton, religionButton, slightOfHandButton,
                stealthButton, survivalButton};
        //If all skill points are used, disables all unchecked skills
        if (skillCount == 0) {
            //enableButton(buttonGoToSelectName);
            for (int i = 0; i < 18; i++) {
                if (!skillArray[i].isChecked()) {
                    skillArray[i].setEnabled(false);
                }
            }
        //If skill points > 0 then all unchecked skills will be enabled.
        } else {
            //disableButton(buttonGoToSelectName);
            for (int i = 0; i < 18; i++) {
                if (!skillArray[i].isChecked()) {
                    selectableProficiencies(getContext());
                }
            }
        }
        //preDeterminedProficiencies();

    }
    //Toast message for testing. Feel free to delete if no longer needed.
    public void toastMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        //toastMessage("Proficient: "+ skillProficiencies[0]);
    }

    //Onclicks for all checkboxes
    public void onClick(View v) {

        boolean checked = ((CheckBox) v).isChecked();
        //Updating each skill based on whether it is selected or not
        //Could be pre selected depending on class specs
        switch (v.getId()) {
            case R.id.radio_acrobatics:
                if (checked) {
                    skillProficiencies[0] = 1;
                    skillCount--;
                }
                if (!checked) {
                    skillProficiencies[0] = 0;
                    skillCount++;
                }
                checkExpendedPoints();
                break;
            case R.id.radio_animalHandling:
                if (checked) {
                    skillProficiencies[1] = 1;
                    skillCount--;
                }
                if (!checked) {
                    skillProficiencies[1] = 0;
                    skillCount++;
                }
                checkExpendedPoints();
                break;
            case R.id.radio_arcana:
                if (checked) {
                    skillProficiencies[2] = 1;
                    skillCount--;
                }
                if (!checked) {
                    skillProficiencies[2] = 0;
                    skillCount++;
                }
                checkExpendedPoints();
                break;
            case R.id.radio_athletics:
                if (checked) {
                    skillProficiencies[3] = 1;
                    skillCount--;
                }
                if (!checked) {
                    skillProficiencies[3] = 0;
                    skillCount++;
                }
                checkExpendedPoints();
                break;
            case R.id.radio_deception:
                if (checked) {
                    skillProficiencies[4] = 1;
                    skillCount--;
                }
                if (!checked) {
                    skillProficiencies[4] = 0;
                    skillCount++;
                }
                checkExpendedPoints();
                break;
            case R.id.radio_history:
                if (checked) {
                    skillProficiencies[5] = 1;
                    skillCount--;
                }
                if (!checked) {
                    skillProficiencies[5] = 0;
                    skillCount++;
                }
                checkExpendedPoints();
                break;
            case R.id.radio_insight:
                if (checked) {
                    skillProficiencies[6] = 1;
                    skillCount--;
                }
                if (!checked) {
                    skillProficiencies[6] = 0;
                    skillCount++;
                }
                checkExpendedPoints();
                break;
            case R.id.radio_intimidation:
                if (checked) {
                    skillProficiencies[7] = 1;
                    skillCount--;
                }
                if (!checked) {
                    skillProficiencies[7] = 0;
                    skillCount++;
                }
                checkExpendedPoints();
                break;
            case R.id.radio_investigation:
                if (checked) {
                    skillProficiencies[8] = 1;
                    skillCount--;
                }
                if (!checked) {
                    skillProficiencies[8] = 0;
                    skillCount++;
                }
                checkExpendedPoints();
                break;
            case R.id.radio_medicine:
                if (checked) {
                    skillProficiencies[9] = 1;
                    skillCount--;
                }
                if (!checked) {
                    skillProficiencies[9] = 0;
                    skillCount++;
                }
                checkExpendedPoints();
                break;
            case R.id.radio_nature:
                if (checked) {
                    skillProficiencies[10] = 1;
                    skillCount--;
                }
                if (!checked) {
                    skillProficiencies[10] = 0;
                    skillCount++;
                }
                checkExpendedPoints();
                break;
            case R.id.radio_perception:
                if (checked) {
                    skillProficiencies[11] = 1;
                    skillCount--;
                }
                if (!checked) {
                    skillProficiencies[11] = 0;
                    skillCount++;
                }
                checkExpendedPoints();
                break;
            case R.id.radio_performance:
                if (checked) {
                    skillProficiencies[12] = 1;
                    skillCount--;
                }
                if (!checked) {
                    skillProficiencies[12] = 0;
                    skillCount++;
                }
                checkExpendedPoints();
                break;
            case R.id.radio_persuasion:
                if (checked) {
                    skillProficiencies[13] = 1;
                    skillCount--;
                }
                if (!checked) {
                    skillProficiencies[13] = 0;
                    skillCount++;
                }
                checkExpendedPoints();
                break;
            case R.id.radio_religion:
                if (checked) {
                    skillProficiencies[14] = 1;
                    skillCount--;
                }
                if (!checked) {
                    skillProficiencies[14] = 0;
                    skillCount++;
                }
                checkExpendedPoints();
                break;
            case R.id.radio_slightOfHand:
                if (checked) {
                    skillProficiencies[15] = 1;
                    skillCount--;
                }
                if (!checked) {
                    skillProficiencies[15] = 0;
                    skillCount++;
                }
                checkExpendedPoints();
                break;
            case R.id.radio_stealth:
                if (checked) {
                    skillProficiencies[16] = 1;
                    skillCount--;
                }
                if (!checked) {
                    skillProficiencies[16] = 0;
                    skillCount++;
                }
                checkExpendedPoints();
                break;
            case R.id.radio_survival:
                if (checked) {
                    skillProficiencies[17] = 1;
                    skillCount--;
                }
                if (!checked) {
                    skillProficiencies[17] = 0;
                    skillCount++;
                }
                checkExpendedPoints();
                break;

        }
    }

    public void calculateSkillModifiers()  //Just a simple example to calculate modifiers, needs more to go off of to be accurate
    {
        for(int i = 0; i < 18; i++)
        {
            if(skillProficiencies[i] == 1)
            {
                skillModifiers[i] += 2;
            }
        }
    }

    //Function that makes a button invisible and disabled
    public void disableButton(Button passButton) {
        passButton.setEnabled(false);
        passButton.setVisibility(View.VISIBLE);
    }

    //Function that makes a button visible and enabled
    public void enableButton(Button passButton) {
        passButton.setEnabled(true);
        passButton.setVisibility(View.VISIBLE);
    }


//    @Produce
//    public SkillProficiencySender proficiencySender() {
//        SkillProficiencySender skillProficiencySender = new SkillProficiencySender(skillProficiencies);
//        return skillProficiencySender;
//    }
//
//    @Subscribe
//    public void getClass(DnDClass dnDClass)
//    {
//        className = dnDClass.getClassName();
//    }

}
