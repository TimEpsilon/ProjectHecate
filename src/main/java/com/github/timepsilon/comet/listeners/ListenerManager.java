package com.github.timepsilon.comet.listeners;

import com.github.timepsilon.ProjectHecate;
import com.github.timepsilon.comet.item.enchants.CosmicEnchant;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class ListenerManager {

    public static void registerEvents(ProjectHecate plugin) {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new BossDamage(),plugin);
        pm.registerEvents(new Loot(),plugin);
        pm.registerEvents(new Spawn(),plugin);
        pm.registerEvents(new EnhanceCosmicBlade(),plugin);
        pm.registerEvents(new CosmicEnchant(),plugin);
        pm.registerEvents(new BlockProtection(),plugin);
        pm.registerEvents(new SlimeSplit(),plugin);
        pm.registerEvents(new Score(),plugin);
        pm.registerEvents(new CustomDeath(),plugin);
        pm.registerEvents(new EntityExplosion(),plugin);
        pm.registerEvents(new RemoveLostArrows(),plugin);
        pm.registerEvents(new FlyingArena(),plugin);
    }
}
