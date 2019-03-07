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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_spells, container, false);
        super.onCreate(savedInstanceState);

        spellsListView = (ListView) view.findViewById(R.id.listViewSpells);
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
                            toastMessage("Enter an spell name.");
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

        //creates spinner list for class filtering
        String[] classesArray = {"All", "Bard", "Cleric", "Druid", "Paladin", "Ranger", "Sorcerer", "Warlock", "Wizard"};
        Spinner classSpinner = view.findViewById(R.id.allSpellsClassSpinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, classesArray);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(spinnerAdapter);
        classSpinner.setOnItemSelectedListener(this);


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
                spellType.setText(data.getString(13));
                spellDesc.setText(data.getString(2));
                spellNameTextView.setText(data.getString(1));
            }
        }

        data.close();
    }


    private void addSpellToSpellbooks(int charID, String spellSlug)
    {
        boolean inSpellbook;
        int spellCount;

        inSpellbook = myDatabaseAccess.isSpellinSpellbook(spellSlug);


        if(inSpellbook == false) {    //item not in inventories list yet

            boolean spellbooksAdded = myDatabaseAccess.addToSpellbooks(charID, spellSlug, 1);

            if(spellbooksAdded) {
                toastMessage("Spell added to spellbook!");
            }
            else {
                toastMessage("Something went wrong...");
            }
        }

        else {
            spellCount = myDatabaseAccess.getExistingSpellCount(spellSlug);
            myDatabaseAccess.addToSpellbooksCount(spellSlug, spellCount+1); //add one to spellCount
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
                            addSpellToSpellbooks(1, finalSpellSlug);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { //updates shown spells depending on class selected
        String filterClass = parent.getItemAtPosition(position).toString();
        spellNames = myDatabaseAccess.classSearch(filterClass);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, spellNames);
        spellsListView.setAdapter(adapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
