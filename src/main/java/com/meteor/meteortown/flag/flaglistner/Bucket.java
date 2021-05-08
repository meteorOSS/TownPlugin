package com.meteor.meteortown.flag.flaglistner;

import com.meteor.meteorlib.message.MessageManager;
import com.meteor.meteortown.MeteorTown;
import com.meteor.meteortown.data.town.Town;
import com.meteor.meteortown.flag.FlagManager;
import com.meteor.meteortown.flag.IFlag;
import com.meteor.meteortown.flag.IconItemType;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.inventory.ItemStack;

public class Bucket implements Listener, IFlag {
    MeteorTown plugin;
    MessageManager messageManager;
    public Bucket(){
        this.plugin = MeteorTown.Instance;
        this.messageManager = plugin.getTownManager().getMessageManager();
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
    }
    @Override
    public String getName() {
        return "bucket";
    }

    @Override
    public boolean getDefaultSetting() {
        return false;
    }

    @Override
    public boolean isMemberPer() {
        return true;
    }

    @Override
    public boolean isAgainstPlayer() {
        return true;
    }

    @Override
    public ItemStack getIconItem(boolean bool) {
        return bool ? FlagManager.flagItem.getIconItem("bucket", IconItemType.ENABLE):FlagManager.flagItem.getIconItem("bucket",IconItemType.DISABLE);
    }

    private boolean operate(Cancellable c, Block block, Player player){
        if(!plugin.getConfig().getStringList("Setting.enable-world").contains(block.getWorld().getName())||player.isOp()){
            return true;
        }
        Town town = plugin.getTownManager().getTown(block.getLocation());
        if(town==null){
            c.setCancelled(true);
            player.sendMessage(messageManager.getString("message.no-use-buck"));
            return false;
        }else{
            if(town.getOwnerUUID().equals(player.getUniqueId())||town.isHasPerm(player.getName(),getName(),getDefaultSetting())||town.isPermEnable("bucket",getDefaultSetting())){
                return true;
            }else{
                c.setCancelled(true);
                player.sendMessage(messageManager.getString("message.no-use-buck").replace("@owner@",town.getOwnerName()));
                return true;
            }
        }
    }
    @EventHandler
    void useBuck(PlayerBucketEmptyEvent playerBucketEvent){
        operate(playerBucketEvent,playerBucketEvent.getBlockClicked(),playerBucketEvent.getPlayer());
    }
}