package com.meteor.meteortown.flag;

import org.bukkit.inventory.ItemStack;

public interface IFlag {
    String getName();
    boolean getDefaultSetting();
    boolean isMemberPer();
    boolean isAgainstPlayer();
    ItemStack getIconItem(boolean bool);
}
