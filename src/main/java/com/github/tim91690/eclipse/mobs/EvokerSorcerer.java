package com.github.tim91690.eclipse.mobs;

import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.monster.EntityEvoker;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.LivingEntity;

public class EvokerSorcerer extends EntityEvoker {

    public EvokerSorcerer(Location loc) {
        super(EntityTypes.y,((CraftWorld)loc.getWorld()).getHandle());
        this.setPosition(loc.getX(),loc.getY(),loc.getZ());

        ((LivingEntity) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40);
        ((LivingEntity) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(40);
        ((LivingEntity) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.4);

        this.setHealth(40);

        ((LivingEntity) this.getBukkitEntity()).addScoreboardTag("Eclipse");

    }
}