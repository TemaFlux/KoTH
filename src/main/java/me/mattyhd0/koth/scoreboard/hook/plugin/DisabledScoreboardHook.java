package me.mattyhd0.koth.scoreboard.hook.plugin;

import me.mattyhd0.koth.playeable.CurrentKoth;
import me.mattyhd0.koth.scoreboard.hook.ScoreboardHook;

public class DisabledScoreboardHook extends ScoreboardHook {

    @Override
    public String getHookName() {
        return "Scoreboard Disabled";
    }

    @Override
    public void onKothStart(CurrentKoth currentKoth) {

    }

    @Override
    public void onKothEnd(CurrentKoth currentKoth) {


    }

    @Override
    public void update(CurrentKoth currentKoth) {

    }

}
