package com.meteor.meteortown.data.town.invholder;

import com.meteor.meteortown.data.town.Town;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AddMemberListInvHolder implements InventoryHolder {
    Town town;
    int pageCur;
    int pageMax;
    Map<Integer,String> playerMap = new HashMap<>();
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

    public int getPageCur() {
        return pageCur;
    }

    public void setPageCur(int pageCur) {
        this.pageCur = pageCur;
    }

    public int getPageMax() {
        return pageMax;
    }

    public void setPageMax(int pageMax) {
        this.pageMax = pageMax;
    }

    public Map<Integer, String> getPlayerMap() {
        return playerMap;
    }

    public void setPlayerMap(List<Player> playerList) {
        AtomicInteger i = new AtomicInteger(0);
        playerList.forEach(a->{
            playerMap.put(i.get(),a.getName());
            i.set(i.get()+1);
        });
    }
}
