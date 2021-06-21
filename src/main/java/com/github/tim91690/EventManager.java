package com.github.tim91690;

import com.github.tim91690.commands.BossList;
import com.github.tim91690.commands.StartEclipse;
import com.github.tim91690.eclipse.listeners.ListenerManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class EventManager extends JavaPlugin {

    public static EventManager plugin;

    @Override
    public void onEnable() {
        registerCommands();
        ListenerManager.registerEvents(this);
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onLoad() {
        plugin = this;
    }

    private void registerCommands() {
        getCommand("eclipse").setExecutor(new StartEclipse());
        getCommand("bosslocation").setExecutor(new BossList());
    }

    public static EventManager getPlugin() {
        return plugin;
    }
}
