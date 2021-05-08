package com.meteor.meteortown.flag.flaglistner;

import com.meteor.meteorlib.message.MessageManager;
import com.meteor.meteortown.MeteorTown;
import com.meteor.meteortown.data.town.Town;
import com.meteor.meteortown.flag.FlagManager;
import com.meteor.meteortown.flag.IFlag;
import com.meteor.meteortown.flag.IconItemType;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;

public class MobSpawnerFlag implements Listener, IFlag {
    MeteorTown plugin;
    MessageManager messageManager;
    public MobSpawnerFlag(){
        plugin = MeteorTown.Instance;
        messageManager = plugin.getTownManager().getMessageManager();
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
    }
    @Override
    public String getName() {
        return "mob-spawn";
    }

    @Override
    public boolean getDefaultSetting() {
        return false;
    }

    @Override
    public boolean isMemberPer() {
        return false;
    }

    @Override
    public boolean isAgainstPlayer() {
        return false;
    }

    @Override
    public ItemStack getIconItem(boolean bool) {
        return bool ? FlagManager.flagItem.getIconItem("mob-spawner", IconItemType.ENABLE):FlagManager.flagItem.getIconItem("mob-spawner",IconItemType.DISABLE);
    }
    @EventHandler(priority = EventPriority.HIGHEST,ignoreCancelled = true)
    void weaterFlow(EntitySpawnEvent event){
        if(event.getEntity() instanceof Monster){
            Town town = plugin.getTownManager().getTown(event.getLocation());
            if(town!=null&&plugin.getConfig().getStringList("Setting.enable-world").contains(event.getLocation().getWorld().getName())&&!town.isPermEnable(getName(),getDefaultSetting())){
                event.setCancelled(true);
            }
        }
    }
}
