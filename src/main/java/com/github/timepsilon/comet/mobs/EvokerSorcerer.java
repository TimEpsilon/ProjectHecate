package com.github.timepsilon.comet.mobs;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public class EvokerSorcerer extends CometMobs {

    public EvokerSorcerer(Location loc) {
        super((LivingEntity) loc.getWorld().spawnEntity(loc,EntityType.EVOKER),60);

        this.entity.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(40);
        this.entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.4);
    }
}