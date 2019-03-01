package com.at.gmail.tomeofadventurers.Fragments;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.at.gmail.tomeofadventurers.Adapters.ViewPagerAdapter;
import com.at.gmail.tomeofadventurers.R;
import com.at.gmail.tomeofadventurers.Fragments.CharacterSheetFragment;
import com.at.gmail.tomeofadventurers.Fragments.InventoryFragment;
import com.at.gmail.tomeofadventurers.Fragments.SpellbookFragment;


public class TabFragment extends Fragment {

    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_fragment, container, false);
        super.onCreate(savedInstanceState);

        viewPager = view.findViewById(R.id.unique_viewPager);
        addTabs(viewPager);
        ((TabLayout) view.findViewById(R.id.unique_tabLayout)).setupWithViewPager(viewPager);


        return view;
    }

    private void addTabs(ViewPager viewPager) {
        //getChildFragment is used when you are using fragments within fragments
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new CharacterSheetFragment(), "TAB1");
        adapter.addFrag(new InventoryFragment(), "TAB2");
        adapter.addFrag(new SpellbookFragment(), "TAB3");
        viewPager.setAdapter(adapter);
    }
}