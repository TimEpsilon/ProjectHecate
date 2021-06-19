package com.github.tim91690.eclipse.mobs;

import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.monster.EntityCaveSpider;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpiderHerd extends EntityCaveSpider {

    public SpiderHerd(Location loc) {
        super(EntityTypes.k,((CraftWorld)loc.getWorld()).getHandle());
        this.setPosition(loc.getX(),loc.getY(),loc.getZ());

        ((LivingEntity) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(30);
        ((LivingEntity) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(50);
        ((LivingEntity) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.4);

        this.setHealth(30);

        ((LivingEntity) this.getBukkitEntity()).addScoreboardTag("Eclipse");
        ((LivingEntity) this.getBukkitEntity()).addPotionEffect(new PotionEffect(PotionEffectType.JUMP,2000000,4,true,true));
    }
}