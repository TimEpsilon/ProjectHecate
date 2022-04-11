package com.github.timepsilon.comet.listeners;

import com.github.timepsilon.EventManager;
import com.github.timepsilon.comet.item.enchants.CosmicEnchant;
import com.github.timepsilon.comet.structure.BlockProtection;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class ListenerManager {

    public static void registerEvents(EventManager plugin) {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new BossDamage(),plugin);
        pm.registerEvents(new Loot(),plugin);
        pm.registerEvents(new Spawn(),plugin);
        pm.registerEvents(new OnJoin(),plugin);
        pm.registerEvents(new CosmicEnchant(),plugin);
        pm.registerEvents(new BlockProtection(),plugin);
        pm.registerEvents(new SlimeSplit(),plugin);
        pm.registerEvents(new Score(),plugin);
        pm.registerEvents(new CustomDeath(),plugin);
    }
}
