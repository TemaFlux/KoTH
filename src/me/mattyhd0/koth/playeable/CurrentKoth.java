package me.mattyhd0.koth.playeable;

import me.mattyhd0.koth.KoTHPlugin;
import me.mattyhd0.koth.scoreboard.ScoreboardHook;
import me.mattyhd0.koth.util.Config;
import me.mattyhd0.koth.creator.Koth;
import me.mattyhd0.koth.manager.koth.KothManager;
import me.mattyhd0.koth.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Score;

import java.util.ArrayList;
import java.util.List;

public class CurrentKoth {

    private static CurrentKoth currectKoth;


    private String id;
    private int defaultDuration;
    private int timeLeft;
    private int defaultBroadcastInterval;
    private int broadcastTick;
    private Player king;
    private List<Player> playersInZone;

    public CurrentKoth(String kothId, int time){

        this.id = kothId;
        this.defaultBroadcastInterval = Config.getConfig().getInt("koth-in-progress.broadcast-every");
        this.broadcastTick = defaultBroadcastInterval;
        this.defaultDuration = time;
        this.timeLeft = defaultDuration;
        ScoreboardHook.getHook().onKothStart(this);

    }

    public CurrentKoth(String kothId){

        this.id = kothId;
        this.defaultBroadcastInterval = Config.getConfig().getInt("koth-in-progress.broadcast-every");
        this.broadcastTick = defaultBroadcastInterval;
        this.defaultDuration = Config.getConfig().getInt("koth-duration");
        this.timeLeft = defaultDuration;
        ScoreboardHook.getHook().onKothStart(this);

    }

    public static CurrentKoth getCurrectKoth() {
        return currectKoth;
    }

    public static void setCurrectKoth(CurrentKoth koth) {

        KothManager kothManager = KoTHPlugin.getInstance().getKothManager();

        if(kothManager.getKothByID(koth.getId()) != null){

            currectKoth = koth;
            ScoreboardHook.getHook().update(koth);
            Location kothLocation = koth.getKoth().getCenterLocation();

            Bukkit.broadcastMessage(
                    Config.getMessage("koth-started")
                            .replaceAll("\\{name}", currectKoth.getKoth().getDisplayName())
                            .replaceAll("\\{world}", kothLocation.getWorld().getName())
                            .replaceAll("\\{x}", (int)kothLocation.getX()+"")
                            .replaceAll("\\{y}", (int)kothLocation.getY()+"")
                            .replaceAll("\\{z}", (int)kothLocation.getZ()+"")
                            .replaceAll("\\{time_left}", currectKoth.getFormattedTimeLeft())
            );
        }

    }

    public static void stopCurrentKoth(){
        ScoreboardHook.getHook().update(currectKoth);
        ScoreboardHook.getHook().onKothEnd(currectKoth);
        currectKoth = null;
    }

    public Koth getKoth(){
        return KoTHPlugin.getInstance().getKothManager().getKothByID(getId());
    }

    public int getDefaultDuration() {
        return defaultDuration;
    }

    public int getTimeLeft(){
        return timeLeft;
    }

    public void broadcastTick(){
        broadcastTick--;
    }

    public int getDefaultBroadcastInterval() {
        return defaultBroadcastInterval;
    }

    public int getBroadcastTick() {
        return broadcastTick;
    }

    public void resetBroadcastInterval() {
        broadcastTick = defaultBroadcastInterval;
    }

    public String getId() {
        return id;
    }

    public List<Player> getPlayersInKoth(){
        return playersInZone;
    }

    public Player getKing(){
        return king;
    }

    public void tick(){

        List<Player> players = new ArrayList<>();
        Koth koth = getKoth();

        for(Player player: Bukkit.getOnlinePlayers()){

            if(Util.locationIsInZone(player.getLocation(), koth.getPos1(), koth.getPos2())){

                players.add(player);

            }

        }

        if(king == null || !players.contains(king)){
            king = null;
            if(players.size() > 0) king = players.get(0);
            playersInZone = players;
            timeLeft = Config.getConfig().getBoolean("reset-time-on-king-change") ? defaultDuration : timeLeft-1;
            return;
        }

        playersInZone = players;

        timeLeft--;

    }

    public String getFormattedTimeLeft(){

        int minutes = timeLeft/60;
        int seconds = timeLeft%60;

        return Config.getMessage("time-left-format")
                .replaceAll("\\{minutes}", minutes+"")
                .replaceAll("\\{seconds}", seconds+"");

    }
}
