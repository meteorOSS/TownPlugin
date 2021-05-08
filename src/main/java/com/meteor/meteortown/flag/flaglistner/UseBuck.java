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
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.inventory.ItemStack;

public class UseBuck implements Listener, IFlag {
    MeteorTown plugin;
    MessageManager messageManager;
    public UseBuck(){
        plugin = MeteorTown.Instance;
        messageManager = plugin.getTownManager().getMessageManager();
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
    }
    @Override
    public String getName() {
        return "use-buck";
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
        return bool ? FlagManager.flagItem.getIconItem("use-buck", IconItemType.ENABLE):FlagManager.flagItem.getIconItem("use-buck",IconItemType.DISABLE);
    }

    private boolean operate(Cancellable c, Location location, Player player){
        if(!plugin.getConfig().getStringList("Setting.enable-world").contains(location.getWorld().getName())||player.isOp()){
            return true;
        }
        Town town = plugin.getTownManager().getTown(location);
        if(town!=null){
            if(town.isPermEnable("use-buck",getDefaultSetting())||town.getOwnerUUID().equals(player.getUniqueId())||town.isHasPerm(player.getName(),getName(),getDefaultSetting())){
                return true;
            }else{
                c.setCancelled(true);
                player.sendMessage(messageManager.getString("message.no-use-buck").replace("@owner@",town.getOwnerName()));
                return false;
            }
        }
        return true;
    }

    @EventHandler
    void useBuck(PlayerBucketEvent playerBucketEvent){
        operate(playerBucketEvent,playerBucketEvent.getPlayer().getLocation(),playerBucketEvent.getPlayer());
    }
}
