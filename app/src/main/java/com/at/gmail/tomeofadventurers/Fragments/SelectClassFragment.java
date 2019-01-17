package com.at.gmail.tomeofadventurers.Fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.at.gmail.tomeofadventurers.Classes.DnDClass;
import com.at.gmail.tomeofadventurers.R;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class SelectClassFragment extends Fragment {
    //Global Variables
    String hitDice;
    int hpAtLVL1;
    String hpHighLVL;
    String proficienciesArmor[] = new String[10];
    String proficienciesWeapons[] = new String[10];
    String proficienciesTools[] = new String[5];
    String proficienciesSavingThrows[] = new String [6];
    String proficienciesSkills[] = new String[19];
    String equipment[] = new String[6];
    String equipmentChoices[] = new String[10];
    String equipmentChoicesInternal[][][] = new String[10][10][10];
    String subClasses[] = new String[10];
    String className="NA";
    //get lengths for all global variables
    int profArmorLength;
    int profWeaponsLength;
    int profToolsLength;
    int profSavingThrowsLength;
    int profSkillsLength;
    int equipLength;
    int equipChoiceLength[] = new int[10];
    int equipChoiceInternalLength[][] = new int[10][10];
    int subClassLength;


    //variables
    Button buttonToClassProperties;
    TextView txtvwDisplayText;
    Spinner spinnerClass;
    Button buttonMoreInfo;
    Bus BUS;
    //Dialog popup test
    Dialog testDialog;
    TextView txtvwPassAttributes;
    Button btnClosePopup;

    //array list variables
    //ArrayList<String> testDescription = new ArrayList<>();
    //ArrayList<String> raceList = new ArrayList<>();

    //string alternatives
    String [] stringClassList;
    ArrayAdapter<String> classListAdapter;

    //bus variables
    String busClassName="NA";

    @Nullable
    @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
       View view=inflater.inflate(R.layout.fragment_select_class,container,false);
       super.onCreate(savedInstanceState);

       //Get the instance of the bus
        BUS=BusProvider.getInstance();

        //TextView variables
        txtvwDisplayText = (TextView) view.findViewById(R.id.txtvwJSONResultClass);
        txtvwDisplayText.setText("Initial Setting Text");


        //spinner variables
        spinnerClass = (Spinner) view.findViewById(R.id.spinnerClass);
        addItemsToSpinner();
        spinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectAndParse(adapterView, i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                txtvwDisplayText.setText("Nothing Selected");
            }
        });


        //Register the BUS
        BUS.register(this);

        buttonToClassProperties = (Button) view.findViewById(R.id.btnToClassPropertiesFragment);
        buttonToClassProperties.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //Post to the BUS before transfering fragments
                className=busClassName;
                BUS.post(sendDnDClass());

                //Unregister the BUS
//                BUS.unregister(this);

                //go to class properties fragment
                Fragment frag = new SelectClassPropertiesFragment();
                FragmentManager fragManager = getFragmentManager();
                android.app.FragmentTransaction fragTrans = fragManager.beginTransaction();

                //Set the Bundle
                Bundle sendData = new Bundle();
                sendData.putInt("subClassLength", subClassLength);
                sendData.putStringArray("subClasses", subClasses);

                frag.setArguments(sendData);
                fragTrans.replace(R.id.fragment_container, frag);
                fragTrans.commit();

                //fragManager.beginTransaction().replace(R.id.fragment_container, new SelectClassPropertiesFragment()).commit();
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
        testDialog = new Dialog(getContext());

       return view;
   }


    public void readAndParse(String assetName) {
        String readJSON = loadJSONFromAsset(assetName);
        txtvwDisplayText.setText(parse_JSON(readJSON));
    }

    public void toastMessage(String message){
        //Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

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

    public String parse_JSON(String jsonFile){
        try{
            JSONObject classObject = new JSONObject(jsonFile);

            //************Put values inside the Global Variables*****************
            //Read SubClass
            JSONArray readSubClass = classObject.getJSONArray("subClass");
            subClassLength = readSubClass.length();
            for(int i = 0; i < subClassLength; i++){
                subClasses[i] = readSubClass.getString(i);
            }

            //Get the hit dice
            hitDice = classObject.getString("HP_Hit_Dice");

            //get the HP at level 1 (one)
            hpAtLVL1 = classObject.getInt("HP_HP_LVL1");

            //Get the HP at a higher level
            hpHighLVL = classObject.getString("HP_HP_HighLVL");

            //Get the Armor Proficiencies
            JSONArray profArmorArray = classObject.getJSONArray("Proficiencies_Armor");
            profArmorLength = profArmorArray.length();
            for(int i = 0; i < profArmorLength; i++){
                proficienciesArmor[i] = profArmorArray.getString(i);
            }

            //Get the Weapon Proficiencies
            JSONArray profWeaponsArray = classObject.getJSONArray("Proficiencies_Weapons");
            profWeaponsLength = profWeaponsArray.length();
            for(int i = 0; i < profWeaponsLength; i++){
                proficienciesWeapons[i] = profWeaponsArray.getString(i);
            }

            //Get the Tools Proficiencies
            JSONArray profToolsArray = classObject.getJSONArray("Proficiencies_Tools");
            profToolsLength = profToolsArray.length();
            for(int i = 0; i < profToolsLength; i++){
                proficienciesTools[i] = profToolsArray.getString(i);
            }

            //Get the Saving Throws Proficiencies
            JSONArray profSavingThrowsArray = classObject.getJSONArray("Proficiencies_SavingThrows");
            profSavingThrowsLength = profSavingThrowsArray.length();
            for(int i = 0; i < profSavingThrowsLength; i++){
                proficienciesSavingThrows[i] = profSavingThrowsArray.getString(i);
            }

            //Get the Skills Proficiencies
            JSONArray profSkillsArray = classObject.getJSONArray("Proficiencies_Skills");
            profSkillsLength = profSkillsArray.length();
            for(int i = 0; i < profSkillsLength; i++){
                proficienciesSkills[i] = profSkillsArray.getString(i);
            }

            //get the Equipment and equipment choices
            JSONArray equipmentArray = classObject.getJSONArray("Equipment");
            equipLength = equipmentArray.length();
            for(int i = 0; i < equipLength; i++){
                //Create internal object
                JSONObject eqChoiceObject = equipmentArray.getJSONObject(i);

                JSONArray eqChoiceArray = eqChoiceObject.getJSONArray("EQ_Choice");
                equipChoiceLength[i] = eqChoiceArray.length();
                for(int j = 0; j < equipChoiceLength[i]; j++){
                    //Create Internal Array
                    JSONArray eqChoiceInternalArray = eqChoiceArray.getJSONArray(j);

                    equipChoiceInternalLength[i][j] = eqChoiceInternalArray.length();
                    for(int k = 0; k < equipChoiceInternalLength[i][j]; k++){
                        equipmentChoicesInternal[i][j][k] = eqChoiceInternalArray.getString(k);
                    }
                    /*
                    JSONArray eqChoiceInternalValuesArray = eqChoiceInternalArray.getJSONArray(j);
                    equipChoiceInternalLength[i][j] = eqChoiceInternalValuesArray.length();
                    for(int k = 0; k < equipChoiceInternalLength[i][j]; k++){
                        //set the stuff
                        equipmentChoicesInternal[j][k] = eqChoiceInternalValuesArray.getString(k);
                    }
                    */
                }
            }

            //************Done putting values in Global Variables****************

            String classDescription = classObject.getString("description") + "\n\n" + classObject.getString("creating_a_class") ;

            return classDescription;
        }
        catch(JSONException e){
            return "Error: JSONException happened";
        }
        //return "Never went through";
    }

    public void addItemsToSpinner() {
        /*List list = new ArrayList();
        raceList.add("Nothing Selected");
        raceList.add("Dwarf");
        raceList.add("Hill Dwarf");
        raceList.add("Mountain Dwarf");
        raceList.add("Elf");
        raceList.add("High Elf");
        raceList.add("Dark Elf (Drow)");
        raceList.add("Halfling");
        raceList.add("Lightfoot Halfling");
        raceList.add("Stout Halfling");
        raceList.add("Human");
        raceList.add("Dragonborn");
        raceList.add("Gnome");
        raceList.add("Forest Gnome");
        raceList.add("Rock Gnome");
        raceList.add("Half-Elf");
        raceList.add("Half-Orc");
        raceList.add("Tiefling");
        ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, raceList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRace.setAdapter(dataAdapter);*/

        //alternate way to make the spinner
        stringClassList = getResources().getStringArray(R.array.ClassList);
        classListAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, stringClassList);
        classListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClass.setAdapter(classListAdapter);
    }

    public void selectAndParse(AdapterView adapterView, int i){
        //toastMessage("In 'onItemSelected'");

        //test
        String index = adapterView.getItemAtPosition(i).toString();

        //toastMessage(index);

        switch (index){
            case "Nothing Selected":
                txtvwDisplayText.setText("Nothing Selected");
                disableButton(buttonMoreInfo);
                disableButton(buttonToClassProperties);
                break;
            case "Barbarian":
                readAndParse("JSONs/ClassJSONs/Barbarian.json");
                enableButton(buttonMoreInfo);
                enableButton(buttonToClassProperties);
                busClassName = "Barbarian";
                break;
            case "Bard":
                readAndParse("JSONs/ClassJSONs/Bard.json");
                enableButton(buttonMoreInfo);
                enableButton(buttonToClassProperties);
                busClassName = "Bard";
                break;
            case "Cleric":
                readAndParse("JSONs/ClassJSONs/Cleric.json");
                enableButton(buttonMoreInfo);
                enableButton(buttonToClassProperties);
                busClassName = "Cleric";
                break;
            case "Druid":
                readAndParse("JSONs/ClassJSONs/Druid.json");
                enableButton(buttonMoreInfo);
                enableButton(buttonToClassProperties);
                busClassName = "Druid";
                break;
            case "Fighter":
                readAndParse("JSONs/ClassJSONs/Fighter.json");
                enableButton(buttonMoreInfo);
                enableButton(buttonToClassProperties);
                busClassName = "Fighter";
                break;
            case "Monk":
                readAndParse("JSONs/ClassJSONs/Monk.json");
                enableButton(buttonMoreInfo);
                enableButton(buttonToClassProperties);
                busClassName = "Monk";
                break;
            case "Paladin":
                readAndParse("JSONs/ClassJSONs/Paladin.json");
                enableButton(buttonMoreInfo);
                enableButton(buttonToClassProperties);
                busClassName = "Paladin";
                break;
            case "Ranger":
                readAndParse("JSONs/ClassJSONs/Ranger.json");
                enableButton(buttonMoreInfo);
                enableButton(buttonToClassProperties);
                busClassName = "Ranger";
                break;
            case "Rogue":
                readAndParse("JSONs/ClassJSONs/Rogue.json");
                enableButton(buttonMoreInfo);
                enableButton(buttonToClassProperties);
                busClassName = "Rogue";
                break;
            case "Sorcerer":
                readAndParse("JSONs/ClassJSONs/Sorcerer.json");
                enableButton(buttonMoreInfo);
                enableButton(buttonToClassProperties);
                busClassName = "Sorcerer";
                break;
            case "Warlock":
                readAndParse("JSONs/ClassJSONs/Warlock.json");
                enableButton(buttonMoreInfo);
                enableButton(buttonToClassProperties);
                busClassName = "Warlock";
                break;
            case "Wizard":
                readAndParse("JSONs/ClassJSONs/Wizard.json");
                enableButton(buttonMoreInfo);
                enableButton(buttonToClassProperties);
                busClassName = "Wizard";
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
        txtvwPassAttributes.setText(internalJSONClass());

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
    public String internalJSONClass(){
        String internalString;

        //Add SubClasses
        String showSubClasses = "Sub Classes: ";
        for (int i = 0; i < subClassLength; i++){
            if(i == (subClassLength - 1)){
                showSubClasses = showSubClasses + subClasses[i] + "\n\n";
            }else{
                showSubClasses = showSubClasses + subClasses[i] + ", ";
            }
        }

        //Add the Hit Dice
        String strHitDice = "Hit Dice: " + hitDice + "\n\n";

        //Add the HP at Level 1
        String strHPLVL1 = "HP @ LVL 1: " + hpAtLVL1 + "\n\n";

        //Add the HP at High Level
        String strHPHIGHLVL = "HP after Level 1: " + hpHighLVL + "\n\n";

        //Add armor proficiencies
        String showProfArmor = "Armor Proficiencies: ";
        for (int i = 0; i < profArmorLength; i++){
            if(i == (profArmorLength - 1)){
                showProfArmor = showProfArmor + proficienciesArmor[i] + "\n\n";
            }else{
                showProfArmor = showProfArmor + proficienciesArmor[i] + ", ";
            }
        }

        //Add weapons proficiencies
        String showProfWeapons = "Weapons Proficiencies: ";
        for (int i = 0; i < profWeaponsLength; i++){
            if(i == (profWeaponsLength - 1)){
                showProfWeapons = showProfWeapons + proficienciesWeapons[i] + "\n\n";
            }else{
                showProfWeapons = showProfWeapons + proficienciesWeapons[i] + ", ";
            }
        }

        //Add Tools proficiencies
        String showProfTools = "Tool Proficiencies: ";
        for (int i = 0; i < profToolsLength; i++){
            if(i == (profToolsLength - 1)){
                showProfTools = showProfTools + proficienciesTools[i] + "\n\n";
            }else{
                showProfTools = showProfTools + proficienciesTools[i] + ", ";
            }
        }

        //Add Saving Throws
        String showProfSaveThrow = "Saving Throw Proficiencies: ";
        for (int i = 0; i < profSavingThrowsLength; i++){
            if(i == (profSavingThrowsLength - 1)){
                showProfSaveThrow = showProfSaveThrow + proficienciesSavingThrows[i] + "\n\n";
            }else{
                showProfSaveThrow = showProfSaveThrow + proficienciesSavingThrows[i] + ", ";
            }
        }

        //Add skills proficiencies
        String showProfSkills = "Skill Proficiencies: \nChoose (";
        for (int i = 0; i < profSkillsLength; i++) {
            if (i == 0){
                showProfSkills = showProfSkills + proficienciesSkills[i] + ") from ";
            }else if(i == (profSkillsLength - 1)) {
                showProfSkills = showProfSkills + proficienciesSkills[i] + "\n\n";
            }else{
                showProfSkills = showProfSkills + proficienciesSkills[i] + ", ";
            }
        }

        //Add Equipment choices
        String showEquipment = "Equipment:\n";
        for (int i = 0; i < equipLength; i++){
            showEquipment = showEquipment + "Choice " + (i + 1) + ": ";

            for (int j = 0; j < equipChoiceLength[i]; j++){
                for(int k = 0; k < equipChoiceInternalLength[i][j]; k++){
                    if (k == (equipChoiceInternalLength[i][j] - 1)){
                        showEquipment = showEquipment + equipmentChoicesInternal[i][j][k];
                    }else{
                        showEquipment = showEquipment + equipmentChoicesInternal[i][j][k] + ", ";
                    }
                }

                if(j == (equipChoiceLength[i] - 1)){
                    showEquipment = showEquipment + "\n";
                }else{
                    showEquipment = showEquipment + " or ";
                }

            }

            showEquipment = showEquipment + "\n";
        }


        internalString = showSubClasses + strHitDice + strHPLVL1 + strHPHIGHLVL + showProfArmor + showProfWeapons + showProfTools + showProfSaveThrow + showProfSkills + showEquipment;
        return internalString;
    }

    //Here is a function that will produce a race.
    // Pass it a string that holds the name of the race you are trying to pass.
    // You will want to call it in the format when you switch to the next stage
    //  BUS.register(this)
    //  BUS.post(sendDnDClass("CLASSNAME"))
    //  BUS.unregister(this)
    @Produce
    public DnDClass sendDnDClass()
    {
        DnDClass dnDClass = new DnDClass();
        dnDClass.setClassName(className);

        //Alex Code
        dnDClass.setHP(hpAtLVL1);
        dnDClass.setHitDice(hitDice);

        return dnDClass;
    }

}
