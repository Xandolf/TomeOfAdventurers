package com.at.gmail.tomeofadventurers.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.at.gmail.tomeofadventurers.Adapters.ItemListAdapter;
import com.at.gmail.tomeofadventurers.Classes.DatabaseAccess;
import com.at.gmail.tomeofadventurers.R;

import java.util.List;

public class InventoryFragment extends Fragment {

    RecyclerView inventoryListView;
    DatabaseAccess myDatabaseAccess;
    List<String> itemNames;
    ItemListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);
        super.onCreate(savedInstanceState);

        inventoryListView = view.findViewById(R.id.listViewInventory);
        myDatabaseAccess = DatabaseAccess.getInstance(this.getContext());
        myDatabaseAccess.open();
        itemNames = myDatabaseAccess.fillInventory();

        String[] itemNamesArray = itemNames.toArray(new String[itemNames.size()]);

        adapter = new ItemListAdapter(getContext(), itemNamesArray, itemNamesArray.length);
        inventoryListView.setLayoutManager(new LinearLayoutManager(getContext()));
        inventoryListView.setHasFixedSize(true);
        inventoryListView.setAdapter(adapter);

        return view;
    }
}
