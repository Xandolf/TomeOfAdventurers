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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.at.gmail.tomeofadventurers.Classes.CharacterDBAccess;
import com.at.gmail.tomeofadventurers.Classes.DatabaseAccess;
import com.at.gmail.tomeofadventurers.R;

import java.util.List;

public class SpellbookFragment extends Fragment
{

    //General Listview Variables
    ListView spellbookListView;
    DatabaseAccess myDatabaseAccess;
    List<String> spellNames;
    ArrayAdapter<String> adapter;
    //Spell Info Variables
    Dialog myDialog;
    TextView spellNameTextView;
    Button removeSpellBttn;
    Button closeBttn;
    TextView spellSource;
    TextView spellType;
    TextView spellDesc;
    TextView spellCount;

    String charID;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_spellbook, container, false);
        super.onCreate(savedInstanceState);

        spellbookListView = (ListView) view.findViewById(R.id.listViewSpellbook);

        CharacterDBAccess myCharDBAccess;
        myCharDBAccess = CharacterDBAccess.getInstance(getContext());
        myCharDBAccess.open();
        charID = myCharDBAccess.findSelectedCharacter();

        myDatabaseAccess = DatabaseAccess.getInstance(this.getContext());
        myDatabaseAccess.open();
        spellNames = myDatabaseAccess.fillSpellbook();

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,
                                           spellNames);
        spellbookListView.setAdapter(adapter);

        myDialog = new Dialog(getContext()); //for spell infospellbook popup

        getSlugFromListView();

        return view;
    }

    private void getSpellInfo(String slug, Dialog myDialog)
    {
        Cursor data = myDatabaseAccess.getSpellsData();

        spellSource = (TextView) myDialog.findViewById(R.id.spellSourceTextView);
        spellType = (TextView) myDialog.findViewById(R.id.spellTypeTextView);
        spellDesc = (TextView) myDialog.findViewById(R.id.spellDescTextView);
        spellNameTextView = (TextView) myDialog.findViewById(R.id.spellNameTextView);
        spellCount = (TextView) myDialog.findViewById(R.id.spellCountTextView);

        while (data.moveToNext())
        {
            if (slug.equals(data.getString(0)))
            {
                spellSource.setText(data.getString(3));
                spellType.setText(data.getString(4));
                spellDesc.setText(data.getString(2));
                spellNameTextView.setText(data.getString(1));
            }
        }

        data.close();

        spellCount.setText("QTY: " + Integer.toString(myDatabaseAccess.getExistingSpellCount(slug, charID)));
    }

    private void getSlugFromListView()
    {
        //set an onItemClickListener to the ListView
        spellbookListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l)
            {
                String name = adapterView.getItemAtPosition(i).toString();

                Cursor data = myDatabaseAccess.getSpellSlugSpells(name); //get the slug
                // associated with that name
                String spellSlug = "_";

                while (data.moveToNext())
                {
                    spellSlug = data.getString(0);
                }

                data.close();

                if (spellSlug != "_")
                {

                    myDialog.setContentView(R.layout.popup_spellinfospellbook);

                    getSpellInfo(spellSlug, myDialog);

                    removeSpellBttn = (Button) myDialog.findViewById(R.id.spellRemoveSpellbookBtn);
                    closeBttn = (Button) myDialog.findViewById(R.id.closeBtn);

                    closeBttn.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            myDialog.dismiss();
                        }
                    });

                    final String finalSpellSlug = spellSlug;
                    removeSpellBttn.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            int spellCount = myDatabaseAccess.getExistingSpellCount(finalSpellSlug, charID);

                            if (spellCount > 1)
                            {
                                myDatabaseAccess.removeFromSpellbooksCount(finalSpellSlug,
                                                                           spellCount - 1, charID);
                                //remove 1 from count
                                getSpellInfo(finalSpellSlug, myDialog);
                            } else
                            {
                                myDatabaseAccess.deleteItemFromSpellbook(finalSpellSlug, charID);
                                adapter.remove(adapter.getItem(i));
                                adapter.notifyDataSetChanged();
                                myDialog.dismiss();
                                toastMessage("Spell removed from spellbook!");
                            }
                        }
                    });
                    myDialog.show();
                } else
                {
                    toastMessage("No ID associated with that name");
                }
            }
        });
    }

    private void toastMessage(String message)
    {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}

