package com.github.tim91690.eclipse.mobs;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class CreeperBomb extends EclipseMobs {

    public CreeperBomb(Location loc) {
        super((Monster) loc.getWorld().spawnEntity(loc, EntityType.CREEPER, CreatureSpawnEvent.SpawnReason.NATURAL),40);

        this.entity.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(50);
        this.entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.25);

        ((Creeper)this.entity).setPowered(true);
        }
}