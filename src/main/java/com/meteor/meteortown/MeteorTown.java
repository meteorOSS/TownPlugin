package com.meteor.meteortown;

import com.meteor.meteortown.commands.TownCommands;
import com.meteor.meteortown.events.InventoryClickEvents;
import com.meteor.meteortown.events.PlayerInteractiveEvents;
import com.meteor.meteortown.flag.FlagManager;
import com.meteor.meteortown.hook.PapiHook;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;

public final class MeteorTown extends JavaPlugin {
    TownManager townManager = null;
    public static MeteorTown Instance;
    public MeteorTown(){
        Instance = this;
    }

    public TownManager getTownManager() {
        return townManager;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        townManager = new TownManager(this);
        townManager.register();
        registerCmdEvents();
        FlagManager.initialization();
        getLogger().info("城镇插件已载入，联系qq2260483272，接中小型插件定制");
        new PapiHook().register();
        autoSaveData(getConfig().getInt("Setting.auto-save-time"));
        // Plugin startup logi
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getTownManager().saveData(false);
    }

    @Override
    public void saveDefaultConfig() {
        Arrays.asList("config.yml","message.yml","flag-info.yml","data","schematics","drawing.yml").forEach(a->{
            File f = new File(getDataFolder()+"/"+a);
            if((a.equalsIgnoreCase("data")||a.equalsIgnoreCase("schematics"))&&!f.exists()){
                f.mkdirs();
            }else if(!f.exists()){
                saveResource(a,false);
            }
        });
    }
    private void registerCmdEvents(){
        getServer().getPluginCommand("mtown").setExecutor(new TownCommands());
        getServer().getPluginManager().registerEvents(new InventoryClickEvents(),this);
        getServer().getPluginManager().registerEvents(new PlayerInteractiveEvents(),this);
//        getServer().getPluginManager().registerEvents(new OthertEvents(),this);
    }

    private void autoSaveData(int timeM){
        Bukkit.getScheduler().runTaskLaterAsynchronously(this,()->{
            getTownManager().saveData(true);
            autoSaveData(timeM);
        },timeM*60*20);
    }
}
