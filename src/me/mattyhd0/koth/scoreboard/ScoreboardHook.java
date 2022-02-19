package me.mattyhd0.koth.scoreboard;

import me.mattyhd0.koth.playeable.CurrentKoth;

public abstract class ScoreboardHook {

    public static ScoreboardHook scoreboardHook;

    public abstract String getHookName();

    public abstract void onKothStart(CurrentKoth currentKoth);

    public abstract void onKothEnd(CurrentKoth currentKoth);

    public abstract void update(CurrentKoth currentKoth);

    public void hook(){
        scoreboardHook = this;
    }

    public static ScoreboardHook getHook(){
        return scoreboardHook;
    }

}
