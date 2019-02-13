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
import com.at.gmail.tomeofadventurers.R;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


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
    TextView txtvwDisplayText;
    Spinner spinnerRace;
    Bus BUS;
    //Dialog popupTest;
    Dialog testDialog;
    TextView txtvwPassAttributes;
    Button btnClosePopup;

    //array list variables
    //ArrayList<String> testDescription = new ArrayList<>();
    //ArrayList<String> raceList = new ArrayList<>();

    //string alternatives
    String [] stringRaceList;
    ArrayAdapter<String> raceListAdapter;

    //define the bus Race object
    String busRaceName="NA";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_select_race,container,false);
        super.onCreate(savedInstanceState);

        //Get the instance of the bus
        BUS=BusProvider.getInstance();

        //TextView variables
        txtvwDisplayText = (TextView) view.findViewById(R.id.txtvwJSONResultRace);
        txtvwDisplayText.setText("Initial Setting Text");


        //spinner variables
        spinnerRace = (Spinner) view.findViewById(R.id.spinnerRace);
        addItemsToSpinner();
        spinnerRace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectAndParse(adapterView, i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                txtvwDisplayText.setText("Nothing Selected");
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

        return view;
    }


    //Function that reads and parses json files and sets the text view display text to the descriptions
    public void readAndParse(String assetName) {
        String readJSON = loadJSONFromAsset(assetName);
        txtvwDisplayText.setText(parse_JSON(readJSON));
    }

    //Function for quickly generating a toast message
    public void toastMessage(String message){
        //Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    //Function for loading the JSON
    public String loadJSONFromAsset(String filename) {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            toastMessage("IOException reading JSON");
            return null;
        }
        return json;
    }

    //Function that actually parses the JSON and returns a string of the Race Description
    public String parse_JSON(String jsonFile){
        try{
            //make a JSON object based off of the file that is being read
            JSONObject raceObject = new JSONObject(jsonFile);

            //************Put values inside the Global Variables*************
            //Get the ability scores
            abilityScores[0] = raceObject.getInt("AbilityScore_Strength");
            abilityScores[1] = raceObject.getInt("AbilityScore_Dexterity");
            abilityScores[2] = raceObject.getInt("AbilityScore_Constitution");
            abilityScores[3] = raceObject.getInt("AbilityScore_Intelligence");
            abilityScores[4] = raceObject.getInt("AbilityScore_Wisdom");
            abilityScores[5] = raceObject.getInt("AbilityScore_Charisma");

            //Get the Alignment Array
            JSONArray alignmentArray = raceObject.getJSONArray("Alignment");
            alignmentLength = alignmentArray.length();
            for(int i = 0; i < alignmentArray.length(); i++){
                alignment[i] = alignmentArray.getString(i);
            }

            //Get the Speed
            speed = raceObject.getInt("Speed");

            //Get the abilities and ability Description
            JSONArray abilitiesArray = raceObject.getJSONArray("Abilities");
            abilityLength = abilitiesArray.length();
            for(int i = 0; i < abilitiesArray.length(); i++){
                //Create Internal Object
                JSONObject abilitiesObject = abilitiesArray.getJSONObject(i);

                //Set the stuff
                ability[i] = abilitiesObject.getString("AbilityName");
                abilityDescription[i] = abilitiesObject.getString("AbilityDescription");
            }

            //Get the Languages
            JSONArray languagesArray = raceObject.getJSONArray("Languages");
            languagesLength = languagesArray.length();
            for(int i = 0; i < languagesArray.length(); i++){
                if (languagesArray.getString(i) == "null"){
                    //don't assign anything
                }else{
                    languages[i] = languagesArray.getString(i);
                }
            }
            //***********Done putting values in Global Variables****************

            //set temporary variable for parsing
            String raceDescription;

            //if there is a subrace, return description + subrace description else return just the description
            if (raceObject.getBoolean("isSubrace") == true){
                raceDescription = raceObject.getString("Description") + "\n\n" + raceObject.getString("SubRaceDescription");
            }
            else{
                raceDescription = raceObject.getString("Description");
            }

            //testGlobalValues();

            return raceDescription;
        }
        catch(JSONException e){
            return "Error: JSONException happened";
        }
        //return "Never went through";
    }

    //Function that adds items to the spinner
    public void addItemsToSpinner() {
        //alternate way to make the spinner
        stringRaceList = getResources().getStringArray(R.array.RaceList);
        raceListAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, stringRaceList);
        raceListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRace.setAdapter(raceListAdapter);
    }

    //Function that reads and parses depending on what was selected on the spinner
    public void selectAndParse(AdapterView adapterView, int i){
        //toastMessage("In 'onItemSelected'");

        //test
        String index = adapterView.getItemAtPosition(i).toString();

        //toastMessage(index);

        switch (index){
            case "Nothing Selected":
                txtvwDisplayText.setText("Nothing Selected");
                disableButton(buttonMoreInfo);
                disableButton(buttonToClass);
                break;
            /*case "Dwarf":
                readAndParse("JSONs/RaceJSONs/Dwarf.json");
                break;*/
            case "Hill Dwarf":
                readAndParse("JSONs/RaceJSONs/Dwarf_Hill.json");
                enableButton(buttonMoreInfo);
                enableButton(buttonToClass);
                busRaceName = "Hill Dwarf";
                break;
            case "Mountain Dwarf":
                readAndParse("JSONs/RaceJSONs/Dwarf_Mountain.json");
                enableButton(buttonMoreInfo);
                enableButton(buttonToClass);
                busRaceName = "Mountain Dwarf";
                break;
            /*case "Elf":
                readAndParse("JSONs/RaceJSONs/Elf.json");
                break;*/
            case "High Elf":
                readAndParse("JSONs/RaceJSONs/Elf_High_Elf.json");
                enableButton(buttonMoreInfo);
                enableButton(buttonToClass);
                busRaceName = "High Elf";
                break;
            case "Wood Elf":
                readAndParse("JSONs/RaceJSONs/Elf_Wood_Elf.json");
                enableButton(buttonMoreInfo);
                enableButton(buttonToClass);
                busRaceName = "Wood Elf";
                break;
            case "Dark Elf (Drow)":
                readAndParse("JSONs/RaceJSONs/Elf_Dark_Elf_Drow.json");
                enableButton(buttonMoreInfo);
                enableButton(buttonToClass);
                busRaceName = "Dark Elf";
                break;
            /*case "Halfling":
                readAndParse("JSONs/RaceJSONs/Halfling.json");
                break;*/
            case "Lightfoot Halfling":
                readAndParse("JSONs/RaceJSONs/Halfling_Lightfoot.json");
                enableButton(buttonMoreInfo);
                enableButton(buttonToClass);
                busRaceName = "Lightfoot Halfling";
                break;
            case "Stout Halfling":
                readAndParse("JSONs/RaceJSONs/Halfling_Stout.json");
                enableButton(buttonMoreInfo);
                enableButton(buttonToClass);
                busRaceName = "Stout Halfling";
                break;
            case "Human":
                readAndParse("JSONs/RaceJSONs/Human.json");
                enableButton(buttonMoreInfo);
                enableButton(buttonToClass);
                busRaceName = "Human";
                break;
            case "Dragonborn":
                readAndParse("JSONs/RaceJSONs/Dragonborn.json");
                enableButton(buttonMoreInfo);
                enableButton(buttonToClass);
                busRaceName = "Dragonborn";
                break;
            /*case "Gnome":
                readAndParse("JSONs/RaceJSONs/Gnome.json");
                break;*/
            case "Forest Gnome":
                readAndParse("JSONs/RaceJSONs/Gnome_Forest_Gnome.json");
                enableButton(buttonMoreInfo);
                enableButton(buttonToClass);
                busRaceName = "Forest Gnome";
                break;
            case "Rock Gnome":
                readAndParse("JSONs/RaceJSONs/Gnome_Rock_Gnome.json");
                enableButton(buttonMoreInfo);
                enableButton(buttonToClass);
                busRaceName = "Rock Gnome";
                break;
            case "Half-Elf":
                readAndParse("JSONs/RaceJSONs/Half-Elf.json");
                enableButton(buttonMoreInfo);
                enableButton(buttonToClass);
                busRaceName = "Half-Elf";
                break;
            case "Half-Orc":
                readAndParse("JSONs/RaceJSONs/Half-Orc.json");
                enableButton(buttonMoreInfo);
                enableButton(buttonToClass);
                busRaceName = "Half-Orc";
                break;
            case "Tiefling":
                readAndParse("JSONs/RaceJSONs/Tiefling.json");
                enableButton(buttonMoreInfo);
                enableButton(buttonToClass);
                busRaceName = "Tiefling";
                break;
        }
    }

    //Function that makes a button invisible and disabled
    public void disableButton(Button passButton){
        passButton.setEnabled(false);
        passButton.setVisibility(View.GONE);
    }

    //Function that makes a button visible and enabled
    public void enableButton(Button passButton){
        passButton.setEnabled(true);
        passButton.setVisibility(View.VISIBLE);
    }

    //Function that calls the popup
    public void callPopup(){
        //set the content view
        testDialog.setContentView(R.layout.popup_moreinfo_race);

        //find the text view in the popup
        txtvwPassAttributes = (TextView) testDialog.findViewById(R.id.txtvwMoreInfoRace);

        //set the text view in the popup
        txtvwPassAttributes.setText(internalJSONRace());

        //find and add the close button
        btnClosePopup = (Button) testDialog.findViewById(R.id.btnClose);
        btnClosePopup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
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
        Race race= new Race();
        race.setRaceName(raceName);

        //Alex Code
        race.setSpeed(speed);

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
