package com.at.gmail.tomeofadventurers.Fragments;

import android.content.Intent;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.at.gmail.tomeofadventurers.Classes.SkillProficiencySender;
import com.at.gmail.tomeofadventurers.R;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;

public class SelectSkillsFragment extends Fragment {

    Button buttonGoToSelectName;
    RadioGroup radioGroup1,radioGroup2;
    RadioButton radioButton;
    Bus BUS;
    //0 = Not Proficient, 1 = Proficient, 2 = Expertise
    int[] skillProficiencies = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}; //19 options for all skills

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_skills, container, false);
        super.onCreate(savedInstanceState);
        buttonGoToSelectName = view.findViewById(R.id.buttonGoToEnterName);


        radioGroup1 = view.findViewById(R.id.radioGroup1);
        radioGroup2 = view.findViewById(R.id.radioGroup2);

        radioGroup1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int radioId = radioGroup1.getCheckedRadioButtonId();

                radioButton = v.findViewById(radioId);
            }
        });



        buttonGoToSelectName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Send the ability scores to the BUS
                BUS.post(proficiencySender());

                //Unregister the BUS
//                BUS.unregister(this);

                //Transfer to Select Name Fragment
//                FragmentManager fragManager = getFragmentManager();
//                fragManager.beginTransaction().replace(R.id.fragment_container, new SelectNameFragment()).commit();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragTrans = fragmentManager.beginTransaction();
                SelectNameFragment frag = new SelectNameFragment();
                fragTrans.replace(R.id.fragment_container, frag);
                fragTrans.commit();
            }

                /*
                Fragment frag = new CharacterSheetFragment();
                FragmentManager fragManager = getActivity().getFragmentManager();
                FragmentTransaction fragTrans = fragManager.beginTransaction();

                fragTrans.replace(R.id.fragment_container, frag);
                fragTrans.commit();
                */

        });
        //disableButton(buttonGoToSelectName);

        return view;
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
