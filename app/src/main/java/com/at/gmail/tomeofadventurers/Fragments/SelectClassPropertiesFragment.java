package com.at.gmail.tomeofadventurers.Fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.at.gmail.tomeofadventurers.R;


public class SelectClassPropertiesFragment extends Fragment {

    Button buttonToSetAbilityScores;

    //Declare Bundle Variables
    String subClasses[] = new String[10];
    int subClassLength;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_class_properties, container, false);
        super.onCreate(savedInstanceState);

        //Get the View Items
        buttonToSetAbilityScores = (Button) view.findViewById(R.id.btnToSetAbilityScores);
        buttonToSetAbilityScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                SetAbilityScoresFragment fragment = new SetAbilityScoresFragment();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();

               // FragmentManager fragManager = getFragmentManager();
               // fragManager.beginTransaction().replace(R.id.fragment_container, new SetAbilityScoresFragment()).commit();
            }
        });

        //Disable the button
        disableButton(buttonToSetAbilityScores);

        //Get the items from the bundles
        subClasses = getArguments().getStringArray("subClasses");
        subClassLength = getArguments().getInt("subClassLength");

        //Dynamically Generate Radio buttons
        //create the layout
        LinearLayout LI = view.findViewById(R.id.classLinearLayout);
        LinearLayout.LayoutParams LP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //add the textView
        TextView textViewChooseSubclass = new TextView(getActivity());
        textViewChooseSubclass.setText("Please choose a Sub Class:");
        LI.addView(textViewChooseSubclass);

        //make the Radio Buttons
        RadioGroup l1 = new RadioGroup(this.getActivity());
        for (int i = 0; i < subClassLength; i++){
            RadioButton radButton = new RadioButton(this.getActivity());
            radButton.setId(i+1);
            radButton.setText(subClasses[i]);
            l1.addView(radButton);

            radButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    enableButton(buttonToSetAbilityScores);
                }
            });
        }
        LI.addView(l1);

        return view;
    }

    //Function for quickly generating a toast message
    public void toastMessage(String message){
        //Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
}