package me.mattyhd0.koth.reward.types;

import me.mattyhd0.koth.reward.api.Reward;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ItemReward implements Reward {

    private final String id;
    private final double chances;
    private final ItemStack itemStack;

    public ItemReward(String id, double chances, ItemStack itemStack){

        this.id = id;
        this.chances = chances;
        this.itemStack = itemStack;

    }

    @Override
    public void give(Player player) {

        double r = Math.random()*100;

        if(chances >= r){

            player.getInventory().addItem(itemStack);

        }

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
        return itemStack;
    }

    /*
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();

        map.put("chances", chances);
        map.put("itemStack", itemStack);

        return map;
    }
     */
}
