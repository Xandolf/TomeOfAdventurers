package com.at.gmail.tomeofadventurers.Activities;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.at.gmail.tomeofadventurers.Fragments.SelectRaceFragment;
import com.at.gmail.tomeofadventurers.R;


public class CreateCharacterActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_createcharacter);
        super.onCreate(savedInstanceState);


//        Fragment frag = new SelectRaceFragment();
//        FragmentManager fragManager = getFragmentManager();
//        fragManager.beginTransaction().replace(R.id.fragment_container, frag).commit();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragTrans = fragmentManager.beginTransaction();
        SelectRaceFragment frag = new SelectRaceFragment();
        fragTrans.replace(R.id.fragment_container, frag);
        fragTrans.commit();

    }
}

