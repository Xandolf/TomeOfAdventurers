package com.at.gmail.tomeofadventurers.Fragments;

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

import com.at.gmail.tomeofadventurers.Classes.BusProvider;
import com.at.gmail.tomeofadventurers.Classes.SkillProficiencySender;
import com.at.gmail.tomeofadventurers.R;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;

public class SelectSkillsFragment extends Fragment implements View.OnClickListener{

    Button buttonGoToSelectName;
    CheckBox acrobaticsButton,animalHandlingButton, arcanaButton,athleticsButton, deceptionButton,
            historyButton,insightButton,intimidationButton,investigationButton,medicineButton,natureButton,
            perceptionButton,performanceButton,persuasionButton, religionButton, slightOfHandButton,
            stealthButton, survivalButton;

    Bus BUS;
    //0 = Not Proficient, 1 = Proficient, 2 = Expertise
    int[] skillProficiencies = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}; //19 options for all skills


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_select_skills, container, false);
        super.onCreate(savedInstanceState);

        buttonGoToSelectName = view.findViewById(R.id.buttonGoToEnterName);
        //Get the instance of the bus
        BUS = BusProvider.getInstance();
        BUS.register(this);


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
                BUS.post(proficiencySender());

                //Unregister the BUS
//                BUS.unregister(this);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragTrans = fragmentManager.beginTransaction();
                SelectNameFragment frag = new SelectNameFragment();
                fragTrans.replace(R.id.fragment_container, frag);
                fragTrans.commit();
            }

        });
        //disableButton(buttonGoToSelectName);




        acrobaticsButton.setChecked(true);
        acrobaticsButton.setEnabled(false);

        animalHandlingButton.setEnabled(false);


        return view;
    }

    public void onClick(View v){

        boolean checked = ((CheckBox) v).isChecked();






        switch (v.getId()) {
            case R.id.radio_acrobatics:
                if(checked)
                    break;
            case R.id.radio_animalHandling:
                if(checked)
                    break;
            case R.id.radio_arcana:
                if(checked)
                    break;
            case R.id.radio_athletics:
                if(checked)
                    break;
            case R.id.radio_deception:
                if(checked)
                    break;
            case R.id.radio_history:
                if(checked)
                    break;
            case R.id.radio_insight:
                if(checked)
                    break;
            case R.id.radio_intimidation:
                if(checked)
                    break;
            case R.id.radio_investigation:
                if(checked)
                    break;
            case R.id.radio_medicine:
                if(checked)
                    break;
            case R.id.radio_nature:
                if(checked)
                    break;
            case R.id.radio_perception:
                if(checked)
                    break;
            case R.id.radio_performance:
                if(checked)
                    break;
            case R.id.radio_persuasion:
                if(checked)
                    break;
            case R.id.radio_religion:
                if(checked)
                    break;
            case R.id.radio_slightOfHand:
                if(checked)
                    break;
            case R.id.radio_stealth:
                if(checked)
                    break;
            case R.id.radio_survival:
                if(checked)
                    break;

        }
    }

    //Function that makes a button invisible and disabled
    public void disableButton(Button passButton){
        passButton.setEnabled(false);
        passButton.setVisibility(View.VISIBLE);
    }

    //Function that makes a button visible and enabled
    public void enableButton(Button passButton){
        passButton.setEnabled(true);
        passButton.setVisibility(View.VISIBLE);
    }

    @Produce
    SkillProficiencySender proficiencySender ()
    {
        SkillProficiencySender skillProficiencySender = new SkillProficiencySender(skillProficiencies);
        return skillProficiencySender;
    }

}
