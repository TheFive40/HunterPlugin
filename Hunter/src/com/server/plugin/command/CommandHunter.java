package com.server.plugin.command;

import com.server.plugin.Main;
import com.server.plugin.Model.BountyHunter;
import com.server.plugin.events.onPlayerDamage;
import com.server.plugin.util.CC;
import com.server.plugin.util.General;
import com.server.plugin.util.command.BaseCommand;
import com.server.plugin.util.command.Command;
import com.server.plugin.util.command.CommandArgs;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CommandHunter extends BaseCommand {
    public static HashMap<String, BukkitRunnable> bukkitRunnableHashMap = new HashMap<>();
    public static ArrayList<ItemStack> playersMenu = new ArrayList<>();
    private String message = "---------------------------------------------";

    public static HashMap<String, Integer> damsList = new HashMap();

    @Command(name = "hunter", aliases = {"hunter", "cazar"}, usage = "&cPrueba utilizando : &7/cazar <player> <reward> " +
            "<time in hours>")
    @Override
    public void onCommand(CommandArgs command) throws IOException {
        int messageWidth = message.length();
        int spacesToAdd = (int) ((60 - messageWidth) / 2);
        String centeredMessage01 = new String(new char[spacesToAdd]).replace("\0", " ");
        try {
            int horas = 0;
            int reward = 0;
            try {
                horas = Integer.parseInt(command.getArgs(2));
                reward = Integer.parseInt(command.getArgs(1));
            } catch (NumberFormatException numberFormatException) {
                command.getPlayer().sendMessage(CC.translate("&c¡Por favor asegurate de estar usando un formato adecuado para la cantidad de horas o la recompensa!" +
                        " solo se admiten valores numericos por favor evita usar caracteres extraños o letras"));
                return;
            }
            if (command.getArgs().length >= 1 && (!damsList.containsKey(command.getArgs(0)) ||
                    damsList.isEmpty()) && reward >= 50000 && horas > 0 && horas <= 24) {
                if (Integer.parseInt(command.getArgs(1)) <= General.getPlayerTps(command.getPlayer())
                        && Main.instance.getServer().getPlayer(command.getArgs(0)) != null) {
                    damsList.put(command.getArgs(0), Integer.parseInt(command.getArgs(1)));
                    command.getPlayer().sendMessage(CC.translate("&cRecompensa ofrecida por el jugdor " + command.getArgs(0)));

                    BountyHunter bountyHunter = new BountyHunter(command.getArgs(0),
                            Integer.parseInt(command.getArgs(1)), Integer.parseInt(command.getArgs(2)), false);
                    onPlayerDamage.playerTimeIsExpired.put(bountyHunter.getName(), false);

                    ItemStack itemStack = new ItemStack(Material.SKULL_ITEM);
                    ArrayList<String> arrayList = new ArrayList<>();
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.setDisplayName(CC.translate("&c&l" + command.getArgs(0)));
                    arrayList.add(CC.translate("&aReward: &e" + command.getArgs(1) + " TPS"));
                    itemMeta.setLore(arrayList);
                    itemStack.setItemMeta(itemMeta);
                    playersMenu.add(itemStack);
                    BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                        int count = 0;
                        private BountyHunter bountyHunter2 = bountyHunter;
                        private ItemStack itemStack2 = itemStack;

                        @Override
                        public void run() {
                            if ((bountyHunter2.getTime() * 60 * 60) == count) {
                                count = 0;
                                onPlayerDamage.playerTimeIsExpired.put(bountyHunter2.getName(), true);
                                CommandHunter.damsList.remove(bountyHunter2.getName());
                                playersMenu.remove(itemStack2);
                                this.cancel();
                            }
                            count++;
                        }
                    };
                    bukkitRunnableHashMap.put(command.getArgs(0), bukkitRunnable);
                    bukkitRunnable.runTaskTimer(Main.instance, 20L, 20L);
                    for (Player player : Main.instance.getServer().getOnlinePlayers()) {
                        player.sendMessage(CC.translate("&d---") + CC.translate("&d---------------------------------------------"));
                        player.sendMessage("");
                        player.sendMessage(centeredMessage01 + CC.translate("&d¡El jugador &5" + command.getPlayer().getName() + "&d ha ofrecido una recompensa"));
                        player.sendMessage("");
                        player.sendMessage(centeredMessage01 + CC.translate("&dPor la cabeza del jugador : &5" + command.getArgs(0)));
                        player.sendMessage("");
                        player.sendMessage(centeredMessage01 + CC.translate("&dRecompensa: &5" + command.getArgs(1) + " TPS"));
                        player.sendMessage("");
                        player.sendMessage(CC.translate("&d---") + CC.translate("&d---------------------------------------------"));
                        player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 10f, 5f);
                    }
                    General.getAndRemoveTps(command.getPlayer(), Integer.parseInt(command.getArgs(1)));
                    return;
                } else {
                    command.getPlayer().sendMessage(CC.translate("&c¡Se ha producido un error inesperado, por favor escribe bien el comando!"));
                    return;
                }
            } else if (command.getArgs().length >= 1 && (!damsList.containsKey(command.getArgs(0)) ||
                    damsList.isEmpty()) && !(reward >= 50000) && (reward > 0)) {
                command.getPlayer().sendMessage(CC.translate("&c¡El monto minimo requerido para la cabeza de un jugador son 50.000 TPS!"));
                return;
            } else if (command.getArgs().length >= 1 && (!damsList.containsKey(command.getArgs(0)) ||
                    damsList.isEmpty()) && reward >= 50000 && horas < 0 || horas > 24) {
                command.getPlayer().sendMessage(CC.translate("&c¡No puedes ingresar una hora con un valor negativo! o superior" +
                        " a 24 horas"));
                return;

            } else if (command.getArgs().length >= 1 && damsList.containsKey(command.getArgs(0))) {
                command.getPlayer().sendMessage(CC.translate("&c¡Ya hay una recompensa por la cabeza de este jugador!"));
                return;
            } else if (command.getArgs().length >= 1 && reward < 0) {
                command.getPlayer().sendMessage(CC.translate("&c¡No puedes usar tps negativos!"));
                return;
            }
        } catch (ArrayIndexOutOfBoundsException exception) {
            command.getPlayer().sendMessage(CC.translate(command.getCommand().getUsage()));
            return;
        }
        command.getPlayer().sendMessage(CC.translate(command.getCommand().getUsage()));
    }
}
