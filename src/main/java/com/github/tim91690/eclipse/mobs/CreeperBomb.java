package com.github.tim91690.eclipse.mobs;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.LivingEntity;

public class CreeperBomb extends EclipseMobs {

    public CreeperBomb(LivingEntity entity) {
        super(entity,40);

        this.entity.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(50);
        this.entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.25);

        ((Creeper)this.entity).setPowered(true);
        }
}