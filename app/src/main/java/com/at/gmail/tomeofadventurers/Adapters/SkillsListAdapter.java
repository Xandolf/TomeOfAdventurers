package com.at.gmail.tomeofadventurers.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.at.gmail.tomeofadventurers.R;

public class SkillsListAdapter extends RecyclerView.Adapter<SkillsListAdapter.SkillListViewHolder>
{
    private Context myContext;
    String [] skillNames;
    boolean [] skillProficiencies;
    int [] skillModifiers;
    //Constructor
    public SkillsListAdapter(Context myContext, String[] skillStrings,boolean [] skillProficiencies, int [] skillModifiers){
        this.myContext = myContext;
        skillNames = skillStrings;
        this.skillProficiencies=skillProficiencies;
        this.skillModifiers=skillModifiers;
    }


    @NonNull
    @Override
    public SkillListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(myContext);
        View view = inflater.inflate(R.layout.layout_skills, null);
        SkillListViewHolder Holder = new SkillListViewHolder(view);
        return Holder;


    }

    //This function will populate the textView within the skillsScoreHolder
    @Override
    public void onBindViewHolder(@NonNull SkillListViewHolder skillListViewHolder, int i) {


        String leftSkillName = skillNames[i];
        String rightSkillName = skillNames[i+9];
        int left = skillModifiers[i];
        int right = skillModifiers[i+9];
        String leftSkillModifier="";
        String rightSkillModifier="";
        if(left>=0)
        {
            leftSkillModifier ="+";
        }
        if(right>=0)
        {
            rightSkillModifier ="+";
        }
        leftSkillModifier+=Integer.toString(left);
        rightSkillModifier+=Integer.toString(right);
        skillListViewHolder.leftSkillsRadioButton.setText(leftSkillModifier);
        skillListViewHolder.leftSkillsTextView.setText(leftSkillName);
        skillListViewHolder.rightSkillsRadioButton.setText(rightSkillModifier);
        skillListViewHolder.rightSkillsTextView.setText(rightSkillName);
        if(skillProficiencies[i])
            skillListViewHolder.leftSkillsRadioButton.setChecked(true);
        if(skillProficiencies[i+9])
            skillListViewHolder.rightSkillsRadioButton.setChecked(true);

    }

    //There are 18 different Skills. Each view has two of them on the same line
    @Override
    public int getItemCount() {
        return 9;
    }

    //The View holder is a single instance of the layout used in the recycler.
    public class SkillListViewHolder extends RecyclerView.ViewHolder{
        RadioButton leftSkillsRadioButton,rightSkillsRadioButton;
        TextView leftSkillsTextView, rightSkillsTextView;
        public SkillListViewHolder(@NonNull View itemView) {
            super(itemView);

            leftSkillsRadioButton = itemView.findViewById(R.id.leftSkillsRadioButton);
            rightSkillsRadioButton = itemView.findViewById(R.id.rightSkillsRadioButton);
            leftSkillsTextView =itemView.findViewById(R.id.leftSkillTextView);
            rightSkillsTextView = itemView.findViewById(R.id.rightSkillTextView);
        }
    }


}
