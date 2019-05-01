package com.at.gmail.tomeofadventurers.Fragments.CharacterCreationProcess;

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

import com.at.gmail.tomeofadventurers.Classes.CharacterDBAccess;
import com.at.gmail.tomeofadventurers.Classes.ClassDatabaseAccess;
import com.at.gmail.tomeofadventurers.Classes.SubClassDatabaseAccess;
import com.at.gmail.tomeofadventurers.R;
import com.squareup.otto.Bus;


public class SelectClassFragment extends Fragment
{
    String classIds[];
    String subClassIds[];
    String classNames[];
    String selectedClassId;
    String selectedSubClassID;
    String selectedClassName;

    //variables
    Button buttonToClassProperties;
    TextView textViewDisplayText;
    Spinner spinnerClass, spinnerSubClass;
    Button buttonMoreInfo;

    Dialog testDialog;
    TextView textViewPassAttributes;
    Button buttonClosePopup;
    ClassDatabaseAccess classDatabaseAccess;
    SubClassDatabaseAccess subClassDatabaseAccess;

    //string alternatives
    ArrayAdapter<String> classListAdapter;
    ArrayAdapter<String> subClassListAdapter;

    @Nullable
    @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
       View view=inflater.inflate(R.layout.fragment_select_class,container,false);
       super.onCreate(savedInstanceState);


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
                populateSecondSpinner(adapterView, i);
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

        buttonToClassProperties = view.findViewById(R.id.buttonToClassPropertiesFragment);
        buttonToClassProperties.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                CharacterDBAccess characterDBAccess;
                characterDBAccess = CharacterDBAccess.getInstance(getContext());
                characterDBAccess.open();

                characterDBAccess.saveClass(selectedSubClassID);
                characterDBAccess.close();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragTrans = fragmentManager.beginTransaction();
                SetAbilityScoresFragment frag = new SetAbilityScoresFragment();
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
        return view;
    }
    @Override
    public void onPause(){
        super.onPause();
    }

    public void toastMessage(String message)
    {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public void addItemsToSpinner()
    {
        classDatabaseAccess = classDatabaseAccess.getInstance(this.getContext());
        classDatabaseAccess.open();

        classIds = classDatabaseAccess.getClassIds();
        classNames = classDatabaseAccess.getClassNames(classIds);

        classDatabaseAccess.close();

        classListAdapter = new ArrayAdapter<>(this.getActivity(),
                                              android.R.layout.simple_spinner_item,
                                              classNames);
        classListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClass.setAdapter(classListAdapter);
    }


    public void populateSecondSpinner(AdapterView adapterView, int i)
    {
        selectedClassName = classNames[i];
        selectedClassId = classIds[i];
        subClassDatabaseAccess = SubClassDatabaseAccess.getInstance(this.getContext());
        subClassDatabaseAccess.open();
        subClassIds = subClassDatabaseAccess.getSubClassIdsFor(selectedClassId);
        String subClassNames[] = subClassDatabaseAccess.getSubClassNames(subClassIds);
        subClassDatabaseAccess.close();
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

}
