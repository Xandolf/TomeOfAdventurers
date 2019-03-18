package com.at.gmail.tomeofadventurers.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.at.gmail.tomeofadventurers.Adapters.SelectCharacterAdapter;
import com.at.gmail.tomeofadventurers.Classes.CharacterDBAccess;
import com.at.gmail.tomeofadventurers.R;

import java.util.ArrayList;
import java.util.List;

public class SelectCharacterActivity extends AppCompatActivity {

    List<String> characterNames;
    CharacterDBAccess charDBAccess;
    RecyclerView selectCharacterRecyclerView;
    SelectCharacterAdapter adapter;
    Button createCharButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_select_character);
        super.onCreate(savedInstanceState);

//Recycler View initialization
        selectCharacterRecyclerView = findViewById(R.id.RecyclerViewSelectChar);

        charDBAccess = CharacterDBAccess.getInstance(this);
        charDBAccess.open();

        characterNames = new ArrayList<>();
        characterNames = charDBAccess.getCharacterNames();

        adapter = new SelectCharacterAdapter(this, characterNames, characterNames.size());
        selectCharacterRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectCharacterRecyclerView.setHasFixedSize(true);
        selectCharacterRecyclerView.setAdapter(adapter);
//Create New Character Button initializtion
        createCharButton = findViewById(R.id.createNewCharBtn);

        createCharButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                charDBAccess.clearSelectedCharacters();
                Intent createCharacter = new Intent(getApplicationContext(), CreateCharacterActivity.class);
                startActivity(createCharacter);
            }
        });
    }

}

