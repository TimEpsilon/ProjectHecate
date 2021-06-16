package com.github.tim91690;

import com.github.tim91690.commands.StartEclipse;
import org.bukkit.plugin.java.JavaPlugin;

public final class EventManager extends JavaPlugin {

    public static EventManager plugin;

    @Override
    public void onEnable() {
        registerCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerCommands() {
        getCommand("eclipse").setExecutor(new StartEclipse());
    }
}
