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
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class BuildFlag implements Listener, IFlag {
    MeteorTown plugin;
    MessageManager messageManager;
    public BuildFlag(){
        this.plugin = MeteorTown.Instance;
        this.messageManager = plugin.getTownManager().getMessageManager();
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
    }
    @Override
    public String getName() {
        return "build";
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
        return bool ? FlagManager.flagItem.getIconItem("build", IconItemType.ENABLE):FlagManager.flagItem.getIconItem("build",IconItemType.DISABLE);
    }

    private boolean operate(Cancellable c, Block block, Player player){
        if(!plugin.getConfig().getStringList("Setting.enable-world").contains(block.getWorld().getName())||player.isOp()){
            return true;
        }
        Town town = plugin.getTownManager().getTown(block.getLocation());
        if(town!=null){
            if(town.isPermEnable("build",getDefaultSetting())||town.getOwnerUUID().equals(player.getUniqueId())||town.isHasPerm(player.getName(),getName(),getDefaultSetting())){
                return true;
            }else{
                c.setCancelled(true);
                player.sendMessage(messageManager.getString("message.no-break-flag").replace("@owner@",town.getOwnerName()));
                return true;
            }
        }else{
            c.setCancelled(true);
            player.sendMessage(messageManager.getString("message.break-no-cleam"));
            return false;
//            if(plugin.getConfig().getStringList("Setting.allow-break").contains(block.getType().toString())){
//                if(block.getY()>75){
//                    return true;
//                }
//                c.setCancelled(true);
//                return false;
//            }else{
//                c.setCancelled(true);
//                return false;
//            }
        }
    }
    @EventHandler
    public void breakBlock(BlockBreakEvent breakEvent){
        operate(breakEvent,breakEvent.getBlock(),breakEvent.getPlayer());
    }
    @EventHandler
    public void placeBlock(BlockPlaceEvent placeEvent){
        operate(placeEvent,placeEvent.getBlock(),placeEvent.getPlayer());
    }
}
