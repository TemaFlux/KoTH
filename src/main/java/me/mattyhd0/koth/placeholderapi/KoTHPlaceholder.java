package me.mattyhd0.koth.placeholderapi;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.mattyhd0.koth.KoTHPlugin;
import me.mattyhd0.koth.playeable.CurrentKoth;
import me.mattyhd0.koth.schedule.ScheduleManager;
import me.mattyhd0.koth.util.Config;
import me.mattyhd0.koth.util.Util;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.PluginDescriptionFile;

@Getter
@ToString
public class KoTHPlaceholder
extends PlaceholderExpansion {
    private final String identifier;
    private final String author;
    private final String version;

    public KoTHPlaceholder(@NonNull KoTHPlugin plugin) {
        PluginDescriptionFile description = plugin.getDescription();

        identifier = description.getName().toLowerCase();
        author = description.getAuthors() == null || description.getAuthors().isEmpty() ? "unknown" : String.join(", ", description.getAuthors());
        version = description.getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        KoTHPlugin plugin = KoTHPlugin.getInstance();
        CurrentKoth currentKoth = plugin.getKothManager().getCurrectKoth();
        ScheduleManager scheduleManager = plugin.getScheduleManager();

        switch (params) {
            case "current_name":
                return currentKoth == null ? "" : currentKoth.getDisplayName();
            case "current_world":
                return currentKoth == null ? "" : currentKoth.getCenterLocation().getWorld().getName();
            case "current_x":
                return currentKoth == null ? "" : String.valueOf(currentKoth.getCenterLocation().getX());
            case "current_y":
                return currentKoth == null ? "" : String.valueOf(currentKoth.getCenterLocation().getY());
            case "current_z":
                return currentKoth == null ? "" : String.valueOf(currentKoth.getCenterLocation().getZ());
            case "current_time_left":
                return currentKoth == null ? "" : currentKoth.getFormattedTimeLeft();
            case "current_king":
                return currentKoth == null ? "" : (currentKoth.getKing() != null ? currentKoth.getKing().getName() : Util.color(Config.getConfig().getString("koth-in-progress.king-null-placeholder")));
            case "schedule_next_name":
                return scheduleManager.getNextKothSchedule() == null ? "" : scheduleManager.getNextKothSchedule().getKoth().getDisplayName();
            case "schedule_next_time":
                return scheduleManager.getNextKothSchedule() == null ? "" : scheduleManager.getNextKothSchedule().getFormattedTimeLeft();
            default:
                return "";
        }
    }
}
