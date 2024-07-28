package me.mattyhd0.koth.scoreboard.koth;

import com.xism4.sternalboard.SternalBoardPlugin;
import me.mattyhd0.koth.KoTHPlugin;
import me.mattyhd0.koth.api.KOTHCache;
import me.mattyhd0.koth.scoreboard.hook.sternalboard.SternalBoardHook;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class ScoreboardListener
implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();
        KoTHPlugin plugin = KoTHPlugin.getInstance();

        if (plugin.getScoreboardHook() instanceof SternalBoardHook && plugin.getKothManager().getCurrectKoth() != null) {
            Plugin boardPlugin = Bukkit.getPluginManager().getPlugin("SternalBoard");

            if (boardPlugin instanceof SternalBoardPlugin) {
                ((SternalBoardPlugin) boardPlugin).getScoreboardManager().removeScoreboard(player);
            }
        }

        if (KOTHCache.points.get(uniqueId) == null) {
            if (KoTHPlugin.getInstance().getKothApi().doesUserExist(uniqueId)) {
                int amount = KoTHPlugin.getInstance().getKothApi().getPoints(uniqueId);
                KOTHCache.points.put(uniqueId, amount);
            } else {
                KOTHCache.points.put(uniqueId, 0);
            }
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if (ScoreboardManager.hasScore(player)) ScoreboardManager.removeScore(player);
    }
}
