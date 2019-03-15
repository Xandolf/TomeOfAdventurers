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
import com.at.gmail.tomeofadventurers.Classes.ClassDatabaseAccess;
import com.at.gmail.tomeofadventurers.Classes.DnDClass;
import com.at.gmail.tomeofadventurers.Classes.SubClassDatabaseAccess;
import com.at.gmail.tomeofadventurers.R;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;


public class SelectClassFragment extends Fragment
{
    String classIds[] = {"a", "b"};
    String subClassIds[];
    String className = "NA";
    String selectedClassId;
    String selectedSubClassID;

    //variables
    Button buttonToClassProperties;
    TextView textViewDisplayText;
    Spinner spinnerClass, spinnerSubClass;
    Button buttonMoreInfo;
    Bus BUS;
    //Dialog popup test
    Dialog testDialog;
    TextView textViewPassAttributes;
    Button buttonClosePopup;
    ClassDatabaseAccess classDatabaseAccess;
    SubClassDatabaseAccess subClassDatabaseAccess;
    //string alternatives
    String[] stringClassList;
    ArrayAdapter<String> classListAdapter;
    ArrayAdapter<String> subClassListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_select_class, container, false);
        super.onCreate(savedInstanceState);

        //Get the instance of the bus
        BUS = BusProvider.getInstance();

        //TextView variables
        textViewDisplayText = view.findViewById(R.id.txtvwJSONResultClass);
        textViewDisplayText.setText("Initial Setting Text");

        //spinner variables
        spinnerClass = view.findViewById(R.id.spinnerClass);
        spinnerSubClass = view.findViewById(R.id.spinnerSubClass);

        addItemsToSpinner();
        spinnerClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
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
        spinnerSubClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                selectedSubClassID = subClassIds[i];
                enableButton(buttonToClassProperties);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                textViewDisplayText.setText("Nothing Selected");
            }
        });

        //Register the BUS
//        BUS.register(this);

        buttonToClassProperties = (Button) view.findViewById(R.id.buttonToClassPropertiesFragment);
        buttonToClassProperties.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                //Post to the BUS before transferring fragments
//                BUS.post(sendDnDClass());

                //Unregister the BUS
//                BUS.unregister(this); //go to class properties fragment

                Fragment                        frag        = new SetAbilityScoresFragment();
                FragmentManager                 fragManager = getFragmentManager();
                android.app.FragmentTransaction fragTrans   = fragManager.beginTransaction();

                fragTrans.replace(R.id.fragment_container, frag);
                fragTrans.commit();
            }
        });

        buttonMoreInfo = (Button) view.findViewById(R.id.btnMoreInfo);
        buttonMoreInfo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                callPopup();
            }
        });
        disableButton(buttonMoreInfo);

        //********************TESTING POPUP*************************
        testDialog = new Dialog(getContext());

        return view;
    }


    public void toastMessage(String message)
    {
        //Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public void addItemsToSpinner()
    {
        classDatabaseAccess = classDatabaseAccess.getInstance(this.getContext());
        classDatabaseAccess.open();

        classIds = classDatabaseAccess.getClassIds();
        String[] classNames = classDatabaseAccess.getClassNames(classIds);

        classListAdapter = new ArrayAdapter<>(this.getActivity(),
                                              android.R.layout.simple_spinner_item,
                                              classNames);
        classListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClass.setAdapter(classListAdapter);
    }


    public void selectAndParse(AdapterView adapterView, int i)
    {
        String index = adapterView.getItemAtPosition(i).toString();


        selectedClassId = classIds[i];

        subClassDatabaseAccess = SubClassDatabaseAccess.getInstance(this.getContext());
        subClassDatabaseAccess.open();
        subClassIds = subClassDatabaseAccess.getSubClassIdsFor(selectedClassId);
        String subClassNames[] = subClassDatabaseAccess.getSubClassNames(subClassIds);
        subClassListAdapter = new ArrayAdapter<String>(this.getActivity(),
                                                       android.R.layout.simple_spinner_item,
                                                       subClassNames);
        subClassListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerSubClass.setAdapter(subClassListAdapter);

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
        textViewPassAttributes = testDialog.findViewById(R.id.txtvwMoreInfoRace);

        //set the text view in the popup
        textViewPassAttributes.setText("Text Unavailable");

        //find and add the close button
        buttonClosePopup = testDialog.findViewById(R.id.btnClose);
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
    //  BUS.post(sendDnDClass("CLASSNAME"))
    //  BUS.unregister(this)
    @Produce
    public DnDClass sendDnDClass()
    {
        DnDClass dnDClass = new DnDClass();
        dnDClass.setClassName(selectedSubClassID);
        return dnDClass;
    }
}
