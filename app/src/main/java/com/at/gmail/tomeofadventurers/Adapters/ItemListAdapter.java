package com.at.gmail.tomeofadventurers.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import com.at.gmail.tomeofadventurers.Fragments.HomeFragment;
import com.at.gmail.tomeofadventurers.Fragments.InventoryFragment;
import com.at.gmail.tomeofadventurers.R;

//From my current understanding and Adapter 'adapts' a given view into a listview or recycler.
//it "recycles" a given layout to do this
public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemListAdapterViewHolder>
{
    private Context myContext;
    String[] itemNames;

    //Constructor
    public ItemListAdapter(Context myContext, String[] itemNameStrings) {
        this.myContext = myContext;
        itemNames = itemNameStrings;
    }

    @NonNull
    @Override
    public ItemListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(myContext);
        View view = inflater.inflate(R.layout.layout_items, viewGroup, false);
        ItemListAdapterViewHolder Holder = new ItemListAdapterViewHolder(view);
        return Holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListAdapterViewHolder itemListAdapterViewHolder, int i) {
        itemListAdapterViewHolder.textViewItemName.setText(itemNames[i]);

        itemListAdapterViewHolder.parentLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                toastMessage("button clicked?");
            }
        });
    }

    private void toastMessage(String message) {
        Toast.makeText(myContext, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return itemNames.length;
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

