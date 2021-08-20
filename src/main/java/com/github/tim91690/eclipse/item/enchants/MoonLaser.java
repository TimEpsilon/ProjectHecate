package com.github.tim91690.eclipse.item.enchants;

import com.github.tim91690.EventManager;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import java.util.List;

public class MoonLaser {
    Color color;
    Trident laser;

    public MoonLaser(Location loc, Color color,Player p,int delay) {
        //loc.setDirection(p.getPo)
        this.laser = (Trident)loc.getWorld().spawnEntity(loc, EntityType.TRIDENT);

        this.color = color;
        this.laser.setInvulnerable(true);
        this.laser.setShooter(p);
        this.laser.setGravity(false);
        this.laser.teleport();
        damageTick(delay);
    }

    private Vector goToTarget() {
        Location targetLoc = this.laser.getLocation().add(0,-20,0);
        List<Entity> nearby = this.laser.getNearbyEntities(10,10,10);
        for (Entity e : nearby) {
            if (e instanceof Monster && this.laser.getLocation().distance(e.getLocation()) < this.laser.getLocation().distance(targetLoc)) targetLoc = e.getLocation();
        }
        Location diff = targetLoc.add(this.laser.getLocation().multiply(-1));
        return new Vector(diff.getX(),diff.getY(),diff.getZ()).normalize();
    }

    private void summon(int delay) {
        if (delay == 0) return;
        int task = Bukkit.getScheduler().runTaskTimer(EventManager.getPlugin(),() -> {

        },0,1).getTaskId();
    }

    private void damageTick(int delay) {
        int task = Bukkit.getScheduler().runTaskTimer(EventManager.getPlugin(), () -> {
            if (!this.laser.isOnGround()) {
                this.laser.setGravity(true);
                this.laser.getWorld().spawnParticle(Particle.REDSTONE,this.laser.getLocation(),25,0.2,0.2,0.2,0,new Particle.DustOptions(this.color,1),true);
                this.laser.setVelocity(goToTarget());
            } else {
                this.laser.remove();
            }
        },delay,1).getTaskId();

        Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(),() -> {
            this.laser.remove();
            Bukkit.getScheduler().cancelTask(task);
        },100+delay);
    }
}
