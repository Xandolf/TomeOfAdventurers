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
        return this.itemName;
    }

    public Integer getItemCount()
    {
        return this.itemCount;
    }

    public Integer isItemEquipped()
    {
        return this.itemEquipped;
    }

    public void setItemName(String aName)
    {
        this.itemName = aName;
    }

    public void setItemCount(Integer aCount)
    {
        this.itemCount = aCount;
    }

    public void setItemEquipped(Integer isEquipped)
    {
        this.itemCount = isEquipped;
    }
}
