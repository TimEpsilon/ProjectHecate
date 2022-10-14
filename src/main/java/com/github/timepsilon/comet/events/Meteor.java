package com.github.timepsilon.comet.events;

import com.github.timepsilon.ProjectHecate;
import com.github.timepsilon.comet.mobs.boss.fairy.NightFairy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Random;

public class Meteor {
    private final Location loc;
    private final int fallingTask;
    private int beaconTask;
    private final Fireball fireball;
    private NightFairy fairy;

    private static final Random random = new Random();

    public Meteor(Location loc) {
        this.loc = loc;

        double theta = random.nextDouble(2*Math.PI);

        fireball = (Fireball)loc.getWorld().spawnEntity(loc.clone().add(40*Math.cos(theta),100,40*Math.sin(theta)), EntityType.FIREBALL);
        Vector direction = loc.toVector().subtract(fireball.getLocation().toVector()).normalize().multiply(0.1);
        fireball.setDirection(direction);
        fireball.setVelocity(direction);
        fireball.setVisualFire(true);

        fireball.getWorld().playSound(this.loc,"minecraft:custom/meteor_fall", SoundCategory.AMBIENT,20,1);

        this.fallingTask = Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(), () -> {
            if(fireball.isDead()) MeteorDeath();
            loc.getWorld().spawnParticle(Particle.GLOW_SQUID_INK,fireball.getLocation(),50,1,2,1,0,null,true);
        },0,1).getTaskId();
    }

    private void MeteorDeath() {
        Bukkit.getScheduler().cancelTask(this.fallingTask);
        Beacon();
    }

    private void Beacon() {
        this.beaconTask = Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(),() -> {
            this.loc.getWorld().spawnParticle(Particle.GLOW,this.loc.clone().add(0,50,0),1000,5,50,5,0);
            this.loc.getWorld().spawnParticle(Particle.TOTEM,this.loc.clone().add(0,50,0),500,1,50,1,0,null,true);
            this.loc.getWorld().spawnParticle(Particle.WAX_ON,this.loc,500,30,30,30,0);
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!p.getWorld().equals(loc.getWorld())) continue;
                if (p.getLocation().distance(this.loc) < 30) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,40,2));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,40,0));
                    p.getWorld().spawnParticle(Particle.HEART,p.getLocation(),3,0.2,0.2,1);
                }
            }
            if (this.loc.distance(this.fairy.getEntity().getLocation())> 30) this.fairy.getEntity().teleport(this.loc);

            for (Entity entity : this.loc.getNearbyEntities(10,10,10)) {
                if (!entity.getScoreboardTags().contains("Comet") || entity.getScoreboardTags().contains("Boss")) continue;
                entity.setVelocity(entity.getLocation().toVector().subtract(this.loc.toVector()).multiply(2));
            }
        },40,30).getTaskId();

        this.fairy = new NightFairy(this.loc,this.beaconTask);

        Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(),() -> {
            Bukkit.getScheduler().cancelTask(this.beaconTask);
        },6000);
    }
}
