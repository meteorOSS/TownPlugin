package com.meteor.meteortown.data.town.invholder;

import com.meteor.meteortown.data.town.Town;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class MemberListInvHolder implements InventoryHolder {
    Town town;
    Map<Integer,String> meberMap = new HashMap<>();
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


    public void setMeberMap(Set<String> mebers){
        AtomicInteger i = new AtomicInteger(0);
        mebers.forEach(a->{
            meberMap.put(i.get(),a);
            i.set(i.get()+1);
        });
    }

    public Map<Integer, String> getMeberMap() {
        return meberMap;
    }
}
