package com.meteor.meteortown.util;

import com.meteor.meteortown.MeteorTown;
import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.world.DataException;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipException;

public class Util {
    private static MeteorTown plugin = MeteorTown.Instance;
    public static String getLocText(Location location){
        return location.getX()+"#"+location.getY()+"#"+location.getZ();
    }
    public static Vector getVector(String sc){
        File schem = new File(MeteorTown.Instance.getDataFolder()+ "/schematics/"+sc+".schematic");
        CuboidClipboard cc = null;
        try {
            cc = CuboidClipboard.loadSchematic(schem);
        } catch (DataException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cc.getSize();
    }
    public static int[] getSize(Player player){
        String permKey = "mtown.group.";
        for (PermissionAttachmentInfo permissionAttachmentInfo : player.getEffectivePermissions()){
            if(permissionAttachmentInfo.getPermission().startsWith(permKey)){
                String group = permissionAttachmentInfo.getPermission().substring(permKey.length());
                player.sendMessage(group);
                String key = plugin.getConfig().getString("group."+group)!=null?"group."+group:"group.default";
                int[] size = {plugin.getConfig().getInt(key+".x"),plugin.getConfig().getInt(key+".y"),plugin.getConfig().getInt(key+".z")};
                return size;
            }
        }
        String defV = "group.default";
        int[] defSize = {plugin.getConfig().getInt(defV+".x"),plugin.getConfig().getInt(defV+".y"),plugin.getConfig().getInt(defV+".z")};
        return defSize;
    }
    public static int getAmount(Player player){
        String permKey = "mtown.amount.";
        for (PermissionAttachmentInfo permissionAttachmentInfo : player.getEffectivePermissions()){
            if(permissionAttachmentInfo.getPermission().startsWith(permKey)){
                String amount = permissionAttachmentInfo.getPermission().substring(permKey.length());
                try {
                    return Integer.valueOf(amount);
                }catch (NumberFormatException numberFormatException){
                    return 1;
                }
            }
        }
        return 1;
    }
    public static String getUpLevelGroup(Player player){
        String permKey = "mtown.up.";
        for (PermissionAttachmentInfo permissionAttachmentInfo : player.getEffectivePermissions()){
            if(permissionAttachmentInfo.getPermission().startsWith(permKey)){
                String group = permissionAttachmentInfo.getPermission().substring(permKey.length());
                return group;
            }
        }
        return "default";
    }
    public static Location[] getTownVertices(Location hub, int[] size) {
        return new Location[] { hub
                .clone().add(-size[0] + 0.5D, size[1] + 0.5D, size[2] + 0.5D), hub
                .clone().add(size[0] + 0.5D, size[1] + 0.5D, size[2] + 0.5D), hub
                .clone().add(-size[0] + 0.5D, size[1] + 0.5D, -size[2] + 0.5D), hub
                .clone().add(size[0] + 0.5D, size[1] + 0.5D, -size[2] + 0.5D), hub
                .clone().add(-size[0] + 0.5D, -size[1] + 0.5D, size[2] + 0.5D), hub
                .clone().add(size[0] + 0.5D, -size[1] + 0.5D, size[2] + 0.5D), hub
                .clone().add(-size[0] + 0.5D, -size[1] + 0.5D, -size[2] + 0.5D), hub
                .clone().add(size[0] + 0.5D, -size[1] + 0.5D, -size[2] + 0.5D) };
    }
    public static boolean createWeSc(Player player,Location location){
        EditSession editSession = new EditSession(new BukkitWorld(location.getWorld()),Integer.MAX_VALUE);
        Vector loc = new Vector(location.getX(),location.getY(),location.getZ());
        File sc = new File(plugin.getDataFolder()+"/schematics/"+plugin.getConfig().getString("Setting.we-sc","fangwu")+".schematic");
        try {
            CuboidClipboard cc = CuboidClipboard.loadSchematic(sc);
            cc.paste(editSession,loc,false);
            return true;
        }catch (MaxChangedBlocksException | DataException | IOException e){
            return false;
        }
    }
    public static  Vector toVector(Location location){
        return new Vector(location.getX(),location.getY(),location.getZ());
    }
}
