package me.mattyhd0.koth.scoreboard.hook.sternalboard;

import com.xism4.sternalboard.SternalBoard;
import com.xism4.sternalboard.Structure;
import com.xism4.sternalboard.managers.ScoreboardManager;
import me.mattyhd0.koth.playeable.CurrentKoth;
import me.mattyhd0.koth.scoreboard.hook.ScoreboardHook;
import me.mattyhd0.koth.scoreboard.hook.plugin.KoTHScoreboardHook;
import me.mattyhd0.koth.util.Config;
import me.mattyhd0.koth.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

public class SternalBoardHook extends ScoreboardHook {

    private String title;
    private List<String> lines;
    private boolean papiSupport;

    private KoTHScoreboardHook koTHScoreboardHook;

    private ScoreboardManager scoreboardManager;
    private ConcurrentMap<UUID, SternalBoard> oldBoards;

    public SternalBoardHook(){
        koTHScoreboardHook = new KoTHScoreboardHook();
    }

    @Override
    public String getHookName() {
        return "SternalBoard (Beta)";
    }

    @Override
    public void onKothStart(CurrentKoth currentKoth) {

        title = Util.color(Config.getConfig().getString("scoreboard.title"));
        lines = Util.coloredList(Config.getConfig().getStringList("scoreboard.body"));
        papiSupport = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;

        ScoreboardManager manager = Structure.getInstance().getScoreboardManager();

        for(Player player: Bukkit.getServer().getOnlinePlayers()){
            manager.removeScoreboard(player);
        }
        koTHScoreboardHook.onKothStart(currentKoth);

    }

    @Override
    public void onKothEnd(CurrentKoth currentKoth) {

        for(Player player: Bukkit.getServer().getOnlinePlayers()){
            Structure.getInstance().getScoreboardManager().setScoreboard(player);
        }
        koTHScoreboardHook.onKothEnd(currentKoth);
    }

    @Override
    public void update(CurrentKoth currentKoth) {

        ScoreboardManager manager = Structure.getInstance().getScoreboardManager();
        koTHScoreboardHook.update(currentKoth);

    }

}
