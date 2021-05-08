package com.meteor.meteortown.flag.flaglistner;

import com.meteor.meteorlib.message.MessageManager;
import com.meteor.meteortown.MeteorTown;
import com.meteor.meteortown.data.town.Town;
import com.meteor.meteortown.flag.FlagManager;
import com.meteor.meteortown.flag.IFlag;
import com.meteor.meteortown.flag.IconItemType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerMoveFlag implements Listener, IFlag {
    MeteorTown plugin;
    MessageManager messageManager;
    public PlayerMoveFlag(){
        this.plugin = MeteorTown.Instance;
        this.messageManager = plugin.getTownManager().getMessageManager();
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
    }
    @Override
    public String getName() {
        return "move-flag";
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
        return true;
    }

    @Override
    public ItemStack getIconItem(boolean bool) {
        return bool ? FlagManager.flagItem.getIconItem("player-move", IconItemType.ENABLE):FlagManager.flagItem.getIconItem("player-move",IconItemType.DISABLE);
    }

    private boolean operate(Cancellable c, Location location, Player player){
        if(!plugin.getConfig().getStringList("Setting.enable-world").contains(location.getWorld().getName())||player.isOp()){
            return true;
        }
        Town town = plugin.getTownManager().getTown(location);
        if(town!=null){
            if(town.isPermEnable("move-flag",getDefaultSetting())||town.getOwnerUUID().equals(player.getUniqueId())||town.isHasPerm(player.getName(),getName(),getDefaultSetting())){
                return true;
            }else{
                c.setCancelled(true);
                player.sendMessage(messageManager.getString("message.no-move-flag").replace("@owner@",town.getOwnerName()));
                return false;
            }
        }
        return true;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    void playerMove(PlayerMoveEvent event){
        operate(event,event.getPlayer().getLocation(),event.getPlayer());
    }

}
