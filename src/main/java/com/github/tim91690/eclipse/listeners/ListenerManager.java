package com.github.tim91690.eclipse.listeners;

import com.github.tim91690.EventManager;
import com.github.tim91690.misc.CustomBossBar;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class ListenerManager {

    public static void registerEvents(EventManager plugin) {
        PluginManager pm = Bukkit.getPluginManager();

        new CustomBossBar();

        pm.registerEvents(new BossDamage(),plugin);
        pm.registerEvents(new Loot(),plugin);
        pm.registerEvents(new Spawn(),plugin);
    }
}
