package me.mattyhd0.koth;

import me.mattyhd0.koth.bstats.Metrics;
import me.mattyhd0.koth.commands.KothCommand;
import me.mattyhd0.koth.creator.selection.KothSelectionListener;
import me.mattyhd0.koth.creator.selection.item.KothSelectionWand;
import me.mattyhd0.koth.manager.koth.KothManager;
import me.mattyhd0.koth.manager.reward.RewardManager;
import me.mattyhd0.koth.placeholderapi.KoTHPlaceholder;
import me.mattyhd0.koth.playeable.CurrentKoth;
import me.mattyhd0.koth.playeable.KothDetectionTask;
import me.mattyhd0.koth.schedule.ScheduleTask;
import me.mattyhd0.koth.scoreboard.ScoreboardHook;
import me.mattyhd0.koth.scoreboard.hook.KoTHScoreboardHook;
import me.mattyhd0.koth.scoreboard.hook.scoreboard.r.ScoreboardRHook;
import me.mattyhd0.koth.scoreboard.hook.sternalboard.SternalBoardHook;
import me.mattyhd0.koth.scoreboard.koth.ScoreboardListener;
import me.mattyhd0.koth.update.UpdateChecker;
import me.mattyhd0.koth.util.Config;
import me.mattyhd0.koth.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class KoTHPlugin extends JavaPlugin {

    private static KoTHPlugin instance;
    private ItemStack selectionWandItem;
    private KothManager kothManager;
    private RewardManager rewardManager;
    public static Map<String, Boolean> supportedPlugins = new HashMap<>();

    @Override
    public void onEnable() {

        setInstance(this);
        Metrics metrics = new Metrics(this, 13335);
        setupCommands();
        setupListeners();
        Config.loadConfiguration();
        selectionWandItem = new KothSelectionWand();
        detectSupport("PlaceholderAPI");
        setupScoreboardHook();
        kothManager = new KothManager(true);
        rewardManager = new RewardManager();
        new KothDetectionTask().runTaskTimer(this, 0L, 20L);
        new ScheduleTask().runTaskTimer(this, 0L, 20L);
        updateChecker(this, 97741);

        if(supportedPlugins.get("PlaceholderAPI")){
            new KoTHPlaceholder().register();
        }
    }

    @Override
    public void onDisable() {
        CurrentKoth currentKoth = CurrentKoth.getCurrectKoth();
        if(currentKoth != null) ScoreboardHook.getHook().onKothEnd(currentKoth);
    }

    public void setupListeners(){
        getServer().getPluginManager().registerEvents(new KothSelectionListener(), this);
        getServer().getPluginManager().registerEvents(new ScoreboardListener(), this);
    }

    public void setupCommands(){
        getCommand("koth").setExecutor(new KothCommand());
    }

    public void setupScoreboardHook(){

        ScoreboardHook scoreboardHook = new KoTHScoreboardHook();

        if(getServer().getPluginManager().getPlugin("Scoreboard-revision") != null){
            scoreboardHook = new ScoreboardRHook();
        } else if (getServer().getPluginManager().getPlugin("SternalBoard") != null){
            scoreboardHook = new SternalBoardHook();
        }

        scoreboardHook.hook();
        Bukkit.getConsoleSender().sendMessage(
                Util.color("&8[&cKoTH&8] &7Scoreboard Hook: &c"+scoreboardHook.getHookName())
        );

    }

    private static void setInstance(KoTHPlugin pl){
        instance = pl;
        Bukkit.getConsoleSender().sendMessage(
                Util.color("&8[&cKoTH&8] &7Enabling KoTH &cv"+pl.getDescription().getVersion())
        );
    }

    public static KoTHPlugin getInstance() {
        return instance;
    }

    public static void detectSupport(String plugin){

        boolean hasSupport = (Bukkit.getPluginManager().getPlugin(plugin) != null);
        supportedPlugins.put(plugin, hasSupport);
        String hasSupportStr = hasSupport ? "&aYes" : "&cNo";

        Bukkit.getConsoleSender().sendMessage(
                Util.color("&8[&cKoTH&8] &7"+plugin+" Support: "+hasSupportStr)
        );

    }

    public static boolean hasSupport(String plugin){
        return (supportedPlugins.get(plugin) != null) ? supportedPlugins.get(plugin) : false;
    }

    private void updateChecker(Plugin plugin, int spigotId) {

        String name = plugin.getDescription().getName();
        String color = "&c";

        if(Config.getBoolean("update-checker")) {
            String prefix = "&8["+color+name+"&8]&7";

            UpdateChecker updateChecker = new UpdateChecker(plugin, spigotId);
            ConsoleCommandSender console = Bukkit.getConsoleSender();
            if (updateChecker.requestIsValid()) {
                if (updateChecker.isRunningLatestVersion()) {
                    String message = Util.color(prefix+" &7You are using the latest version of "+name+"!");
                    console.sendMessage(message);
                } else {
                    String message = Util.color(prefix+" &7You are using version " + color + updateChecker.getVersion() + "&7 and the latest version is " + color + updateChecker.getLatestVersion());
                    String message2 = Util.color(prefix+" &7You can download the latest version at: " + color + updateChecker.getSpigotResource().getDownloadUrl());
                    console.sendMessage(message);
                    console.sendMessage(message2);
                }
            } else {
                String message = Util.color(prefix+" &7Could not verify if you are using the latest version of " + color + name + " :(");
                String message2 = Util.color(prefix+" &7You can disable update checker in config.yml file");
                console.sendMessage(message);
                console.sendMessage(message2);
            }
        }
    }

    public ItemStack getSelectionWandItem() {
        return selectionWandItem;
    }

    public KothManager getKothManager() {
        return kothManager;
    }

    public RewardManager getRewardManager() {
        return rewardManager;
    }
}
