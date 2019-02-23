package com.at.gmail.tomeofadventurers.Adapters;

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
import android.widget.TextView;
import android.widget.Toast;

import com.at.gmail.tomeofadventurers.Classes.DatabaseAccess;
import com.at.gmail.tomeofadventurers.R;


public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemListAdapterViewHolder>
{
    private Context myContext;
    String[] itemNames;
    int itemNameArrayLength;

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
    public ItemListAdapter(Context myContext, String[] itemNameStrings, int length) {
        this.myContext = myContext;
        itemNames = itemNameStrings;
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
        itemListAdapterViewHolder.textViewItemName.setText(itemNames[i]);

        itemListAdapterViewHolder.parentLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                myDatabaseAccess = DatabaseAccess.getInstance(myContext);
                myDatabaseAccess.open();

                String name = itemNames[i];

                Cursor data = myDatabaseAccess.getItemSlugitems(name); //get the slug associated with that name
                String itemSlug = "_";

                while (data.moveToNext()) {
                    itemSlug = data.getString(0);
                }

                data.close();

                if (itemSlug != "_") {

                    myDialog = new Dialog(myContext); //for item infoinventory popup
                    myDialog.setContentView(R.layout.popup_iteminfoinventory);

                    getItemInfo(itemSlug, myDialog);

                    removeItemBttn = (Button) myDialog.findViewById(R.id.itemRemoveInventoryBtn);

                    closeBttn = (Button) myDialog.findViewById(R.id.closeBtn);

                    closeBttn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myDialog.dismiss();
                        }
                    });

                    myDialog.show();

                    final String finalItemSlug = itemSlug;
                    removeItemBttn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int itemCount = myDatabaseAccess.getExistingItemCount(finalItemSlug);

                            if(itemCount > 1) {
                                myDatabaseAccess.removeFromInventoriesCount(finalItemSlug, itemCount-1); //remove 1 from count
                                getItemInfo(finalItemSlug, myDialog);
                            }

                            else {
                                itemNameArrayLength--; //reduce by one item

                                myDatabaseAccess.deleteItemFromInv(finalItemSlug);
                                notifyItemRemoved(i);
                                notifyItemRangeChanged(i, itemNameArrayLength);
                                myDialog.dismiss();
                                toastMessage("Item removed from inventory!");
                            }
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

        private void getItemInfo(String slug, Dialog myDialog) {
        Cursor data = myDatabaseAccess.getItemsData();

        itemSource = (TextView) myDialog.findViewById(R.id.itemSourceTextView);
        itemType = (TextView) myDialog.findViewById(R.id.itemTypeTextView);
        itemDesc = (TextView) myDialog.findViewById(R.id.itemDescTextView);
        itemNameTextView = (TextView) myDialog.findViewById(R.id.itemNameTextView);
        itemCount = (TextView) myDialog.findViewById(R.id.itemCountTextView);

        while (data.moveToNext()) {
            if (slug.equals(data.getString(0))) {
                itemSource.setText(data.getString(6));
                itemType.setText(data.getString(2));
                itemDesc.setText(data.getString(3));
                itemNameTextView.setText(data.getString(1));
            }
        }

        data.close();

        itemCount.setText("QTY: " + Integer.toString(myDatabaseAccess.getExistingItemCount(slug)));
    }

    @Override
    public int getItemCount() {
        return itemNameArrayLength;
    }

    //The View holder is a single instance of the layout used in the recycler.
    public class ItemListAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView textViewItemName;
        LinearLayout parentLayout;

        public ItemListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItemName = itemView.findViewById(R.id.ItemTextView);
            parentLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}

