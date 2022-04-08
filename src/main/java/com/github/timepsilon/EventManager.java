package com.github.timepsilon;

import com.github.timepsilon.commands.*;
import com.github.timepsilon.comet.events.Comet;
import com.github.timepsilon.comet.item.CustomCraft;
import com.github.timepsilon.comet.item.enchants.EnchantRegister;
import com.github.timepsilon.comet.listeners.ListenerManager;
import com.github.timepsilon.comet.misc.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class EventManager extends JavaPlugin {

    public static EventManager plugin;
    public static boolean isRunningEvent = false;
    private static Comet comet;

    @Override
    public void onEnable() {
        registerCommands();
        EnchantRegister.register();
        ListenerManager.registerEvents(this);
        new CustomCraft();
        ConfigManager.loadResource();
        comet = new Comet();
    }

    @Override
    public void onDisable() {
        EnchantRegister.unregister();
        comet.stopEvent(true);
    }

    @Override
    public void onLoad() {
        plugin = this;
    }

    private void registerCommands() {
        getCommand("comet").setExecutor(new StartComet());
        getCommand("comet").setTabCompleter(new CometOnTabComplete());
        getCommand("bosslocation").setExecutor(new BossList());
        getCommand("showwaypoint").setExecutor(new Waypoint());
        getCommand("showwaypoint").setTabCompleter(new WaypointOnTabComplete());
    }

    public static EventManager getPlugin() {
        return plugin;
    }

    public static Comet getComet() {
        return comet;
    }
}
