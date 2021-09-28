package com.github.tim91690.eclipse.misc;

import net.minecraft.core.BlockPosition;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.navigation.Navigation;
import net.minecraft.world.level.IWorldReader;
import org.bukkit.Location;

import java.util.EnumSet;


public class PathfinderGoalFlyToLocation extends PathfinderGoal {
    private double speed;
    private EntityInsentient entity;
    private Location loc;
    private Navigation navigation;

    public PathfinderGoalFlyToLocation(EntityInsentient mob, Location loc) {
        this.entity = mob;
        this.loc = loc;
        this.navigation = (Navigation) this.entity.getNavigation();
        this.a(EnumSet.of(Type.a,Type.c)); //set flag -> MOVE, JUMP
        this.entity.getNavigation().d(true); //setcanfloat(true)
    }

    public boolean a() { //can use
        return true;
    }

    //can continue?
    public boolean b() {
        return false;
    }

    //start
    public void c() {
        this.g(); //move to block
    }

    //move to block
    protected void g() {
        this.entity.getNavigation().a((double)((float)this.loc.getX()) + 0.5D, (double)(this.loc.getY() + 1), (double)((float)this.loc.getZ()) + 0.5D, 1);
    }

}