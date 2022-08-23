package com.github.timepsilon.comet.listeners;

import com.github.timepsilon.ProjectHecate;
import com.github.timepsilon.comet.misc.WeightCollection;
import com.github.timepsilon.comet.mobs.*;
import com.github.timepsilon.comet.mobs.semiboss.DrownedOverlordHorse;
import com.github.timepsilon.comet.mobs.semiboss.IllusionerMage;
import com.github.timepsilon.comet.mobs.semiboss.PhantomFurries;
import com.github.timepsilon.comet.mobs.semiboss.RavagerBeast;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.Vector;

import java.util.Random;

public class EntityExplosion implements Listener {
    private final Random random = new Random();

    @EventHandler
    public void onCreeperExplode(EntityExplodeEvent e) {
        if (!ProjectHecate.isRunningEvent) return;
        e.blockList().clear();
        if (!e.getEntity().getType().equals(EntityType.CREEPER)) return;
        WeightCollection<String> rc;
        rc = new WeightCollection<String>()
                .add(20, "CreeperBomb")
                .add(5, "SemiBoss")
                .add(25, "SkeletonSniper")
                .add(25, "SpiderCrawler")
                .add(15, "SpiderHerd")
                .add(25, "ZombieTank")
                .add(15, "WickedWitch");

        Location loc = e.getLocation().add(0,1,0);
        for (int i = 0; i < 1 + ProjectHecate.getComet().getPhase(); i++) {
            double angle = random.nextDouble(2*Math.PI);
            CometMobs mob = null;
            switch (rc.next()) {
                case "CreeperBomb":
                default:
                    mob = new CreeperBomb(loc);
                    break;

                case "SemiBoss":
                    switch (ProjectHecate.getComet().getPhase()) {
                        case 1 -> mob = new PhantomFurries(loc);
                        case 2 -> mob = new RavagerBeast(loc);
                        case 3 -> mob = new IllusionerMage(loc);
                        case 4 -> mob = new DrownedOverlordHorse(loc);
                    }
                    break;

                case "SkeletonSniper":
                    mob = new SkeletonSniper(loc);
                    break;

                case "SpiderCrawler":
                    mob = new SpiderCrawler(loc);
                    break;

                case "SpiderHerd":
                    mob = new SpiderHerd(loc);
                    break;

                case "ZombieTank":
                    mob = new ZombieTank(loc,ProjectHecate.getComet().getPhase());
                    break;

                case "WickedWitch":
                    mob = new WickedWitch(loc);
                    break;
            }
            if (mob == null) mob = new CreeperBomb(loc);
            mob.getEntity().setVelocity(new Vector(Math.cos(angle),1,Math.sin(angle)));
        }
    }
}
