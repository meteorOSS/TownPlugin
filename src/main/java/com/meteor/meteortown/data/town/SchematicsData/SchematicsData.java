package com.meteor.meteortown.data.town.SchematicsData;

import com.meteor.meteortown.data.town.Dir;
import com.sk89q.worldedit.CuboidClipboard;
import org.bukkit.Location;


public class SchematicsData {
    Dir dir;
    Location location;
    CuboidClipboard cuboidClipboard;
    public Dir getDir() {
        return dir;
    }

    public void setDir(Dir dir) {
        this.dir = dir;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public SchematicsData(Dir dir, Location location,CuboidClipboard cuboidClipboard) {
        this.dir = dir;
        this.location = location;
        this.cuboidClipboard = cuboidClipboard;
    }

    public CuboidClipboard getCuboidClipboard() {
        return cuboidClipboard;
    }

    public void setCuboidClipboard(CuboidClipboard cuboidClipboard) {
        this.cuboidClipboard = cuboidClipboard;
    }
}
