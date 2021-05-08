package com.meteor.meteortown.util;

import com.meteor.meteorlib.message.MessageManager;
import com.meteor.meteortown.MeteorTown;
import com.meteor.meteortown.data.town.Dir;
import com.meteor.meteortown.data.town.SchematicsData.SchematicsData;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.*;

public class SchematicsUtil {
    private static MessageManager messageManager = MeteorTown.Instance.getTownManager().getMessageManager();

    public static CuboidClipboard getCuboidClipboard(String name,Location location){
        EditSession es = new EditSession(new BukkitWorld(location.getWorld()), Integer.MAX_VALUE);
        File schem = new File(MeteorTown.Instance.getDataFolder()+"/schematics/"+name+".schematic");
        try {
            CuboidClipboard cc = CuboidClipboard.loadSchematic(schem);
            return cc;
        }catch (Exception e){
            return null;
        }
    }
    public static Location[] getTownVertices(Location loc, org.bukkit.util.Vector size) {
        return new Location[] { loc
                .clone().add(-size.getX() + 0.5D, size.getY() + 0.5D, size.getZ() + 0.5D), loc
                .clone().add(size.getX() + 0.5D, size.getY() + 0.5D, size.getZ() + 0.5D), loc
                .clone().add(-size.getX() + 0.5D, size.getY() + 0.5D, -size.getZ() + 0.5D), loc
                .clone().add(size.getX() + 0.5D, size.getY() + 0.5D, -size.getZ() + 0.5D), loc
                .clone().add(-size.getX() + 0.5D, -size.getY() + 0.5D, size.getZ() + 0.5D), loc
                .clone().add(size.getX() + 0.5D, -size.getY() + 0.5D, size.getZ() + 0.5D), loc
                .clone().add(-size.getX() + 0.5D, -size.getY() + 0.5D, -size.getZ() + 0.5D), loc
                .clone().add(size.getX() + 0.5D, -size.getY() + 0.5D, -size.getZ() + 0.5D) };
    }
    public static SchematicsData getCenterLocation(Location location, Double range){
        Dir dir = getDir(location);
        CuboidClipboard cuboidClipboard = getCuboidClipboard("chushi",location);
        if(dir==Dir.X){
            return null;
        }
        location.setYaw((float) dir.getVaulue());
        double r = Double.valueOf(cuboidClipboard.getLength()/2);
        MeteorTown.Instance.getLogger().info(location.getYaw()+"");
        MeteorTown.Instance.getLogger().info(r+"");
        switch (dir){
            case N:
                return new SchematicsData(dir,location.subtract(0,0,r),cuboidClipboard);
            case W:
                return new SchematicsData(dir,location.subtract(r,0,0),cuboidClipboard);
            case S:
                return new SchematicsData(dir,location.add(0,0,r),cuboidClipboard);
            case E:
                return new SchematicsData(dir,location.add(r,0,0),cuboidClipboard);
            default:
                return null;
        }
    }
    private static Location getCenterLocation(Location location,Dir dir,Double range){
        switch (dir){
            case N:
                return location.subtract(0,0,range);
            case W:
                return location.subtract(range,0,0);
            case S:
                return location.add(0,0,range);
            case E:
                return location.add(range,0,0);
            default:
                return null;
        }
    }
    public static Dir getDir(Location location){
        double rotation = location.getYaw()-180;
        if(rotation<0){
            rotation+=360.0D;
        }
        if(0<=rotation&&rotation<22.5){
            return Dir.N;
        }
        if(67.5<=rotation&&rotation<112.5){
            return Dir.E;
        }
        if(157.5<=rotation&&rotation<202.5){
            return Dir.S;
        }
        if(247.5<=rotation&&rotation<292.5){
            return Dir.W;
        }
        if(337.5<=rotation&&rotation<=360){
            return Dir.N;
        }
        return Dir.X;
    }

    @Deprecated
    public static void loadSchematic(Player player, Location location) {
        EditSession es = new EditSession(new BukkitWorld(player.getLocation().getWorld()), Integer.MAX_VALUE);
        Vector loc = new Vector(location.getX(), location.getY(), location.getZ());
        try {
            CuboidClipboard cc = getCuboidClipboard("fishwater",location);
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
        } catch (Exception e) {
            System.out.print("DataException while loading schematic!");
            e.printStackTrace();
        }
    }
    @Deprecated
    private static String dir(Player player) {
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
//    public static void disRange(Location location, Vector size, Player player) {
//        final Location[] loc = getAreaVertices(location, size);
//        particleLine(loc[0], loc[1], player);
//        particleLine(loc[0], loc[2], player);
//        particleLine(loc[3], loc[1], player);
//        particleLine(loc[3], loc[2], player);
//        particleLine(loc[4], loc[5], player);
//        particleLine(loc[4], loc[6], player);
//        particleLine(loc[7], loc[5], player);
//        particleLine(loc[7], loc[6], player);
//        particleLine(loc[0], loc[4], player);
//        particleLine(loc[1], loc[5], player);
//        particleLine(loc[2], loc[6], player);
//        particleLine(loc[3], loc[7], player);
//    }
//    public static void particleLine(Location locA, Location locB, Player player) {
//        final org.bukkit.util.Vector vectorAB = locB.clone().subtract(locA).toVector();
//        final double vectorLength = vectorAB.length();
//        vectorAB.normalize();
//        for (double i = 0.0; i < vectorLength; i += 1.5) {
//            final org.bukkit.util.Vector vector = vectorAB.clone().multiply(i);
//            final Location point = locA.clone().add(vector);
//            if (player == null) {
//                SchematicsUtil.effLib.display(0.0f, 0.0f, 0.0f, 0.0f, 10, point, 50.0);
//            }
//            else {
//                SchematicsUtil.effLib.display(0.0f, 0.0f, 0.0f, 0.0f, 10, point, player);
//            }
//        }
//    }
}
