package me.mattyhd0.koth.util;


import me.mattyhd0.koth.KoTHPlugin;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class YMLFile {

    private String fileName;
    private File file;
    private FileConfiguration fileConfiguration;
    
    public YMLFile(String fileName) {
        this.fileName = fileName;
        this.file = new File(KoTHPlugin.getPlugin().getDataFolder(), this.fileName);
        this.check();
    }
    
    public YMLFile(File file) {
        this.fileName = file.getName();
        this.file = file;
        this.check();
    }
    
    public FileConfiguration get() {
        return this.fileConfiguration;
    }
    
    public void check() {
        if (!this.file.exists()) {
            this.createFile();
        }
        this.loadFile();
    }
    
    public void createFile() {
        this.file.getParentFile().mkdirs();
        KoTHPlugin.getPlugin().saveResource(this.fileName, false);
    }
    
    public void loadFile() {
        this.fileConfiguration = new YamlConfiguration();
        try {
            this.fileConfiguration.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    
    public void save() {
        try {
            this.get().save(this.file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
