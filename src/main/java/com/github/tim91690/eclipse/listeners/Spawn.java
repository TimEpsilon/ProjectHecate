package com.github.tim91690.eclipse.listeners;

import com.github.tim91690.eclipse.mobs.*;
import net.minecraft.server.level.WorldServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class Spawn implements Listener {
    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        if (!(event.getEntity() instanceof Monster) || event.getEntity().getScoreboardTags().contains("Eclipse")) return;

        /*Semi boss à chaque étapes (1% -> 5%) :
          1) -> Phantom Furries (100%)
          2) -> Ravager Beast (70%), Phantom Furries (30%)
          3) -> Illusioner Mage (70%), Ravager Beast (25%), Phantom Furries (5%)
          4) -> Drowned Overlord (70%), Illusioner Mage (20%), Ravager Beast (7%), Phantom Furries (3%)
          5) -> Shadows (70%), Drowned Overlord (15%), Illusioner Mage (8%), Ravager Beast (5%), Phantom Furries (2%)
         */

        WorldServer world = ((CraftWorld) event.getLocation().getWorld()).getHandle();
        switch (event.getEntity().getType().toString()) {
            case ("ZOMBIE"):
            case ("DROWNED"):
            case ("HUSK"):
                //Zombie Tank remplace les zombies, drowned et husk
                event.setCancelled(true);
                ZombieTank zt = new ZombieTank(event.getLocation());
                world.addEntity(zt, CreatureSpawnEvent.SpawnReason.NATURAL);
                return;
            case ("CREEPER"):
                //Creeper Bomb remplace creeper
                event.setCancelled(true);
                CreeperBomb cb = new CreeperBomb(event.getLocation());
                world.addEntity(cb,CreatureSpawnEvent.SpawnReason.NATURAL);
                return;
            case ("SKELETON"):
                //Skeleton Sniper remplace skeletons
                event.setCancelled(true);
                SkeletonSniper ss = new SkeletonSniper(event.getLocation());
                world.addEntity(ss,CreatureSpawnEvent.SpawnReason.NATURAL);
                return;
            case ("SPIDER"):
                //Spider Crawler remplace spider
                event.setCancelled(true);
                SpiderCrawler sc = new SpiderCrawler(event.getLocation());
                world.addEntity(sc,CreatureSpawnEvent.SpawnReason.NATURAL);
        }
    }
}
