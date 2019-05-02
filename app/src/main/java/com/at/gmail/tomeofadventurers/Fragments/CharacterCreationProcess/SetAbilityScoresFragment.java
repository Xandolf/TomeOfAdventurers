package com.at.gmail.tomeofadventurers.Fragments.CharacterCreationProcess;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.at.gmail.tomeofadventurers.Classes.CharacterDBAccess;
import com.at.gmail.tomeofadventurers.Classes.SubRaceDatabaseAccess;
import com.at.gmail.tomeofadventurers.R;

//
///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link SetAbilityScoresFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link SetAbilityScoresFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class SetAbilityScoresFragment extends Fragment {

    EditText editTextStr,editTextDex,editTextCon,editTextInt,editTextWis,editTextCha;
    Button buttonCalculateModifiers, buttonGoToSelectSkills;
    TextView textViewStrModifier, textViewDexModifier, textViewConModifier, textViewIntModifier, textViewWisModifier, textViewChaModifier;
    boolean validInput;
    int [] abilityScores={0,0,0,0,0,0};
    int [] abilityScoreModifiers;
    int[] abilityScoreBonuses;
    View view;

    CharacterDBAccess characterDBAccess;
    SubRaceDatabaseAccess subRaceDatabaseAccess;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_set_ability_scores, container, false);
        super.onCreate(savedInstanceState);

        characterDBAccess = CharacterDBAccess.getInstance(getContext());
        characterDBAccess.open();
        String subRaceId = characterDBAccess.loadCharacterSubRace();
        characterDBAccess.close();
        subRaceDatabaseAccess = SubRaceDatabaseAccess.getInstance(getContext());
        subRaceDatabaseAccess.open();
        abilityScoreBonuses = subRaceDatabaseAccess.getTotalAbilityScoreBonusesForSubrace(subRaceId);
        subRaceDatabaseAccess.close();

        //Initialize the text views. A lot of this should be moved to a recycler view but this is a
        //'fast' n sloppy implementation
        validInput=false;
        editTextStr = view.findViewById(R.id.editTextStrScore);
        editTextDex = view.findViewById(R.id.editTextDexScore);
        editTextCon = view.findViewById(R.id.editTextConScore);
        editTextInt = view.findViewById(R.id.editTextIntScore);
        editTextWis = view.findViewById(R.id.editTextWisScore);
        editTextCha = view.findViewById(R.id.editTextChaScore);
        buttonCalculateModifiers = view.findViewById(R.id.buttonCalculateModifiers);
        textViewStrModifier = view.findViewById(R.id.textViewStrModifier);
        textViewDexModifier = view.findViewById(R.id.textViewDexModifier);
        textViewConModifier = view.findViewById(R.id.textViewConModifier);
        textViewIntModifier = view.findViewById(R.id.textViewIntModifier);
        textViewWisModifier= view.findViewById(R.id.textViewWisModifier);
        textViewChaModifier = view.findViewById(R.id.textViewChaModifier);
        buttonGoToSelectSkills = view.findViewById(R.id.buttonGoToSelectSkills);
        TextView textViewRacialBonusStr = view.findViewById(R.id.textViewRacialBonusStr);
        TextView textViewRacialBonusDex = view.findViewById(R.id.textViewRacialBonusDex);
        TextView textViewRacialBonusCon = view.findViewById(R.id.textViewRacialBonusCon);
        TextView textViewRacialBonusInt = view.findViewById(R.id.textViewRacialBonusInt);
        TextView textViewRacialBonusWis = view.findViewById(R.id.textViewRacialBonusWis);
        TextView textViewRacialBonusCha = view.findViewById(R.id.textViewRacialBonusCha);
        String text = "+"+String.valueOf(abilityScoreBonuses[0]);
        textViewRacialBonusStr.setText(text);
        text = "+"+String.valueOf(abilityScoreBonuses[1]);
        textViewRacialBonusDex.setText(text);
        text = "+"+String.valueOf(abilityScoreBonuses[2]);
        textViewRacialBonusCon.setText(text);
        text = "+"+String.valueOf(abilityScoreBonuses[3]);
        textViewRacialBonusInt.setText(text);
        text = "+"+String.valueOf(abilityScoreBonuses[4]);
        textViewRacialBonusWis.setText(text);
        text = "+"+String.valueOf(abilityScoreBonuses[5]);
        textViewRacialBonusCha.setText(text);


        buttonCalculateModifiers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validInput=true;
                String stringMod="";
                String stringScore="";
                int mod = 0;
                int score = 0;
                if(editTextStr.getText()!=null)
                {
                    stringScore = editTextStr.getText().toString();
                    score = Integer.parseInt(stringScore);
                    abilityScores[0]=score+abilityScoreBonuses[0];
                    mod = (abilityScores[0]-10)/2;
                    if(mod>=0) stringMod = "+";
                    stringMod += Integer.toString(mod);
                    textViewStrModifier.setText(stringMod);
                }
                else validInput=false;
                stringMod="";

                if(editTextDex.getText()!=null)
                {
                    stringScore = editTextDex.getText().toString();
                    score = Integer.parseInt(stringScore);
                    abilityScores[1]=score+abilityScoreBonuses[1];
                    mod = (abilityScores[1]-10)/2;
                    if(mod>=0) stringMod = "+";
                    stringMod += Integer.toString(mod);
                    textViewDexModifier.setText(stringMod);
                }
                else validInput=false;
                stringMod="";

                if(editTextCon.getText()!=null)
                {
                    stringScore = editTextCon.getText().toString();
                    score = Integer.parseInt(stringScore);
                    abilityScores[2]=score+abilityScoreBonuses[2];
                    mod = (abilityScores[2]-10)/2;
                    if(mod>=0) stringMod = "+";
                    stringMod += Integer.toString(mod);
                    textViewConModifier.setText(stringMod);
                }
                else validInput=false;
                stringMod="";

                if(editTextInt.getText()!=null)
                {
                    stringScore = editTextInt.getText().toString();
                    score = Integer.parseInt(stringScore);
                    abilityScores[3]=score+abilityScoreBonuses[3];
                    mod = (abilityScores[3]-10)/2;
                    if(mod>=0) stringMod = "+";
                    stringMod += Integer.toString(mod);
                    textViewIntModifier.setText(stringMod);
                }
                else validInput=false;
                stringMod="";

                if(editTextWis.getText()!=null)
                {
                    stringScore = editTextWis.getText().toString();
                    score = Integer.parseInt(stringScore);
                    abilityScores[4]=score+abilityScoreBonuses[4];
                    mod = (abilityScores[4]-10)/2;
                    if(mod>=0) stringMod = "+";
                    stringMod += Integer.toString(mod);
                    textViewWisModifier.setText(stringMod);
                }
                else validInput=false;
                stringMod="";

                if(editTextCha.getText()!=null)
                {
                    stringScore = editTextCha.getText().toString();
                    score = Integer.parseInt(stringScore);
                    abilityScores[5]=score+abilityScoreBonuses[5];
                    mod = (abilityScores[5]-10)/2;
                    if(mod>=0) stringMod = "+";
                    stringMod += Integer.toString(mod);
                    textViewChaModifier.setText(stringMod);
                }
                else validInput=false;

                if(!validInput)
                {
                    //Toast.makeText(getActivity(),"There were errors!",Toast.LENGTH_SHORT).show();
                }

                //enable button after calculating
                enableButton(buttonGoToSelectSkills);
            }
        });//end OnClickListener


        //Go to Select Skills
        buttonGoToSelectSkills.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                abilityScoreModifiers = getAllAbilityScoreModifiers();
                CharacterDBAccess characterDBAccess;
                characterDBAccess = CharacterDBAccess.getInstance(getContext());
                characterDBAccess.open();

                characterDBAccess.saveAbilityScores(abilityScores);
                characterDBAccess.saveAbilityScoresModifiers(abilityScoreModifiers);

                characterDBAccess.close();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragTrans = fragmentManager.beginTransaction();
                SelectSkillsFragment frag = new SelectSkillsFragment();
                fragTrans.replace(R.id.fragment_container, frag);
                fragTrans.commit();
            }
        });


        //disable the button to proceed
        disableButton(buttonGoToSelectSkills);

        return view;
    }//end OnCreateView

    @Override
    public void onPause(){
        super.onPause();
    }

    public int[] getAllAbilityScoreModifiers()
    {
        int abilityScoreModifiers[] = new int[6];
        for (int i = 0; i < 6; i++)
        {
            abilityScoreModifiers[i] = (abilityScores[i] - 10) / 2;
        }
        return abilityScoreModifiers;
    }


    //Function that makes a button invisible and disabled
    public void disableButton(Button passButton){
        passButton.setEnabled(false);
        passButton.setVisibility(View.GONE);
    }

    //Function that makes a button visible and enabled
    public void enableButton(Button passButton){
        passButton.setEnabled(true);
        view.clearFocus();
        passButton.setVisibility(View.VISIBLE);
    }


//
//    I will want to call this function when they press the next button.
//    I should make sure that all the values are valid first.
//    the procedure is to register with the BUS. Post the producing function Then unregistering.
//    @Produce
//    public AbilityScoreSender sendAbilityScores()
//    {
//       return new AbilityScoreSender(abilityScores);
//    }
}
