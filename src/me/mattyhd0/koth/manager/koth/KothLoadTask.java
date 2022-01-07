package me.mattyhd0.koth.manager.koth;

import org.bukkit.scheduler.BukkitRunnable;

public class KothLoadTask extends BukkitRunnable {

    boolean sayToConsole;

    public KothLoadTask(boolean sayToConsole){
        sayToConsole = sayToConsole;
    }

    @Override
    public void run() {
        KothManager.loadAllKoths(sayToConsole);
    }

}
