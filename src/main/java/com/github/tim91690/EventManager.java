package com.github.tim91690;

import com.github.tim91690.commands.*;
import com.github.tim91690.eclipse.Comet;
import com.github.tim91690.eclipse.item.CustomCraft;
import com.github.tim91690.eclipse.item.enchants.EnchantRegister;
import com.github.tim91690.eclipse.listeners.ListenerManager;
import com.github.tim91690.eclipse.misc.ConfigManager;
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
    }

    @Override
    public void onLoad() {
        plugin = this;
    }

    private void registerCommands() {
        getCommand("eclipse").setExecutor(new StartEclipse());
        getCommand("bosslocation").setExecutor(new BossList());
        getCommand("moonblade").setExecutor(new GiveCosmicBlade());
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
