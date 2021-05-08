package com.meteor.meteortown.events;

import com.meteor.meteorlib.message.MessageManager;
import com.meteor.meteortown.MeteorTown;
import com.meteor.meteortown.data.town.Town;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

public class PlayerInteractiveEvents implements Listener {
    MeteorTown plugin;
    MessageManager messageManager;
    public PlayerInteractiveEvents(){
        plugin = MeteorTown.Instance;
        messageManager = plugin.getTownManager().getMessageManager();
    }
    @EventHandler
    void enterTown(PlayerMoveEvent moveEvent){
        Town town  = plugin.getTownManager().getTown(moveEvent.getTo());
        Player p = moveEvent.getPlayer();
        UUID uuid = plugin.getTownManager().getEnterTownTitleMap().get(p.getName())==null?UUID.randomUUID():plugin.getTownManager().getEnterTownTitleMap().get(p.getName());
        if(!plugin.getTownManager().getEnterTownTitleMap().containsKey(p.getName())||
                (town!=null&&!uuid.equals(town.getIndex()))||
                (town==null&&plugin.getTownManager().getEnterTownTitleMap().get(p.getName())!=null)){
            String townName = town==null?messageManager.getString("message.enter-town.field"):town.getTownName();
            String title = messageManager.getString("message.enter-town.title").replace("@town@",townName);
            String subtitle = messageManager.getString("message.enter-town.subtitle").replace("@town@",townName);
            p.sendTitle(title,subtitle);
            plugin.getTownManager().getEnterTownTitleMap().put(p.getName(),town==null?null:town.getIndex());
        }
    }
}
