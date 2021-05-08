package com.meteor.meteortown.hook;

import com.meteor.meteortown.MeteorTown;
import com.meteor.meteortown.data.town.Town;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PapiHook extends PlaceholderExpansion {
    MeteorTown plugin;
    public PapiHook(){
        plugin = MeteorTown.Instance;
    }
    @Override
    public String getIdentifier() {
        return "mtown";
    }

    @Override
    public String getAuthor() {
        return "meteor";
    }

    @Override
    public String getVersion() {
        return "bzd";
    }

    @Override
    public String onRequest(OfflinePlayer p, String params) {
        Town town = plugin.getTownManager().getTownName(p.getName());
        if(params.equalsIgnoreCase("name")){
            if(town==null){
                return "暂无";
            }else{
                return town.getTownName();
            }
        }
        if(params.equalsIgnoreCase("size")){
            Player player = (Player)p;
            town = plugin.getTownManager().getTown(player.getLocation());
            if(town==null){
                return "暂无";
            }else{
                return town.getSize()[0]+"x"+town.getSize()[1]+"x"+town.getSize()[2];
            }
        }
        return super.onRequest(p, params);
    }
}
