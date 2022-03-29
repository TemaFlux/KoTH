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

    public HashMap<String, Reward> rewards = new HashMap<>();

    public RewardManager(){

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

    public void saveAllRewards(){

        YMLFile ymlFile = Config.getRewardsFile();
        FileConfiguration rewardsConfig = ymlFile.get();

        for(Map.Entry<String, Reward> rewardEntry: rewards.entrySet()){

            rewardsConfig.set(rewardEntry.getKey(), rewardEntry.getValue());

        }

        ymlFile.save();

    }

    public void put(String id, Reward reward){
        rewards.put(id, reward);
    }

    public Reward getReward(String id){
        return rewards.get(id);
    }

    public List<Reward> getAllRewards(){
        return new ArrayList<>(rewards.values());
    }

    public void create(RewardBuilder builder){

        YMLFile rewardsFile = Config.getRewardsFile();
        rewardsFile.loadFile();
        FileConfiguration rewardsConfiguration = rewardsFile.get();

        String id = builder.getId();
        Reward reward = builder.getReward();

        rewardsConfiguration.set(id+".chances", reward.getChances());
        rewardsConfiguration.set(id+".reward", reward.getReward());

        rewardsFile.save();
        rewards.put(id, reward);

    }

    public void delete(String id){

        YMLFile rewardsFile = Config.getRewardsFile();
        rewardsFile.loadFile();
        FileConfiguration rewardsConfiguration = rewardsFile.get();

        rewardsConfiguration.set(id, null);

        rewardsFile.save();
        rewards.remove(id);

    }

}
