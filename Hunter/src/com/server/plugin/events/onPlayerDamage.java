package com.server.plugin.events;
import com.server.plugin.command.CommandHunter;
import com.server.plugin.util.command.Command;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;

public class onPlayerDamage implements Listener {
    public static volatile HashMap<Player, Player> playerDamageByPlayer = new HashMap<>();
    public static volatile HashMap<Player, Long> playerCurrentTime = new HashMap<>();
    public static volatile HashMap<String, Boolean> playerTimeIsExpired = new HashMap<>();
    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event){

        if(event.getEntity() instanceof Player && CommandHunter.damsList.containsKey(((Player) event.getEntity()).getName()) &&
        event.getDamager() instanceof Player && (playerTimeIsExpired.isEmpty() || playerTimeIsExpired.get(((Player) event.getEntity()).getName()) != null)){
            if(!playerTimeIsExpired.get(((Player) event.getEntity()).getName()))
            playerDamageByPlayer.put((Player) event.getEntity(), (Player) event.getDamager());
            playerCurrentTime.put((Player) event.getEntity(),System.currentTimeMillis());
        }
    }
}
