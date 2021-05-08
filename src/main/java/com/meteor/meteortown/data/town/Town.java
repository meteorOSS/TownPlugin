package com.meteor.meteortown.data.town;

import com.meteor.meteortown.util.SchematicsUtil;
import com.sk89q.worldedit.Vector;
import org.bukkit.Location;

import java.util.*;

public class Town implements Cloneable{
    UUID index;
    Location location;
    Location[] locations;
    String townName;
    UUID ownerUUID;
    Location home;
    String ownerName;
    int[] size;
    Map<String,Boolean> flags = new HashMap<>();
    Map<String,Map<String, Boolean>> members;


    public Town() {
        super();
    }
    public Town(Town town){
        this.location = town.getLocation();
        this.locations = town.getLocations();
        this.size = town.getSize();
        this.index = town.getIndex();
    }

    public UUID getIndex() {
        return index;
    }

    public void setIndex(UUID index) {
        this.index = index;
    }

    public Location getLocation() {
        return location;
    }

    public Location getHome() {
        return home;
    }

    public void setHome(Location home) {
        this.home = home;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location[] getLocations() {
        return locations;
    }

    public void setLocations(Location[] locations) {
        this.locations = locations;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public void setOwnerUUID(UUID ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public int[] getSize() {
        return size;
    }

    public void setSize(int[] size) {
        this.size = size;
    }

    public Map<String, Boolean> getFlags() {
        return flags;
    }

    public void setFlags(Map<String, Boolean> flags) {
        this.flags = flags;
    }

    public Map<String, Map<String, Boolean>> getMembers() {
        return members;
    }

    public void setMembers(Map<String, Map<String, Boolean>> members) {
        this.members = members;
    }

    public Town(Location location, int[] size, String player,UUID uuid) {
        this.index = UUID.randomUUID();
        this.ownerName = player;
        this.ownerUUID = uuid;
        this.location = location;
        this.size = size;
        this.locations = getNewLocations();
        this.members = new HashMap<>();
        this.flags = new HashMap<>();
        this.townName = player+"的城镇";
        this.home = location;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    private Location[] getNewLocations(){
        return new Location[] {
                this.location.clone().subtract(size[0], size[1], size[2]), this.location.clone().add(size[0], size[1], size[2])
        };
    }


    private Location[] getImpactLocations(int[] rangeSize){
        return new Location[]{
                this.location.clone().subtract(size[0]+rangeSize[0], size[1]+rangeSize[1],size[2]+rangeSize[2]),
                this.location.clone().add(size[0]+rangeSize[0], size[1]+rangeSize[1],size[2]+rangeSize[2])
        };
    }


    public boolean isInTown(Location location){
        return (location.getX()>=this.locations[0].getX()&&location.getX()<=this.locations[1].getX()&&location.getY()>=this.locations[0].getY()&&location.getY()<=this.locations[1].getY()
                &&location.getZ()>=this.locations[0].getZ()&&location.getZ()<=this.locations[1].getZ());
    }


    public boolean isInTown(Location location,int[] rangeSize){
        Location[] locations = getImpactLocations(rangeSize);
        return (location.getX()>=locations[0].getX()&&location.getX()<=locations[1].getX()&&location.getY()>=locations[0].getY()&&location.getY()<=locations[1].getY()
                &&location.getZ()>=locations[0].getZ()&&location.getZ()<=locations[1].getZ());
    }




    public boolean isHasPerm(String player,String flag,Boolean defV){
        if(!members.containsKey(player)){
            return false;
        }
        return (Boolean)members.get(player).getOrDefault(flag,defV);
    }

    public boolean isPermEnable(String flag,Boolean def){
        return flags.containsKey(flag)?flags.get(flag):def;
    }
    public void setMembersPer(String members,String flag,Boolean enable){
        getMembers().get(members).put(flag,enable);
    }

    public void upSize(UpSize upSize){
        size[0] = size[0]+upSize.getX();
        size[1] = size[1]+upSize.getY();
        size[2] = size[2]+upSize.getZ();
        locations = getNewLocations();
    }

    public void setPerm(String flag,Boolean enable){
        flags.put(flag,enable);
    }

}
