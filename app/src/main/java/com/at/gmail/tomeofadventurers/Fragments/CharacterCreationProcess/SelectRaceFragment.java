package com.at.gmail.tomeofadventurers.Fragments.CharacterCreationProcess;

import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.at.gmail.tomeofadventurers.Classes.CharacterDBAccess;
import com.at.gmail.tomeofadventurers.Classes.Race;
import com.at.gmail.tomeofadventurers.Classes.RaceDatabaseAccess;
import com.at.gmail.tomeofadventurers.Classes.SubRaceDatabaseAccess;
import com.at.gmail.tomeofadventurers.R;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;



public class SelectRaceFragment extends Fragment {
    //Global Variables
//    int abilityScores[] = new int[6];

    //variables
    Button buttonToClass, buttonMoreInfo;
    TextView textViewDisplayText;
    Spinner spinnerRace, spinnerSubRace;
    Bus BUS;
    String[] raceIds;
    String[] subRaceIds;
    String[] subRaceNames;
    String[] stringRaceList;
    String selectedRaceId;
    String selectedSubRaceId;
    String selectedRaceName;
    String selectedSubRaceName;

    SubRaceDatabaseAccess subRaceDatabaseAccess;
    RaceDatabaseAccess raceDatabaseAccess;

    ArrayAdapter<String> subRaceListAdapter;

    //Dialog popupTest;
    Dialog testDialog;
    TextView textViewPassAttributes;
    Button buttonClosePopup;

    //string alternatives
    ArrayAdapter<String> raceListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_select_race,container,false);
        super.onCreate(savedInstanceState);

        //Get the instance of the bus
//        BUS = BusProvider.getInstance();
//        BUS.register(this);

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


        //button variables
        buttonToClass = (Button) view.findViewById(R.id.btnToClassFragment);
        buttonToClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                BUS.post(sendRace());
                CharacterDBAccess characterDBAccess;
                characterDBAccess = CharacterDBAccess.getInstance(getContext());
                characterDBAccess.open();

                characterDBAccess.saveRace(selectedRaceName);
                characterDBAccess.saveSubRace(selectedSubRaceId);

                characterDBAccess.close();

                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragTrans = fragmentManager.beginTransaction();
                SelectClassFragment frag = new SelectClassFragment();
                fragTrans.replace(R.id.fragment_container, frag);
                fragTrans.commit();

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


        spinnerSubRace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                selectedSubRaceName = subRaceNames[i];
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

    @Override
    public void onPause(){
//        BUS.unregister(this);
        super.onPause();
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
        stringRaceList = raceDatabaseAccess.getRaceNames(raceIds);

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

        selectedRaceName = stringRaceList[i];

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

    //Here is a function that will produce a race.
    // Pass it a string that holds the name of the race you are trying to pass.
    // You will want to call it in the format when you switch to the next stage
    //  BUS.register(this)
    //  BUS.post(sendRace("RACENAME"))
    //  BUS.unregister(this)
//    @Produce
//    public Race sendRace()
//    {
//        Race race = new Race();
//        race.setRaceName(selectedRaceId);
//        race.setSubRaceId(selectedSubRaceId);
//        return race;
//    }


}
