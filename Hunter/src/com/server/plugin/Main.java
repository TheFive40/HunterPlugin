package com.server.plugin;

import com.server.plugin.Model.BountyHunter;
import com.server.plugin.Model.Gift;
import com.server.plugin.Model.Localizaciones;
import com.server.plugin.command.CommandAddGift;
import com.server.plugin.command.CommandHunter;
import com.server.plugin.events.InteractPlayerEvent;
import com.server.plugin.events.interactWithGift;
import com.server.plugin.events.onPlayerDamage;
import com.server.plugin.util.CC;
import com.server.plugin.util.ClassesRegistration;
import com.server.plugin.util.General;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import com.server.plugin.util.command.CommandFramework;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import static com.server.plugin.command.CommandAddGift.itemStacks;

public class Main extends JavaPlugin {
    public static Main instance;
    public static HashMap<String, BukkitRunnable> threadsOfHunted = new HashMap<>();
    private final CommandFramework commandFramework = new CommandFramework(this);
    private final ClassesRegistration classesRegistration = new ClassesRegistration();

    @Override
    public void onEnable() {
        instance = this;
        load();
        loadGifts();
    }

    @Override
    public void onDisable() {
        writeData();
        try {
            writeHunterData();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (Player player : instance.getServer().getOnlinePlayers()) {
            if (InteractPlayerEvent.playerBooleanHashMap.containsKey(player.getName())) {
                General.restoreStatistics(player);
            }
        }
    }

    public CommandFramework getCommandFramework() {
        return commandFramework;
    }

    public void writeHunterData() throws FileNotFoundException {
        String ruta = (System.getProperty("user.dir") + File.separator +
                "cazaRecompensa");
        File file = new File(ruta + File.separator + "hunters" + ".txt");
        ObjectOutputStream fileWriter = null;
        BountyHunter bountyHunter = null;
        try {
            fileWriter = new ObjectOutputStream(new FileOutputStream(file));
            bountyHunter = new BountyHunter(CommandHunter.damsList, onPlayerDamage.playerTimeIsExpired);
            fileWriter.writeObject(bountyHunter);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void writeData() {
        String ruta = (System.getProperty("user.dir") + File.separator +
                "gifts");
        File file = new File(ruta + File.separator + "locations" + ".txt");
        try {
            ObjectOutputStream fileWriter = new ObjectOutputStream(new FileOutputStream(file));
            Gift gift = new Gift(CommandAddGift.itemStackHashMap, interactWithGift.contadorRegalos,
                    interactWithGift.regalosEncontrados, interactWithGift.misionCompletada);
            fileWriter.writeObject(gift);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void loadGifts() {
        String ruta = (System.getProperty("user.dir") + File.separator +
                "gifts");
        File directorio = new File(ruta);
        if (directorio.isDirectory() && directorio.canRead()) {
            for (File file : directorio.listFiles()) {
                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
                    Gift gift = (Gift) objectInputStream.readObject();
                    CommandAddGift.itemStackHashMap = gift.getItemStackHashMap();
                    interactWithGift.contadorRegalos = gift.getContadorRegalos();
                    interactWithGift.misionCompletada = gift.getMisionCompletada();
                    interactWithGift.regalosEncontrados = gift.getRegalosEncontrados();
                    objectInputStream.close();
                    for (Localizaciones localizaciones : CommandAddGift.itemStackHashMap) {
                        ItemStack regalo = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                        SkullMeta skullMeta = (SkullMeta) regalo.getItemMeta();
                        skullMeta.setOwner("defib");
                        ArrayList<String> skullLore = new ArrayList<>();
                        skullLore.add(CC.translate("&cCoordenadas &4x: " + localizaciones.getBloqueX() + " y: " + localizaciones.getBloqueY() + "" +
                                " z: " + localizaciones.getBloqueZ()));
                        skullLore.add(CC.translate("&cMundo: &4" + localizaciones.getWorld()));
                        skullMeta.setLore(skullLore);
                        skullMeta.setDisplayName(CC.translate("&c&lRegalo de Navidad"));
                        regalo.setItemMeta(skullMeta);
                        itemStacks.add(regalo);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void load() {
        classesRegistration.loadCommands("com.server.plugin.command");
        classesRegistration.loadListeners("com.server.plugin.events");
        try {
            String ruta = (System.getProperty("user.dir") + File.separator +
                    "cazaRecompensa");
            File file = new File(ruta + File.separator + "hunters" + ".txt");
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
            BountyHunter bountyHunter = (BountyHunter) objectInputStream.readObject();
            CommandHunter.damsList = bountyHunter.getDamsList();
            onPlayerDamage.playerTimeIsExpired = bountyHunter.getPlayerTimeIsExpired();
            for (String name : CommandHunter.damsList.keySet()) {
                ItemStack itemStack = new ItemStack(Material.SKULL_ITEM);
                ArrayList<String> arrayList = new ArrayList<>();
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(CC.translate("&c&l" + name));
                arrayList.add(CC.translate("&aReward: &e" + CommandHunter.damsList.get(name) + " TPS"));
                itemMeta.setLore(arrayList);
                itemStack.setItemMeta(itemMeta);
                CommandHunter.playersMenu.add(itemStack);
                BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                    int count = 0;
                    private BountyHunter bountyHunter2 = bountyHunter;
                    private ItemStack itemStack2 = itemStack;

                    private String name2 = name;

                    @Override
                    public void run() {
                        if ((bountyHunter2.getTime() * 60 * 60) == count) {
                            count = 0;
                            onPlayerDamage.playerTimeIsExpired.put(name2, true);
                            CommandHunter.damsList.remove(name2);
                            CommandHunter.playersMenu.remove(itemStack2);
                            this.cancel();
                        }
                        count++;
                    }
                };
                bukkitRunnable.runTaskTimer(Main.instance, 20L, 20L);
                threadsOfHunted.put(name, bukkitRunnable);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}