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
import android.widget.TextView;
import android.widget.Toast;

import com.at.gmail.tomeofadventurers.Adapters.ItemListAdapter;
import com.at.gmail.tomeofadventurers.Classes.BusProvider;
import com.at.gmail.tomeofadventurers.Classes.DatabaseAccess;
import com.at.gmail.tomeofadventurers.Classes.Item;
import com.at.gmail.tomeofadventurers.R;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class InventoryFragment extends Fragment {

    RecyclerView inventoryRecyclerView;
    DatabaseAccess myDatabaseAccess;
    List<String> itemNames;
    List<Integer> itemCounts;
    List<Integer> isItemEquipped;
    List<Item> inventoryItems;
    ItemListAdapter adapter;
    TextView weight;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);
        super.onCreate(savedInstanceState);

        inventoryRecyclerView = view.findViewById(R.id.listViewInventory);
        myDatabaseAccess = DatabaseAccess.getInstance(this.getContext());
        myDatabaseAccess.open();

        inventoryItems = new ArrayList<>();

        itemNames = myDatabaseAccess.fillInventoryNames();
        itemCounts = myDatabaseAccess.fillInventoryQty();
        isItemEquipped = myDatabaseAccess.fillInventoryEquipped();

        for(int i = 0; i < itemNames.size(); i++)
        {
            Item myItem = new Item(itemNames.get(i), itemCounts.get(i), isItemEquipped.get(i)); //need to save equipped value in DB
            inventoryItems.add(myItem);
        }

        if(itemNames.size() > 0)
        {
            adapter = new ItemListAdapter(getContext(), inventoryItems, itemNames.size());
            inventoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            inventoryRecyclerView.setHasFixedSize(true);
            inventoryRecyclerView.setAdapter(adapter);

            weight = view.findViewById(R.id.textViewInventoryWeight);
            weight.setText(myDatabaseAccess.inventoryWeight());
        }

        return view;
    }
}