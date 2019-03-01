package com.at.gmail.tomeofadventurers.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.at.gmail.tomeofadventurers.Classes.DatabaseAccess;
import com.at.gmail.tomeofadventurers.Classes.Item;
import com.at.gmail.tomeofadventurers.R;

import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemListAdapterViewHolder>
{
    private Context myContext;
    int itemNameArrayLength;

    List<Item> items;

    DatabaseAccess myDatabaseAccess;

    Dialog myDialog;

    TextView itemNameTextView;
    Button removeItemBttn;
    Button closeBttn;
    TextView itemSource;
    TextView itemType;
    TextView itemDesc;
    TextView itemCount;

    //Constructor
    public ItemListAdapter(Context myContext, List<Item> myItems, int length) {
        this.myContext = myContext;
        this.items = myItems;
        itemNameArrayLength = length;
    }

    @NonNull
    @Override
    public ItemListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(myContext);
        View view = inflater.inflate(R.layout.layout_items, viewGroup, false);
        ItemListAdapterViewHolder Holder = new ItemListAdapterViewHolder(view);

        return Holder;
    }

    //i holds position
    @Override
    public void onBindViewHolder(@NonNull final ItemListAdapterViewHolder itemListAdapterViewHolder, final int i) {

        String itemQuantity = items.get(i).getItemCount().toString();

        itemListAdapterViewHolder.textViewItemName.setText(items.get(i).getItemName());
        itemListAdapterViewHolder.textViewQty.setText(itemQuantity);

        myDatabaseAccess = DatabaseAccess.getInstance(myContext);
        myDatabaseAccess.open();

        String myItemName = items.get(i).getItemName();

        final String myItemID = myDatabaseAccess.getIDFromItembook(myItemName); //get the id associated with that name

        if((items.get(i).isItemEquipped()) == 1)
        {
            itemListAdapterViewHolder.itemEquipped.setSelected(true);
            itemListAdapterViewHolder.itemEquipped.setChecked(true);
        }
        else
        {
            itemListAdapterViewHolder.itemEquipped.setSelected(false);
            itemListAdapterViewHolder.itemEquipped.setChecked(false);
        }

        itemListAdapterViewHolder.itemEquipped.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(itemListAdapterViewHolder.itemEquipped.isSelected())
                {
                    myDatabaseAccess.setEquipped(myItemID, 0);
                    items.get(i).setItemEquipped(0);
                    itemListAdapterViewHolder.itemEquipped.setSelected(false);
                    itemListAdapterViewHolder.itemEquipped.setChecked(false);
                }
                else
                {
                    myDatabaseAccess.setEquipped(myItemID, 1);
                    items.get(i).setItemEquipped(1);
                    itemListAdapterViewHolder.itemEquipped.setSelected(true);
                    itemListAdapterViewHolder.itemEquipped.setChecked(true);
                }
            }
        });

        itemListAdapterViewHolder.parentLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String name = items.get(i).getItemName();

                String itemID = myDatabaseAccess.getIDFromItembook(name); //get the id associated with that name

                if (myDatabaseAccess.isIteminInventories(itemID)) {

                    myDialog = new Dialog(myContext); //for item infoinventory popup
                    myDialog.setContentView(R.layout.popup_iteminfoinventory);

                    getItemInfo(itemID, myDialog);

                    removeItemBttn = (Button) myDialog.findViewById(R.id.itemRemoveInventoryBtn);

                    closeBttn = (Button) myDialog.findViewById(R.id.closeBtn);

                    closeBttn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myDialog.dismiss();
                        }
                    });

                    myDialog.show();

                    final String finalItemID = itemID;
                    removeItemBttn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            int itemCount = myDatabaseAccess.getExistingItemCount(finalItemID);

                            if(itemCount > 1) {
                                myDatabaseAccess.removeFromInventoriesCount(finalItemID, itemCount-1); //remove 1 from count
                                getItemInfo(finalItemID, myDialog);

                                items.get(i).setItemCount(itemCount-1);
                                notifyItemChanged(i);
                            }

                            else {
                                myDatabaseAccess.deleteItemFromInv(finalItemID);

                                items.remove(i);
                                itemNameArrayLength--;

                                notifyItemRemoved(i);
                                notifyItemRangeChanged(i, itemNameArrayLength);
                                myDialog.dismiss();
                                toastMessage("Item removed from inventory!");
                            }

                            updateInventoryWeight();
                        }
                    });
                }
                else
                {
                    toastMessage("No ID associated with that name");
                }
            }
        });
    }

    private void toastMessage(String message) {
        Toast.makeText(myContext, message, Toast.LENGTH_SHORT).show();
    }

        private void getItemInfo(String id, Dialog myDialog) {
        Cursor data = myDatabaseAccess.getItemsData();

        itemSource = (TextView) myDialog.findViewById(R.id.itemSourceTextView);
        itemType = (TextView) myDialog.findViewById(R.id.itemTypeTextView);
        itemDesc = (TextView) myDialog.findViewById(R.id.itemDescTextView);
        itemNameTextView = (TextView) myDialog.findViewById(R.id.itemNameTextView);
        itemCount = (TextView) myDialog.findViewById(R.id.itemCountTextView);

        while (data.moveToNext()) {
            if (id.equals(data.getString(0))) {
                itemSource.setText(data.getString(4));
                itemType.setText(data.getString(2));
                itemDesc.setText(data.getString(3));
                itemNameTextView.setText(data.getString(1));
            }
        }

        data.close();

        itemCount.setText("QTY: " + Integer.toString(myDatabaseAccess.getExistingItemCount(id)));
    }

    @Override
    public int getItemCount() {
        return itemNameArrayLength;
    }

    //The View holder is a single instance of the layout used in the recycler.
    public class ItemListAdapterViewHolder extends RecyclerView.ViewHolder {
        RadioButton itemEquipped;
        TextView textViewItemName;
        TextView textViewQty;
        LinearLayout parentLayout;


        public ItemListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            itemEquipped = itemView.findViewById(R.id.itemRadioButton);
            textViewItemName = itemView.findViewById(R.id.ItemTextView);
            textViewQty = itemView.findViewById(R.id.qtyTextView);
            parentLayout = itemView.findViewById(R.id.linearLayout);
        }
    }

    public void updateInventoryWeight(){
        TextView txtView = (TextView) ((Activity)myContext).findViewById(R.id.textViewInventoryWeight);
        txtView.setText(myDatabaseAccess.inventoryWeight());
    }
}

