package com.meteor.meteortown;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.meteor.meteorlib.message.MessageManager;
import com.meteor.meteortown.data.drawing.Drawing;
import com.meteor.meteortown.data.town.Dir;
import com.meteor.meteortown.data.town.PayType;
import com.meteor.meteortown.data.town.SchematicsData.SchematicsData;
import com.meteor.meteortown.data.town.Town;
import com.meteor.meteortown.data.town.UpSize;
import com.meteor.meteortown.util.EffLib;
import com.meteor.meteortown.util.ItemUtil;
import com.meteor.meteortown.util.SchematicsUtil;
import com.meteor.meteortown.util.Util;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.world.DataException;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class TownManager extends AbstractListener<MeteorTown>{
    MessageManager messageManager;
    Map<String, List<Town>> townsMap = Maps.newHashMap();
    Map<String,UUID> enterTownTitleMap = new HashMap<>();
    Economy economy = null;
    PlayerPointsAPI pointsAPI = null;
    DrawingManager drawingManager;
    EffLib effLib;
    public MessageManager getMessageManager() {
        return messageManager;
    }

    public Map<String, UUID> getEnterTownTitleMap() {
        return enterTownTitleMap;
    }

    public void setMessageManager(MessageManager messageManager) {
        this.messageManager = messageManager;
    }

    public Map<String, List<Town>> getTownsMap() {
        return townsMap;
    }

    public void setTownsMap(Map<String, List<Town>> townsMap) {
        this.townsMap = townsMap;
    }

    public DrawingManager getDrawingManager() {
        return drawingManager;
    }

    public TownManager(MeteorTown plugin){
        super(plugin);
        reloadConfig();
        RegisteredServiceProvider<Economy> ecoapi = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        economy = ecoapi.getProvider();
        pointsAPI = PlayerPoints.getPlugin(PlayerPoints.class).getAPI();
        loadData();
        drawingManager = new DrawingManager(plugin);
        effLib = EffLib.valueOf(plugin.getConfig().getString("Setting.effect.type","CLOUD"));
    }
    public void reloadConfig(){
        plugin.reloadConfig();
        File f = new File(plugin.getDataFolder()+"/message.yml");
        messageManager = new MessageManager(YamlConfiguration.loadConfiguration(f),true);
    }

    public List<Town> getTowns(World world){
        return townsMap.computeIfAbsent(world.getName(),a-> Lists.newArrayList());
    }

    //寻找领地
    public Town getTown(Location location){
        return getTowns(location.getWorld()).stream().filter(a->a.isInTown(location)).findFirst().orElse(null);
    }
    //返回玩家城镇数量
    public int getTownAmount(Player player){
        return (int) getTowns(player.getWorld()).stream().filter(a -> a.getOwnerUUID().equals(player.getUniqueId())).count();
    }
    //检测是否发生区域碰撞
    public boolean isImpact(Town town){
        for(Town t : getTowns(town.getLocation().getWorld())){
            if(!t.getIndex().equals(town.getIndex())){
                for(Location location1 : Util.getTownVertices(town.getLocation(),town.getSize())){
                    if(t.isInTown(location1)){
                        return true;
                    }
                }
                for(Location location2 : Util.getTownVertices(t.getLocation(),t.getSize())){
                    if(town.isInTown(location2)){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean isImpactCleam(Town town){
        int[] rangeSize = {Integer.valueOf(plugin.getConfig().getInt("Setting.protect.x",20)),
                Integer.valueOf(plugin.getConfig().getInt("Setting.protect.y",20)),
                Integer.valueOf(plugin.getConfig().getInt("Setting.protect.z",20))};
        for(Town t : getTowns(town.getLocation().getWorld())){
            if(!t.getIndex().equals(town.getIndex())){
                for(Location location1 : Util.getTownVertices(town.getLocation(),town.getSize())){
                    if(t.isInTown(location1,rangeSize)){
                        return true;
                    }
                }
                for(Location location2 : Util.getTownVertices(t.getLocation(),t.getSize())){
                    if(town.isInTown(location2,rangeSize)){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    //升级城镇区域
    public boolean upSize(Town town, UpSize upSize,Player player,boolean takeItem){
        //创建升级后的town实例检测区域碰撞
        if(isImpact(new Town(town))){
            player.sendMessage(messageManager.getString("message.impact-town"));
            return false;
        }
        if(takeItem){
            if(ItemUtil.isReqItem(player,plugin.getConfig().getString("Setting.item.up"))){
                ItemUtil.takeNameItem(player,plugin.getConfig().getString("Setting.item.up"),1);
                town.upSize(upSize);
                player.sendMessage(messageManager.getString("message.up-sur"));
            }else{
                player.sendMessage(messageManager.getString("message.no-req-item"));
            }
            return true;
        }else {
            if(((upSize.getPayType()==PayType.POINTS&&pointsAPI.look(player.getName())>=upSize.getPoints())||
                    (upSize.getPayType()==PayType.MONEY&&economy.getBalance(player.getName())>=upSize.getPoints()))){
                if(ItemUtil.isPass(player,upSize.getTakeItemList())){
                    if(upSize.getPayType()==PayType.MONEY){
                        economy.withdrawPlayer(player.getName(),upSize.getPoints());
                    }else{
                        pointsAPI.take(player.getName(),upSize.getPoints());
                    }
                    town.upSize(upSize);
                    player.sendMessage(messageManager.getString("message.up-sur"));
                    return true;
                }
                player.sendMessage(messageManager.getString("message.no-req-item"));
                return false;
            }else{
                player.sendMessage(messageManager.getString("message.no-points")
                        .replace("@v@",String.valueOf(upSize.getPoints())).replace("@type@",
                                upSize.getPayType()==PayType.POINTS?"点卷":"金币"));
                return false;
            }
        }
    }
    public Town getTownName(String player){
        for(List<Town> town : townsMap.values()){
            for(Town t : town){
                if(t.getOwnerName().equalsIgnoreCase(player)){
                    return t;
                }
            }
        }
        return null;
    }

    public List<Town> getTownList(String player){
        List<Town> townList = new ArrayList<>();
        for(List<Town> towns : townsMap.values()){
            for(Town t: towns){
                if(t.getOwnerName().equalsIgnoreCase(player)){
                    townList.add(t);
                }
            }
        }
        return townList;
    }
    public boolean createNewTown(Player player,int[] size,boolean isPaste){
        if(!plugin.getConfig().getStringList("Setting.enable-world").contains(player.getWorld().getName())){
            messageManager.sendMessage(player,"message.disable-world");
            return false;
        }
        Location pLocation = player.getLocation();
        SchematicsData schematicsData = SchematicsUtil.getCenterLocation(player.getLocation(),Double.valueOf(size[0]));
        if(schematicsData==null){
            messageManager.sendMessage(player,"message.no-dir");
            return false;
        }
        Town town = new Town(schematicsData.getLocation(),size,player.getName(),player.getUniqueId());
        town.setHome(player.getLocation());
//        SchematicsUtil.disRange(town.getLocation(),new Vector(Double.valueOf(size[0]),Double.valueOf(size[1]),Double.valueOf(size[2])),player);
        if(isImpactCleam(town)){
            player.sendMessage(messageManager.getString("message.impact-town"));
            return false;
        }
        try {
            if(isPaste){
                if(schematicsData.getDir()!= Dir.X&&schematicsData.getDir()!=Dir.N){
                    schematicsData.getCuboidClipboard().rotate2D((int) schematicsData.getDir().getRotate());
                }
                schematicsData.getCuboidClipboard().paste(new EditSession(new BukkitWorld(pLocation.getWorld()),Integer.MAX_VALUE),Util.toVector(pLocation),false);
            }
            getTowns(pLocation.getWorld()).add(town);
            String title = messageManager.getString("message.cleam-title.title");
            String subtitle = messageManager.getString("message.cleam-title.subtitle");
            player.sendTitle(title,subtitle);
            messageManager.sendMessage(player,"message.cleam-sur");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    //Save and Load
    public void saveData(boolean autoSave){
        getTownsMap().keySet().forEach(key->{
            File f = new File(plugin.getDataFolder()+"/data/"+key+".yml");
            YamlConfiguration yml = new YamlConfiguration();
            getTownsMap().get(key).forEach(a->{
                String path = a.getIndex().toString();
                yml.set(path+".loc",Util.getLocText(a.getLocation()));
                yml.set(path+".size.x",a.getSize()[0]);
                yml.set(path+".size.y",a.getSize()[1]);
                yml.set(path+".name",a.getTownName());
                yml.set(path+".home",Util.getLocText(a.getHome()));
                yml.set(path+".size.z",a.getSize()[2]);
                yml.set(path+".owner.uuid",a.getOwnerUUID().toString());
                yml.set(path+".owner.name",a.getOwnerName());
                a.getFlags().forEach((k,v)->yml.set(path+".flags."+k,v));
                a.getMembers().forEach((k,v)->v.forEach((m,p)->yml.set(path+".members."+k+"."+m,p)));
            });
            try {
                if(autoSave){
                    yml.save(new File(plugin.getDataFolder()+"/backup/"+System.currentTimeMillis()+".yml"));
                }else{
                    yml.save(f);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void loadData(){
        File f = new File(plugin.getDataFolder()+"/data");
        for(File file : f.listFiles()){
            List<Town> towns = new ArrayList<>();
            YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
            String name = file.getName().substring(0,file.getName().indexOf("."));
            yml.getKeys(false).forEach(key->{
                String[] loc = yml.getString(key+".loc").split("#");
                String[] homeLoc = yml.getString(key+".home").split("#");
                Location location = new Location(Bukkit.getWorld(name),Double.valueOf(loc[0]),Double.valueOf(loc[1]),Double.valueOf(loc[2]));
                Location homeLocation = new Location(Bukkit.getWorld(name),Double.valueOf(homeLoc[0]),Double.valueOf(homeLoc[1]),Double.valueOf(homeLoc[2]));
                int[] size = {yml.getInt(key+".size.x"),yml.getInt(key+".size.y"),yml.getInt(key+".size.z")};
                Town town = new Town(location,size,yml.getString(key+".owner.name"), UUID.fromString(yml.getString(key+".owner.uuid")));
                town.setHome(homeLocation);
                if(yml.isConfigurationSection(key+".flags")){
                    yml.getConfigurationSection(key+".flags").getKeys(false).forEach(a->town.getFlags().put(a,yml.getBoolean(key+".flags."+a)));
                }
                ConfigurationSection membersC = yml.getConfigurationSection(key+".members");
                if(membersC!=null){
                    membersC.getKeys(false).forEach(member->{
                        town.getMembers().put(member,new HashMap<>());
                        ConfigurationSection permList = membersC.getConfigurationSection(member);
                        permList.getKeys(false).forEach(p -> town.setMembersPer(member,p,permList.getBoolean(p)));
                    });
                }
                town.setTownName(yml.getString(key+".name"));
                towns.add(town);
            });
            townsMap.put(name,towns);
        }
    }

    private boolean isPlace(Location loc,String owner,int r){
        for(int x = (r * -1);x<=r;x++){
            for(int y = (r*-1);y<=r;y++){
                for(int z = (r*-1);z<=r;z++){
                    Block b = loc.getWorld().getBlockAt(loc.getBlockX()+x,loc.getBlockY()+y,loc.getBlockZ()+z);
                    Town town = getTown(b.getLocation());
                    if(town==null||!town.getOwnerName().equalsIgnoreCase(owner)){
                        return false;
                    }
                }
            }
        }
        return true;
    }


    public void particleLine(Location locA, Location locB, Player player) {
        org.bukkit.util.Vector vectorAB = locB.clone().subtract(locA).toVector();
        double vectorLength = vectorAB.length();
        vectorAB.normalize();
        double i;
        int speed = plugin.getConfig().getInt("Setting.effect.speed",0);
        int amount = plugin.getConfig().getInt("Setting.effect.amount",150);
        for (i = 0.0D; i < vectorLength; i += 1.5) {
            org.bukkit.util.Vector vector = vectorAB.clone().multiply(i);
            Location point = locA.clone().add(vector);
            if (player == null) {
                effLib.display(0,0,0,speed,amount,point,50);
            } else {
                effLib.display(0,0,0,speed,amount,point,new Player[] { player });
            }
        }
    }

    public void displayRange(Town town){
        org.bukkit.util.Vector vector = new Vector(town.getSize()[0],town.getSize()[1],town.getSize()[2]);
        Location[] loc = SchematicsUtil.getTownVertices(town.getLocation(),vector);
        Player player = Bukkit.getPlayer(town.getOwnerName());
        particleLine(loc[0], loc[1],player);
        particleLine(loc[0], loc[2], player);
        particleLine(loc[3], loc[1], player);
        particleLine(loc[3], loc[2], player);
        particleLine(loc[4], loc[5], player);
        particleLine(loc[4], loc[6], player);
        particleLine(loc[7], loc[5], player);
        particleLine(loc[7], loc[6], player);
        particleLine(loc[0], loc[4], player);
        particleLine(loc[1], loc[5], player);
        particleLine(loc[2], loc[6], player);
        particleLine(loc[3], loc[7], player);
    }

    @EventHandler
    void clickBlock(PlayerInteractEvent interactEvent){
        if(interactEvent.hasBlock()){
            if(interactEvent.getHand().equals(EquipmentSlot.OFF_HAND)){
                return;
            }
            Player p = interactEvent.getPlayer();

            if(p.getItemInHand()!=null){
                ItemStack i = p.getItemInHand();
                Drawing dr = drawingManager.getDrawing(i);
                if(dr!=null){
                    Location location = p.getLocation();
                    if(!isPlace(location,p.getName(),dr.getR())){
                        p.sendMessage(messageManager.getString("message.no-town-place"));
                        return;
                    }
//                    Town town = getTown(location);
//                    if(town==null){
//                        p.sendMessage(messageManager.getString("message.no-town-place"));
//                        return;
//                    }else if(!town.getOwnerName().equalsIgnoreCase(p.getName())){
//                        p.sendMessage(messageManager.getString("message.no-town-owner"));
//                        return;
//                    }
                    p.sendMessage(messageManager.getString("message.wait-place"));
                    drawingManager.addPlayer(p,i);
                    i.setAmount(i.getAmount()-1);
                    Bukkit.getScheduler().runTaskLaterAsynchronously(plugin,()->{
                        if(drawingManager.isExitPlayer(p)){
                            drawingManager.loadSchematic(p,location,dr);
                            p.sendMessage(messageManager.getString("message.use-drawing").replace("@dr@",dr.getName()));
                            drawingManager.delPlayer(p,false);
                        }
                    },plugin.getConfig().getInt("Setting.place-delay",20)*20);
                }
            }
        }
    }
    @EventHandler
    void shiftCancelPlace(PlayerMoveEvent moveEvent){
        Player p = moveEvent.getPlayer();
        if(drawingManager.isExitPlayer(p)){
            if(p.isSneaking()){
                drawingManager.delPlayer(p,true);
                p.sendMessage(messageManager.getString("message.cancel-drawing"));
            }
        }
    }

    @EventHandler
    void quitGame(PlayerQuitEvent quitEvent){
        Player p = quitEvent.getPlayer();
        if(drawingManager.isExitPlayer(p)){
            drawingManager.delPlayer(p,true);
        }
    }



}
