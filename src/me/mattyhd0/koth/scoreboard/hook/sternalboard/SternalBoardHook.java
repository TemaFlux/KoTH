package me.mattyhd0.koth.scoreboard.hook.sternalboard;

import com.xism4.sternalboard.Structure;
import com.xism4.sternalboard.api.scoreboard.SternalBoard;
import com.xism4.sternalboard.managers.ScoreboardManager;
import me.clip.placeholderapi.PlaceholderAPI;
import me.mattyhd0.koth.playeable.CurrentKoth;
import me.mattyhd0.koth.scoreboard.hook.ScoreboardHook;
import me.mattyhd0.koth.util.Config;
import me.mattyhd0.koth.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class SternalBoardHook extends ScoreboardHook {

    private String title;
    private List<String> lines;
    private boolean papiSupport;
    private ScoreboardManager scoreboardManager;

    @Override
    public String getHookName() {
        return "SternalBoard (Beta)";
    }

    @Override
    public void onKothStart(CurrentKoth currentKoth) {

        title = Util.color(Config.getConfig().getString("scoreboard.title"));
        lines = Util.coloredList(Config.getConfig().getStringList("scoreboard.body"));
        papiSupport = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;

        scoreboardManager = Structure.getInstance().getScoreboardManager();
        Structure.getInstance().scoreboardManager = new ScoreboardManager(null);
        ScoreboardManager manager = Structure.getInstance().getScoreboardManager();

        for(Player player: Bukkit.getServer().getOnlinePlayers()){
            SternalBoard board = new SternalBoard(player);
            board.updateTitle( papiSupport ? PlaceholderAPI.setPlaceholders(player, title) : title );
            board.updateLines( papiSupport ? PlaceholderAPI.setPlaceholders(player, lines) : lines );
            manager.getBoards().put(player.getUniqueId(), board);
        }

    }

    @Override
    public void onKothEnd(CurrentKoth currentKoth) {

        Structure.getInstance().scoreboardManager = scoreboardManager;
        ScoreboardManager sbManager = Structure.getInstance().getScoreboardManager();
        for (Player player: Bukkit.getServer().getOnlinePlayers()){
            sbManager.getBoards().put(player.getUniqueId(), new SternalBoard(player));
        }

    }

    @Override
    public void update(CurrentKoth currentKoth) {

        ScoreboardManager manager = Structure.getInstance().getScoreboardManager();

        for(Player player: Bukkit.getServer().getOnlinePlayers()){
            SternalBoard board = manager.getBoards().get(player.getUniqueId());
            board.updateTitle( papiSupport ? PlaceholderAPI.setPlaceholders(player, title) : title );
            board.updateLines( papiSupport ? PlaceholderAPI.setPlaceholders(player, lines) : lines );
        }

    }

}
