package com.github.timepsilon.comet.structure;


import com.github.timepsilon.ProjectHecate;
import com.github.timepsilon.comet.misc.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class AvianBoost {

    private Location loc;
    private int task;
    private boolean isActive = false;
    private final float sizeX;
    private final float sizeY;
    private final float sizeZ;

    private static final Location center = ConfigManager.getLoc();
    public static final List<AvianBoost> BOOST_LIST = new ArrayList<>();

    public AvianBoost(Location loc) {
        this(loc,2,2,2);
    }

    public AvianBoost(int x,int y,int z,float dx,float dy,float dz) {
        this(center.clone().add(x,y,z),dx,dy,dz);
    }

    public AvianBoost(Location loc,float x, float y, float z) {
        this.loc = loc.add(0.5,0.5,0.5);
        sizeX = x;
        sizeY = y;
        sizeZ = z;
        BOOST_LIST.add(this);
        activate();
    }

    public void activate() {
        isActive = true;
        task = Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(),()-> loc.getWorld().spawnParticle(Particle.SWEEP_ATTACK,loc,5,sizeX/2f,sizeY/2f,sizeZ/2f,0,null,true),0,10).getTaskId();
    }

    public boolean isInZone(Location loc) {
        Location offset = loc.subtract(this.loc);
        return (Math.abs(offset.getX()) < sizeX && Math.abs(offset.getY()) < sizeY && Math.abs(offset.getZ()) < sizeZ);
    }

    public void deactivate() {
        isActive = false;
        Bukkit.getScheduler().cancelTask(task);
    }

    public boolean isActive() {
        return isActive;
    }

    public static void spawnBoost() {
        new AvianBoost(-30,-47,-16,1.5f,3,3);
        new AvianBoost(-26,-18,26,6,1.5f,6);
        new AvianBoost(-26,-6,-26,6,1.5f,6);
        new AvianBoost(-22,-39,5,3,1.5f,3);
        new AvianBoost(-22,-19,-11,3,3,1.5f);
        new AvianBoost(-4,-21,-26,3,1.5f,3);
        new AvianBoost(3,-58,0,3,1.5f,3);
        new AvianBoost(4,-32,7,3,3,1.5f);
        new AvianBoost(4,-26,26,3,1.5f,3);
        new AvianBoost(5,-26,-28,1.5f,3,3);
        new AvianBoost(14,-61,23,1.5f,3,3);
        new AvianBoost(15,-7,-23,3,3,1.5f);
        new AvianBoost(16,-2,25,1.5f,3,3);
        new AvianBoost(26,-50,-26,6,1.5f,6);
        new AvianBoost(26,-35,26,6,1.5f,6);
        new AvianBoost(29,-46,15,3,3,1.5f);
        new AvianBoost(32,-35,-8,3,1.5f,3);
    }
}
