package com.github.tim91690.eclipse.listeners;

import com.github.tim91690.eclipse.mobs.*;
import com.github.tim91690.eclipse.mobs.semiboss.IllusionerMage;
import com.github.tim91690.eclipse.mobs.semiboss.PhantomFurries;
import com.github.tim91690.eclipse.mobs.semiboss.RavagerBeast;
import net.minecraft.server.level.WorldServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class Spawn implements Listener {
    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        if (!(event.getEntity() instanceof Monster) || event.getEntity().getScoreboardTags().contains("Eclipse")) return;

        if ((int)(Math.random()*100) < 10) {
            event.setCancelled(true);
            PhantomFurries pf = new PhantomFurries(event.getLocation());
            WorldServer world = ((CraftWorld) event.getLocation().getWorld()).getHandle();
            world.addEntity(pf);
            return;
        }

        if (event.getEntity() instanceof Zombie) {
            event.setCancelled(true);
            ZombieTank zt = new ZombieTank(event.getLocation());
            WorldServer world = ((CraftWorld) event.getLocation().getWorld()).getHandle();
            world.addEntity(zt);
            return;
        }

        if (event.getEntity() instanceof Creeper) {
            event.setCancelled(true);
            CreeperBomb cb = new CreeperBomb(event.getLocation());
            WorldServer world = ((CraftWorld) event.getLocation().getWorld()).getHandle();
            world.addEntity(cb);
            return;
        }

        if (event.getEntity() instanceof Skeleton) {
            event.setCancelled(true);
            SkeletonSniper ss = new SkeletonSniper(event.getLocation());
            WorldServer world = ((CraftWorld) event.getLocation().getWorld()).getHandle();
            world.addEntity(ss);
            return;
        }

        if (event.getEntity() instanceof Spider && !(event.getEntity() instanceof CaveSpider)) {
            event.setCancelled(true);
            SpiderCrawler sc = new SpiderCrawler(event.getLocation());
            WorldServer world = ((CraftWorld) event.getLocation().getWorld()).getHandle();
            world.addEntity(sc);
            return;
        }
        return;
    }
}
