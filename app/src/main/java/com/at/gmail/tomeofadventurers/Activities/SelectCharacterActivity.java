package com.at.gmail.tomeofadventurers.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.at.gmail.tomeofadventurers.Adapters.SelectCharacterAdapter;
import com.at.gmail.tomeofadventurers.Classes.CharacterDBAccess;
import com.at.gmail.tomeofadventurers.R;

import java.util.ArrayList;
import java.util.List;

public class SelectCharacterActivity extends AppCompatActivity {

    List<String> characterNames;
    CharacterDBAccess myDatabaseAccess;
    RecyclerView selectCharacterRecyclerView;
    SelectCharacterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_select_character);
        super.onCreate(savedInstanceState);

        selectCharacterRecyclerView = findViewById(R.id.RecyclerViewSelectChar);

        myDatabaseAccess = CharacterDBAccess.getInstance(this);
        myDatabaseAccess.open();

        characterNames = new ArrayList<>();
        characterNames = myDatabaseAccess.getCharacterNames();

        adapter = new SelectCharacterAdapter(this, characterNames, characterNames.size());
        selectCharacterRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectCharacterRecyclerView.setHasFixedSize(true);
        selectCharacterRecyclerView.setAdapter(adapter);
    }
}

