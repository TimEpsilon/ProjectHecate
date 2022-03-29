package com.github.tim91690.comet.mobs;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public class CreeperBomb extends CometMobs {


    public CreeperBomb(LivingEntity creeper) {
        super(creeper,40);
        this.entity.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(50);
        this.entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.25);

        ((Creeper)this.entity).setPowered(true);
    }
    public CreeperBomb(Location loc) {
        this((Creeper)loc.getWorld().spawnEntity(loc, EntityType.CREEPER));
    }
}