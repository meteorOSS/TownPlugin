package com.meteor.meteortown.events;

import com.meteor.meteorlib.message.MessageManager;
import com.meteor.meteortown.MeteorTown;
import com.meteor.meteortown.commands.TownCommands;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class OthertEvents implements Listener {
    MessageManager messageManager = MeteorTown.Instance.getTownManager().getMessageManager();
    @EventHandler
    void tpMove(PlayerMoveEvent event){
        if(TownCommands.locationMap.containsKey(event.getPlayer().getName())){
            String title = messageManager.getString("message.back-home.cancel.title");
            String subtitle = messageManager.getString("message.back-home.cancel.subtitle");
            TownCommands.locationMap.remove(event.getPlayer().getName());
            event.getPlayer().sendTitle(title,subtitle);
        }
    }
}
