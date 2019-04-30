package com.at.gmail.tomeofadventurers.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;

import com.at.gmail.tomeofadventurers.Adapters.FirebaseUserAdapter;
import com.at.gmail.tomeofadventurers.Classes.FirebaseUser;
import com.at.gmail.tomeofadventurers.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    EditText editTextUserMessage, editSendToUser;
    Button sendMessage, backBtn;
    String message, receiverUser;

    FirebaseAuth mAuth;

    //widget
    private RecyclerView mRecyclerView;

    //vars
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mUserEmails = new ArrayList<>();
    private ArrayList<String> mUserNames = new ArrayList();

    //private  ArrayList<FirebaseUser> mFireUsersList = new ArrayList<>();
    private FirebaseUserAdapter mFirebaseUserAdapter;
    private DocumentSnapshot mLastQueriedDocument;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        backBtn = findViewById(R.id.backBtn);
        mRecyclerView = findViewById(R.id.RecyclerViewSelectUser);

        InitImageBitMaps();

        backBtn.setOnClickListener(v -> BackToLogin() );
    }


    private void InitImageBitMaps(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference usersCollectionRef = db.collection("users");

        Query userQuery = db.collection("users");

        userQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    for(QueryDocumentSnapshot document: task.getResult()) {
                        String userPic = String.valueOf(document.getDocumentReference("ProfilePic"));//My made up Firebase user class
                        String userEmail = String.valueOf(document.getDocumentReference("email"));//My made up Firebase user class
                        String userName = String.valueOf(document.getDocumentReference("UserName"));//My made up Firebase user class

                        //mImageUrls.add(userPic);
                        mUserEmails.add(userEmail);
                        mUserNames.add(userName);
                    }

                    mFirebaseUserAdapter.notifyDataSetChanged();

                }

                else{

                }

            }
        });

        initRecyclerView();

    }

    private void initRecyclerView(){

        mFirebaseUserAdapter = new FirebaseUserAdapter(this, mImageUrls, mUserEmails, mUserNames);

        mRecyclerView.setAdapter(mFirebaseUserAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    public void BackToLogin() {
        Intent mainIntent = new Intent(ChatActivity.this, FirebaseActivity.class);
        startActivity(mainIntent);
    }

}
