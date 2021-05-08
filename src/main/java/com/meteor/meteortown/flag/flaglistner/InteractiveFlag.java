package com.meteor.meteortown.flag.flaglistner;

import com.meteor.meteorlib.message.MessageManager;
import com.meteor.meteortown.MeteorTown;
import com.meteor.meteortown.data.town.Town;
import com.meteor.meteortown.flag.FlagManager;
import com.meteor.meteortown.flag.IFlag;
import com.meteor.meteortown.flag.IconItemType;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InteractiveFlag implements Listener, IFlag {
    MeteorTown plugin;
    MessageManager messageManager;
    public InteractiveFlag(){
        this.plugin = MeteorTown.Instance;
        this.messageManager = plugin.getTownManager().getMessageManager();
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
    }
    @Override
    public String getName() {
        return "interactive";
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
        return true;
    }

    @Override
    public ItemStack getIconItem(boolean bool) {
        return bool ? FlagManager.flagItem.getIconItem("interactive", IconItemType.ENABLE):FlagManager.flagItem.getIconItem("interactive",IconItemType.DISABLE);
    }

    private boolean operateBlock(Cancellable c, Block block, Player player){
        if(!plugin.getConfig().getStringList("Setting.enable-world").contains(block.getWorld().getName())||player.isOp()){
            return true;
        }
        Town town = plugin.getTownManager().getTown(block.getLocation());
        if(town!=null&&plugin.getConfig().getStringList("Setting.intera-block").contains(block.getType().toString())){
            if(town.isPermEnable("interactive",getDefaultSetting())||town.getOwnerUUID().equals(player.getUniqueId())||town.isHasPerm(player.getName(),getName(),getDefaultSetting())){
                return true;
            }else{
                c.setCancelled(true);
                player.sendMessage(messageManager.getString("message.no-interactive-flag").replace("@owner@",town.getOwnerName()));
                return false;
            }
        }
        return true;
    }
    private boolean operateEntity(Cancellable c, Player player) {
        if (!this.plugin.getConfig().getStringList("Setting.enable-world").contains(player.getLocation().getWorld().getName()) || player.isOp())
            return true;
        Town town = this.plugin.getTownManager().getTown(player.getLocation());
        if (town != null) {
            if (town.isPermEnable("interactive", Boolean.valueOf(getDefaultSetting())) || town.getOwnerUUID().equals(player.getUniqueId()) || town.isHasPerm(player.getName(), getName(), Boolean.valueOf(getDefaultSetting())))
                return true;
            c.setCancelled(true);
            player.sendMessage(this.messageManager.getString("message.no-interactive-flag").replace("@owner@", town.getOwnerName()));
            return false;
        }
        return true;
    }

    @EventHandler
    void interaBlock(PlayerInteractEvent interactEvent) {
        if (interactEvent.hasBlock())
            operateBlock((Cancellable)interactEvent, interactEvent.getClickedBlock(), interactEvent.getPlayer());
    }

    @EventHandler
    void interaFrame(PlayerInteractEntityEvent interactEntityEvent) {
        if (interactEntityEvent.getRightClicked().getType() == EntityType.ITEM_FRAME || interactEntityEvent.getRightClicked().getType() == EntityType.ARMOR_STAND)
            operateEntity((Cancellable)interactEntityEvent, interactEntityEvent.getPlayer());
    }

    @EventHandler
    void damageFrame(EntityDamageByEntityEvent damageByEntityEvent) {
        if (damageByEntityEvent.getDamager() instanceof Player) {
            Player player = (Player)damageByEntityEvent.getDamager();
            Entity entity = damageByEntityEvent.getEntity();
            if (entity.getType() == EntityType.ITEM_FRAME || entity.getType() == EntityType.ARMOR_STAND)
                operateEntity((Cancellable)damageByEntityEvent, player);
        }
    }
}
