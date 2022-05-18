package me.mattyhd0.koth.schedule;

import me.mattyhd0.koth.KoTHPlugin;
import me.mattyhd0.koth.creator.Koth;

public class KothSchedule {

    private final String kothId;
    private final long start;


    public KothSchedule(String kothId, long millisStart){

        this.kothId = kothId;
        this.start = millisStart;

    }

    public Koth getKoth() {
        return KoTHPlugin.getInstance().getKothManager().getKothByID(kothId);
    }

    public long getStartMillis() {
        return start;
    }

    public String getFormattedTime(){

        long difference = getStartMillis()-System.currentTimeMillis();

        long seconds = difference / 1000 % 60;
        long minutes = difference / (60 * 1000) % 60;
        long hours = difference / (60 * 60 * 1000) % 24;
        long days = difference / (24 * 60 * 60 * 1000);

        StringBuilder builder = new StringBuilder();

        if(days > 0) builder.append(days).append(days == 1 ? " day " : " days ");
        if(hours > 0) builder.append(hours).append(hours == 1 ? " hour " : " hours ");
        if(minutes > 0) builder.append(minutes).append(minutes == 1 ? " minute " : " minutes ");
        if(seconds > 0) builder.append(seconds).append(seconds == 1 ? " second" : " seconds");

        return builder.toString();

    }
}
