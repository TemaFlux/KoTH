package me.mattyhd0.koth.playeable;

import me.mattyhd0.katylib.scoreboard.ScoreboardManager;
import me.mattyhd0.koth.util.Config;
import me.mattyhd0.koth.creator.Koth;
import me.mattyhd0.koth.manager.koth.KothManager;
import me.mattyhd0.koth.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CurrentKoth {

    private static CurrentKoth currectKoth;


    private String id;
    private int timeLeft;
    private Player king;
    private List<Player> playersInZone;


    public CurrentKoth(String kothId, int timeLeft){

        this.id = kothId;
        this.timeLeft = timeLeft;

    }

    public CurrentKoth(String kothId){

        this.id = kothId;
        this.timeLeft = Config.getConfig().getInt("koth-duration");

    }

    public static CurrentKoth getCurrectKoth() {
        return currectKoth;
    }

    public static void setCurrectKoth(CurrentKoth koth) {

        if(KothManager.getKoth(koth.getId()) != null){

            currectKoth = koth;
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
        ScoreboardManager.disableAllScoreboards();
        currectKoth = null;
    }

    public Koth getKoth(){
        return KothManager.getKoth(currectKoth.getId());
    }

    public int getTimeLeft(){
        return timeLeft;
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
        }

        playersInZone = players;

        timeLeft -= 1;

    }

    public String getFormattedTimeLeft(){

        int minutes = timeLeft/60;
        int seconds = timeLeft%60;

        return Config.getMessage("time-left-format")
                .replaceAll("\\{minutes}", minutes+"")
                .replaceAll("\\{seconds}", seconds+"");

    }
}
