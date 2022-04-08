package com.github.timepsilon.comet.item.enchants;

import com.github.timepsilon.EventManager;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import java.util.List;

public class CosmicLaser {
    Color color;
    Trident laser;
    Player owner;

    public CosmicLaser(Location loc, Color color, Player p, int delay) {
        this.laser = (Trident)loc.getWorld().spawnEntity(loc, EntityType.TRIDENT);
        this.color = color;
        this.owner = p;

        this.laser.setVelocity(p.getLocation().getDirection().multiply(0.01));
        this.laser.setInvulnerable(true);
        this.laser.setShooter(this.owner);
        this.laser.setGravity(false);
        summon(delay);
        damageTick(delay);
    }

    private Vector goToTarget() {
        Location targetLoc = this.laser.getLocation().add(this.owner.getLocation().getDirection().normalize().multiply(20));
        List<Entity> nearby = this.laser.getNearbyEntities(10,10,10);
        for (Entity e : nearby) {
            if (e instanceof LivingEntity && e.getScoreboardTags().contains("Comet") && this.laser.getLocation().distance(e.getLocation()) < this.laser.getLocation().distance(targetLoc)) targetLoc = e.getLocation();
        }
        Location diff = targetLoc.subtract(this.laser.getLocation());
        return new Vector(diff.getX(),diff.getY(),diff.getZ()).normalize();
    }

    private void summon(int delay) {
        if (delay == 0) return;
        int task = Bukkit.getScheduler().runTaskTimer(EventManager.getPlugin(),() ->
                this.laser.getWorld().spawnParticle(Particle.REDSTONE,this.laser.getLocation(),
                20,0.3,0.3,0.3,0,new Particle.DustOptions(this.color,2),true),0,1)
                .getTaskId();

        Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(),() -> Bukkit.getScheduler().cancelTask(task),delay);
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
