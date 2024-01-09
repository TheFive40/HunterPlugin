package com.server.plugin;

import com.server.plugin.Model.BountyHunter;
import com.server.plugin.command.CommandHunter;
import com.server.plugin.events.onPlayerDamage;
import com.server.plugin.util.CC;
import com.server.plugin.util.ClassesRegistration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import com.server.plugin.util.command.CommandFramework;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main extends JavaPlugin {
    static{
        String ruta = (System.getProperty("user.dir") + File.separator +
                "BountyHunters");
        File file = new File(ruta);
        file.mkdir();
    }
    public static Main instance;
    public static HashMap<String, BukkitRunnable> threadsOfHunted = new HashMap<>();
    private final CommandFramework commandFramework = new CommandFramework(this);
    private final ClassesRegistration classesRegistration = new ClassesRegistration();

    @Override
    public void onEnable() {
        instance = this;
        System.out.println("Plugin activated successfully");
        System.out.println("By TheFive");
        load();
    }

    @Override
    public void onDisable() {
        try {
            writeHunterData();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Plugin successfully deactivated");
        System.out.println("By DelawareX");
    }

    public CommandFramework getCommandFramework() {
        return commandFramework;
    }

    public void writeHunterData() throws FileNotFoundException {
        String ruta = (System.getProperty("user.dir") + File.separator +
                "BountyHunters");
        File file = new File(ruta + File.separator + "hunters" + ".dat");
        ObjectOutputStream fileWriter = null;
        BountyHunter bountyHunter = null;
        try {
            fileWriter = new ObjectOutputStream(new FileOutputStream(file));
            bountyHunter = new BountyHunter(CommandHunter.damsList, onPlayerDamage.playerTimeIsExpired
            , CommandHunter.concurrentHashMap);
            fileWriter.writeObject(bountyHunter);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public void load() {
        classesRegistration.loadCommands("com.server.plugin.command");
        classesRegistration.loadListeners("com.server.plugin.events");
        try {
            String ruta = (System.getProperty("user.dir") + File.separator +
                    "BountyHunters");
            File file = new File(ruta + File.separator + "hunters" + ".dat");
            if (file.canRead() && file.exists() && file != null) {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
                BountyHunter bountyHunter = (BountyHunter) objectInputStream.readObject();
                CommandHunter.damsList = bountyHunter.getDamsList();
                CommandHunter.concurrentHashMap = bountyHunter.getConcurrentHashMap();
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
                            if(bountyHunter2.getConcurrentHashMap().containsKey(name2)){
                                if ((bountyHunter2.getConcurrentHashMap().get(name2) * 60 * 60) == count) {
                                    count = 0;
                                    onPlayerDamage.playerTimeIsExpired.put(name2, true);
                                    CommandHunter.damsList.remove(name2);
                                    CommandHunter.playersMenu.remove(itemStack2);
                                    CommandHunter.concurrentHashMap.remove(name2);
                                    this.cancel();
                                }
                                count++;
                            }
                        }
                    };
                    bukkitRunnable.runTaskTimer(Main.instance, 20L, 20L);
                    threadsOfHunted.put(name, bukkitRunnable);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}