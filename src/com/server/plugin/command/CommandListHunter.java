package com.server.plugin.command;
import com.server.plugin.util.CC;
import com.server.plugin.util.command.BaseCommand;
import com.server.plugin.util.command.Command;
import com.server.plugin.util.command.CommandArgs;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.HashSet;

public class CommandListHunter extends BaseCommand {
    public SmartInventory INVENTORY;
    @Command(name = "hunterlist", aliases = {"victimas", "cazarecompensas","cazadoresactivos","cazasactivas"})
    @Override
    public void onCommand(CommandArgs command) throws IOException {
        if ((!CommandHunter.damsList.isEmpty()) && !CommandHunter.playersMenu.isEmpty()) {
            HashSet hashSet = new HashSet();
            hashSet.addAll(CommandHunter.damsList.keySet());
            INVENTORY = SmartInventory.builder().type(InventoryType.CHEST)
                    .size(6, 9).title(CC.translate("&4&lCAZA RECOMPENSAS")).id("cazarecompensas")
                    .provider(new InventoryProvider() {
                        @Override
                        public void init(Player player, InventoryContents inventoryContents) {
                            inventoryContents.fillBorders(ClickableItem.empty(new ItemStack(Material.STAINED_GLASS_PANE)));
                            ClickableItem[] clickableItem = new ClickableItem[CommandHunter.playersMenu.size()];
                            Pagination pagination = inventoryContents.pagination();
                            for (int i = 0; i < clickableItem.length; i++) {
                                clickableItem[i] = ClickableItem.empty(CommandHunter.playersMenu.get(i));
                            }
                            pagination.setItems(clickableItem);
                            pagination.setItemsPerPage(21);
                            ItemStack previous = new ItemStack(Material.ARROW);
                            ItemMeta itemMeta = previous.getItemMeta();
                            itemMeta.setDisplayName(CC.translate("&c&lAtrás"));
                            ItemStack next = new ItemStack(Material.ARROW);
                            ItemMeta itemMetaNext = next.getItemMeta();
                            itemMetaNext.setDisplayName(CC.translate("&2&lSiguiente"));
                            previous.setItemMeta(itemMeta);
                            next.setItemMeta(itemMetaNext);
                            pagination.addToIterator(inventoryContents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1));
                            inventoryContents.set(4, 3, ClickableItem.of(previous,
                                    e -> INVENTORY.open(command.getPlayer(), pagination.previous().getPage())));
                            inventoryContents.set(4, 5, ClickableItem.of(next,
                                    e -> INVENTORY.open(command.getPlayer(), pagination.next().getPage())));

                        }
                        @Override
                        public void update(Player player, InventoryContents inventoryContents) {
                            inventoryContents.fillBorders(ClickableItem.empty(new ItemStack(Material.STAINED_GLASS_PANE)));
                        }
                    }).build();
            INVENTORY.open(command.getPlayer());
            return;
        }
        command.getPlayer().sendMessage(CC.translate("&c¡Al parecer no hay cazas activas!"));
    }
}