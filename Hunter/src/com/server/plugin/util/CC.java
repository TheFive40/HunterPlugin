package com.server.plugin.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import java.util.List;
import java.util.stream.Collectors;

public class CC {
	public static String translate(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> translate(List<String> list) {
        return list.stream().map(CC::translate).collect(Collectors.toList());
    }

    
	public static void sendMessage(String message) {
        Bukkit.getOnlinePlayers().forEach(players -> {players.sendMessage(translate(message));});}

    public static void sendMessage(List<String> listMessage) {
        listMessage.forEach(CC::sendMessage);
    }
        /*for (String message : listMessage) {
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                player.sendMessage(translate(message));
            }
        }
    }*/

    public static void playSound(Sound sound) {
        Bukkit.getOnlinePlayers().forEach(players -> {players.playSound(players.getLocation(), sound, 1, 1);});
    }
}