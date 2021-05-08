package com.meteor.meteortown.commands;

import com.meteor.meteorlib.message.MessageManager;
import com.meteor.meteortown.MeteorTown;
import com.meteor.meteortown.data.town.PayType;
import com.meteor.meteortown.data.town.Town;
import com.meteor.meteortown.data.town.UpSize;
import com.meteor.meteortown.util.InvUtil;
import com.meteor.meteortown.util.ItemUtil;
import com.meteor.meteortown.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TownCommands implements CommandExecutor{
    MeteorTown plugin;
    MessageManager messageManager;
    public static Map<String, Location> locationMap = new HashMap<>();
    public TownCommands(){
        this.plugin = MeteorTown.Instance;
        this.messageManager = plugin.getTownManager().getMessageManager();
    }
    private boolean passCmd(Town town,Player player){
        if(!plugin.getConfig().getStringList("Setting.enable-world").contains(player.getWorld().getName())){
            messageManager.sendMessage(player,"message.disable-world");
            return false;
        }
        if(town==null){
            messageManager.sendMessage(player,"message.no-town");
            return false;
        }else if(!town.getOwnerName().equalsIgnoreCase(player.getName())){
            messageManager.sendMessage(player,"message.no-owner");
            return false;
        }
        return true;
    }
    private boolean passCleam(Player player){
        String blockname = player.getLocation().subtract(0,1,0).getBlock().getType().toString();
        if(!plugin.getConfig().getStringList("Setting.enable-world").contains(player.getWorld().getName())){
            messageManager.sendMessage(player,"message.disable-world");
            return false;
        }
        else if(!player.isOp()&&plugin.getTownManager().getTownAmount(player)>=Util.getAmount(player)){
            messageManager.sendMessage(player,"message.amount-limit");
            return false;
        }
        else if(!plugin.getConfig().getStringList("Setting.cleam-block").contains(blockname)){
            messageManager.sendMessage(player,"message.no-block");
            return false;
        }
        return true;
    }
    private boolean passUseTown(Player player,Town town){
        if(town==null){
            messageManager.sendMessage(player,"message.no-town");
            return false;
        }else if(!player.isOp()&&!town.getOwnerUUID().equals(player.getUniqueId())){
            messageManager.sendMessage(player,"message.no-owner");
            return false;
        }
        return true;
    }
    private void sendHelp(CommandSender sender){
        messageManager.getStringList("help.player").forEach(m->sender.sendMessage(m));
        if(sender.isOp()){
            messageManager.getStringList("help.admin").forEach(m->sender.sendMessage(m));
        }
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length==0||args[0].equalsIgnoreCase("help")){
            sendHelp(sender);
            return true;
        }
        if(args[0].equalsIgnoreCase("reload")&&args.length==1&&sender.isOp()){
            plugin.getTownManager().reloadConfig();
            plugin.getTownManager().getDrawingManager().loadData();
            sender.sendMessage(messageManager.getString("message.reload"));
            return true;
        }
        if(args[0].equalsIgnoreCase("rname")&&args.length==2&&sender instanceof Player){
            Player p = (Player)sender;
            Town town = plugin.getTownManager().getTown(p.getLocation());
            if(passUseTown(p,town)){
                String name = plugin.getConfig().getString("Setting.item.rname").replace("&","§");
                if(ItemUtil.isReqItem(p,name)){
                    ItemUtil.takeNameItem(p,name,1);
                    town.setTownName(args[1]);
                    p.sendMessage(messageManager.getString("message.rname-sur").replace("@dname@",args[1]));
                    return true;
                }else{
                    p.sendMessage(messageManager.getString("message.req-item").replace("@item@",name));
                    return true;
                }
            }
        }
        if(args.length>=1&&sender instanceof Player){
            Player p = (Player)sender;
            Town town = plugin.getTownManager().getTown(p.getLocation());
            switch (args[0]){
                case "home":
                    List<Town> townList = plugin.getTownManager().getTownList(p.getName());
                    if(townList.isEmpty()){
                        messageManager.sendMessage(p,"message.no-cleam-town");
                        return true;
                    }
                    int index = 0;

                    try {
                        index = Integer.parseInt(args[1]);
                        index--;
                        town = townList.get(index);
                    } catch (Exception exception) {
                        town = townList.get(0);
                    }

                    locationMap.put(p.getName(),town.getHome());
                    String title = messageManager.getString("message.back-home.title");
                    String subtitle = messageManager.getString("message.back-home.subtitle");
                    p.sendTitle(title,subtitle);
                    Bukkit.getScheduler().runTaskLater(plugin,()->{
                        if(locationMap.containsKey(p.getName())){
                            messageManager.sendMessage(p,"message.back-home.mes");
                            p.teleport(locationMap.get(p.getName()));
                            locationMap.remove(p.getName());
                        }
                    },plugin.getConfig().getInt("Setting.tp-time")*20);
                    return true;
                case "sethome":
                    town = plugin.getTownManager().getTownName(p.getName());
                    if(town==null){
                        messageManager.sendMessage(p,"message.no-cleam-town");
                        return true;
                    }
                    town.setHome(p.getLocation());
                    p.sendMessage(messageManager.getString("message.sethome").replace("@loc@",Util.getLocText(p.getLocation())));
                    return true;
                case "perm":
                    if(passUseTown(p,town)){
                        InvUtil.openAdminPerMenu(town,p,1);
                        return true;
                    }
                    return true;
                case "members":
                    if(passCmd(town,p)){
                        InvUtil.openMemberList(town,p);
                        return true;
                    }
                    return true;
                case "up":
                    if(passUseTown(p,town)){
                        InvUtil.openLevelUp(p,town);
                        return true;
                    }
                    return true;
                case "card":
                    town = plugin.getTownManager().getTown(p.getLocation());
                    if(town==null||!town.getOwnerName().equalsIgnoreCase(p.getName())){
                        messageManager.sendMessage(p,"message.no-cleam-town");
                        return true;
                    }
                    String group = Util.getUpLevelGroup(p);
                    String path = "up-size."+group;
                    UpSize upSize = new UpSize(plugin.getConfig().getInt(path+".x"),plugin.getConfig().getInt(path+".y"),plugin.getConfig().getInt(path+".z"));
                    plugin.getTownManager().upSize(town,upSize,p,true);
                    return true;
                case "cleam":
                    int[] size = Util.getSize(p);
                    if(passCleam(p)){
                        InvUtil.openCreateTownGui(p,size);
                    }
                    return true;
                case "range":
                    town = plugin.getTownManager().getTown(p.getLocation());
                    if(town!=null){
                        long current = System.currentTimeMillis();
                        Town finalTown = town;
                        Bukkit.getScheduler().runTaskTimer(plugin,()->{
                            long lastTime = (System.currentTimeMillis()-current)/1000;
                            if(lastTime<plugin.getConfig().getInt("Setting.effect.time",5))
                                plugin.getTownManager().displayRange(finalTown);
                        },0,20L);
                    }
                    return true;
                default:
                    return true;
            }
        }
        sendHelp(sender);
        return false;
    }
}
