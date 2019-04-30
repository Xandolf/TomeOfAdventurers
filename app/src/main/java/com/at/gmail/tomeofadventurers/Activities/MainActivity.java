package com.at.gmail.tomeofadventurers.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.at.gmail.tomeofadventurers.Classes.AbilityScoreSender;
import com.at.gmail.tomeofadventurers.Classes.Character;
import com.at.gmail.tomeofadventurers.Classes.CharacterDBAccess;
import com.at.gmail.tomeofadventurers.Fragments.MenuFragments.AllItemsFragment;
import com.at.gmail.tomeofadventurers.Fragments.MenuFragments.AllSpellsFragment;
import com.at.gmail.tomeofadventurers.Fragments.MenuFragments.DiceFragment;
import com.at.gmail.tomeofadventurers.Fragments.MenuFragments.FirebaseFragment;
import com.at.gmail.tomeofadventurers.Fragments.MenuFragments.InventoryFragment;
import com.at.gmail.tomeofadventurers.Fragments.MenuFragments.SpellbookFragment;
import com.at.gmail.tomeofadventurers.Fragments.MenuFragments.TabFragment;
import com.at.gmail.tomeofadventurers.R;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    //public Bus BUS; // I declared it as a variable for easy reference. Not sure if it needs to be public or private
    public int [] abilityScores = {5,8,10,13,20,15};
    TextView characterName, characterClass;
    CharacterDBAccess characterDBAccess;
    LinearLayout linearLayout;
    String name = "";
    String className = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        characterDBAccess = CharacterDBAccess.getInstance(this);
        characterDBAccess.open();

        name = characterDBAccess.loadCharacterName();
        className = characterDBAccess.loadCharacterClass();

//        BUS = BusProvider.getInstance();
//        BUS.register(this); //You must register with the BUS to produce or subscribe but not to post
//        BUS.post(sendCharacter());
////        BUS.unregister(this);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView =findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View headerView = navigationView.getHeaderView(0);
        linearLayout = headerView.findViewById(R.id.header);
        if(className.equals("Barbarian"))
        {
            linearLayout.setBackgroundResource(R.drawable.barbarian);
        }
        else if(className.equals("Bard")){
            linearLayout.setBackgroundResource(R.drawable.bard);
        }
        else if(className.equals("Cleric")){
            linearLayout.setBackgroundResource(R.drawable.cleric);
        }
        else if(className.equals("Druid")){
            linearLayout.setBackgroundResource(R.drawable.druid);
        }
        else if(className.equals("Fighter")){
            linearLayout.setBackgroundResource(R.drawable.fighter);
        }
        else if(className.equals("Monk")){
            linearLayout.setBackgroundResource(R.drawable.monk);
        }
        else if(className.equals("Paladin")){
            linearLayout.setBackgroundResource(R.drawable.paladin);
        }
        else if(className.equals("Ranger")){
            linearLayout.setBackgroundResource(R.drawable.ranger);
        }
        else if(className.equals("Rogue")){
            linearLayout.setBackgroundResource(R.drawable.rogue);
        }
        else if(className.equals("Sorcerer")){
            linearLayout.setBackgroundResource(R.drawable.sorcerer);
        }
        else if(className.equals("Warlock")){
            linearLayout.setBackgroundResource(R.drawable.warlock);
        }
        else if(className.equals("Wizard")){
            linearLayout.setBackgroundResource(R.drawable.wizard);
        }
        //Update character name and class
        characterName = headerView.findViewById(R.id.character_menu_name);
        characterName.setText(name);
        characterClass = headerView.findViewById(R.id.class_menu_name);
        characterClass.setText(className);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null) {
//            Fragment frag = new HomeFragment();
//            android.app.FragmentManager fragManager = getFragmentManager();
//            fragManager.beginTransaction().replace(R.id.fragment_container, frag).commit();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragTrans = fragmentManager.beginTransaction();
            TabFragment frag = new TabFragment();
            fragTrans.replace(R.id.fragment_container, frag);
            fragTrans.commit();

            //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
            //        new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);

        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragTrans = fragmentManager.beginTransaction();
//                HomeFragment frag = new HomeFragment();
//                fragTrans.replace(R.id.fragment_container, frag);
//                fragTrans.commit();
                Intent switcher = new Intent(this, SelectCharacterActivity.class);
                startActivity(switcher);
                break;
            case R.id.nav_stats:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new TabFragment()).commit();
                break;
            case R.id.nav_inventory:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new InventoryFragment()).commit();
                break;
            case R.id.nav_spellbook:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SpellbookFragment()).commit();
                break;
            case R.id.nav_dice:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new DiceFragment()).commit();
                break;
            case R.id.nav_items:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AllItemsFragment()).commit();
                //Toast.makeText(this, "send", Toast.LENGTH_SHORT).show(); for reference only
                break;
            case R.id.nav_spells:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AllSpellsFragment()).commit();
                break;
            case R.id.nav_firebase:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FirebaseFragment()).commit();

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy()
    {
        //BUS.unregister(this);
        super.onDestroy();
    }

    //Produce functions for a given even class are called when a different
    // a class registers with the Bus. They are required for dynamically created things
    // such as new fragments or activities.

    @Produce
    public Character sendCharacter ()
    {
        String name="Xanandorf";
        Character sampleCharacter = new Character(name, abilityScores, "raceName", "className", 13, 25, abilityScores);

        return sampleCharacter;
    }

    @Subscribe
    void setAbilityScores(AbilityScoreSender abilityScoreSender)
    {
        abilityScores=abilityScoreSender.getAbilityScores();
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        super.onBackPressed();
    }
}
