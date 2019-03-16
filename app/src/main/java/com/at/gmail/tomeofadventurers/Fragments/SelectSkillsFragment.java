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

import com.at.gmail.tomeofadventurers.Activities.MainActivity;
import com.at.gmail.tomeofadventurers.Classes.BusProvider;
import com.at.gmail.tomeofadventurers.R;
import com.squareup.otto.Bus;

public class SelectSkillsFragment extends Fragment {

    Button buttonGoToSelectName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_skills, container, false);
        super.onCreate(savedInstanceState);
        buttonGoToSelectName = view.findViewById(R.id.buttonGoToEnterName);
        //enableButton(buttonGoToSelectName);
        //Go to Set Name
        buttonGoToSelectName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Send the ability scores to the BUS
                //BUS.post(sendAbilityScores());

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
}
