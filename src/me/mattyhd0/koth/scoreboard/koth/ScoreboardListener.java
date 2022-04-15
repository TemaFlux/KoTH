package me.mattyhd0.koth.scoreboard.koth;

import com.xism4.sternalboard.Structure;
import me.mattyhd0.koth.KoTHPlugin;
import me.mattyhd0.koth.scoreboard.hook.sternalboard.SternalBoardHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ScoreboardListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){

        Player player = event.getPlayer();
        KoTHPlugin plugin = KoTHPlugin.getInstance();
        if(plugin.getScoreboardHook() instanceof SternalBoardHook && plugin.getKothManager().getCurrectKoth() != null) Structure.getInstance().getScoreboardManager().removeScoreboard(player);

    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){

        Player player = event.getPlayer();
        if(ScoreboardManager.hasScore(player)) ScoreboardManager.removeScore(player);

    }

}
