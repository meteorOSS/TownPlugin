package com.meteor.meteortown.data.town.invholder;

import com.meteor.meteortown.data.town.Town;
import com.meteor.meteortown.data.town.UpSize;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class LevelUpTownInvHolder implements InventoryHolder {
    Town town;
    boolean levelMax;
    UpSize upSize;
    @Override
    public Inventory getInventory() {
        return null;
    }

    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    public boolean isLevelMax() {
        return levelMax;
    }

    public void setLevelMax(boolean levelMax) {
        this.levelMax = levelMax;
    }

    public UpSize getUpSize() {
        return upSize;
    }

    public void setUpSize(UpSize upSize) {
        this.upSize = upSize;
    }
}
