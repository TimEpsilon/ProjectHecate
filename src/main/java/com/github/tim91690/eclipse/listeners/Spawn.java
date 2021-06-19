package com.github.tim91690.eclipse.listeners;

import com.github.tim91690.eclipse.mobs.*;
import com.github.tim91690.eclipse.mobs.semiboss.DrownedOverlord;
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

        /**Semi boss à chaque étapes (1% -> 5%) :
         * 1) -> Phantom Furries (100%)
         * 2) -> Ravager Beast (70%), Phantom Furries (30%)
         * 3) -> Illusioner Mage (70%), Ravager Beast (25%), Phantom Furries (5%)
         * 4) -> Drowned Overlord (70%), Illusioner Mage (20%), Ravager Beast (7%), Phantom Furries (3%)
         * 5) -> Shadows (70%), Drowned Overlord (15%), Illusioner Mage (8%), Ravager Beast (5%), Phantom Furries (2%)
         */
        if ((int)(Math.random()*100) < 10) {
            event.setCancelled(true);
            DrownedOverlord Do = new DrownedOverlord(event.getLocation());
            WorldServer world = ((CraftWorld) event.getLocation().getWorld()).getHandle();
            world.addEntity(Do);
            return;
        }

        /**Zombie Tank remplace les zombies et husk
         */
        if (event.getEntity() instanceof Zombie && !(event.getEntity() instanceof Drowned) && !(event.getEntity() instanceof PigZombie)) {
            event.setCancelled(true);
            ZombieTank zt = new ZombieTank(event.getLocation());
            WorldServer world = ((CraftWorld) event.getLocation().getWorld()).getHandle();
            world.addEntity(zt);
            return;
        }

        /**Creeper Bomb ou Evoker Sorcerer (10%) remplace creepers
         */
        if (event.getEntity() instanceof Creeper) {
            event.setCancelled(true);
            WorldServer world = ((CraftWorld) event.getLocation().getWorld()).getHandle();
            if((int)(Math.random()*100) < 10) {
                EvokerSorcerer es = new EvokerSorcerer(event.getLocation());
                world.addEntity(es);

            } else {
                CreeperBomb cb = new CreeperBomb(event.getLocation());
                world.addEntity(cb);
            }
            return;
        }

        /**Skeleton Sniper remplace skeletons
         */
        if (event.getEntity() instanceof Skeleton) {
            event.setCancelled(true);
            SkeletonSniper ss = new SkeletonSniper(event.getLocation());
            WorldServer world = ((CraftWorld) event.getLocation().getWorld()).getHandle();
            world.addEntity(ss);
            return;
        }

        /**Spider Crawler et Spider Herd (5%) remplace spiders
         */
        if (event.getEntity() instanceof Spider && !(event.getEntity() instanceof CaveSpider)) {
            event.setCancelled(true);
            WorldServer world = ((CraftWorld) event.getLocation().getWorld()).getHandle();
            if ((int)(Math.random()*100) < 5) {
                for (int i =-5; i < Math.random()*10;i++) {
                    SpiderHerd sh = new SpiderHerd(event.getLocation());
                    world.addEntity(sh);
                }

            } else {
                SpiderCrawler sc = new SpiderCrawler(event.getLocation());
                world.addEntity(sc);
            }
            return;
        }
        return;
    }
}
