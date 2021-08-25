package com.github.tim91690.eclipse.events;

import com.github.tim91690.EventManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Meteor {
    private Location loc;
    private int task;
    private int beaconTask;
    private Fireball fireball;

    public Meteor(Location loc) {
        this.loc = loc;

        this.fireball = (Fireball)loc.getWorld().spawnEntity(loc.clone().add(0,100,0), EntityType.FIREBALL);
        this.fireball.setDirection(new Vector(0,-0.5,0));
        this.fireball.setVelocity(new Vector(0,-0.5,0));
        this.fireball.setVisualFire(true);

        this.task = Bukkit.getScheduler().runTaskTimer(EventManager.getPlugin(), () -> {
            if(this.fireball.isDead()) MeteorDeath();
            loc.getWorld().spawnParticle(Particle.GLOW_SQUID_INK,this.fireball.getLocation(),60,1,2,1,0,null,true);
        },0,1).getTaskId();
    }

    private void MeteorDeath() {
        Bukkit.getScheduler().cancelTask(this.task);
        Beacon();
    }

    private void Beacon() {

        this.beaconTask = Bukkit.getScheduler().runTaskTimer(EventManager.getPlugin(),() -> {
            this.loc.getWorld().spawnParticle(Particle.GLOW,this.loc.clone().add(0,50,0),1000,5,50,5,0,null,true);
            this.loc.getWorld().spawnParticle(Particle.TOTEM,this.loc.clone().add(0,50,0),500,1,50,1,0,null,true);
            this.loc.getWorld().spawnParticle(Particle.WAX_ON,this.loc,500,30,30,30,0,null,true);
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getLocation().distance(this.loc) < 30) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20,0));
                }
            }
        },40,20).getTaskId();

        Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(),() -> {
            Bukkit.getScheduler().cancelTask(this.beaconTask);
        },6000);
    }
}
