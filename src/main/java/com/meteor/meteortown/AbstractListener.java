package com.meteor.meteortown;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public abstract class AbstractListener<P extends Plugin> implements Listener {
    Plugin plugin;
    public AbstractListener(P plugin){
        this.plugin = plugin;
    }
    public void register(){
        this.plugin.getServer().getPluginManager().registerEvents(this,plugin);
    }
    public void unRegister(){
        HandlerList.unregisterAll((Listener)this);
    }
}