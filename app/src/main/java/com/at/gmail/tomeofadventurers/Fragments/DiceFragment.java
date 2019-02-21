/*
This class holds the dice roller logic that creates and displays a random number based on the
dice selected. If multiple dice are selected it will add them and display the sum.
*/

package com.at.gmail.tomeofadventurers.Fragments;
//Deleted Fragment import
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.at.gmail.tomeofadventurers.R;

import java.util.Random;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class DiceFragment extends Fragment implements View.OnClickListener
{
    private Button rollButton, d4, d6, d8, d10, d12, d20, d100;
    private TextView diceResultTextView, diceQuant;

    private Random rng = new Random();
    private int[] diceCount = {0, 0, 0, 0, 0, 0, 0};//Number of dice being rolled

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.d4: //diceCount[0]
                diceCount[0]++;
                displayDiceSelected();
                break;
            case R.id.d6:
                diceCount[1]++;
                displayDiceSelected();
                break;
            case R.id.d8:
                diceCount[2]++;
                displayDiceSelected();
                break;
            case R.id.d10:
                diceCount[3]++;
                displayDiceSelected();
                break;
            case R.id.d12:
                diceCount[4]++;
                displayDiceSelected();
                break;
            case R.id.d20:
                diceCount[5]++;
                displayDiceSelected();
                break;
            case R.id.d100:
                diceCount[6]++;
                displayDiceSelected();
                break;
            case R.id.roll:
                rollDice();
                for (int i = 0; i < 7; i++)
                { //Resetting all counts
                    diceCount[i] = 0;
                }
                break;
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_diceroller, container, false);
        super.onCreate(savedInstanceState);

        //Onclick listeners
        d4 = view.findViewById(R.id.d4); //Assigns id d4 from fragment_diceroller.xml to local
        // button d4
        d4.setOnClickListener(this);

        d6 = view.findViewById(R.id.d6);
        d6.setOnClickListener(this);

        d8 = view.findViewById(R.id.d8);
        d8.setOnClickListener(this);

        d10 = view.findViewById(R.id.d10);
        d10.setOnClickListener(this);

        d12 = view.findViewById(R.id.d12);
        d12.setOnClickListener(this);

        d20 = view.findViewById(R.id.d20);
        d20.setOnClickListener(this);

        d100 = view.findViewById(R.id.d100);
        d100.setOnClickListener(this);

        rollButton = view.findViewById(R.id.roll);
        rollButton.setOnClickListener(this);

        diceQuant = view.findViewById(R.id.diceQuant);
        diceResultTextView = view.findViewById(R.id.diceResultTextView);

        return view;
    }

    private void displayDiceSelected()
    {
        int[]   diceSize    = {4, 6, 8, 10, 12, 20, 100};
        String  displayDice = "No dice Selected", additionalDice;
        boolean firstRoll   = TRUE;

        for (int i = 0; i < 7; i++)
        { //Loop for all 7 dice
            if (diceCount[i] > 0 && firstRoll == TRUE)
            { //If dice[i] is selected and it is the first dice
                displayDice = Integer.toString(diceCount[i]) + "d" + Integer.toString(diceSize[i]);
                firstRoll = FALSE;
            } else
                if (diceCount[i] > 0)
                { //Concat all other dice onto first roll
                    additionalDice = " + " + Integer.toString(diceCount[i]) + "d" + Integer
                            .toString(diceSize[i]);
                    displayDice += additionalDice;
                } else
                {
                }//No dice selected = Do nothing
        }
        diceQuant.setText(displayDice); //Update text to show dice selected
    }


    private void rollDice()
    {
        int[]   diceSize           = {4, 6, 8, 10, 12, 20, 100};
        int     sum                = 0;
        String  individualDiceRoll = "No Dice Selected", newRoll;
        boolean firstRoll          = TRUE;

        for (int j = 0; j < 7; j++)
        {
            for (int i = 0; i < diceCount[j]; i++)
            {
                int diceResults = rng.nextInt(diceSize[j]) + 1;
                if (firstRoll == TRUE)
                {
                    individualDiceRoll = Integer.toString(diceResults);
                    firstRoll = FALSE;
                } else
                {
                    newRoll = " + " + Integer.toString(diceResults);
                    individualDiceRoll += newRoll;
                }
                sum += diceResults;
            }
        }
        if (firstRoll == FALSE)
        {
            diceResultTextView.setText(individualDiceRoll + " = " + String.valueOf(sum));
        } else
        {
            diceResultTextView.setText(individualDiceRoll);
            diceQuant.setText("No Dice Selected");
        }
    }
}
