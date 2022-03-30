package me.mattyhd0.koth.playeable;

import me.mattyhd0.koth.KoTHPlugin;
import me.mattyhd0.koth.scoreboard.hook.ScoreboardHook;
import me.mattyhd0.koth.util.Config;
import me.mattyhd0.koth.reward.api.Reward;
import me.mattyhd0.koth.manager.reward.RewardManager;
import me.mattyhd0.koth.misc.WinEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class KothDetectionTask extends BukkitRunnable {

    private ScoreboardHook scoreboardHook;
    private RewardManager rewardManager;

    public KothDetectionTask(){
        scoreboardHook = KoTHPlugin.getInstance().getScoreboardHook();
        rewardManager = KoTHPlugin.getInstance().getRewardManager();
    }

    @Override
    public void run() {

        CurrentKoth currectKoth = CurrentKoth.getCurrectKoth();

        if(currectKoth == null) return;

        currectKoth.broadcastTick();

        if (currectKoth.getTimeLeft() <= 0) { //Koth Finish

            Player winner = currectKoth.getKing();

            String message = winner == null ? Config.getMessage("koth-finised.without-winner") : Config.getMessage("koth-finised.with-winner");

            message = message.replaceAll("\\{name}", currectKoth.getKoth().getDisplayName());

            if (winner != null) {
                message = message.replaceAll("\\{player}", winner.getName());
                if (Config.getConfig().getBoolean("koth-finish.winner-fireworks")) WinEffect.apply(winner);

                for (Reward reward : rewardManager.getAllRewards()) {
                    reward.give(winner);
                }
            }

            Bukkit.broadcastMessage(message);
            CurrentKoth.stopCurrentKoth();

        }

        currectKoth.tick();
        scoreboardHook.update(currectKoth);

        if (currectKoth.getBroadcastTick() <= 0) {
            currectKoth.resetBroadcastInterval();

            String message = currectKoth.getKing() == null ? Config.getMessage("koth-in-progress.without-king") : Config.getMessage("koth-in-progress.with-king");

            Location kothLocation = currectKoth.getKoth().getCenterLocation();

            message = message.replaceAll("\\{name}", currectKoth.getKoth().getDisplayName())
                    .replaceAll("\\{world}", kothLocation.getWorld().getName())
                    .replaceAll("\\{x}", (int) kothLocation.getX() + "")
                    .replaceAll("\\{y}", (int) kothLocation.getY() + "")
                    .replaceAll("\\{z}", (int) kothLocation.getZ() + "")
                    .replaceAll("\\{time_left}", currectKoth.getFormattedTimeLeft());

            if (currectKoth.getKing() != null) message = message.replaceAll("\\{player}", currectKoth.getKing().getName());

            Bukkit.broadcastMessage(message);
        }

    }

}
