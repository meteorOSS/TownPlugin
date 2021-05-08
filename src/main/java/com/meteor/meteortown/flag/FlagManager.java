package com.meteor.meteortown.flag;

import com.meteor.meteorlib.MeteorLib;
import com.meteor.meteortown.MeteorTown;
import com.meteor.meteortown.flag.flaglistner.*;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FlagManager {
    private static List<IFlag> flagList = new ArrayList<>();
    public static FlagItem flagItem = null;
    public static void registerFlag(IFlag flag){
        flagList.add(flag);
    }
    public static void initialization(){
        flagItem = new FlagItem(YamlConfiguration.loadConfiguration(new File(MeteorTown.Instance.getDataFolder()+"/flag-info.yml")));
        flagList.add(new BuildFlag());
        flagList.add(new WaterFlow());
        flagList.add(new InteractiveFlag());
        flagList.add(new LavaFlow());
        flagList.add(new PlayerMoveFlag());
        flagList.add(new MobSpawnerFlag());
        flagList.add(new Bucket());
    }
    public static List<IFlag> getFlagList(boolean isAgainstPlayer){
        List<IFlag> iFlagList = flagList.stream().filter(f-> !MeteorTown.Instance.getConfig().getStringList("black-flag").contains(f.getName())).collect(Collectors.toList());
        if(isAgainstPlayer){
            iFlagList.removeIf(a->!a.isAgainstPlayer());
        }
        return iFlagList;
    }

}
