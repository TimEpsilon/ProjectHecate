package com.github.tim91690.eclipse.mobs;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpiderCrawler extends EclipseMobs {

    public SpiderCrawler(Location loc) {
        super((Monster) loc.getWorld().spawnEntity(loc, EntityType.SPIDER, CreatureSpawnEvent.SpawnReason.NATURAL),35);

        this.entity.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(40);
        this.entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.5);

        this.entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING,2000000,2,true,true));
        this.entity.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,2000000,3,true,true));
    }
}