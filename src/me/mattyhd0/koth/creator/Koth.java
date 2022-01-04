package me.mattyhd0.koth.creator;

import me.mattyhd0.koth.util.Util;
import org.bukkit.Location;

public class Koth {

    private String displayName;
    private String id;
    private Location pos1;
    private Location pos2;
    private Location centerLocation;

    public Koth(String id, String displayName, Location pos1, Location pos2){

        this.id = id;
        this.displayName = displayName;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.centerLocation = Util.getCenterFrom(pos1, pos2);

    }

    public String getId() {
        return id;
    }

    public String getDisplayName(){
        return Util.color(displayName);
    }

    public Location getPos1() {
        return pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public Location getCenterLocation() {
        return centerLocation;
    }
}
