package com.at.gmail.tomeofadventurers.Fragments;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.at.gmail.tomeofadventurers.Classes.CharacterDBAccess;
import com.at.gmail.tomeofadventurers.Classes.DatabaseAccess;
import com.at.gmail.tomeofadventurers.R;

import java.util.List;



public class AllSpellsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    //General Listview Variables
    ListView spellsListView;
    DatabaseAccess myDatabaseAccess;
    List<String> spellNames;
    ArrayAdapter<String> adapter;
    //Item Info Variables
    Dialog myDialog;
    TextView spellNameTextView;
    Button addSpellBttn;
    Button closeBttn;
    Button removeSpellBttn;
    TextView spellSource;
    TextView spellType;
    TextView spellDesc;
    Dialog areYouSureDialog;
    Button yesAreYouSureBtn;
    Button noAreYouSureBtn;
    //Create Item Variables
    Dialog createSpellDialog;
    Button createSpellBttn;
    EditText createSpellName;
    EditText createSpellType;
    EditText createSpellSource;
    EditText createSpellDesc;
    Button createSpellClose;
    Button createSpellAddSpell;
    //Spell Search and Sort Variables
    String classURL = "%";
    String spellLevel = "%";
    String orderBy = "name";
    String spellSchool = "%";

    String charID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_spells, container, false);
        super.onCreate(savedInstanceState);

        spellsListView = (ListView) view.findViewById(R.id.listViewSpells);

        CharacterDBAccess myCharDBAccess;
        myCharDBAccess = CharacterDBAccess.getInstance(getContext());
        myCharDBAccess.open();
        charID = myCharDBAccess.findSelectedCharacter();
        myCharDBAccess.close();

        myDatabaseAccess = DatabaseAccess.getInstance(this.getContext());
        myDatabaseAccess.open();
        spellNames = myDatabaseAccess.getSpellNames();

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, spellNames);
        spellsListView.setAdapter(adapter);

        myDialog = new Dialog(getContext()); //Used for spell info popup

        getNameFromListView(); //Based on spell clicked in listview

        createSpellDialog = new Dialog(getContext()); //Used for create spell popup

        createSpellBttn = (Button) view.findViewById(R.id.createSpellBtn);

        createSpellBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSpellDialog.setContentView(R.layout.popup_createspell);

                createSpellName = (EditText) createSpellDialog.findViewById(R.id.spellNameTextView);
                createSpellType = (EditText) createSpellDialog.findViewById(R.id.spellTypeTextView);
                createSpellSource = (EditText) createSpellDialog.findViewById(R.id.spellSourceTextView);
                createSpellDesc = (EditText) createSpellDialog.findViewById(R.id.spellDescTextView);

                createSpellDialog.show();

                createSpellName.setText("");
                createSpellType.setText("");
                createSpellSource.setText("");
                createSpellDesc.setText("");

                createSpellAddSpell = (Button) createSpellDialog.findViewById(R.id.spellCreateAddBtn);
                createSpellClose = (Button) createSpellDialog.findViewById(R.id.closeCreateSpellBtn);

                createSpellClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createSpellDialog.dismiss();
                    }
                });

                createSpellAddSpell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String newEntryName;
                        String newEntryType;
                        String newEntrySource;
                        String newEntryDesc;

                        newEntryName = createSpellName.getText().toString();
                        newEntryType = createSpellType.getText().toString();
                        newEntrySource = createSpellSource.getText().toString();
                        newEntryDesc = createSpellDesc.getText().toString();

                        if(newEntryName.length() == 0) {
                            toastMessage("Enter a spell name.");
                        }

                        else {
                            addNewSpell(newEntryName, newEntryDesc, newEntrySource, newEntryType);
                            adapter.add(newEntryName);
                            adapter.notifyDataSetChanged();
                        }
                        createSpellDialog.dismiss();
                    }
                });
            }
        });

        //creates spinner view for class filtering
        String[] classesArray = {"Class", "Bard", "Cleric", "Druid", "Paladin", "Ranger", "Sorcerer", "Warlock", "Wizard"};
        Spinner classSpinner = view.findViewById(R.id.allSpellsClassSpinner);
        ArrayAdapter<String> classSpinnerAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, classesArray);
        classSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(classSpinnerAdapter);
        classSpinner.setOnItemSelectedListener(this);
        //creates spinner view for level filtering
        String[] levelsArray = {"Lvl", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        Spinner levelsSpinner = view.findViewById(R.id.allSpellsLevelSpinner);
        ArrayAdapter<String> levelSpinnerAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, levelsArray);
        levelSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        levelsSpinner.setAdapter(levelSpinnerAdapter);
        levelsSpinner.setOnItemSelectedListener(this);
        //creates spinner view for school filtering
        String[] schoolArray = {"School", "Abjuration", "Conjuration", "Divination", "Enchantment", "Evocation", "Illusion", "Necromancy", "Transmutation"};
        Spinner schoolSpinner = view.findViewById(R.id.allSpellsSchoolSpinner);
        ArrayAdapter<String> schoolSpinnerAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, schoolArray);
        schoolSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        schoolSpinner.setAdapter(schoolSpinnerAdapter);
        schoolSpinner.setOnItemSelectedListener(this);
        //creates spinner view for ordering results
        String[] orderArray = {"Order", "Name", "Spell Level", "School "};
        Spinner orderSpinner = view.findViewById(R.id.allSpellsOrderSpinner);
        ArrayAdapter<String> orderSpinnerAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, orderArray);
        orderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderSpinner.setAdapter(orderSpinnerAdapter);
        orderSpinner.setOnItemSelectedListener(this);

        return view;
    }

    public void addNewSpell(String spellName, String descr, String source1, String type1) {
        boolean insertData = myDatabaseAccess.addSpellToSpells(spellName, descr, source1, type1);

        if (insertData) {
            toastMessage("Spell Successfully Created!");
        } else {
            toastMessage("Something went wrong");
        }

    }

    private void getSpellInfo(String slug, Dialog myDialog) {
        Cursor data = myDatabaseAccess.getSpellsData();

        spellSource = (TextView) myDialog.findViewById(R.id.spellSourceTextView);
        spellType = (TextView) myDialog.findViewById(R.id.spellTypeTextView);
        spellDesc = (TextView) myDialog.findViewById(R.id.spellDescTextView);
        spellNameTextView = (TextView) myDialog.findViewById(R.id.spellNameTextView);

        while(data.moveToNext())
        {
            if(slug.equals(data.getString(0)))
            {
                spellSource.setText(data.getString(4));
                spellType.setText(data.getString(15));
                spellDesc.setText(data.getString(2));
                spellNameTextView.setText(data.getString(1));
                break;
            }
        }

        data.close();
    }


    private void addSpellToSpellbooks(String idChar, String spellSlug)
    {
        boolean inSpellbook;
        int spellCount;

        inSpellbook = myDatabaseAccess.isSpellinSpellbook(spellSlug, idChar);


        if(inSpellbook == false) {    //item not in inventories list yet

            boolean spellbooksAdded = myDatabaseAccess.addToSpellbooks(idChar, spellSlug, 1);

            if(spellbooksAdded) {
                toastMessage("Spell added to spellbook!");
            }
            else {
                toastMessage("Something went wrong...");
            }
        }

        else {
            spellCount = myDatabaseAccess.getExistingSpellCount(spellSlug, idChar);
            myDatabaseAccess.addToSpellbooksCount(spellSlug, spellCount+1, idChar); //add one to spellCount
            toastMessage("Updated QTY of spell!");
        }
    }

    private void getNameFromListView() {
        //set an onItemClickListener to the ListView
        spellsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();

                Cursor data = myDatabaseAccess.getSpellSlugSpells(name); //get the slug associated with that name
                String spellSlug = "_";

                while(data.moveToNext()){
                    spellSlug = data.getString(0);
                }

                data.close();

                if(spellSlug != "_"){

                    myDialog.setContentView(R.layout.popup_spellinfo); //popup for spell info

                    getSpellInfo(spellSlug, myDialog);

                    addSpellBttn = (Button) myDialog.findViewById(R.id.spellAddBtn);
                    closeBttn = (Button) myDialog.findViewById(R.id.closeBtn);
                    removeSpellBttn = (Button) myDialog.findViewById(R.id.spellRemoveBtn);

                    closeBttn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myDialog.dismiss();
                        }
                    });

                    final String finalSpellSlug = spellSlug;
                    addSpellBttn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addSpellToSpellbooks(charID, finalSpellSlug);
                        }
                    });

                    areYouSureDialog = new Dialog(getContext());

                    removeSpellBttn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            areYouSureDialog.setContentView(R.layout.popup_areyousure);

                            yesAreYouSureBtn = (Button) areYouSureDialog.findViewById(R.id.yesAreYouSureBtn);
                            noAreYouSureBtn = (Button) areYouSureDialog.findViewById(R.id.noAreYouSureBtn);

                            yesAreYouSureBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myDatabaseAccess.deleteSpellFromSpells(finalSpellSlug);
                                    adapter.remove(adapter.getItem(i));
                                    adapter.notifyDataSetChanged();
                                    areYouSureDialog.dismiss();
                                    myDialog.dismiss();
                                    toastMessage("Spell removed from spells!");
                                }
                            });

                            noAreYouSureBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    areYouSureDialog.dismiss();
                                }
                            });

                            areYouSureDialog.show();
                        }
                    });

                    myDialog.show();
                }

                else{
                    toastMessage("No slug associated with that name");
                }

            }
        });
    }

    private void toastMessage(String message){
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
    }
    //spinner on selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String spinnerSelection = parent.getItemAtPosition(position).toString();

        if (spinnerSelection == "Class") //yes, its ugly but its clear and it works, uses the spinner selection to determine what new filter is being added then requests a new spell list from that
        {
            classURL = "%";
        }
        else if (spinnerSelection == "Bard")
        {
            classURL = "http://www.dnd5eapi.co/api/classes/2";
        }
        else if (spinnerSelection == "Cleric")
        {
            classURL = "http://www.dnd5eapi.co/api/classes/3";
        }
        else if (spinnerSelection == "Druid")
        {
            classURL = "http://www.dnd5eapi.co/api/classes/4";
        }
        else if (spinnerSelection == "Paladin")
        {
            classURL = "http://www.dnd5eapi.co/api/classes/7";
        }
        else if (spinnerSelection == "Ranger")
        {
            classURL = "http://www.dnd5eapi.co/api/classes/8";
        }
        else if (spinnerSelection == "Sorcerer")
        {
            classURL = "http://www.dnd5eapi.co/api/classes/10";
        }
        else if (spinnerSelection == "Warlock")
        {
            classURL = "http://www.dnd5eapi.co/api/classes/11";
        }
        else if (spinnerSelection == "Wizard")
        {
            classURL = "http://www.dnd5eapi.co/api/classes/12";
        }
        else if (spinnerSelection == "Lvl")
        {
            spellLevel = "%";
        }
        else if (spinnerSelection == "0")
        {
            spellLevel = "0";
        }
        else if (spinnerSelection == "1")
        {
            spellLevel = "1";
        }
        else if (spinnerSelection == "2")
        {
            spellLevel = "2";
        }
        else if (spinnerSelection == "3")
        {
            spellLevel = "3";
        }
        else if (spinnerSelection == "4")
        {
            spellLevel = "4";
        }
        else if (spinnerSelection == "5")
        {
            spellLevel = "5";
        }
        else if (spinnerSelection == "6")
        {
            spellLevel = "6";
        }
        else if (spinnerSelection == "7")
        {
            spellLevel = "7";
        }
        else if (spinnerSelection == "8")
        {
            spellLevel = "8";
        }
        else if (spinnerSelection == "9")
        {
            spellLevel = "9";
        }
        else if (spinnerSelection == "School")
        {
            spellSchool = "%";
        }
        else if (spinnerSelection == "Abjuration")
        {
            spellSchool = "http://www.dnd5eapi.co/api/magic-schools/1";
        }
        else if (spinnerSelection == "Conjuration")
        {
            spellSchool = "http://www.dnd5eapi.co/api/magic-schools/2";
        }
        else if (spinnerSelection == "Divination")
        {
            spellSchool = "http://www.dnd5eapi.co/api/magic-schools/3";
        }
        else if (spinnerSelection == "Enchantment")
        {
            spellSchool = "http://www.dnd5eapi.co/api/magic-schools/4";
        }
        else if (spinnerSelection == "Evocation")
        {
            spellSchool = "http://www.dnd5eapi.co/api/magic-schools/5";
        }
        else if (spinnerSelection == "Illusion")
        {
            spellSchool = "http://www.dnd5eapi.co/api/magic-schools/6";
        }
        else if (spinnerSelection == "Necromancy")
        {
            spellSchool = "http://www.dnd5eapi.co/api/magic-schools/7";
        }
        else if (spinnerSelection == "Transmutation")
        {
            spellSchool = "http://www.dnd5eapi.co/api/magic-schools/8";
        }
        else if (spinnerSelection == "Order" || spinnerSelection == "Name")
        {
            orderBy = "name";
        }
        else if (spinnerSelection == "Spell Level")
        {
            orderBy = "spell_level";
        }
        else if (spinnerSelection == "School ")
        {
            orderBy = "school";
        }

        spellNames = myDatabaseAccess.searchSort(classURL, spellLevel, spellSchool, orderBy);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, spellNames);
        spellsListView.setAdapter(adapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
