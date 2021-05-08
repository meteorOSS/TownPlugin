package com.meteor.meteortown.data.town.invholder;

import com.meteor.meteortown.data.town.Town;
import com.meteor.meteortown.flag.IFlag;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AdminPerMenuInvHolder implements InventoryHolder {
    Town town;
    Map<Integer, IFlag> integerIFlagMap = new HashMap<>();
    int pageCur;
    int pageMax;
    @Override
    public Inventory getInventory() {
        return null;
    }

    public Town getTown() {
        return town;
    }

    public Map<Integer, IFlag> getIntegerIFlagMap() {
        return integerIFlagMap;
    }

    public int getPageCur() {
        return pageCur;
    }

    public int getPageMax() {
        return pageMax;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    public void setPageCur(int pageCur) {
        this.pageCur = pageCur;
    }

    public void setPageMax(int pageMax) {
        this.pageMax = pageMax;
    }

    public void setIntegerIFlagMap(List<IFlag> flagList){
        AtomicInteger i = new AtomicInteger(0);
        flagList.forEach(a->{
            integerIFlagMap.put(i.get(),a);
            i.set(i.get()+1);
        });
    }

}
