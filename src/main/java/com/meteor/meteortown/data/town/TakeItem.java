package com.meteor.meteortown.data.town;

import org.bukkit.inventory.ItemStack;

public class TakeItem {
    String name;
    ItemStack itemStack;

    public TakeItem(String name, ItemStack itemStack) {
        this.name = name;
        this.itemStack = itemStack;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
}
