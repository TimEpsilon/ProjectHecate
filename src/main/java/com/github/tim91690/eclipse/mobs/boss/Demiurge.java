package com.github.tim91690.eclipse.mobs.boss;

import net.kyori.adventure.text.Component;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.monster.EntityBlaze;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;

public class Demiurge extends EntityBlaze {

    public Demiurge(Location loc) {

        super(EntityTypes.h,((CraftWorld)loc.getWorld()).getHandle());
        this.setPosition(loc.getX(),loc.getY(),loc.getZ());
        this.
        Bukkit.broadcast(Component.text(ChatColor.translateAlternateColorCodes('&', "&eLe &2&bDemiurge &ea spawn en &a<" + (int) loc.getX() + " , " + (int) loc.getY() + " , " + (int) loc.getZ() + ">")));

    }


}
