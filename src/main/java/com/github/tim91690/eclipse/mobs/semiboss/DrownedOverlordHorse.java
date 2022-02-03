package com.github.tim91690.eclipse.mobs.semiboss;

import com.github.tim91690.eclipse.mobs.EclipseMobs;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;


public class DrownedOverlordHorse extends EclipseMobs {

    public DrownedOverlordHorse(Location loc) {
        super((Monster)loc.getWorld().spawnEntity(loc,EntityType.ZOMBIE_HORSE, CreatureSpawnEvent.SpawnReason.NATURAL),30);

        this.entity.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(15);
        this.entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.5);

        DrownedOverlordRider rider = new DrownedOverlordRider(loc);
        this.entity.addPassenger(rider.getEntity());

        this.entity.addScoreboardTag("SemiBoss");
    }
}