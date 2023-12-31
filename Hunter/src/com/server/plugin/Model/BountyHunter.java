package com.server.plugin.Model;

import java.io.Serializable;
import java.util.HashMap;

public class BountyHunter implements Serializable {
    private String name;
    private Integer reward, time;
    private boolean isExpired;
    private HashMap<String, Integer> damsList = new HashMap();
    private HashMap<String, Boolean> playerTimeIsExpired = new HashMap<>();

    public HashMap<String, Integer> getDamsList() {
        return damsList;
    }

    public HashMap<String, Boolean> getPlayerTimeIsExpired() {
        return playerTimeIsExpired;
    }

    public BountyHunter(HashMap<String, Integer> damsList, HashMap<String, Boolean> playerTimeIsExpired) {
        this.damsList = damsList;
        this.playerTimeIsExpired = playerTimeIsExpired;
    }

    public BountyHunter(String name, Integer reward, Integer time, boolean isExpired) {
        this.name = name;
        this.reward = reward;
        this.time = time;
        this.isExpired = isExpired;
    }

    public String getName() {
        return name;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public Integer getReward() {
        return reward;
    }

    public Integer getTime() {
        return time;
    }
}
