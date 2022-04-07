package me.mattyhd0.koth.schedule;

import me.mattyhd0.koth.KoTHPlugin;
import me.mattyhd0.koth.creator.Koth;
import me.mattyhd0.koth.manager.koth.KothManager;
import me.mattyhd0.koth.util.Config;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.*;
import java.util.*;

public class ScheduleManager {

    private static final String[] days = {"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"};
    private final long initTime;
    private final LinkedList<KothSchedule> incomingKoths;

    public ScheduleManager() {

        initTime = System.currentTimeMillis();

        FileConfiguration scheduleConfig = Config.getScheduleFile().get();

        String timeZone = scheduleConfig.getString("options.timezone");

        incomingKoths = new LinkedList<>();
        KothManager kothManager = KoTHPlugin.getInstance().getKothManager();

        LocalDate localDate = LocalDate.now();
        LocalDateTime localDateTime = LocalDateTime.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth(), 0, 0);
        ZonedDateTime zonedDateTime = localDateTime.atZone(timeZone.equals("DEFAULT") ? ZoneId.systemDefault() : ZoneId.of(timeZone));
        long currentMillis = zonedDateTime.toInstant().toEpochMilli();

        FileConfiguration fileConfiguration = Config.getScheduleFile().get();

        for (int i = 0; i < 7; i++) {

            Date date = new Date(zonedDateTime.toInstant().toEpochMilli()+(MillisUtil.DAY*i));
            ZonedDateTime zdt = date.toInstant().atZone(zonedDateTime.getZone());
            String dayString = days[zdt.getDayOfWeek().getValue()-1];
            List<String> todaySchedules = fileConfiguration.getStringList("schedule."+dayString);

            for(String schedule: todaySchedules){

                String[] data = schedule.split(" ");
                String[] timeStr = data[0].split(":");

                int hour = Integer.parseInt(timeStr[0]);
                int minute = Integer.parseInt(timeStr[1]);

                long startHourMillis = hour*MillisUtil.HOUR;
                long startMinuteMillis = minute*MillisUtil.MINUTE;

                long millis = currentMillis+(MillisUtil.DAY*i);

                Koth koth = kothManager.getKothByID(data[1]);

                if(koth != null) {

                    KothSchedule kothSchedule = new KothSchedule(
                            koth,
                            millis + startHourMillis + startMinuteMillis
                    );


                    if (kothSchedule.getStartMillis() > System.currentTimeMillis()) {
                        incomingKoths.add(kothSchedule);
                    }
                    // Debug
                    /*System.out.println(
                            MessageFormat.format("{0} [{1}] {2}: {3}",
                                    dayString,
                                    kothSchedule.getStartMillis(),
                                    kothSchedule.getKoth().getDisplayName(),
                                    kothSchedule.getFormattedTime()
                            )
                    );*/

                }

            }

        }

    }

    public KothSchedule getNextKothSchedule(){

        return incomingKoths.getFirst();

    }

    public List<KothSchedule> getIncomingKoths() {
        return incomingKoths;
    }

    public static void main(String[] args){

        LocalDate localDate = LocalDate.now();

        LocalDateTime localDateTime = LocalDateTime.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth(), 0, 0);

        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());

        System.out.println(zonedDateTime.toInstant().toEpochMilli());

    }

    public static class Task extends BukkitRunnable {

        private final KoTHPlugin plugin;
        private final KothManager kothManager;
        private final ScheduleManager scheduleManager;

        public Task(){
            plugin = KoTHPlugin.getInstance();
            kothManager = plugin.getKothManager();
            scheduleManager = plugin.getScheduleManager();
        }


        @Override
        public void run() {

            KothSchedule kothSchedule = scheduleManager.getNextKothSchedule();

            if(kothSchedule == null) return;

            if(System.currentTimeMillis() > scheduleManager.initTime+MillisUtil.DAY) KoTHPlugin.getInstance().setScheduleManager(new ScheduleManager());

            if(System.currentTimeMillis() > kothSchedule.getStartMillis() && kothManager.getCurrectKoth() == null){

                kothSchedule.getKoth().start();
                scheduleManager.incomingKoths.removeFirst();

            }


        }

    }

}
