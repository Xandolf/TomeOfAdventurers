package com.example.victor.tomeofadventurers.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.victor.tomeofadventurers.Fragments.SelectRaceFragment;
import com.example.victor.tomeofadventurers.R;


public class CreateCharacterActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_createcharacter);
        super.onCreate(savedInstanceState);


        Fragment frag = new SelectRaceFragment();
        FragmentManager fragManager = getFragmentManager();
        fragManager.beginTransaction().replace(R.id.fragment_container, frag).commit();

    }
}

