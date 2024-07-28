package me.mattyhd0.koth.scoreboard.hook;

import me.mattyhd0.koth.playeable.CurrentKoth;

public abstract class ScoreboardHook {

    public abstract String getHookName();

    public abstract void onKothStart(CurrentKoth currentKoth);

    public abstract void onKothEnd(CurrentKoth currentKoth);

    public abstract void update(CurrentKoth currentKoth);

}
