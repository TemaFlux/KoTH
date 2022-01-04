package me.mattyhd0.koth.manager.koth;

import me.mattyhd0.koth.util.Config;
import me.mattyhd0.koth.builders.KothBuilder;
import me.mattyhd0.koth.util.YMLFile;
import me.mattyhd0.koth.creator.Koth;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

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

    public static void loadAllKoths(){

        YMLFile kothsFile = new YMLFile("koths.yml");
        FileConfiguration configuration = kothsFile.get();

        koths.clear();

        for(String key: configuration.getKeys(false)){

            String name = configuration.getString(key+".name");
            Location pos1 = (Location) configuration.get(key+".pos1");
            Location pos2 = (Location) configuration.get(key+".pos2");

            koths.put(key, new Koth(key, name, pos1, pos2));

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
        koths.set(id+".pos1", pos1);
        koths.set(id+".pos2", pos2);

        kothsFile.save();
        loadAllKoths();

    }

    public static void delete(String id ){

        YMLFile kothsFile = Config.getKothsFile();
        kothsFile.loadFile();
        FileConfiguration koths = kothsFile.get();


        koths.set(id, null);

        kothsFile.save();
        loadAllKoths();

    }

}
