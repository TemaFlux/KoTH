package me.mattyhd0.koth;

import lombok.Getter;
import lombok.Setter;
import me.mattyhd0.koth.commands.KothCommand;
import me.mattyhd0.koth.creator.selection.KothSelectionListener;
import me.mattyhd0.koth.creator.selection.item.KothSelectionWand;
import me.mattyhd0.koth.manager.koth.KothManager;
import me.mattyhd0.koth.manager.reward.RewardManager;
import me.mattyhd0.koth.placeholderapi.KoTHPlaceholder;
import me.mattyhd0.koth.playeable.CurrentKoth;
import me.mattyhd0.koth.schedule.ScheduleManager;
import me.mattyhd0.koth.scoreboard.hook.ScoreboardHook;
import me.mattyhd0.koth.scoreboard.hook.plugin.DisabledScoreboardHook;
import me.mattyhd0.koth.scoreboard.hook.plugin.KoTHScoreboardHook;
import me.mattyhd0.koth.scoreboard.hook.scoreboard.r.ScoreboardRHook;
import me.mattyhd0.koth.scoreboard.hook.sternalboard.SternalBoardHook;
import me.mattyhd0.koth.scoreboard.koth.ScoreboardListener;
import me.mattyhd0.koth.util.Config;
import me.mattyhd0.koth.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class KoTHPlugin
extends JavaPlugin {
    @Getter private static KoTHPlugin instance;
    @Getter private ItemStack selectionWandItem;
    @Getter @Setter private KothManager kothManager;
    @Getter @Setter private RewardManager rewardManager;
    @Getter @Setter private ScheduleManager scheduleManager;
    @Getter @Setter private ScoreboardHook scoreboardHook;
    private final Map<String, Boolean> supportedPlugins = new HashMap<>();
    private boolean loadedListeners = false;
    private KoTHPlaceholder placeholder;

    @Override
    public void onLoad() {
        instance = this;
        getLogger().info(Util.color("&8[&cKoTH&8] &7Enabling KoTH &cv" + getDescription().getVersion()));

        setupCommands();
        setupListeners();

        Config.loadConfiguration();
        selectionWandItem = new KothSelectionWand();
    }

    @Override
    public void onEnable() {
        detectSupport("PlaceholderAPI");
        setupScoreboardHook();

        kothManager = new KothManager(true);
        rewardManager = new RewardManager();
        scheduleManager = new ScheduleManager();

        new ScheduleManager.Task().runTaskTimer(this, 0L, 5);
        if (supportedPlugins.get("PlaceholderAPI")) {
            placeholder = new KoTHPlaceholder(this);
            placeholder.register();
        }
    }

    @Override
    public void onDisable() {
        CurrentKoth currentKoth = kothManager.getCurrectKoth();
        if (currentKoth != null) scoreboardHook.onKothEnd(currentKoth);
        if (placeholder != null) placeholder.unregister();
    }

    public void setupListeners() {
        if (loadedListeners) return;

        getServer().getPluginManager().registerEvents(new KothSelectionListener(), this);
        getServer().getPluginManager().registerEvents(new ScoreboardListener(), this);
        getServer().getPluginManager().registerEvents(new KothManager(), this);

        loadedListeners = true;
    }

    public void setupCommands() {
        PluginCommand kothCommand = getCommand("koth");
        if (kothCommand != null) kothCommand.setExecutor(new KothCommand());
    }

    public void setupScoreboardHook() {
        if (!Config.getConfig().getBoolean("scoreboard.enable")) {
            scoreboardHook = new DisabledScoreboardHook();
        } else if(getServer().getPluginManager().getPlugin("Scoreboard-revision") != null) {
            scoreboardHook = new ScoreboardRHook();
        } else if (getServer().getPluginManager().getPlugin("SternalBoard") != null) {
            scoreboardHook = new SternalBoardHook();
        } else {
            scoreboardHook = new KoTHScoreboardHook();
        }

        getLogger().info(Util.color("&8[&cKoTH&8] &7Scoreboard Hook: &c" + scoreboardHook.getHookName()));
    }

    public void detectSupport(String plugin){
        boolean hasSupport = Bukkit.getPluginManager().getPlugin(plugin) != null;

        supportedPlugins.put(plugin, hasSupport);
        getLogger().info(Util.color("&8[&cKoTH&8] &7" + plugin + " Support: " + (hasSupport ? "&aYes" : "&cNo")));
    }

    public boolean isSupport(String plugin) {
        return supportedPlugins.getOrDefault(plugin, false);
    }
}
