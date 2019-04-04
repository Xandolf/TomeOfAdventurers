package com.at.gmail.tomeofadventurers.Fragments.MenuFragments;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
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
    TextView platinum, gold, electrum, silver, copper;
    Dialog currencyConverterDialog;
    Button currencyConverterBttn, closeBttn, convertBttn;
    Spinner currencyTypeSpinner1,currencyTypeSpinner2;
    String[] currencyTypes;
    ArrayAdapter<String> currencyTypesAdapter;
    String spinnerSelection1, spinnerSelection2;
    SeekBar convertPercentBar;
    int seekBarProgress;


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
            weight.setText("Weight: "+myDatabaseAccess.inventoryWeight());
        }

        String[] myCurrencyValues = myDatabaseAccess.inventoryCurrency();

        platinum = view.findViewById(R.id.textViewInventoryPlatinum);
        platinum.setText(myCurrencyValues[0]+"pp");

        gold = view.findViewById(R.id.textViewInventoryGold);
        gold.setText(myCurrencyValues[1]+"gp");

        electrum = view.findViewById(R.id.textViewInventoryElectrum);
        electrum.setText(myCurrencyValues[2]+"ep");

        silver = view.findViewById(R.id.textViewInventorySilver);
        silver.setText(myCurrencyValues[3]+"sp");

        copper = view.findViewById(R.id.textViewInventoryCopper);
        copper.setText(myCurrencyValues[4]+"cp");

        currencyConverterBttn = view.findViewById(R.id.buttonCurrencyConverter);

        currencyConverterBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                currencyConverterDialog = new Dialog(getContext());
                currencyConverterDialog.setContentView(R.layout.popup_currency_converter);

                closeBttn = (Button) currencyConverterDialog.findViewById(R.id.closeBtn);

                closeBttn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currencyConverterDialog.dismiss();
                    }
                });

                currencyTypeSpinner1 = currencyConverterDialog.findViewById(R.id.spinnerCurrencyType1);
                currencyTypeSpinner2 = currencyConverterDialog.findViewById(R.id.spinnerCurrencyType2);
                convertPercentBar = currencyConverterDialog.findViewById(R.id.seekBar);
                addItemsToSpinners();


                currencyTypeSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        spinnerSelection1 = adapterView.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });

                currencyTypeSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        spinnerSelection2 = adapterView.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });

                convertBttn = (Button) currencyConverterDialog.findViewById(R.id.convertButton);

                convertBttn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        seekBarProgress = convertPercentBar.getProgress();
                        toastMessage(Integer.toString(seekBarProgress));
                        myDatabaseAccess.convertCurrency(spinnerSelection1,spinnerSelection2, seekBarProgress);
                        //Update textviews in inventory for copper and silver
                        setCurrencyTextViews();
                    }
                });

                currencyConverterDialog.show();
            }
        });

        return view;
    }

    public void addItemsToSpinners() {
        currencyTypes = getResources().getStringArray(R.array.currencyList);
        currencyTypesAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, currencyTypes);
        currencyTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencyTypeSpinner1.setAdapter(currencyTypesAdapter);
        currencyTypeSpinner2.setAdapter(currencyTypesAdapter);
    }

    public void setCurrencyTextViews(){

        String[] currencyValues = myDatabaseAccess.inventoryCurrency();

        TextView txtView1 = (TextView) ((Activity)getContext()).findViewById(R.id.textViewInventoryCopper);
        txtView1.setText(currencyValues[4]+"cp");

        TextView txtView2 = (TextView) ((Activity)getContext()).findViewById(R.id.textViewInventorySilver);
        txtView2.setText(currencyValues[3]+"sp");

        TextView txtView3 = (TextView) ((Activity)getContext()).findViewById(R.id.textViewInventoryElectrum);
        txtView3.setText(currencyValues[2]+"ep");

        TextView txtView4 = (TextView) ((Activity)getContext()).findViewById(R.id.textViewInventoryGold);
        txtView4.setText(currencyValues[1]+"gp");

        TextView txtView5 = (TextView) ((Activity)getContext()).findViewById(R.id.textViewInventoryPlatinum);
        txtView5.setText(currencyValues[0]+"pp");
    }

    private void toastMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

}