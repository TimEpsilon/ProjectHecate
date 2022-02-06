package com.github.tim91690.eclipse.mobs;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class EvokerSorcerer extends EclipseMobs {

    public EvokerSorcerer(LivingEntity entity) {
        super(entity,60);

        this.entity.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(40);
        this.entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.4);
    }
}