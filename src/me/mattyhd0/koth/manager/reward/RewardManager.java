package me.mattyhd0.koth.manager.reward;

import me.mattyhd0.koth.util.Config;
import me.mattyhd0.koth.reward.types.CommandReward;
import me.mattyhd0.koth.reward.types.ItemReward;
import me.mattyhd0.koth.reward.api.Reward;
import me.mattyhd0.koth.builders.RewardBuilder;
import me.mattyhd0.koth.util.YMLFile;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RewardManager {

    public static HashMap<String, Reward> rewards = new HashMap<>();

    public static void loadAllRewards(){

        YMLFile ymlFile = Config.getRewardsFile();
        ymlFile.loadFile();
        FileConfiguration rewardsConfig = ymlFile.get();

        rewards.clear();

        for(String key: rewardsConfig.getKeys(false)){

            double chance = rewardsConfig.getDouble(key+".chances");
            Object reward = rewardsConfig.get(key+".reward");

            if(reward instanceof ItemStack){

                rewards.put(key, new ItemReward(key, chance, (ItemStack) reward));

            } else if (reward instanceof String){

                rewards.put(key, new CommandReward(key, chance, (String) reward));

            }

        }

    }

    public static void saveAllRewards(){

        YMLFile ymlFile = Config.getRewardsFile();
        FileConfiguration rewardsConfig = ymlFile.get();

        for(Map.Entry<String, Reward> rewardEntry: rewards.entrySet()){

            rewardsConfig.set(rewardEntry.getKey(), rewardEntry.getValue());

        }

        ymlFile.save();

    }

    public static void put(String id, Reward reward){
        rewards.put(id, reward);
    }

    public static Reward getReward(String id){
        return rewards.get(id);
    }

    public static List<Reward> getAllRewards(){
        return new ArrayList<>(rewards.values());
    }

    public static void create(RewardBuilder builder){

        YMLFile rewardsFile = Config.getRewardsFile();
        rewardsFile.loadFile();
        FileConfiguration rewards = rewardsFile.get();

        String id = builder.getId();
        Reward reward = builder.getReward();

        rewards.set(id+".chances", reward.getChances());
        rewards.set(id+".reward", reward.getReward());

        rewardsFile.save();

        loadAllRewards();

    }

    public static void delete(String id){

        YMLFile rewardsFile = Config.getRewardsFile();
        rewardsFile.loadFile();
        FileConfiguration rewards = rewardsFile.get();

        rewards.set(id, null);

        rewardsFile.save();
        loadAllRewards();

    }

}
