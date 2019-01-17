//This file is used to populate the RecyclerView with the abilityScores and their values.
//Later we will need to pass it the character information to get the actual values.
//I will look into populating other RecyclerView's on the stats page using this same adapter

package com.at.gmail.tomeofadventurers.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.at.gmail.tomeofadventurers.R;

//From my current understanding and Adapter 'adapts' a given view into a listview or recycler.
//it "recycles" a given layout to do this
public class AbilityScoreAdapter extends RecyclerView.Adapter<AbilityScoreAdapter.AbilityScoreViewHolder>
{
    private Context myContext;
    String[] abilityScoreNames;
    int[] abilityScores,abilityScoreModifiers;

    //Constructor
    public AbilityScoreAdapter(Context myContext, String[] AbilityScoreStrings, int[] abilityScores,int []  abilityScoreModifiers ) {
        this.myContext = myContext;
        abilityScoreNames = AbilityScoreStrings;
        this.abilityScores = abilityScores;
        this.abilityScoreModifiers=abilityScoreModifiers;
    }


    @NonNull
    @Override
    public AbilityScoreViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(myContext);
        View view = inflater.inflate(R.layout.layout_ability_scores,null);
        AbilityScoreViewHolder Holder = new AbilityScoreViewHolder(view);
        return Holder;


    }

    //This function will populate the textView within the abilityScoreHolder
    @Override
    public void onBindViewHolder(@NonNull AbilityScoreViewHolder abilityScoreViewHolder, int i) {

        int abilityScore = abilityScores[i];
        int abilityScoreModifier = abilityScoreModifiers[i];

        String abilityScoreModifierText ="";
        if (abilityScoreModifier>=0) abilityScoreModifierText="+";
        abilityScoreModifierText+= Integer.toString(abilityScoreModifier);
        abilityScoreViewHolder.textViewAbilityScoreName.setText(abilityScoreNames[i]);
//        abilityScoreViewHolder.textViewAbilityScoreValue.setText(Integer.toString(abilityScore));
        abilityScoreViewHolder.textViewAbilityScoreModifier.setText(abilityScoreModifierText);
    }

    //There are 6 different Ability Scores
    @Override
    public int getItemCount() {
        return 6;
    }

    //The View holder is a single instance of the layout used in the recycler.
    public class AbilityScoreViewHolder extends RecyclerView.ViewHolder{
        TextView textViewAbilityScoreName, textViewAbilityScoreValue,textViewAbilityScoreModifier;

        public AbilityScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAbilityScoreName = itemView.findViewById(R.id.textViewAbilityScoreName);
//            textViewAbilityScoreValue = itemView.findViewById(R.id.textViewAbilityScoreValue);
            textViewAbilityScoreModifier = itemView.findViewById(R.id.textViewAbilityScoreModifier);
        }
    };
}
