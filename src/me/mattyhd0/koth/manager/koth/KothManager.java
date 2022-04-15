package me.mattyhd0.koth.manager.koth;

import me.mattyhd0.koth.KoTHPlugin;
import me.mattyhd0.koth.playeable.CurrentKoth;
import me.mattyhd0.koth.util.Config;
import me.mattyhd0.koth.builders.KothBuilder;
import me.mattyhd0.koth.util.Util;
import me.mattyhd0.koth.util.YMLFile;
import me.mattyhd0.koth.creator.Koth;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KothManager implements Listener {

    private final Map<String, Koth> koths = new HashMap<>();
    private CurrentKoth currentKoth = null;

    public KothManager(){
        this(false);
    }

    public KothManager(boolean sayToConsole){

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
                        Util.color("&8[&cKoTH&8] &cThe koth "+key+" could not be loaded since the world "+kothsFile.get().getString(key+".pos1.world")+" is not loaded.")
                );
            }

        }

        if(!canLoadAll) Bukkit.getConsoleSender().sendMessage(
                Util.color("&8[&cKoTH&8] &cCould not load all the koths because one or more worlds were not loaded, These will be loaded when the worlds load.")
        );

    }

    public List<Koth> getKoths(){

        return new ArrayList<>(koths.values());

    }

    public Koth getKothByID(String id){
        return koths.get(id);
    }

    private void loadFromWorld(String worldName) {

        YMLFile kothsFile = Config.getKothsFile();
        FileConfiguration configuration = kothsFile.get();

        for (String key : configuration.getKeys(false)) {

            String name = configuration.getString(key + ".name");

            Location pos1 = Util.getLocationFromConfig(kothsFile, key + ".pos1");
            Location pos2 = Util.getLocationFromConfig(kothsFile, key + ".pos2");

            if (pos1 != null && pos1.getWorld().getName().equals(worldName) && pos2 != null) {
                KoTHPlugin.getInstance().getKothManager().koths.put(key, new Koth(key, name, pos1, pos2));
            }

        }

    }

    private void  unloadFromWorld(String worldName){

        for (Map.Entry<String, Koth> entry: koths.entrySet()){

            String id = entry.getKey();
            Koth koth = entry.getValue();

            if(koth.getCenterLocation().getWorld().getName().equals(worldName)) KoTHPlugin.getInstance().getKothManager().koths.remove(id);

        }

    }

    public void create(KothBuilder builder){

        YMLFile kothsFile = Config.getKothsFile();
        kothsFile.loadFile();
        FileConfiguration kothsConfiguration = kothsFile.get();

        String id = builder.getId();
        String name = builder.getName();
        Location pos1 = builder.getPos1();
        Location pos2 = builder.getPos2();

        kothsConfiguration.set(id+".name", name);

        kothsFile.save();

        Util.saveLocationToConfig(kothsFile, id+".pos1", pos1);
        Util.saveLocationToConfig(kothsFile, id+".pos2", pos2);

        koths.put(id, new Koth(id, name, pos1, pos2));

    }

    public void delete(String id){

        YMLFile kothsFile = Config.getKothsFile();
        kothsFile.loadFile();
        FileConfiguration kothsConfiguration = kothsFile.get();

        kothsConfiguration.set(id, null);
        koths.remove(id);

        kothsFile.save();

    }

    public CurrentKoth getCurrectKoth() {
        return currentKoth;
    }

    public void setCurrectKoth(Koth koth) {

       if(koth != null) {
           currentKoth = new CurrentKoth(koth);
           return;
       }

       currentKoth = null;

    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event){
        loadFromWorld(event.getWorld().getName());
    }

    @EventHandler
    public void onWorldUnload(WorldUnloadEvent event){
        unloadFromWorld(event.getWorld().getName());
    }

}
