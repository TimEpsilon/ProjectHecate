package com.github.tim91690.comet.mobs;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpiderHerd extends CometMobs {

    public SpiderHerd(Location loc) {
        super((LivingEntity) loc.getWorld().spawnEntity(loc,EntityType.CAVE_SPIDER),30);

        this.entity.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(40);
        this.entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.4);

        this.entity.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,2000000,4,true,true));
    }
}