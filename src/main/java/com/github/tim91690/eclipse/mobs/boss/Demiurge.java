package com.github.tim91690.eclipse.mobs.boss;

import com.github.tim91690.eclipse.misc.PathfinderGoalFlyToLocation;
import net.kyori.adventure.text.Component;
import net.minecraft.core.BlockPosition;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.control.ControllerMoveFlying;
import net.minecraft.world.entity.ai.goal.PathfinderGoalFloat;
import net.minecraft.world.entity.ai.goal.PathfinderGoalLookAtPlayer;
import net.minecraft.world.entity.ai.goal.PathfinderGoalRandomFly;
import net.minecraft.world.entity.animal.EntityBee;
import net.minecraft.world.entity.boss.wither.EntityWither;
import net.minecraft.world.entity.monster.EntityVex;
import net.minecraft.world.entity.player.EntityHuman;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.ArmorStand;


public class Demiurge extends EntityVex {

    private ArmorStand ring1;
    private ArmorStand ring2;
    private ArmorStand core;
    private ArmorStand shell;
    private ArmorStand wings;

    public Demiurge(Location loc) {

        super(EntityTypes.aU,((CraftWorld)loc.getWorld()).getHandle());
        this.setPosition(loc.getX(),loc.getY(),loc.getZ());
        this.setSilent(true);
        this.getNavigation().d(true);
        //this.setInvisible(true);
        this.bM = new ControllerMoveFlying(this, 20, true);

        Bukkit.broadcast(Component.text(ChatColor.translateAlternateColorCodes('&', "&eLe &2&bDemiurge &ea spawn en &a<" + (int) loc.getX() + " , " + (int) loc.getY() + " , " + (int) loc.getZ() + ">")));

    }


    @Override
    protected void initPathfinder() {
        super.initPathfinder();
        Location loc = new Location(this.getWorld().getWorld(),0,100,0);
        this.bP.a(1, new PathfinderGoalFloat(this));
        this.bP.a(0, new PathfinderGoalFlyToLocation(this,loc));
    }
}
