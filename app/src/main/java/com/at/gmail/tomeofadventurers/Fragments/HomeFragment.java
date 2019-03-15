package com.at.gmail.tomeofadventurers.Fragments;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.at.gmail.tomeofadventurers.Activities.CreateCharacterActivity;
import com.at.gmail.tomeofadventurers.Activities.SelectCharacterActivity;
import com.at.gmail.tomeofadventurers.R;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private Button charCreateBtn, selCharBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        super.onCreate(savedInstanceState);

        charCreateBtn = view.findViewById(R.id.creatCharBtn);
        charCreateBtn.setOnClickListener(this);

        selCharBtn = view.findViewById(R.id.selCharBtn);
        selCharBtn.setOnClickListener(this);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.creatCharBtn:
            //Intent intent = new Intent(getActivity(), CreateCharacterActivity.class);
            Intent createCharacter = new Intent(getActivity(), CreateCharacterActivity.class);
            startActivity(createCharacter);
            break;
            case R.id.selCharBtn:
                Intent selectCharacter = new Intent(getActivity(), SelectCharacterActivity.class);
                startActivity(selectCharacter);
        }
    }
}

