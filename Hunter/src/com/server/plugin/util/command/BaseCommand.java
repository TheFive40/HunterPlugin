package com.server.plugin.util.command;

import com.server.plugin.Main;

import java.io.IOException;

public abstract class BaseCommand {

    public Main main = Main.instance;

    public BaseCommand() {
        main.getCommandFramework().registerCommands(this);
    }

    public abstract void onCommand(CommandArgs command) throws IOException;

}
