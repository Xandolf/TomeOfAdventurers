package com.at.gmail.tomeofadventurers.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.at.gmail.tomeofadventurers.Activities.MainActivity;
import com.at.gmail.tomeofadventurers.Classes.CharacterDBAccess;
import com.at.gmail.tomeofadventurers.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class SelectCharacterAdapter extends RecyclerView.Adapter<SelectCharacterAdapter.SelectCharacterAdapterViewHolder>
{
    private Context myContext;
    int charNameListLength;
    List<String> charNames;
    CharacterDBAccess myCharacterDBAccess;
    String myCharID;


    //Constructor
    public SelectCharacterAdapter(Context myContext, List<String> names, int length) {
        this.myContext = myContext;
        charNames = names;
        charNameListLength = length;
    }


    @NonNull
    @Override
    public SelectCharacterAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(myContext);
        View view = inflater.inflate(R.layout.layout_characters, viewGroup, false);
        SelectCharacterAdapterViewHolder Holder = new SelectCharacterAdapterViewHolder(view);

        return Holder;
    }

    //i holds position
    @Override
    public void onBindViewHolder(@NonNull final SelectCharacterAdapterViewHolder selectCharacterAdapterViewHolder, final int i) {
        String charClass;

        selectCharacterAdapterViewHolder.textViewCharacterName.setText(charNames.get(i));

        myCharacterDBAccess = CharacterDBAccess.getInstance(myContext);
        myCharacterDBAccess.open();

        charClass = myCharacterDBAccess.getCharacterClass(charNames.get(i));

        if(charClass.equals("Barbarian"))
        {
            selectCharacterAdapterViewHolder.textViewCharacterName.setBackgroundResource(R.drawable.barbarian);
        }
        else if(charClass.equals("Bard"))
        {
            selectCharacterAdapterViewHolder.textViewCharacterName.setBackgroundResource(R.drawable.bard);
        }
        else if(charClass.equals("Cleric"))
        {
            selectCharacterAdapterViewHolder.textViewCharacterName.setBackgroundResource(R.drawable.cleric);
        }
        else if(charClass.equals("Druid"))
        {
            selectCharacterAdapterViewHolder.textViewCharacterName.setBackgroundResource(R.drawable.druid);
        }
        else if(charClass.equals("Fighter"))
        {
            selectCharacterAdapterViewHolder.textViewCharacterName.setBackgroundResource(R.drawable.fighter);
        }
        else if(charClass.equals("Monk"))
        {
            selectCharacterAdapterViewHolder.textViewCharacterName.setBackgroundResource(R.drawable.monk);
        }
        else if(charClass.equals("Paladin"))
        {
            selectCharacterAdapterViewHolder.textViewCharacterName.setBackgroundResource(R.drawable.paladin);
        }
        else if(charClass.equals("Ranger"))
        {
            selectCharacterAdapterViewHolder.textViewCharacterName.setBackgroundResource(R.drawable.ranger);
        }
        else if(charClass.equals("Rogue"))
        {
            selectCharacterAdapterViewHolder.textViewCharacterName.setBackgroundResource(R.drawable.rogue);
        }
        else if(charClass.equals("Sorcerer"))
        {
            selectCharacterAdapterViewHolder.textViewCharacterName.setBackgroundResource(R.drawable.sorcerer);
        }
        else if(charClass.equals("Warlock"))
        {
            selectCharacterAdapterViewHolder.textViewCharacterName.setBackgroundResource(R.drawable.warlock);
        }
        else if(charClass.equals("Wizard"))
        {
            selectCharacterAdapterViewHolder.textViewCharacterName.setBackgroundResource(R.drawable.wizard2);
        }

        selectCharacterAdapterViewHolder.parentLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String name = charNames.get(i);

                myCharID = myCharacterDBAccess.getIDFromCharacters(name); //get the id associated with that name

                myCharacterDBAccess.chooseCharacter(myCharID);

                toastMessage(name+" selected");

                FirebaseUpdateChar();

                Intent switcher = new Intent(myContext, MainActivity.class);
                myContext.startActivity(switcher);
            }
        });

    }

    private void toastMessage(String message) {
        Toast.makeText(myContext, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return charNameListLength;
    }


    //The View holder is a single instance of the layout used in the recycler.
    public class SelectCharacterAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCharacterName;
        LinearLayout parentLayout;

        public SelectCharacterAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCharacterName = itemView.findViewById(R.id.CharacterNameTextView);
            parentLayout = itemView.findViewById(R.id.linearLayout);
        }
    }


    public void FirebaseUpdateChar(){

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Log.d("TAG","USER IS LOGGED IN");

            FirebaseUser user = mAuth.getCurrentUser();
            //String userEmail = user.getEmail();
            DocumentReference updateChar = db.collection("users").document(user.getUid());

            com.at.gmail.tomeofadventurers.Classes.FirebaseUser fireUser = new com.at.gmail.tomeofadventurers.Classes.FirebaseUser();

            fireUser.setEmail( user.getEmail() );
            fireUser.setUserName( user.getDisplayName() );
            fireUser.setUserUID(user.getUid());
            fireUser.setProfilePic( String.valueOf( user.getPhotoUrl()) );
            fireUser.setCharName(myCharacterDBAccess.loadCharacterName());
            fireUser.setCharRace(myCharacterDBAccess.loadCharacterRace());
            fireUser.setCharClass(myCharacterDBAccess.loadCharacterClassName());
            fireUser.setCharMaxHP(myCharacterDBAccess.loadCharacterMaxHP());
            fireUser.setCharCurrentHP(myCharacterDBAccess.loadCharacterCurrentHP());

            updateChar.set(fireUser);
        }

    }
}