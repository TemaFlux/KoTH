package me.mattyhd0.koth.schedule;

import me.mattyhd0.koth.playeable.CurrentKoth;
import me.mattyhd0.koth.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ScheduleTask extends BukkitRunnable{


    String[] days = {"sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"};
    String lastCheck = "";


    @Override
    public void run() {

        FileConfiguration schedule = Config.getScheduleFile().get();

        String tz = schedule.getString("options.timezone");
        TimeZone timeZone = tz.equals("DEFAULT") ? TimeZone.getDefault() : TimeZone.getTimeZone(tz);

        DateFormat df = new SimpleDateFormat("HH:mm");
        df.setTimeZone(timeZone);

        Calendar calendar = Calendar.getInstance(timeZone);
        Date date = calendar.getTime();

        String day = days[date.getDay()];
        String clock = df.format(date);

        if(!lastCheck.equals(clock)){

            lastCheck = clock;

            if(Bukkit.getServer().getOnlinePlayers().size() >= schedule.getInt("options.minimum-players")){

                for(String scheduleLine: schedule.getStringList("schedule."+day)){

                    String[] data = scheduleLine.split(" ");

                    if(clock.equals(data[0])){ //Koth Start!

                        CurrentKoth.setCurrectKoth(new CurrentKoth(data[1], Config.getConfig().getInt("koth-duration")));
                        break;

                    }

                }

            }

        }

    }
}
