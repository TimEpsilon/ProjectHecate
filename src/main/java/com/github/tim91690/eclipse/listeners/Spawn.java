package com.github.tim91690.eclipse.listeners;

import com.github.tim91690.eclipse.misc.WeightCollection;
import com.github.tim91690.eclipse.mobs.*;

import com.github.tim91690.eclipse.mobs.semiboss.DrownedOverlordHorse;
import com.github.tim91690.eclipse.mobs.semiboss.IllusionerMage;
import com.github.tim91690.eclipse.mobs.semiboss.PhantomFurries;
import com.github.tim91690.eclipse.mobs.semiboss.RavagerBeast;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.Random;

public class Spawn implements Listener {

    private float probaSemiBoss = 0.01f;
    private static Random random = new Random();

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        if (!(event.getEntity() instanceof Monster) || !event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL)) return;

        int lvl = 1; //TODO getlevel

        if (random.nextFloat()<probaSemiBoss) {
            event.setCancelled(true);
            spawnSemiBoss(event.getLocation(),lvl);
        }

        switch (event.getEntity().getType().toString()) {
            case ("ZOMBIE"):
            case ("DROWNED"):
            case ("HUSK"):
                //Zombie Tank remplace les zombies, drowned et husk
                new ZombieTank(event.getEntity(),lvl);
                return;
            case ("CREEPER"):
                //Creeper Bomb remplace creeper
                new CreeperBomb(event.getEntity());
                return;
            case ("SKELETON"):
                //Skeleton Sniper remplace skeletons
                new SkeletonSniper(event.getEntity());
                return;
            case ("SPIDER"):
                //Spider Crawler remplace spider
                new SpiderCrawler(event.getEntity());
        }
    }

    /**Semi boss à chaque étapes (1% -> 5%) :
     1) -> Phantom Furries (100%)
     2) -> Ravager Beast (70%), Phantom Furries (30%)
     3) -> Illusioner Mage (70%), Ravager Beast (25%), Phantom Furries (5%)
     4) -> Drowned Overlord (70%), Illusioner Mage (20%), Ravager Beast (7%), Phantom Furries (3%)
     **/
    private void spawnSemiBoss(Location loc,int lvl) {
        WeightCollection<String> rc = new WeightCollection<>();
        switch (lvl) {
            case 1:
                rc.add(100,"PhantomFurries");
                break;
            case 2:
                rc.add(70,"RavagerBeast").add(30,"PhantomFurries");
                break;
            case 3:
                rc.add(70,"IllusionerMage").add(25,"RavagerBeast").add(5,"PhantomFurries");
                break;
            case 4:
                rc.add(70,"DrownedOverlord").add(20,"IllusionerMage").add(7,"RavagerBeast").add(3,"PhantomFurries");
        }

        switch (rc.next()) {
            case "PhantomFurries":
                new PhantomFurries(loc);
                break;
            case "RavagerBeast":
                new RavagerBeast(loc);
                break;
            case "IllusionerMage":
                new IllusionerMage(loc);
                break;
            case "DrownedOverlord":
                new DrownedOverlordHorse(loc);
                break;
        }
    }
}
