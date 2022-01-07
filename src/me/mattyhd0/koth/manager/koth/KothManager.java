package me.mattyhd0.koth.manager.koth;

import me.mattyhd0.koth.KoTHPlugin;
import me.mattyhd0.koth.util.Config;
import me.mattyhd0.koth.builders.KothBuilder;
import me.mattyhd0.koth.util.Util;
import me.mattyhd0.koth.util.YMLFile;
import me.mattyhd0.koth.creator.Koth;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KothManager {

    private static Map<String, Koth> koths = new HashMap<>();

    public static List<Koth> getKoths(){

        return new ArrayList<>(koths.values());

    }

    public static Koth getKoth(String name){
        return koths.get(name);
    }

    public static void loadAllKoths(boolean sayToConsole){

        YMLFile kothsFile = new YMLFile("koths.yml");
        FileConfiguration configuration = kothsFile.get();

        koths.clear();

        boolean canLoadAll = true;

        for(String key: configuration.getKeys(false)){

            String name = configuration.getString(key+".name");

            Location pos1 = Util.getLocationFromConfig(kothsFile, key+".pos1");
            Location pos2 = Util.getLocationFromConfig(kothsFile, key+".pos2");

            if(pos1 != null && pos2 != null){
                koths.put(key, new Koth(key, name, pos1, pos2));
                if(sayToConsole) Bukkit.getConsoleSender().sendMessage(
                        Util.color("&8[&cKoTH&8] &7The koth &c"+key+" &7was loaded correctly.")
                );
            } else {
                canLoadAll = false;
                if(sayToConsole) Bukkit.getConsoleSender().sendMessage(
                        Util.color("&8[&cKoTH&8] &cThe koth "+key+" could not be loaded.")
                );
            }

        }

        if(!canLoadAll && !sayToConsole) {
            Bukkit.getConsoleSender().sendMessage(
                    Util.color("&8[&cKoTH&8] &7In &c30 seconds &7they will try to load all the koths.")
            );
            new KothLoadTask(sayToConsole).runTaskLater(KoTHPlugin.getPlugin(), 20L*30L);
        }

    }

    public static void create(KothBuilder builder){

        YMLFile kothsFile = Config.getKothsFile();
        kothsFile.loadFile();
        FileConfiguration koths = kothsFile.get();

        String id = builder.getId();
        String name = builder.getName();
        Location pos1 = builder.getPos1();
        Location pos2 = builder.getPos2();

        koths.set(id+".name", name);

        kothsFile.save();

        Util.saveLocationToConfig(kothsFile, id+".pos1", pos1);
        Util.saveLocationToConfig(kothsFile, id+".pos2", pos2);

        loadAllKoths(false);

    }

    public static void delete(String id ){

        YMLFile kothsFile = Config.getKothsFile();
        kothsFile.loadFile();
        FileConfiguration koths = kothsFile.get();

        koths.set(id, null);

        kothsFile.save();
        loadAllKoths(false);

    }

}
