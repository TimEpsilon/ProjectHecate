package com.github.tim91690.eclipse.listeners;

import com.github.tim91690.EventManager;
import com.github.tim91690.eclipse.item.enchants.MoonEnchant;
import com.github.tim91690.eclipse.structure.BlockProtection;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class ListenerManager {

    public static void registerEvents(EventManager plugin) {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new BossDamage(),plugin);
        pm.registerEvents(new Loot(),plugin);
        pm.registerEvents(new Spawn(),plugin);
        pm.registerEvents(new OnJoin(),plugin);
        pm.registerEvents(new MoonEnchant(),plugin);
        pm.registerEvents(new BlockProtection(),plugin);
        pm.registerEvents(new SlimeSplit(),plugin);
    }
}
