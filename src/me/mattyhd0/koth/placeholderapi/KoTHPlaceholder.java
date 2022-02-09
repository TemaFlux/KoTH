package me.mattyhd0.koth.placeholderapi;

import com.sun.istack.internal.NotNull;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.mattyhd0.koth.playeable.CurrentKoth;
import me.mattyhd0.koth.util.Config;
import me.mattyhd0.koth.util.Util;
import org.bukkit.OfflinePlayer;

public class KoTHPlaceholder extends PlaceholderExpansion {

    @Override
    public String getIdentifier() {
        return "koth";
    }

    @Override
    public String getAuthor() {
        return "MattyHD0";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {



        if(!params.startsWith("current_")) return "";

        CurrentKoth currentKoth = CurrentKoth.getCurrectKoth();
        if(currentKoth == null) return "";

        switch (params){

            case "current_name":
                return currentKoth.getKoth().getDisplayName();
            case "current_world":
                return currentKoth.getKoth().getCenterLocation().getWorld().getName();
            case "current_x":
                return String.valueOf(currentKoth.getKoth().getCenterLocation().getX());
            case "current_y":
                return String.valueOf(currentKoth.getKoth().getCenterLocation().getY());
            case "current_z":
                return String.valueOf(currentKoth.getKoth().getCenterLocation().getZ());
            case "current_time_left":
                return currentKoth.getFormattedTimeLeft();
            case "current_king":
                return currentKoth.getKing() != null ? currentKoth.getKing().getName() : Util.color(Config.getConfig().getString("koth-in-progress.king-null-placeholder"));
            default:
                return "";

        }

    }
}
