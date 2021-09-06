package com.github.tim91690.eclipse.misc;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

public class MagicCircle {

    public static void inCircle(Location loc,float radius) {
        drawCircle(loc,radius/2,25);
        drawCircle(loc,radius,50);

        Vector direction = loc.getDirection().normalize();
        Vector leftDirection = direction.clone().rotateAroundY(2*Math.PI/3).normalize();
        Vector rightDirection = direction.clone().rotateAroundY(-2*Math.PI/3).normalize();

        //triangle externe
        drawLine(loc.clone().add(direction.clone().multiply(radius*1.3)),loc.clone().add(leftDirection.clone().multiply(radius*1.3)),20);
        drawLine(loc.clone().add(direction.clone().multiply(radius*1.3)),loc.clone().add(rightDirection.clone().multiply(radius*1.3)),20);
        drawLine(loc.clone().add(leftDirection.clone().multiply(radius*1.3)),loc.clone().add(rightDirection.clone().multiply(radius*1.3)),20);

        //triangle interne
        drawLine(loc.clone().add(direction.clone().multiply(-radius/2)),loc.clone().add(leftDirection.clone().multiply(-radius/2)),20);
        drawLine(loc.clone().add(direction.clone().multiply(-radius/2)),loc.clone().add(rightDirection.clone().multiply(-radius/2)),20);
        drawLine(loc.clone().add(leftDirection.clone().multiply(-radius/2)),loc.clone().add(rightDirection.clone().multiply(-radius/2)),20);


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
