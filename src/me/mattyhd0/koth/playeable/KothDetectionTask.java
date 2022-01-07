package me.mattyhd0.koth.playeable;

import me.mattyhd0.koth.util.Config;
import me.mattyhd0.koth.reward.api.Reward;
import me.mattyhd0.koth.manager.reward.RewardManager;
import me.mattyhd0.koth.misc.WinEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class KothDetectionTask extends BukkitRunnable{


    @Override
    public void run() {

        CurrentKoth currectKoth = CurrentKoth.getCurrectKoth();

        if(currectKoth != null){

            if(currectKoth.getTimeLeft() > 1){

                CurrentKoth.getCurrectKoth().tick();

                int broadcastEvery = Config.getConfig().getInt("koth-in-progress.broadcast-every");

                if(currectKoth.getTimeLeft() % broadcastEvery == 0){

                    String message;

                    if(currectKoth.getKing() != null){
                        message = Config.getMessage("koth-in-progress.with-king");
                    } else {
                        message = Config.getMessage("koth-in-progress.without-king");
                    }

                    Location kothLocation = currectKoth.getKoth().getCenterLocation();

                    message = message.replaceAll("\\{name}", currectKoth.getKoth().getDisplayName())
                            .replaceAll("\\{world}", kothLocation.getWorld().getName())
                            .replaceAll("\\{x}", (int)kothLocation.getX()+"")
                            .replaceAll("\\{y}", (int)kothLocation.getY()+"")
                            .replaceAll("\\{z}", (int)kothLocation.getZ()+"")
                            .replaceAll("\\{time_left}", currectKoth.getFormattedTimeLeft());

                    if(currectKoth.getKing() != null) message = message.replaceAll("\\{player}", currectKoth.getKing().getName());

                    Bukkit.broadcastMessage(message);
                }

            } else {

                Player winner = currectKoth.getKing();

                String message;

                if(winner != null){
                    message = Config.getMessage("koth-finised.with-winner");
                } else {
                    message = Config.getMessage("koth-finised.without-winner");
                }

                message = message.replaceAll("\\{name}", currectKoth.getKoth().getDisplayName());

                if(winner != null) {
                    message = message.replaceAll("\\{player}", winner.getName());
                    if(Config.getConfig().getBoolean("koth-finish.winner-fireworks")) WinEffect.apply(winner);

                    for(Reward reward: RewardManager.getAllRewards()){
                        reward.give(winner);
                    }
                }

                Bukkit.broadcastMessage(message);
                CurrentKoth.stopCurrentKoth();

            }

        }

    }
}
