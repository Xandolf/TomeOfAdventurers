package com.at.gmail.tomeofadventurers.Classes;

public class Item {

    private String itemName;
    private Integer itemCount;
    private Integer itemEquipped; // 0 for false 1 for true

    public Item(String name, Integer count, Integer equipped)
    {
        this.itemName = name;
        this.itemCount = count;
        this.itemEquipped = equipped;
    }

    public String getItemName()
    {
        return itemName;
    }

    public Integer getItemCount()
    {
        return itemCount;
    }

    public Integer isItemEquipped()
    {
        return itemEquipped;
    }

    public void setItemName(String aName)
    {
        itemName = aName;
    }

    public void setItemCount(Integer aCount)
    {
        itemCount = aCount;
    }

    public void setItemEquipped(Integer isEquipped)
    {
        itemCount = isEquipped;
    }
}
