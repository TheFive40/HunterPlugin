package com.server.plugin.command;

import com.server.plugin.Main;
import com.server.plugin.events.onPlayerDamage;
import com.server.plugin.util.CC;
import com.server.plugin.util.command.BaseCommand;
import com.server.plugin.util.command.Command;
import com.server.plugin.util.command.CommandArgs;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;

public class CommandRemoveHunter extends BaseCommand {
    @Command(name="hunterdelete", aliases = {"removehunter"}, permission = "removehunter.use"
    , usage = "&cPrueba utilizando &7/removehunter <player>")
    @Override
    public void onCommand(CommandArgs command) throws IOException {
        String ruta = (System.getProperty("user.dir") + File.separator +
                "cazaRecompensa");
        try{
            if(CommandHunter.damsList.containsKey(command.getArgs(0))){
                CommandHunter.damsList.remove(command.getArgs(0));
                if(CommandHunter.bukkitRunnableHashMap.containsKey(command.getArgs(0))){
                    CommandHunter.bukkitRunnableHashMap.remove(command.getArgs(0));
                }else{
                    Main.threadsOfHunted.remove(command.getArgs(0));
                }
                onPlayerDamage.playerTimeIsExpired.remove(command.getArgs(0));
                File file = new File(ruta + File.separator + command.getArgs(0) + ".txt");
                file.delete();
                command.getPlayer().sendMessage(CC.translate("&c¡El jugador ha sido eliminado de la lista!"));

                if(!CommandHunter.playersMenu.isEmpty()){
                    for(ItemStack itemStack : CommandHunter.playersMenu){
                        if(itemStack.getItemMeta().getDisplayName().contains(command.getPlayer().getName())){
                            CommandHunter.playersMenu.remove(itemStack);
                        }
                    }

                }
                return;
            }
        }catch(ArrayIndexOutOfBoundsException exception){
            command.getPlayer().sendMessage(CC.translate(command.getCommand().getUsage()));
            return;
        }
        command.getPlayer().sendMessage(CC.translate(command.getCommand().getUsage()));
    }
}
