package me.mattyhd0.koth.playeable;

import me.mattyhd0.koth.KoTHPlugin;
import me.mattyhd0.koth.manager.reward.RewardManager;
import me.mattyhd0.koth.misc.WinEffect;
import me.mattyhd0.koth.reward.api.Reward;
import me.mattyhd0.koth.schedule.MillisUtil;
import me.mattyhd0.koth.scoreboard.hook.ScoreboardHook;
import me.mattyhd0.koth.util.Config;
import me.mattyhd0.koth.creator.Koth;
import me.mattyhd0.koth.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class CurrentKoth extends Koth{

    private final int defaultBroadcastInterval;
    private int lastBroadcast;
    private Player king;
    private List<Player> playersInZone;

    private final long duration;

    private long startMillis;
    private long endMillis;

    private final Task task;


    public CurrentKoth(Koth koth, long timeMillis){

        super(koth.getId(), koth.getDisplayName(), koth.getPos1(), koth.getPos2());
        this.startMillis = System.currentTimeMillis();
        lastBroadcast = -1;
        this.defaultBroadcastInterval = Config.getConfig().getInt("koth-in-progress.broadcast-every");
        this.startMillis = System.currentTimeMillis();
        this.endMillis = System.currentTimeMillis() + timeMillis + 999;
        this.duration = timeMillis;
        KoTHPlugin.getInstance().getScoreboardHook().onKothStart(this);

        task = new Task();

    }

    public CurrentKoth(Koth koth){
        this(koth, Config.getConfig().getInt("koth-duration")*MillisUtil.SECOND);
    }

    public void stop(){
        KoTHPlugin plugin = KoTHPlugin.getInstance();
        CurrentKoth currentKoth = plugin.getKothManager().getCurrectKoth();

        plugin.getScoreboardHook().onKothEnd(currentKoth);
        plugin.getKothManager().setCurrectKoth(null);
    }

    public long getTimeLeftMillis(){
        return endMillis-System.currentTimeMillis();
    }

    public int getBroadcastInterval() {
        return defaultBroadcastInterval;
    }

    public long getStartMillis(){
        return startMillis;
    }

    public List<Player> getPlayersInKoth(){
        return playersInZone;
    }

    public Player getKing(){
        return king;
    }

    public Task getTask() {
        return task;
    }

    public void update(){

        List<Player> players = new ArrayList<>();

        for(Player player: Bukkit.getOnlinePlayers()){

            if(Util.locationIsInZone(player.getLocation(), this.getPos1(), this.getPos2())){

                players.add(player);

            }

        }

        if(king == null || !players.contains(king)){
            king = null;
            if(players.size() > 0) king = players.get(0);
            playersInZone = players;
            if(Config.getConfig().getBoolean("reset-time-on-king-change")) {
                endMillis = System.currentTimeMillis() + duration;
            }
            return;
        }

        playersInZone = players;

    }

    public String getFormattedTimeLeft(){

        long difference = endMillis-System.currentTimeMillis();

        long seconds = difference / 1000 % 60;
        long minutes = difference / (60 * 1000) % 60;
        long hours = difference / (60 * 60 * 1000) % 24;
        long days = difference / (24 * 60 * 60 * 1000);

        return Config.getMessage("time-left-format")
                .replaceAll("\\{minutes}", minutes+"")
                .replaceAll("\\{seconds}", seconds+"");

    }

    public static class Task extends BukkitRunnable {

        private final ScoreboardHook scoreboardHook;
        private final RewardManager rewardManager;
        private CurrentKoth currentKoth;

        public Task(){
            scoreboardHook = KoTHPlugin.getInstance().getScoreboardHook();
            rewardManager = KoTHPlugin.getInstance().getRewardManager();
            currentKoth = KoTHPlugin.getInstance().getKothManager().getCurrectKoth();
        }

        @Override
        public void run() {

            currentKoth = KoTHPlugin.getInstance().getKothManager().getCurrectKoth();

            if(currentKoth == null) {
                cancel();
                return;
            }

            scoreboardHook.update(currentKoth);

            currentKoth.update();

            long calc = ((System.currentTimeMillis()-currentKoth.getStartMillis()) / MillisUtil.SECOND);
            if (calc%currentKoth.getBroadcastInterval() == 0 && calc != currentKoth.lastBroadcast) {
                broadcast();
                currentKoth.lastBroadcast = (int) calc;
            }

            if (currentKoth.getTimeLeftMillis() <= 0) {
                end();
            }

        }

        private void end(){

            Player winner = currentKoth.getKing();

            String message = winner == null ? Config.getMessage("koth-finised.without-winner") : Config.getMessage("koth-finised.with-winner");

            message = message.replaceAll("\\{name}", currentKoth.getDisplayName());

            if (winner != null) {
                message = message.replaceAll("\\{player}", winner.getName());
                if (Config.getConfig().getBoolean("koth-finish.winner-fireworks")) WinEffect.apply(winner);

                for (Reward reward : rewardManager.getAllRewards()) {
                    reward.give(winner);
                }
            }

            Bukkit.broadcastMessage(message);
            currentKoth.stop();

        }

        private void broadcast(){

            String message = currentKoth.getKing() == null ? Config.getMessage("koth-in-progress.without-king") : Config.getMessage("koth-in-progress.with-king");

            Location kothLocation = currentKoth.getCenterLocation();

            message = message.replaceAll("\\{name}", currentKoth.getDisplayName())
                    .replaceAll("\\{world}", kothLocation.getWorld().getName())
                    .replaceAll("\\{x}", (int) kothLocation.getX() + "")
                    .replaceAll("\\{y}", (int) kothLocation.getY() + "")
                    .replaceAll("\\{z}", (int) kothLocation.getZ() + "")
                    .replaceAll("\\{time_left}", currentKoth.getFormattedTimeLeft());

            if (currentKoth.getKing() != null) message = message.replaceAll("\\{player}", currentKoth.getKing().getName());

            Bukkit.broadcastMessage(message);

        }

    }

}
