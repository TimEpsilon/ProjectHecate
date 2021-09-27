package com.github.tim91690.eclipse.misc;

import net.minecraft.core.BlockPosition;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.navigation.Navigation;
import net.minecraft.world.level.pathfinder.PathEntity;
import org.bukkit.Location;

import java.util.EnumSet;


public class PathfinderGoalWalkToLocation extends PathfinderGoal {
    private float speed;
    private EntityInsentient entity;
    private Location loc;
    private Navigation navigation;

    public PathfinderGoalWalkToLocation(EntityInsentient entity, Location loc, float speed) {
        this.entity = entity;
        this.loc = loc;
        this.navigation = (Navigation) this.entity.getNavigation();
        this.speed = speed;
        this.a(EnumSet.of(Type.a));
    }

    @Override
    public boolean a() {
        //est répétée chaque tick
        //Commence le pathfinding goal si vrai
        return true;
    }

    @Override
    public boolean b() {
        return false;
    }

    @Override
    public void c() {
        //runner
        PathEntity pathEntity = this.navigation.a(new BlockPosition(this.loc.getX(), this.loc.getY(), this.loc.getZ()), 5);
        this.navigation.a(pathEntity, speed);
    }
}