package com.meteor.meteortown.flag;

import com.meteor.meteortown.MeteorTown;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlagItem {
    Map<String, Map<IconItemType,ItemStack>> itemStackMap = new HashMap<>();
    private ItemStack getItem(YamlConfiguration yml,String path){
        ItemStack itemStack = new ItemStack(Material.valueOf(yml.getString(path+".id")),1,(short)yml.getInt(path+".data"));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(yml.getString(path+".name").replace("&","§"));
        List<String> lore = new ArrayList<>();
        yml.getStringList(path+".lore").forEach(a->lore.add(a.replace("&","§")));
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    public FlagItem(YamlConfiguration flagYml){
        for(String key:flagYml.getKeys(false)){
            Map<IconItemType,ItemStack> tempMap = new HashMap<>();
            tempMap.put(IconItemType.ENABLE,getItem(flagYml,key+".enable"));
            tempMap.put(IconItemType.DISABLE,getItem(flagYml,key+".disable"));
            itemStackMap.put(key,tempMap);
        }
    }
    public ItemStack getIconItem(String flagKey,IconItemType iconItemType){
        return itemStackMap.get(flagKey).get(iconItemType);
    }
}
