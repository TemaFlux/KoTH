package me.mattyhd0.koth.scoreboard.hook;

import me.clip.placeholderapi.PlaceholderAPI;
import me.mattyhd0.koth.KoTHPlugin;
import me.mattyhd0.koth.creator.Koth;
import me.mattyhd0.koth.playeable.CurrentKoth;
import me.mattyhd0.koth.scoreboard.ScoreboardHook;
import me.mattyhd0.koth.scoreboard.koth.ScoreboardManager;
import me.mattyhd0.koth.util.Config;
import me.mattyhd0.koth.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class KoTHScoreboardHook extends ScoreboardHook {

    boolean papiSupport = KoTHPlugin.hasSupport("PlaceholderAPI");

    @Override
    public String getHookName() {
        return "KoTHScoreboard";
    }

    @Override
    public void onKothStart(CurrentKoth currentKoth) {

    }

    @Override
    public void onKothEnd(CurrentKoth currentKoth) {

        ScoreboardManager.disableAllScoreboards();

    }

    @Override
    public void update(CurrentKoth currentKoth) {

        if (CurrentKoth.getCurrectKoth() != null) {

            FileConfiguration config = Config.getConfig();

            if (config.getBoolean("scoreboard.enable")) {

                for (Player player : Bukkit.getOnlinePlayers()) {

                    Koth koth = CurrentKoth.getCurrectKoth().getKoth();
                    Location kothLoc = koth.getCenterLocation();

                    ScoreboardManager manager = ScoreboardManager.getByPlayer(player);
                    if (manager == null) {
                        manager = ScoreboardManager.createScore(player);
                    }

                    String title = config.getString("scoreboard.title");
                    if (papiSupport) title = PlaceholderAPI.setPlaceholders(player, title);
                    manager.setTitle(title);
                    int index = 14;

                    for (String line : config.getStringList("scoreboard.body")) {

                        line = line.replaceAll("\\{world}", kothLoc.getWorld().getName())
                                .replaceAll("\\{x}", (int) kothLoc.getX() + "")
                                .replaceAll("\\{y}", (int) kothLoc.getY() + "")
                                .replaceAll("\\{z}", (int) kothLoc.getZ() + "")
                                .replaceAll("\\{koth_name}", koth.getDisplayName())
                                .replaceAll("\\{time_left}", CurrentKoth.getCurrectKoth().getFormattedTimeLeft())
                        ;

                        Player king = CurrentKoth.getCurrectKoth().getKing();
                        if (king != null) {
                            line = line.replaceAll("\\{king}", king.getName());
                        } else {
                            line = line.replaceAll("\\{king}", Util.color(Config.getConfig().getString("koth-in-progress.king-null-placeholder")));
                        }

                        if (papiSupport) line = PlaceholderAPI.setPlaceholders(player, line);

                        manager.setSlot(index, line);
                        index--;

                    }

                }

            }

        }

    }

}
