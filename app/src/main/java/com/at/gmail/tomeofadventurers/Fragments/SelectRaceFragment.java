package com.at.gmail.tomeofadventurers.Fragments;

import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.at.gmail.tomeofadventurers.Classes.BusProvider;
import com.at.gmail.tomeofadventurers.Classes.Race;
import com.at.gmail.tomeofadventurers.Classes.RaceDatabaseAccess;
import com.at.gmail.tomeofadventurers.Classes.SubRaceDatabaseAccess;
import com.at.gmail.tomeofadventurers.R;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;



public class SelectRaceFragment extends Fragment {
    //Global Variables
    int abilityScores[] = new int[6];
    String alignment[] = new String[9];
    int speed=30;
    String ability[] = new String[10];
    String abilityDescription[] = new String[10];
    String languages[] = new String[16];
    String raceName="NA";
    //Get lengths for all global variables
    int alignmentLength;
    int abilityLength;
    int languagesLength;

    //variables
    Button buttonToClass;
    Button buttonMoreInfo;
    TextView textViewDisplayText;
    Spinner spinnerRace, spinnerSubRace;
    Bus BUS;
    String[] raceIds;
    String selectedRaceId;

    SubRaceDatabaseAccess subRaceDatabaseAccess;
    String[] subRaceIds;
    String[] subRaceNames;
    String selectedSubRaceId;
    ArrayAdapter<String> subRaceListAdapter;

    RaceDatabaseAccess raceDatabaseAccess;

    //Dialog popupTest;
    Dialog testDialog;
    TextView textViewPassAttributes;
    Button buttonClosePopup;

    //string alternatives
    String[] stringRaceList;
    ArrayAdapter<String> raceListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_select_race,container,false);
        super.onCreate(savedInstanceState);

        //Get the instance of the bus
        BUS = BusProvider.getInstance();

        //TextView variables
        textViewDisplayText = (TextView) view.findViewById(R.id.txtvwJSONResultRace);
        textViewDisplayText.setText("Initial Setting Text");

        //spinner variables
        spinnerRace = (Spinner) view.findViewById(R.id.spinnerRace);
        addItemsToSpinner();
        spinnerSubRace = view.findViewById(R.id.spinnerSubRace);

        spinnerRace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                selectAndParse(adapterView, i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                textViewDisplayText.setText("Nothing Selected");
            }
        });

        //popupTest.setContentView(R.layout.popup_test);

        //button variables
        buttonToClass = (Button) view.findViewById(R.id.btnToClassFragment);
        buttonToClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Alternate way to change fragment window
                /*
                Fragment frag = new SelectClassFragment();
                FragmentManager fragManager = getFragmentManager();
                fragManager.beginTransaction().replace(R.id.fragment_container, new SelectClassFragment()).commit();
                */
                //Register the BUS
                BUS.register(this);

                //Send the Race Name to the BUS
                raceName = busRaceName;
                BUS.post(sendRace());

                //Unregister the BUS
//                BUS.unregister(this);


                //Set the fragment before the move is made
//                Fragment frag = new SelectRacePropertiesFragment();
//                FragmentManager fragManager = getFragmentManager();
//                android.app.FragmentTransaction fragTrans = fragManager.beginTransaction();

                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragTrans = fragmentManager.beginTransaction();
                SelectRacePropertiesFragment frag = new SelectRacePropertiesFragment();
                fragTrans.replace(R.id.fragment_container, frag);
                fragTrans.commit();

                //set the bundles
                Bundle sendData = new Bundle();
                sendData.putIntArray("passAbilityScores", abilityScores);
                sendData.putStringArray("passAlignment", alignment);
                sendData.putInt("passAlignmentLength", alignmentLength);
                sendData.putInt("passSpeed", speed);
                sendData.putStringArray("passAbility", ability);
                sendData.putInt("passAbilityLength", abilityLength);
                sendData.putStringArray("passLanguages", languages);
                sendData.putInt("passLanguagesLength", languagesLength);

                //begin transaction with arguments
                frag.setArguments(sendData);
//                fragTrans.replace(R.id.fragment_container, frag);
////                fragTrans.addToBackStack(null);
//                fragTrans.commit();

            }
        });

        buttonMoreInfo = (Button) view.findViewById(R.id.btnMoreInfo);
        buttonMoreInfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                callPopup();
            }
        });
        disableButton(buttonMoreInfo);

        //********************TESTING POPUP*************************
        //testDialog = new Dialog(getContext());

        spinnerSubRace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                selectedSubRaceId=subRaceIds[i];
                enableButton(buttonToClass);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                textViewDisplayText.setText("Nothing Selected");
            }
        });




        return view;
    }

    //Function for quickly generating a toast message
    public void toastMessage(String message)
    {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    //Function that adds items to the spinner
    public void addItemsToSpinner()
    {
        raceDatabaseAccess = RaceDatabaseAccess.getInstance(this.getContext());
        raceDatabaseAccess.open();

        raceIds = raceDatabaseAccess.getRaceKeys();
        String[] stringRaceList = raceDatabaseAccess.getRaceNames(raceIds);

        raceListAdapter = new ArrayAdapter<>(this.getActivity(),
                                             android.R.layout.simple_spinner_item,
                                             stringRaceList);
        raceListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRace.setAdapter(raceListAdapter);
    }

    //Function that reads and parses depending on what was selected on the spinner
    public void selectAndParse(AdapterView adapterView, int i)
    {
        selectedRaceId = raceIds[i];
        String alignmentText = raceDatabaseAccess.getDescriptionOfRace(selectedRaceId);

        textViewDisplayText.setText(alignmentText);
        subRaceDatabaseAccess = SubRaceDatabaseAccess.getInstance(this.getContext());
        subRaceDatabaseAccess.open();
        subRaceIds = subRaceDatabaseAccess.getSubRaceIdsFor(selectedRaceId);
        subRaceNames = subRaceDatabaseAccess.getSubraceNames(subRaceIds);
        subRaceListAdapter = new ArrayAdapter<String>(this.getActivity(),
                                                      android.R.layout.simple_spinner_item,
                                                      subRaceNames);
        subRaceListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerSubRace.setAdapter(subRaceListAdapter);
    }

    //Function that makes a button invisible and disabled
    public void disableButton(Button passButton)
    {
        passButton.setEnabled(false);
        passButton.setVisibility(View.GONE);
    }

    //Function that makes a button visible and enabled
    public void enableButton(Button passButton)
    {
        passButton.setEnabled(true);
        passButton.setVisibility(View.VISIBLE);
    }

    //Function that calls the popup
    public void callPopup()
    {
        //set the content view
        testDialog.setContentView(R.layout.popup_moreinfo_race);

        //find the text view in the popup
        textViewPassAttributes = (TextView) testDialog.findViewById(R.id.txtvwMoreInfoRace);


        //find and add the close button
        buttonClosePopup = (Button) testDialog.findViewById(R.id.btnClose);
        buttonClosePopup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                testDialog.dismiss();
            }
        });

        testDialog.show();
    }

    //Function that puts all of the internal JSON data in a string so it can be put in a popup
    public String internalJSONRace(){
        String internalString;

        //Add the Ability Scores
        String abilityScoreString = "Ability Scores:\n" + "Strength = " + abilityScores[0] + "\n" + "Dexterity = " + abilityScores[1] + "\n" + "Constitution = " + abilityScores[2] + "\n" + "Intelligence = " + abilityScores[3] + "\n" + "Wisdom = " + abilityScores[4] + "\n" + "Charisma = " + abilityScores[5] + "\n\n";

        //Add Alignment
        String showAlignment = "";
        for (int i = 0; i < alignmentLength; i++){
            if(i == (alignmentLength - 1)){
                showAlignment = showAlignment + alignment[i];

            }else{
                showAlignment = showAlignment + alignment[i] + "," + "\n";
            }
        }
        String alignmentString = "Alignment:\n" + showAlignment + "\n\n";

        //Add Speed
        String speedString = "Speed: " + speed + "\n\n";

        //Add abilities and ability description
        String showAbilities = "";
        for (int i = 0; i < abilityLength; i++){
            if(i == (abilityLength - 1)){
                showAbilities = showAbilities + "Ability Name: " + ability[i] + "\n" + "Ability Description: " + abilityDescription[i];
            }else{
                showAbilities = showAbilities + "Ability Name: " + ability[i] + "\n" + "Ability Description: " + abilityDescription[i] + "\n\n";
            }

        }
        String abilitiesString = showAbilities + "\n\n";

        //Add languages
        String showLanguage = "";
        for (int i = 0; i < languagesLength; i++){
            if(i == (languagesLength - 1)){
                showLanguage = showLanguage + languages[i];

            }else{
                showLanguage = showLanguage + languages[i] + "," + "\n";
            }
        }
        String languageString = "Languages: \n" + showLanguage;

        internalString = abilityScoreString + alignmentString + speedString + abilitiesString + languageString;

        return internalString;
    }

    //Here is a function that will produce a race.
    // Pass it a string that holds the name of the race you are trying to pass.
    // You will want to call it in the format when you switch to the next stage
    //  BUS.register(this)
    //  BUS.post(sendRace("RACENAME"))
    //  BUS.unregister(this)
    @Produce
    public Race sendRace()
    {
        Race race = new Race();
        race.setRaceName(selectedRaceId);
        return race;
    }


    //**********************TEST FUNCTIONS************************
    //Function that will toast out all the global variables
    public void testGlobalValues(){
        //Show ability scores
        toastMessage("Ability Score Strength = " + abilityScores[0] + "\n" + "Ability Score Dexterity = " + abilityScores[1] + "\n" + "Ability Score Constitution = " + abilityScores[2] + "\n" + "Ability Score Intelligence = " + abilityScores[3] + "\n" + "Ability Score Wisdom = " + abilityScores[4] + "\n" + "Ability Score Charisma = " + abilityScores[5] + "\n");

        //Show alignment
        String showAlignment = "";
        for (int i = 0; i < alignmentLength; i++){
            if(i == (alignmentLength - 1)){
                showAlignment = showAlignment + alignment[i];

            }else{
                showAlignment = showAlignment + alignment[i] + "," + "\n";
            }
        }
        toastMessage(showAlignment);

        //Show Speed
        toastMessage("Speed = " + speed);

        //show abilities and ability description
        for (int i = 0; i < abilityLength; i++){
            toastMessage("Ability Name: " + ability[i] + "\n" + "Ability Description: " + abilityDescription[i]);
        }

        //show languages
        String showLanguage = "";
        for (int i = 0; i < languagesLength; i++){
            if(i == (languagesLength - 1)){
                showLanguage = showLanguage + languages[i];

            }else{
                showLanguage = showLanguage + languages[i] + "," + "\n";
            }
        }
        toastMessage(showLanguage);
    }

    public int[] getAbilityScores(){
        return new int[]{abilityScores[0], abilityScores[1], abilityScores[2], abilityScores[3], abilityScores[4], abilityScores[5]};
    }


}
