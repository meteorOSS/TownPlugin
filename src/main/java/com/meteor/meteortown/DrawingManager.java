package com.meteor.meteortown;

import com.meteor.meteortown.data.drawing.Drawing;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.world.DataException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DrawingManager {
    MeteorTown plugin;
    Map<String,Drawing> drawingMap;
    Map<Player,ItemStack> playerItemStackMap;
    public DrawingManager(MeteorTown plugin){
        this.plugin = plugin;
        loadData();
        playerItemStackMap = new HashMap<>();
    }
    public void loadData(){
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder()+"/drawing.yml"));
        drawingMap = new HashMap<>();
        yml.getKeys(false).forEach(key->{
            drawingMap.put(key,new Drawing(key, ChatColor.translateAlternateColorCodes('&',yml.getString(key+".name")),yml.getString(key+".sc"),yml.getInt(key+".radius"),yml.getStringList("cmds")));
        });
    }
    public Drawing getDrawing(ItemStack itemStack){
        if(itemStack.hasItemMeta()&&itemStack.getItemMeta().hasDisplayName()){
            for(Drawing drawing : drawingMap.values()){
                if(drawing.getName().equalsIgnoreCase(itemStack.getItemMeta().getDisplayName())){
                    return drawing;
                }
            }
        }
        return null;
    }
    public void loadSchematic(Player player, Location location,Drawing drawing) {
        EditSession es = new EditSession(new BukkitWorld(player.getLocation().getWorld()), Integer.MAX_VALUE);
        Vector loc = new Vector(location.getX(), location.getY(), location.getZ());
        File schem = new File(MeteorTown.Instance.getDataFolder()+ "/schematics/"+drawing.getSc()+".schematic");
        try {
            CuboidClipboard cc = CuboidClipboard.loadSchematic(schem);

            if(dir(player).equalsIgnoreCase("")) {}
            else if(dir(player).equalsIgnoreCase("N")) {
                cc.paste(es, loc, false);
            } else if(dir(player).equalsIgnoreCase("S")) {
                cc.rotate2D(180);
                cc.paste(es, loc, false);
            } else if(dir(player).equalsIgnoreCase("E")) {
                cc.rotate2D(90);
                cc.paste(es, loc, false);
            } else if(dir(player).equalsIgnoreCase("W")) {
                cc.rotate2D(270);
                cc.paste(es, loc, false);
            }
            if(drawing.getCmds()!=null)
                drawing.getCmds().forEach(cmd-> Bukkit.dispatchCommand(Bukkit.getConsoleSender(),cmd.replace("@p@",player.getName())));
        } catch (DataException | IOException | MaxChangedBlocksException e) {
            System.out.print("DataException while loading schematic!");
            e.printStackTrace();
        }
    }

    public static String dir(Player player) {
        double rotation = player.getLocation().getYaw() - 180;
        if (rotation < 0) {
            rotation += 360.0;
        }
        if (0 <= rotation && rotation < 22.5) {
            return "N";
        }
        if (67.5 <= rotation && rotation < 112.5) {
            return "E";
        }
        if (157.5 <= rotation && rotation < 202.5) {
            return "S";
        }
        if (247.5 <= rotation && rotation < 292.5) {
            return "W";
        }
        if (337.5 <= rotation && rotation <= 360) {
            return "N";
        }
        return "";
    }
    public void addPlayer(Player p,ItemStack item){
        ItemStack itemStack = item.clone();
        itemStack.setAmount(1);
        playerItemStackMap.put(p,itemStack);
    }
    public void delPlayer(Player p,Boolean isGetItem){
        if(isGetItem){
            p.getInventory().addItem(playerItemStackMap.get(p));
        }
        playerItemStackMap.remove(p);
    }
    public boolean isExitPlayer(Player p ){
//        ItemStack item = playerItemStackMap.get(p);
//        item.setAmount(1);
//        p.getInventory().addItem(item);
        return playerItemStackMap.containsKey(p);
    }

}
