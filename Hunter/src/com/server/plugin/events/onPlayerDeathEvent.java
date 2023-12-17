package com.server.plugin.events;

import com.server.plugin.Main;
import com.server.plugin.command.CommandHunter;
import com.server.plugin.util.CC;
import com.server.plugin.util.General;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.Map;

public class onPlayerDeathEvent implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (onPlayerDamage.playerCurrentTime.containsKey(event.getEntity())) {
            long tiempoTranscurrido = System.currentTimeMillis() - onPlayerDamage.playerCurrentTime.get(event.getEntity());
            if (tiempoTranscurrido >= (30 * 1000)) {
                onPlayerDamage.playerCurrentTime.remove(event.getEntity());
                onPlayerDamage.playerDamageByPlayer.remove(event.getEntity());
            }
        }

        if (CommandHunter.damsList.containsKey(event.getEntity().getName())) {
            if (onPlayerDamage.playerDamageByPlayer.get(event.getEntity()) != null) {
                Player cazador = onPlayerDamage.playerDamageByPlayer.get(event.getEntity());
                if(cazador.getName().equalsIgnoreCase(event.getEntity().getName())){
                    for(Map.Entry<Player, Player> entry : onPlayerDamage.playerDamageByPlayer.entrySet()){
                        if(event.getEntity().getName().equalsIgnoreCase(entry.getValue().getName())){
                            cazador = entry.getKey();
                        }
                    }
                }
                int tps = CommandHunter.damsList.get(event.getEntity().getName());
                General.setPlayerTps(cazador, tps);
                Main.instance.getServer().broadcastMessage(CC.translate("&c&l-----------------------------------------"));
                Main.instance.getServer().broadcastMessage(CC.translate("&c¡El jugador " + cazador.getName() + " ha cazado a " + event.getEntity().getName() + "!"));
                Main.instance.getServer().broadcastMessage(CC.translate("&cRecompensa obtenida : &2+ " + tps + " Tps"));
                Main.instance.getServer().broadcastMessage(CC.translate("&c¡Felicitaciones Cazador! GG"));
                Main.instance.getServer().broadcastMessage(CC.translate("&c&l-----------------------------------------"));
                for (Player player1 : Main.instance.getServer().getOnlinePlayers()) {
                    player1.playSound(player1.getLocation(), Sound.ENDERDRAGON_DEATH, 10f, 10f);
                }
                String ruta = (System.getProperty("user.dir") + File.separator +
                        "cazaRecompensa");
                if (CommandHunter.bukkitRunnableHashMap.containsKey(event.getEntity().getName())) {
                    CommandHunter.bukkitRunnableHashMap.get(event.getEntity().getName()).cancel();
                    CommandHunter.bukkitRunnableHashMap.remove(event.getEntity().getName());
                    onPlayerDamage.playerTimeIsExpired.remove(event.getEntity().getName());
                    onPlayerDamage.playerDamageByPlayer.remove(event.getEntity().getName());
                } else if (!Main.threadsOfHunted.isEmpty() && Main.threadsOfHunted.containsKey(event.getEntity().getName())) {
                    Main.threadsOfHunted.get(event.getEntity().getName()).cancel();
                    Main.threadsOfHunted.remove(event.getEntity().getName());
                    onPlayerDamage.playerTimeIsExpired.remove(event.getEntity().getName());
                    onPlayerDamage.playerDamageByPlayer.remove(event.getEntity().getName());
                }
                File file = new File(ruta + File.separator + event.getEntity().getName() + ".txt");
                file.delete();
                CommandHunter.damsList.remove(event.getEntity().getName());
                if(!CommandHunter.playersMenu.isEmpty()){
                    for(ItemStack itemStack : CommandHunter.playersMenu){
                        if(itemStack.getItemMeta().getDisplayName().contains(event.getEntity().getName())){
                            CommandHunter.playersMenu.remove(itemStack);
                        }
                    }
                }
            }
        }
    }
}
