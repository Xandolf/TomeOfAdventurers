package com.at.gmail.tomeofadventurers.Fragments.MenuFragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.at.gmail.tomeofadventurers.Adapters.AbilityScoreAdapter;
import com.at.gmail.tomeofadventurers.Adapters.SkillsListAdapter;
import com.at.gmail.tomeofadventurers.Classes.BusProvider;
import com.at.gmail.tomeofadventurers.Classes.Character;
import com.at.gmail.tomeofadventurers.Classes.CharacterDBAccess;
import com.at.gmail.tomeofadventurers.Classes.DnDClass;
import com.at.gmail.tomeofadventurers.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.otto.Bus;

import static java.sql.Types.NULL;

public class CharacterSheetFragment extends Fragment {

    Character currentPlayerCharacter;
    ImageButton buttonLowerHitPoints, buttonIncreaseHitPoints;
    Button healButton, damageButton;
    ProgressBar progressBar;
    RecyclerView abilityScoreRecycler, skillsRecyclerView;
    AbilityScoreAdapter abilityScoreAdapter;
    SkillsListAdapter skillsListAdapter;
    String[] abilityScoreNames, skillNames;
    TextView textViewHitPointValue, textViewClassName, textViewCharacterName;
    String displayHitPoints;
    View view;
    Bus BUS;
    int skillModifiers[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    int abilityScores[] = {10, 10, 10, 10, 10, 10};
    int[] abilityScoreModifiers = {0, 0, 0, 0, 0, 0};
    String name = "NA";
    String className = "";
    Dialog healthEditDialog;
    EditText hitPointValue;
    int heal = 0;
    int damage = 0;

    //FIXME WOW
    boolean skillProficiencies[] = {true, false, true, false, false, true, true, true, false, true, false, true, false, true, true, false, true, false};



    int currentHitPoints;
    int maxHitPoints = 0;

    //Alex Code
    TextView textViewSpeedValue, textViewHitDiceValue, textViewArmorClass, textViewProfBonus, textViewPassivePerception;
    String speedValue;
    String hitDiceValue;
    String armorClassValue;
    int profBonus;
    String passivePerception;

    CharacterDBAccess characterDBAccess;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_character_sheet, container, false);

        characterDBAccess = CharacterDBAccess.getInstance(getContext());
        characterDBAccess.open();

        name = characterDBAccess.loadCharacterName();
        className = characterDBAccess.loadCharacterClassName();
        hitDiceValue = characterDBAccess.loadCharacterHitDice();
        armorClassValue = characterDBAccess.loadCharacterArmorClass();
        speedValue = characterDBAccess.loadCharacterSpeed();
        abilityScores = characterDBAccess.loadAbilityScores();
        abilityScoreModifiers = characterDBAccess.loadAbilityScoresModifiers();
        skillModifiers = characterDBAccess.loadSkillModifiers();
        maxHitPoints = characterDBAccess.loadCharacterMaxHP();
        currentHitPoints = characterDBAccess.loadCharacterCurrentHP();
        profBonus = characterDBAccess.loadCharacterProfBonus();
        passivePerception = characterDBAccess.loadCharacterPassivePerception();
        skillProficiencies = characterDBAccess.loadSkillProficiencies();

        //Used to load PlayerCharacter
//        BUS = BusProvider.getInstance();
//        BUS.register(this);

        //progress bar pop up to edit hit points
        progressBar = view.findViewById(R.id.progressBar);

//        toastMessage("Im in");
        textViewCharacterName = view.findViewById(R.id.textViewCharacterName);
        textViewCharacterName.setText(name);


        textViewClassName = view.findViewById(R.id.textViewClassName);
        textViewClassName.setText(className);

        textViewArmorClass = view.findViewById(R.id.textViewArmorClassValue);
        textViewArmorClass.setText(armorClassValue);

        textViewProfBonus = view.findViewById(R.id.textViewProficencyBonusValue);
        textViewProfBonus.setText("+" + Integer.toString(profBonus));

        textViewPassivePerception = view.findViewById(R.id.textViewPassivePerceptionValue);
        textViewPassivePerception.setText("+" + passivePerception);

        //Load in the ability scores
//        if (currentPlayerCharacter!=null)  abilityScores = currentPlayerCharacter.getAbilityScores();
        abilityScoreRecycler = view.findViewById(R.id.recyclerViewAbilityScores);
        abilityScoreRecycler.setHasFixedSize(true);
        abilityScoreRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.);
        abilityScoreNames = getResources().getStringArray(R.array.AbilityScores);
        abilityScoreAdapter = new AbilityScoreAdapter(getContext(), abilityScoreNames, abilityScores, abilityScoreModifiers);
        abilityScoreRecycler.setAdapter(abilityScoreAdapter);

        //load in skills
        skillsRecyclerView = view.findViewById(R.id.skillsRecyclerView);
        skillsRecyclerView.setHasFixedSize(false);
        skillsRecyclerView.setNestedScrollingEnabled(false);
        skillsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        skillNames = getResources().getStringArray(R.array.Skills);
        skillsListAdapter = new SkillsListAdapter(getContext(), skillNames, skillProficiencies, skillModifiers);
        skillsRecyclerView.setAdapter(skillsListAdapter);

        //Health bar ..............................................................................
        textViewHitPointValue = view.findViewById(R.id.textViewHealthValue);
        buttonLowerHitPoints = view.findViewById(R.id.buttonLowerHealth);
        buttonIncreaseHitPoints = view.findViewById(R.id.buttonIncreaseHealth);

        //Initialize Health Bar Values
//        if (currentPlayerCharacter!=null) {
//            currentHitPoints = currentPlayerCharacter.getCurrentHitPoints();
//            maxHitPoints = currentPlayerCharacter.getMaxHitPoints();
//        }
//        else{currentHitPoints=maxHitPoints=100;}
//        currentHitPoints++;
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setMax(maxHitPoints);
        progressBar.setProgress(currentHitPoints);
        displayHitPoints = (Integer.toString(currentHitPoints) + "/" + Integer.toString(maxHitPoints));

        //these functions need to cause a pop-up that will ask the user for a value to heal/damage
        textViewHitPointValue.setText(displayHitPoints);
        buttonLowerHitPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progressBar != null) {
                    progressBar.incrementProgressBy(-1);
                    currentHitPoints = progressBar.getProgress();
                    displayHitPoints = (Integer.toString(currentHitPoints) + "/" + Integer.toString(maxHitPoints));
                    textViewHitPointValue.setText(displayHitPoints);

                    FirebaseUpdateHealth(currentHitPoints);
                }
                characterDBAccess.saveCurrentHP(currentHitPoints);
            }
        });

        buttonIncreaseHitPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progressBar != null) {
                    progressBar.incrementProgressBy(1);
                    currentHitPoints =progressBar.getProgress(); //curretn hitpoints
                    displayHitPoints = (Integer.toString(currentHitPoints)+ "/" + Integer.toString(maxHitPoints));
                    textViewHitPointValue.setText(displayHitPoints);

                    FirebaseUpdateHealth(currentHitPoints);
                }
                characterDBAccess.saveCurrentHP(currentHitPoints);
            }
        });

        progressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                healthEditDialog = new Dialog(getContext());
                healthEditDialog.setContentView(R.layout.popup_healthedit);

                damageButton =healthEditDialog.findViewById(R.id.damageButton);
                healButton = healthEditDialog.findViewById(R.id.healButton);
                hitPointValue = healthEditDialog.findViewById(R.id.hitPointValue);

                healButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (progressBar != null) {
                            String value = hitPointValue.getText().toString();
                            if(value.equals("")){
                                heal = 0;
                            }
                            else{
                                heal = Integer.parseInt(value);
                            }
                            progressBar.incrementProgressBy(heal);
                            currentHitPoints = progressBar.getProgress();
                            displayHitPoints = (Integer.toString(currentHitPoints) + "/" + Integer.toString(maxHitPoints));
                            textViewHitPointValue.setText(displayHitPoints);

                            FirebaseUpdateHealth(currentHitPoints);

                            healthEditDialog.dismiss();
                        }
                        characterDBAccess.saveCurrentHP(currentHitPoints);
                    }

                });
                damageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (progressBar != null) {
                            String value = hitPointValue.getText().toString();
                            if(value.equals("")){
                                heal = 0;
                            }
                            else{
                                heal = Integer.parseInt(value);
                            }
                            progressBar.incrementProgressBy(-heal);
                            currentHitPoints = progressBar.getProgress();
                            displayHitPoints = (Integer.toString(currentHitPoints) + "/" + Integer.toString(maxHitPoints));
                            textViewHitPointValue.setText(displayHitPoints);

                            FirebaseUpdateHealth(currentHitPoints);

                            healthEditDialog.dismiss();
                        }
                        characterDBAccess.saveCurrentHP(currentHitPoints);
                    }

                });
                healthEditDialog.show();
            }
        });


        //......................................................................................

        //Alex Code
        //get the text views
        textViewSpeedValue = view.findViewById(R.id.textViewSpeedValue);
        textViewHitDiceValue = view.findViewById(R.id.textViewHitDiceValue);
        //set the appropriate values
        textViewSpeedValue.setText(speedValue);
        textViewHitDiceValue.setText(hitDiceValue);


        return view;
    }

    //Firebase stuff
    public void FirebaseUpdateHealth(int cp){

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Log.d("TAG","USER IS LOGGED IN");

            FirebaseUser user = mAuth.getCurrentUser();
            //String userEmail = user.getEmail();
            DocumentReference dr = db.collection("users").document(user.getUid());
            dr.update("charCurrentHP", cp);
        }
    }

    //end OnCreate
//    @Override
//    public void onResume(){
//        super.onResume();
//    }
//    @Override
//    public void onPause(){
////        BUS.unregister(this);
//        super.onPause();
//    }
    /*

     Subscription is the complement to event publishing—it lets you receive notification
      that an event has occurred. To subscribe to an event, annotate a method with
      @Subscribe. The method should take only a single parameter,
      the type of which will be the event you wish to subscribe to.
      You DONT need to call the function, the Otto Bus API will do so
      In order to receive an event you need to register with the Bus
      */
//    @Subscribe
//    public void getCharacter(Character sampleCharacter)
//    {
//        currentPlayerCharacter = sampleCharacter;
//        abilityScores = currentPlayerCharacter.getAbilityScores();
//        abilityScoreModifiers=currentPlayerCharacter.getAllAbilityScoreModifiers();
//        skillModifiers=currentPlayerCharacter.getAllSkillModifiers();
//        skillProficiencies=currentPlayerCharacter.getAllSkillProficiencies();
//        name=currentPlayerCharacter.getName();
//        maxHitPoints=currentPlayerCharacter.getMaxHitPoints();
//        currentHitPoints=currentPlayerCharacter.getCurrentHitPoints();
//        className=currentPlayerCharacter.getClassName();
//
////        //Alex Code
//        speedValue = 30;
//        hitDiceValue = currentPlayerCharacter.getMyHitDice();
//
//    }

//    @Subscribe
//    public void getClass (DnDClass dnDClass)
//    {
//        className=dnDClass.getSubClassId();
//    }
//    private void toastMessage(String message) {
//        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//
}
