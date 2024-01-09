package com.server.plugin.util;

import com.server.plugin.Main;
import me.dpohvar.powernbt.PowerNBT;
import me.dpohvar.powernbt.api.NBTCompound;
import me.dpohvar.powernbt.api.NBTManager;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class General {
    public static final int MAX_TPS = 2000000000;
    public static int bonusSTAT;
    private static NBTCompound PlayerPersisted;
    private static int stat;
    public static final String STR = "jrmcStrI";
    public static final String DEX = "jrmcDexI";
    private static NBTCompound nbtCompound;
    public static final String CON = "jrmcCnsI";
    public static final String WIL = "jrmcWilI";

    public static final String MND = "jrmcIntI";

    public static final String SPI = "jrmcCncI";

    private static boolean isExpired = true;

    public static String getPlayerRace(Player player) {
        int raceNumber = PowerNBT.getApi().readForgeData(player).getCompound("PlayerPersisted").getInt("jrmcRace");
        String playerRace = null;

        if (raceNumber == 0) {
            playerRace = "Humano";
        } else if (raceNumber == 1) {
            playerRace = "Saiyan";
        } else if (raceNumber == 2) {
            playerRace = "Semi-Saiyan";
        } else if (raceNumber == 3) {
            playerRace = "Namekiano";
        } else if (raceNumber == 4) {
            playerRace = "Arcosiano";
        } else if (raceNumber == 5) {
            playerRace = "Majin";
        }

        return playerRace;
    }

    public static int getPlayerLevel(Player player) {
        int level = 0;
        int str = PowerNBT.getApi().readForgeData(player).getCompound("PlayerPersisted").getInt("jrmcStrI");
        int dex = PowerNBT.getApi().readForgeData(player).getCompound("PlayerPersisted").getInt("jrmcDexI");
        int con = PowerNBT.getApi().readForgeData(player).getCompound("PlayerPersisted").getInt("jrmcCnsI");
        int wil = PowerNBT.getApi().readForgeData(player).getCompound("PlayerPersisted").getInt("jrmcWilI");
        int mnd = PowerNBT.getApi().readForgeData(player).getCompound("PlayerPersisted").getInt("jrmcIntI");
        int spi = PowerNBT.getApi().readForgeData(player).getCompound("PlayerPersisted").getInt("jrmcCncI");
        level = (str + dex + con + wil + mnd + spi) / 5 - 11;
        if (level < 1)
            return 0;
        return level;
    }

    public static int getPlayerTps(Player player) {
        return PowerNBT.getApi().readForgeData(player).getCompound("PlayerPersisted").getInt("jrmcTpint");
    }

    public static void getAndRemoveTps(Player player, int tps) {
        if(tps<0) return;
        NBTCompound forgeData = NBTManager.getInstance().readForgeData(player);
        NBTCompound playerPersisted = (NBTCompound) forgeData.get("PlayerPersisted");
        if (tps > playerPersisted.getInt("jrmcTpint")){
            player.sendMessage(CC.translate("&c¡No tienes la cantidad suficiente de Tps!"));
            return;
        }
        playerPersisted.put("jrmcTpint", getPlayerTps(player) - tps);
        forgeData.put("jrmcTpint", getPlayerTps(player)- tps);
        NBTManager.getInstance().writeForgeData(player, forgeData);
        player.sendMessage(CC.translate("&c- " + tps));
    }

    public static void setPlayerTps(Player player, int amount) {
        NBTCompound Forgedata = NBTManager.getInstance().readForgeData(player);
        if (Forgedata == null) {
            return;
        }

        NBTCompound PlayerPersisted = (NBTCompound) Forgedata.get("PlayerPersisted");
        if (PlayerPersisted == null) {
            return;
        }

        if (getPlayerTps(player) >= MAX_TPS) {
            return;
        }

        PlayerPersisted.put("jrmcTpint", getPlayerTps(player) + amount);
        Forgedata.put("jrmcTpint", getPlayerTps(player) + amount);
        NBTManager.getInstance().writeForgeData(player, Forgedata);
    }

    public static int getPlayerDamage(Player player) {
        int str = PowerNBT.getApi().readForgeData(player).getCompound("PlayerPersisted").getInt("jrmcStrI");
        int damage = (int) (str * 3.9D);
        return damage;
    }

    public static int getStat(Stat statType, Player player) {
        if (statType == Stat.STR) {
            return PowerNBT.getApi().readForgeData(player).getCompound("PlayerPersisted").getInt("jrmcStrI");
        } else if (statType == Stat.DEX) {
            return PowerNBT.getApi().readForgeData(player).getCompound("PlayerPersisted").getInt("jrmcDexI");
        } else if (statType == Stat.CON) {
            return PowerNBT.getApi().readForgeData(player).getCompound("PlayerPersisted").getInt("jrmcCnsI");
        } else if (statType == Stat.WIL) {
            return PowerNBT.getApi().readForgeData(player).getCompound("PlayerPersisted").getInt("jrmcWilI");
        } else if (statType == Stat.MND) {
            return PowerNBT.getApi().readForgeData(player).getCompound("PlayerPersisted").getInt("jrmcIntI");
        } else if (statType == Stat.SPI) {
            return PowerNBT.getApi().readForgeData(player).getCompound("PlayerPersisted").getInt("jrmcCncI");
        }
        return 0;
    }

    public static boolean isConvertibleToInt(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException error) {
            return false;
        }
    }
}