package com.server.plugin.util;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@SuppressWarnings("deprecation")
public class ItemBuilder {
    private ItemStack itemStack;

    public ItemBuilder(int itemId) {
        itemStack = new ItemStack(Material.getMaterial(itemId));
    }

    public ItemBuilder(int itemId, byte itemData) {
        itemStack = new ItemStack(Material.getMaterial(itemId), 0, itemData);
    }

    public ItemBuilder(int itemId, int amount, byte itemData) {
        itemStack = new ItemStack(Material.getMaterial(itemId), amount, itemData);
    }

    public ItemBuilder(int itemId, int amount) {
        itemStack = new ItemStack(Material.getMaterial(itemId), amount, (short) 0);
    }

    public ItemBuilder(Material material) {
        itemStack = new ItemStack(material);
    }

    public ItemBuilder setName(String name) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(CC.translate(name));
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(CC.translate(Arrays.asList(lore)));
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(CC.translate(lore));
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public void addEnchantment(String ID, int level) {
        itemStack.addUnsafeEnchantment(Enchantment.getByName(ID), level);
    }

    public ItemStack get() {
        return itemStack;
    }
}