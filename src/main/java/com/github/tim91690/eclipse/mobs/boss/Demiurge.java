package com.github.tim91690.eclipse.mobs.boss;

import com.github.tim91690.eclipse.misc.PathfinderGoalWalkToLocation;
import net.kyori.adventure.text.Component;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.boss.wither.EntityWither;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.ArmorStand;


public class Demiurge extends EntityWither {

    private ArmorStand ring1;
    private ArmorStand ring2;
    private ArmorStand core;
    private ArmorStand shell;
    private ArmorStand wings;

    public Demiurge(Location loc) {

        super(EntityTypes.aZ,((CraftWorld)loc.getWorld()).getHandle());
        this.setPosition(loc.getX(),loc.getY(),loc.getZ());
        this.setSilent(true);
        //this.setInvisible(true);

        Bukkit.broadcast(Component.text(ChatColor.translateAlternateColorCodes('&', "&eLe &2&bDemiurge &ea spawn en &a<" + (int) loc.getX() + " , " + (int) loc.getY() + " , " + (int) loc.getZ() + ">")));

    }


    @Override
    protected void initPathfinder() {
        this.bP.a(0,new PathfinderGoalWalkToLocation(this,new Location((World) this.getWorld(),0,100,0),1));
    }
}
