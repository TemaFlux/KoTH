package me.mattyhd0.koth.util;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Config {

    private static YMLFile configFile;
    private static YMLFile kothsFile;
    private static YMLFile messagesFile;
    private static YMLFile rewardsFile;
    private static YMLFile scheduleFile;


    public static void loadConfiguration(){

        configFile = new YMLFile("config.yml");
        messagesFile = new YMLFile("messages.yml");
        kothsFile = new YMLFile("koths.yml");
        rewardsFile = new YMLFile("koth-rewards.yml");
        scheduleFile = new YMLFile("schedule.yml");

    }
    
    public static String getMessage(String message) {

        FileConfiguration messages = messagesFile.get();
        String msg = messages.getString(message);

        if (msg != null) {
            msg = msg.replaceAll("\\{prefix}", messages.getString("prefix"));
            return Util.color(msg);
        }

        return Util.color("&c[KoTH] Error: Message " + message + " does not exist in messages.yml");
    }

    public static List<String> getMessageList(String message) {
        FileConfiguration messages = messagesFile.get();
        List<String> msgList = messages.getStringList(message);
        List<String> coloredList = new ArrayList<>();

        if (msgList.size() > 0) {

            for(String line: msgList){

                line = line.replaceAll("\\{prefix}", messages.getString("messages.prefix"));
                coloredList.add(Util.color(line));

            }

            return coloredList;

        } else {

            List<String> error = new ArrayList<>();
            error.add(org.bukkit.ChatColor.translateAlternateColorCodes('&', "&c[KoTH] Error: Message " + message + " does not exist in messages.yml"));
            return error;
        }

    }
    
    public static boolean getBoolean(String path) {
        boolean bol = configFile.get().getBoolean(path);
        try {
            return bol;
        } catch (NullPointerException exception) {
            return false;
        }
    }

    public static FileConfiguration getConfig(){
        return configFile.get();
    }


    public static YMLFile getKothsFile(){
        return kothsFile;
    }

    public static YMLFile getRewardsFile(){
        return rewardsFile;
    }

    public static YMLFile getScheduleFile(){
        return scheduleFile;
    }




}
