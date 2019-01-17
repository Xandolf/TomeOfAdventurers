package com.example.victor.tomeofadventurers.Fragments;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.victor.tomeofadventurers.Activities.CreateCharacterActivity;
import com.example.victor.tomeofadventurers.R;

public class HomeFragment extends Fragment {

    private Button charCreateBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        super.onCreate(savedInstanceState);

        charCreateBtn = view.findViewById(R.id.creatCharBtn);

        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        charCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*old code
                Intent intent = new Intent(getActivity(), createChar.class);
                startActivity(intent);
                */

                //Toast.makeText(getActivity(), "Button Pressed", Toast.LENGTH_SHORT).show();

                //Fragment frag = new SelectRaceFragment();
                //FragmentManager fragManager = getFragmentManager();
                //fragManager.beginTransaction().replace(R.id.fragment_container, new SelectRaceFragment()).commit();


                //Intent intent = new Intent(getActivity(), CreateCharacterActivity.class);
                Intent intent = new Intent(getActivity(), CreateCharacterActivity.class);
                startActivity(intent);

            }
        });

        return view;
    }
}
