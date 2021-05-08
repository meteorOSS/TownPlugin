package com.meteor.meteortown.flag.flaglistner;

import com.meteor.meteorlib.message.MessageManager;
import com.meteor.meteortown.MeteorTown;
import com.meteor.meteortown.data.town.Town;
import com.meteor.meteortown.flag.FlagManager;
import com.meteor.meteortown.flag.IFlag;
import com.meteor.meteortown.flag.IconItemType;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.inventory.ItemStack;

public class LavaFlow implements Listener, IFlag {
    MeteorTown plugin;
    MessageManager messageManager;
    public LavaFlow(){
        plugin = MeteorTown.Instance;
        messageManager = plugin.getTownManager().getMessageManager();
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
    }
    @Override
    public String getName() {
        return "lava-flow";
    }

    @Override
    public boolean getDefaultSetting() {
        return true;
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
        return bool ? FlagManager.flagItem.getIconItem("lava-flow", IconItemType.ENABLE):FlagManager.flagItem.getIconItem("lava-flow",IconItemType.DISABLE);
    }
    @EventHandler(priority = EventPriority.HIGHEST,ignoreCancelled = true)
    void weaterFlow(BlockFromToEvent fromToEvent){
        if((((fromToEvent.getBlock().getType() == Material.LAVA) ? 1 : 0) | ((fromToEvent.getBlock().getType() == Material.STATIONARY_LAVA) ? 1 : 0)) != 0){
            Town town = plugin.getTownManager().getTown(fromToEvent.getToBlock().getLocation());
            if(town!=null&&plugin.getConfig().getStringList("Setting.enable-world").contains(town.getLocation().getWorld().getName())&&
                    !town.isPermEnable("lava-flow",getDefaultSetting())){
                fromToEvent.setCancelled(true);
            }
        }
    }
}
