package me.mattyhd0.koth.creator;

import me.mattyhd0.koth.KoTHPlugin;
import me.mattyhd0.koth.manager.koth.KothManager;
import me.mattyhd0.koth.playeable.CurrentKoth;
import me.mattyhd0.koth.util.Config;
import me.mattyhd0.koth.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Koth {

    private final String displayName;
    private final String id;
    private final Location pos1;
    private final Location pos2;
    private final Location centerLocation;

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

    public void start() {

        KoTHPlugin plugin = KoTHPlugin.getInstance();
        KothManager kothManager = plugin.getKothManager();

        kothManager.setCurrectKoth(this);
        CurrentKoth currentKoth = kothManager.getCurrectKoth();
        currentKoth.getTask().runTaskTimerAsynchronously(KoTHPlugin.getInstance(), 0L, 5L);
        plugin.getScoreboardHook().update(currentKoth);

        Bukkit.broadcastMessage(
                Config.getMessage("koth-started")
                        .replaceAll("\\{name}", this.getDisplayName())
                        .replaceAll("\\{world}", centerLocation.getWorld().getName())
                        .replaceAll("\\{x}", (int) centerLocation.getX() + "")
                        .replaceAll("\\{y}", (int) centerLocation.getY() + "")
                        .replaceAll("\\{z}", (int) centerLocation.getZ() + "")
                        .replaceAll("\\{time_left}", currentKoth.getFormattedTimeLeft())
        );

    }
}
