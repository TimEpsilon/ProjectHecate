package com.github.tim91690.eclipse.listeners;

import com.github.tim91690.EventManager;
import com.github.tim91690.eclipse.item.enchants.CosmicEnchant;
import com.github.tim91690.eclipse.structure.BlockProtection;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class ListenerManager {

    public static void registerEvents(EventManager plugin) {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new BossDamage(),plugin);
        pm.registerEvents(new Loot(),plugin);
        pm.registerEvents(new Spawn(),plugin); //on/off
        pm.registerEvents(new OnJoin(),plugin); //on/off
        pm.registerEvents(new CosmicEnchant(),plugin); //on/off
        pm.registerEvents(new BlockProtection(),plugin);
        pm.registerEvents(new SlimeSplit(),plugin);
    }
}
