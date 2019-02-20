package com.at.gmail.tomeofadventurers.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.at.gmail.tomeofadventurers.R;

public class FirebaseFragment extends Fragment implements View.OnClickListener {

    private Button googleLogin;
    @Override
    public void onClick(View v)
    {
        switch(v.getId()){
            case R.id.googleLogin:
                //Function call for button
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_firebase, container, false);
        super.onCreate(savedInstanceState);

        googleLogin = view.findViewById(R.id.googleLogin);
        googleLogin.setOnClickListener(this);

        return view;
    }
}

