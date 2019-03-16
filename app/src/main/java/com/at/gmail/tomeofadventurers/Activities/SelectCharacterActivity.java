package com.at.gmail.tomeofadventurers.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.at.gmail.tomeofadventurers.Fragments.SelectCharacterFragment;
import com.at.gmail.tomeofadventurers.R;

public class SelectCharacterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_select_character);
        super.onCreate(savedInstanceState);


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragTrans = fragmentManager.beginTransaction();
        SelectCharacterFragment frag = new SelectCharacterFragment();
        fragTrans.replace(R.id.select_character_fragment_container, frag);
        fragTrans.commit();



    }
}

