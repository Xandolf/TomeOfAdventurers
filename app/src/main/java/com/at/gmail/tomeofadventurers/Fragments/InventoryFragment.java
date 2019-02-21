package com.at.gmail.tomeofadventurers.Fragments;

import android.app.Dialog;
import android.database.Cursor;
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
import android.widget.TextView;
import android.widget.Toast;

import com.at.gmail.tomeofadventurers.Adapters.ItemListAdapter;
import com.at.gmail.tomeofadventurers.Classes.DatabaseAccess;
import com.at.gmail.tomeofadventurers.R;

import java.util.List;

public class InventoryFragment extends Fragment {

    //General Listview Variables
    RecyclerView inventoryListView;
    DatabaseAccess myDatabaseAccess;
    List<String> itemNames;
    ItemListAdapter adapter;
    //Item Info Variables
    Dialog myDialog;
    TextView itemNameTextView;
    Button removeItemBttn;
    Button closeBttn;
    TextView itemSource;
    TextView itemType;
    TextView itemDesc;
    TextView itemCount;


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

        adapter = new ItemListAdapter(getContext(), itemNamesArray);
        inventoryListView.setLayoutManager(new LinearLayoutManager(getContext()));
        inventoryListView.setHasFixedSize(true);
        inventoryListView.setAdapter(adapter);

//        myDialog = new Dialog(getContext()); //for item infoinventory popup
//
//        getSlugFromListView();

        return view;
    }

//    private void getItemInfo(String slug, Dialog myDialog) {
//        Cursor data = myDatabaseAccess.getItemsData();
//
//        itemSource = (TextView) myDialog.findViewById(R.id.itemSourceTextView);
//        itemType = (TextView) myDialog.findViewById(R.id.itemTypeTextView);
//        itemDesc = (TextView) myDialog.findViewById(R.id.itemDescTextView);
//        itemNameTextView = (TextView) myDialog.findViewById(R.id.itemNameTextView);
//        itemCount = (TextView) myDialog.findViewById(R.id.itemCountTextView);
//
//        while (data.moveToNext()) {
//            if (slug.equals(data.getString(0))) {
//                itemSource.setText(data.getString(6));
//                itemType.setText(data.getString(2));
//                itemDesc.setText(data.getString(3));
//                itemNameTextView.setText(data.getString(1));
//            }
//        }
//
//        data.close();
//
//        itemCount.setText("QTY: " + Integer.toString(myDatabaseAccess.getExistingItemCount(slug)));
//    }
//
//    private void getSlugFromListView() {
//        //set an onItemClickListener to the ListView
//        inventoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
//                String name = adapterView.getItemAtPosition(i).toString();
//
//                Cursor data = myDatabaseAccess.getItemSlugitems(name); //get the slug associated with that name
//                String itemSlug = "_";
//
//                while (data.moveToNext()) {
//                    itemSlug = data.getString(0);
//                }
//
//                data.close();
//
//                if (itemSlug != "_") {
//
//                    myDialog.setContentView(R.layout.popup_iteminfoinventory);
//
//                    getItemInfo(itemSlug, myDialog);
//
//                    removeItemBttn = (Button) myDialog.findViewById(R.id.itemRemoveInventoryBtn);
//                    closeBttn = (Button) myDialog.findViewById(R.id.closeBtn);
//
//                    closeBttn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            myDialog.dismiss();
//                        }
//                    });
//
//                    final String finalItemSlug = itemSlug;
//                    removeItemBttn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            int itemCount = myDatabaseAccess.getExistingItemCount(finalItemSlug);
//
//                            if(itemCount > 1) {
//                                myDatabaseAccess.removeFromInventoriesCount(finalItemSlug, itemCount-1); //remove 1 from count
//                                getItemInfo(finalItemSlug, myDialog);
//                            }
//
//                            else {
//                                myDatabaseAccess.deleteItemFromInv(finalItemSlug);
//                                adapter.remove(adapter.getItem(i));
//                                adapter.notifyDataSetChanged();
//                                myDialog.dismiss();
//                                toastMessage("Item removed from inventory!");
//                            }
//                        }
//                    });
//
//                    myDialog.show();
//                } else {
//                    toastMessage("No ID associated with that name");
//                }
//
//            }
//        });
//    }
//
//    private void toastMessage(String message) {
//        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//    }
}
