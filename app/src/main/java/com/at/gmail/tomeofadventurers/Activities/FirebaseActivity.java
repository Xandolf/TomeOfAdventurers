package com.at.gmail.tomeofadventurers.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.at.gmail.tomeofadventurers.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class FirebaseActivity extends AppCompatActivity {

    static final int GOOGLE_SIGN = 123;
    FirebaseAuth mAuth;                         //Firebase authenticator declared
    Button button_login,button_logout;          //Buttons for log in and log out declared
    TextView text;                              //Text variable declared
    ImageView image;                            //Image variable declared
    ProgressBar progressBar;                    //Progress bar declared
    GoogleSignInClient mGoogleSignInClient;     //Google sign in client declared
    FirebaseFirestore db;                       //Database from Firebase


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);

        button_login = findViewById(R.id.login);            //button_login maps to login button in XML activity_firebase file
        button_logout = findViewById(R.id.logout);          //button_logout maps to logout button in XML activity_firebase file
        text = findViewById(R.id.text);                     //text maps to text display in XML activity_firebase file
        image = findViewById(R.id.image);                   //image maps to the image in XML activity_firebase file
        progressBar = findViewById(R.id.progress_circular);  //progressbar maps to progress_circular in XML activity_firebase file

        db = FirebaseFirestore.getInstance();               //db gets assigned to the TombOfAdventurers Firebase Firestore database
        mAuth = FirebaseAuth.getInstance();                 //mAuth get assigned to the Firebase Authentication


        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions       //Start of googleSignInOptions to get
                .Builder()                                                      //Have google be the sign in method
                .requestIdToken(getString(R.string.default_web_client_id))      //and get users email
                .requestEmail()
                .build();                                                       //End of googleSignInOptions

        mGoogleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);     //mGoogleSignInClient gets the client by using GoogleSignInOptions

        button_login.setOnClickListener(v -> SignInGoogle());//When user clicks login button it will run SignInGoogle
        button_logout.setOnClickListener(v -> Logout());

        if(mAuth.getCurrentUser() != null){
            FirebaseUser user = mAuth.getCurrentUser();
            updateUI(user);
        }


    }

    void SignInGoogle(){
        progressBar.setVisibility(View.VISIBLE);

        Intent signIntent =mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signIntent, GOOGLE_SIGN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GOOGLE_SIGN) {
            Task<GoogleSignInAccount> task = GoogleSignIn
                    .getSignedInAccountFromIntent(data);

            try {

                GoogleSignInAccount account = task.getResult(ApiException.class);

                if(account != null){
                    firebaseAuthWithGoogle(account);
                }

            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        Log.d("TAG","firebaseAuthWithGoogle: " + account.getId());

        AuthCredential credential = GoogleAuthProvider
                .getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {

                    if(task.isSuccessful()){
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.d("TAG","sign in success");

                        Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();

                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);

                        putUserInDatabase(user, db);
                    }
                    else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.w("TAG", "signin failure", task.getException());

                        Toast.makeText(this, "SignIn Failed!", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void putUserInDatabase(FirebaseUser user, FirebaseFirestore db) {

        Map<String, Object> userDefault = new HashMap<>();

        userDefault.put("name", ""); //I would like to change this to be the users characters
        userDefault.put("hp", 0);
        db.collection("users").document(user.getUid())
                .set(userDefault)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener(){
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error writing document", e);
                    }
                });
    }


    private void updateUI(FirebaseUser user) {

        if(user != null){
            String name = user.getDisplayName();
            String email = user.getEmail();
            String photo = String.valueOf(user.getPhotoUrl());

            text.append("Info: \n");
            text.append(name + "\n");
            text.append(email);

            Picasso.get().load(photo).into(image);
            button_login.setVisibility(View.INVISIBLE);
            button_logout.setVisibility(View.VISIBLE);
        }
        else{

            text.setText(getString(R.string.firebase_login));
            Picasso.get().load(R.drawable.firebase_logo).into(image);
            button_login.setVisibility(View.VISIBLE);
            button_logout.setVisibility(View.INVISIBLE);

            Toast.makeText(this, "Signed out!", Toast.LENGTH_SHORT).show();
        }
    }

    void Logout(){
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                task -> updateUI(null));
    }

}
