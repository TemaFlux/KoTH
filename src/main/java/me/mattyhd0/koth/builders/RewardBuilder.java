package me.mattyhd0.koth.builders;

import me.mattyhd0.koth.reward.api.Reward;

public class RewardBuilder {

    private String id;
    private Reward reward;

    public RewardBuilder(){

    }

    public RewardBuilder setReward(Reward reward) {
        this.reward = reward;
        return this;
    }


    public RewardBuilder setId(String id){

        this.id = id;
        return this;

    }

    public Reward getReward() {
        return reward;
    }

    public String getId() {
        return id;
    }
}
