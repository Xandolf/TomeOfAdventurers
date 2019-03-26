package com.at.gmail.tomeofadventurers.Fragments;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.at.gmail.tomeofadventurers.Classes.CharacterDBAccess;
import com.at.gmail.tomeofadventurers.Classes.DatabaseAccess;
import com.at.gmail.tomeofadventurers.R;

import java.util.List;



public class AllItemsFragment extends Fragment {

//General Listview Variables
    ListView itemBookListView;
    DatabaseAccess myDatabaseAccess;
    List<String> itemNames;
    ArrayAdapter<String> adapter;
//Item Info Variables
    Dialog myDialog;
    TextView itemNameTextView;
    Button addItemBttn;
    Button closeBttn;
    Button removeItemBttn;
    TextView itemSource;
    TextView itemType;
    TextView itemDesc;
    Dialog areYouSureDialog;
    Button yesAreYouSureBtn;
    Button noAreYouSureBtn;
//Create Item Variables
    Dialog createItemDialog;
    Button createItemBttn;
    EditText createItemName;
    EditText createItemType;
    EditText createItemSource;
    EditText createItemDesc;
    Button createItemClose;
    Button createItemAddItem;

    String charID;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_items, container, false);
        super.onCreate(savedInstanceState);

        itemBookListView = (ListView) view.findViewById(R.id.listViewItembook);

        CharacterDBAccess myCharDBAccess;
        myCharDBAccess = CharacterDBAccess.getInstance(getContext());
        myCharDBAccess.open();
        charID = myCharDBAccess.findSelectedCharacter();
        myCharDBAccess.close();

        myDatabaseAccess = DatabaseAccess.getInstance(this.getContext());
        myDatabaseAccess.open();
        itemNames = myDatabaseAccess.getItemNames();

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, itemNames);
        itemBookListView.setAdapter(adapter);

        myDialog = new Dialog(getContext()); //Used for item info popup

        getNameFromListView(); //Based on item clicked in listview

        createItemDialog = new Dialog(getContext()); //Used for create item popup

        createItemBttn = (Button) view.findViewById(R.id.createItemBtn);

        createItemBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createItemDialog.setContentView(R.layout.popup_createitem);

                createItemName = (EditText) createItemDialog.findViewById(R.id.itemNameTextView);
                createItemType = (EditText) createItemDialog.findViewById(R.id.itemTypeTextView);
                createItemSource = (EditText) createItemDialog.findViewById(R.id.itemSourceTextView);
                createItemDesc = (EditText) createItemDialog.findViewById(R.id.itemDescTextView);

                createItemDialog.show();

                createItemName.setText("");
                createItemType.setText("");
                createItemSource.setText("");
                createItemDesc.setText("");

                createItemAddItem = (Button) createItemDialog.findViewById(R.id.itemCreateAddBtn);
                createItemClose = (Button) createItemDialog.findViewById(R.id.closeCreateItemBtn);

                createItemClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createItemDialog.dismiss();
                    }
                });

                createItemAddItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String newEntryName;
                        String newEntryType;
                        String newEntrySource;
                        String newEntryDesc;

                        newEntryName = createItemName.getText().toString();
                        newEntryType = createItemType.getText().toString();
                        newEntrySource = createItemSource.getText().toString();
                        newEntryDesc = createItemDesc.getText().toString();

                        if(newEntryName.length() == 0) {
                            toastMessage("Enter an item name.");
                        }

                        else {
                            addNewItem(newEntryName, newEntryDesc, newEntrySource, newEntryType);
                            adapter.add(newEntryName);
                            adapter.notifyDataSetChanged();
                        }
                        createItemDialog.dismiss();
                    }
                });
            }
        });

        return view;
    }

        public void addNewItem(String itemName, String descr, String source1, String type1) {
        boolean insertData = myDatabaseAccess.addItemToItembook(itemName, descr, source1, type1);

        if (insertData) {
            toastMessage("Item Successfully Created!");
        } else {
            toastMessage("Something went wrong");
        }
    }

    private void getItemInfo(String slug, Dialog myDialog) {
        Cursor data = myDatabaseAccess.getItemsData();

        itemSource = (TextView) myDialog.findViewById(R.id.itemSourceTextView);
        itemType = (TextView) myDialog.findViewById(R.id.itemTypeTextView);
        itemDesc = (TextView) myDialog.findViewById(R.id.itemDescTextView);
        itemNameTextView = (TextView) myDialog.findViewById(R.id.itemNameTextView);


        while (data.moveToNext()) {
            if(slug.equals(data.getString(0)))
            {
                itemSource.setText(data.getString(4));
                itemType.setText(data.getString(2));
                itemDesc.setText(data.getString(3));
                itemNameTextView.setText(data.getString(1));
            }
        }

        data.close();
    }

    private void addItemToInventories(String charID, String itemID)
    {
            boolean inInventories; //initially assume no duplicate items
            int itemCount;

            inInventories = myDatabaseAccess.isIteminInventories(itemID, charID);

            if(inInventories == false) {    //item not in inventories list yet

                //item not equipped yet
                boolean inventoriesAdded = myDatabaseAccess.addToInventories(charID, itemID, 1, 0);

                if(inventoriesAdded) {
                    toastMessage("Item added to inventory!");
                }
                else {
                    toastMessage("Something went wrong...");
                }
            }

            else {
                itemCount = myDatabaseAccess.getExistingItemCount(itemID, charID);
                myDatabaseAccess.addToInventoriesCount(itemID, itemCount+1, charID); //add one to itemCount
                toastMessage("Updated QTY of item!");
            }
    }

    private void getNameFromListView() {
        //set an onItemClickListener to the ListView
        itemBookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();

                String itemSlug = myDatabaseAccess.getIDFromItembook(name); //get the slug associated with that name

                if(itemSlug != "_"){

                    myDialog.setContentView(R.layout.popup_iteminfo); //popup for item info

                    getItemInfo(itemSlug, myDialog);

                    addItemBttn = (Button) myDialog.findViewById(R.id.itemAddBtn);
                    closeBttn = (Button) myDialog.findViewById(R.id.closeBtn);
                    removeItemBttn = (Button) myDialog.findViewById(R.id.itemRemoveBtn);

                    closeBttn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myDialog.dismiss();
                        }
                    });

                    final String finalItemSlug = itemSlug;
                    addItemBttn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addItemToInventories(charID, finalItemSlug);
                        }
                    });

                    areYouSureDialog = new Dialog(getContext());

                    removeItemBttn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            areYouSureDialog.setContentView(R.layout.popup_areyousure);

                            yesAreYouSureBtn = (Button) areYouSureDialog.findViewById(R.id.yesAreYouSureBtn);
                            noAreYouSureBtn = (Button) areYouSureDialog.findViewById(R.id.noAreYouSureBtn);

                            yesAreYouSureBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myDatabaseAccess.deleteItemFromItembook(finalItemSlug);
                                    adapter.remove(adapter.getItem(i));
                                    adapter.notifyDataSetChanged();
                                    areYouSureDialog.dismiss();
                                    myDialog.dismiss();
                                    toastMessage("Item removed from items!");
                                }
                            });

                            noAreYouSureBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    areYouSureDialog.dismiss();
                                }
                            });

                            areYouSureDialog.show();
                        }
                    });

                    myDialog.show();
                    }

                else{
                    toastMessage("No ID associated with that name");
                }

            }
        });
    }

    private void toastMessage(String message){
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
    }
}
