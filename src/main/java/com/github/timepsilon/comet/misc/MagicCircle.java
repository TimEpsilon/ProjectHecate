package com.github.timepsilon.comet.misc;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

public class MagicCircle {

    public static void inCircle(Location loc,float radius) {
        drawCircle(loc,radius/2,40);
        drawCircle(loc,radius,60);

        Vector direction = loc.getDirection().normalize();
        Vector leftDirection = direction.clone().rotateAroundY(2*Math.PI/3).normalize();
        Vector rightDirection = direction.clone().rotateAroundY(-2*Math.PI/3).normalize();

        //triangle externe
        drawLine(loc.clone().add(direction.clone().multiply(radius*1.3)),loc.clone().add(leftDirection.clone().multiply(radius*1.3)),25);
        drawLine(loc.clone().add(direction.clone().multiply(radius*1.3)),loc.clone().add(rightDirection.clone().multiply(radius*1.3)),25);
        drawLine(loc.clone().add(leftDirection.clone().multiply(radius*1.3)),loc.clone().add(rightDirection.clone().multiply(radius*1.3)),25);

        //triangle interne
        drawLine(loc.clone().add(direction.clone().multiply(-radius/2)),loc.clone().add(leftDirection.clone().multiply(-radius/2)),8);
        drawLine(loc.clone().add(direction.clone().multiply(-radius/2)),loc.clone().add(rightDirection.clone().multiply(-radius/2)),8);
        drawLine(loc.clone().add(leftDirection.clone().multiply(-radius/2)),loc.clone().add(rightDirection.clone().multiply(-radius/2)),8);
    }

    public static void outCircle(Location loc, float radius) {
        drawCircle(loc,radius,120);
        drawCircle(loc,radius/2,60);

        Vector direction = loc.getDirection().normalize();
        Vector nextDirection = direction.clone().rotateAroundY(Math.PI/4f).normalize();

        for (int i = 0; i<8; i++) {
            drawCircle(loc.clone().add(direction.clone().multiply(radius/2)),radius/10,18);
            drawLine(loc.clone().add(direction.clone().multiply(radius)),loc.clone().add(nextDirection.clone().multiply(radius)),15);
            direction = nextDirection.clone();
            nextDirection.rotateAroundY(Math.PI/4f).normalize();
        }

    }

    private static void drawCircle(Location center, float radius, int n) {
        for (float i = 0;i<n;i++) {
            center.getWorld().spawnParticle(Particle.REDSTONE,center.clone().add(radius*Math.cos(i/(float)n*2*Math.PI),0,radius*Math.sin(i/(float)n*2*Math.PI)),1,0,0,0,0, new Particle.DustOptions(Color.RED,1),true);
        }
    }

    private static void drawLine(Location start, Location end, int n) {
        Vector dl = end.clone().subtract(start).toVector().multiply(1f/(float)n);
        for (int i = 0; i<n;i++) {
            start.getWorld().spawnParticle(Particle.REDSTONE,start.add(dl),1,0,0,0,0,new Particle.DustOptions(Color.RED,1),true);
        }
    }
}
