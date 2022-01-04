package me.mattyhd0.koth.reward.types;

import me.mattyhd0.koth.reward.api.Reward;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CommandReward implements Reward {

    private String id;
    private String command;
    private double chances;


    public CommandReward(String id, double chances, String command){

        this.id = id;
        this.command = command;
        this.chances = chances;

    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public double getChances() {
        return chances;
    }

    @Override
    public Object getReward() {
        return command;
    }

    @Override
    public void give(Player player) {

        Server server = Bukkit.getServer();
        ConsoleCommandSender console = server.getConsoleSender();

        double r = Math.random() * 100;

        if (chances >= r) {
            server.dispatchCommand(console,
                    command.replaceAll("\\{player}", player.getName())
                            .replaceAll("%player%", player.getName())
            );

        }

    }

    /*
    @Override
    public Map<String, Object> serialize() {

        Map<String, Object> map = new HashMap<>();

        map.put("chances", chances);
        map.put("command", command);

        return map;
    }
     */
}
