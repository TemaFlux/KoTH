package me.mattyhd0.koth.scoreboard.hook.scoreboard.r;

import me.mattyhd0.koth.playeable.CurrentKoth;
import me.mattyhd0.koth.scoreboard.ScoreboardHook;
import me.mattyhd0.koth.scoreboard.hook.KoTHScoreboardHook;
import me.mattyhd0.koth.util.Config;
import rien.bijl.Scoreboard.r.Board.Animations.Row;
import rien.bijl.Scoreboard.r.Board.BoardPlayer;
import rien.bijl.Scoreboard.r.Board.ConfigBoard;
import rien.bijl.Scoreboard.r.Plugin.Session;
import rien.bijl.Scoreboard.r.Plugin.Utility.ScoreboardStrings;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ScoreboardRHook extends ScoreboardHook {

    private KoTHScoreboardHook kothScoreboard = new KoTHScoreboardHook();
    private ConfigBoard kothScoreboardRevision;


    @Override
    public String getHookName() {
        return "Scoreboard-revision";
    }

    @Override
    public void onKothStart(CurrentKoth currentKoth) {

        ConfigBoard board = new ConfigBoard("board");

        try {
            Field titleField = board.getClass().getDeclaredField("title");
            Field rowsField = board.getClass().getDeclaredField("rows");

            titleField.setAccessible(true);
            rowsField.setAccessible(true);

            List<String> lines = new ArrayList<String>(){{
                add(Config.getConfig().getString("scoreboard.title"));
            }};

            titleField.set(board, new Row(ScoreboardStrings.makeColoredStringList(lines), 2));

            ArrayList<Row> rows = new ArrayList<>();

            for (String line: Config.getConfig().getStringList("scoreboard.body")) {
                Row row = new Row(ScoreboardStrings.makeColoredStringList(
                        new ArrayList<String>() {{
                            add(line);
                        }}
                ), 2);
                rows.add(row);
            }

            rowsField.set(board, rows);

        } catch (Exception e){
            e.printStackTrace();
        }

        board.enable();
        board.runTaskTimerAsynchronously(Session.getSession().plugin, 1L, 1L);
        Session.getSession().defaultBoard = board;

        for (BoardPlayer bp: BoardPlayer.allBoardPlayers()) {
            bp.attachConfigBoard(board);
        }

    }

    @Override
    public void onKothEnd(CurrentKoth currentKoth) {

        ConfigBoard board = new ConfigBoard("board");

        board.enable();
        Session.getSession().defaultBoard = board;
        board.runTaskTimerAsynchronously(Session.getSession().plugin, 1L, 1L);

        for (BoardPlayer bp: BoardPlayer.allBoardPlayers()) {
            bp.attachConfigBoard(board);
        }

    }

    @Override
    public void update(CurrentKoth currentKoth) {

    }

}
