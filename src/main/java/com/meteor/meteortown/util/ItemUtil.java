package com.meteor.meteortown.util;

import com.meteor.meteortown.data.town.TakeItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemUtil {
    public static boolean isPass(Player player, List<TakeItem> takeItemList){
        List<ItemStack> itemStacks = new ArrayList<>();
        takeItemList.forEach(a->itemStacks.add(a.getItemStack()));
        boolean contain = false;
        for(ItemStack itemStack : itemStacks){
            if(containItem(player,itemStack)){
                contain = true;
            }else{
                contain = false;
            }
        }
        if(contain){
            itemStacks.forEach(a->remoeItem(player,a));
            return true;
        }
        return false;
    }
    private static boolean containItem(Player player,ItemStack itemStack){
//        player.sendMessage(itemStack.getType()+"/"+itemStack.getAmount());
        ItemStack[] itemStacks = player.getInventory().getContents();
        int amount = 0;
        for(ItemStack i : itemStacks){
            if(i!=null&&i.getType()!= Material.AIR){
                if(i.isSimilar(itemStack)){
                    amount+=i.getAmount();
                }
            }
        }
        return amount>=itemStack.getAmount();
    }

    private static boolean remoeItem(Player player,ItemStack itemStack){
        ItemStack[] itemStacks = player.getInventory().getContents();
        int amount = itemStack.getAmount();
        for(ItemStack i:itemStacks){
            if(i!=null&&i.getType()!=Material.AIR){
                if(i.isSimilar(itemStack)){
                    int takeAmount = i.getAmount()>=amount?amount:i.getAmount();
                    i.setAmount(i.getAmount()-takeAmount);
                    amount -= takeAmount;
                }
            }
        }
//        for(ItemStack i : itemStacks){
//            if(i!=null&&i.getType()!=Material.AIR){
//                if(i.isSimilar(itemStack)){
//                    i.setAmount(amount>=i.getAmount()?0:i.getAmount()-amount);
//                    amount = amount-i.getAmount();
//                }
//            }
//        }
        return true;
    }

    public static boolean isReqItem(Player p, String name) {
        name = ChatColor.translateAlternateColorCodes('&', name);
        ItemStack[] itemStacks = p.getInventory().getContents();
        for (ItemStack itemStack : itemStacks) {
            if (itemStack != null && itemStack.getType() != Material.AIR && itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName() && itemStack.getItemMeta().getDisplayName()
                    .equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    public static boolean takeNameItem(Player p, String name, int amount) {
        name = ChatColor.translateAlternateColorCodes('&', name);
        ItemStack[] itemStacks = p.getInventory().getContents();
        for (ItemStack itemStack : itemStacks) {
            if (itemStack != null && itemStack.getType() != Material.AIR &&
                    itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName() &&
                    itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(name))
                itemStack.setAmount(itemStack.getAmount() - amount);
        }
        return false;
    }

}

