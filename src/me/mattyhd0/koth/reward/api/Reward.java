package me.mattyhd0.koth.reward.api;

import org.bukkit.entity.Player;

public interface Reward {
    
    double chances = 100;

    void give(Player player);

    String getId();

    double getChances();

    Object getReward();
    
}
