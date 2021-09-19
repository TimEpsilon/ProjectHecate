package com.github.tim91690.eclipse.events;

import com.github.tim91690.EventManager;
import com.github.tim91690.eclipse.misc.Laser;
import com.github.tim91690.eclipse.misc.MagicCircle;
import com.github.tim91690.eclipse.structure.RitualArena;
import org.bukkit.*;
import org.bukkit.block.data.Lightable;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class MoonRitual {

    private final List<Integer> tasks;

    public MoonRitual(Location loc) {
        this.tasks = new ArrayList<>();

        //Cercle allume bougie
        final float[] pos = {0,0,2};
        this.tasks.add(Bukkit.getScheduler().runTaskTimer(EventManager.getPlugin(),() -> {
            loc.getWorld().spawnParticle(Particle.FLAME,loc.clone().add(pos[1]*Math.cos(pos[0] *Math.PI/180)+0.5,pos[2],pos[1]*Math.sin(pos[0] *Math.PI/180)+0.5),1,0.1,0.1,0.1,0,null,true);
            loc.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME,loc.clone().add(-pos[1]*Math.cos(pos[0] *Math.PI/180)+0.5,pos[2],pos[1]*Math.sin(-pos[0] *Math.PI/180)+0.5),1,0.1,0.1,0.1,0,null,true);
            pos[0] = pos[0] +5;
            if (pos[1]<3) pos[1] = pos[1] + 0.05f;

            Location circleLoc = loc.clone().add(pos[1]*Math.cos(pos[0] *Math.PI/180)+0.5f,1,pos[1]*Math.sin(pos[0] *Math.PI/180)+0.5f);
            if (circleLoc.getBlock().getType() == Material.RED_CANDLE) {
                Lightable l = ((Lightable)circleLoc.getBlock().getBlockData());
                l.setLit(true);
                circleLoc.getBlock().setBlockData(l);
            }

            if (pos[0] > 720) pos[2] = pos[2]+0.1f;

            if (pos[0] > 1080) {
                Bukkit.getScheduler().cancelTask(tasks.get(0));
                loc.getWorld().spawnParticle(Particle.TOTEM,loc.clone().add(0,pos[2],0),500,3,1,3,0,null,true);
                loc.getWorld().playSound(loc, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE,SoundCategory.AMBIENT,5,0);
            }
        },0,2).getTaskId());

        //pilier
        this.tasks.add(Bukkit.getScheduler().runTaskTimer(EventManager.getPlugin(),() -> {
            loc.getWorld().spawnParticle(Particle.REDSTONE,loc.clone().add(0.5,0,0.5).add(5,2,2),100,0.5,1,0.5,0,new Particle.DustOptions(Color.PURPLE,1),true);
            loc.getWorld().spawnParticle(Particle.REDSTONE,loc.clone().add(0.5,0,0.5).add(5,2,-2),100,0.5,1,0.5,0,new Particle.DustOptions(Color.BLUE,1),true);
            loc.getWorld().spawnParticle(Particle.REDSTONE,loc.clone().add(0.5,0,0.5).add(-5,2,2),100,0.5,1,0.5,0,new Particle.DustOptions(Color.ORANGE,1),true);
            loc.getWorld().spawnParticle(Particle.REDSTONE,loc.clone().add(0.5,0,0.5).add(-5,2,-2),100,0.5,1,0.5,0,new Particle.DustOptions(Color.YELLOW,1),true);
            loc.getWorld().spawnParticle(Particle.REDSTONE,loc.clone().add(0.5,0,0.5).add(2,2,5),100,0.5,1,0.5,0,new Particle.DustOptions(Color.BLACK,1),true);
            loc.getWorld().spawnParticle(Particle.REDSTONE,loc.clone().add(0.5,0,0.5).add(-2,2,5),100,0.5,1,0.5,0,new Particle.DustOptions(Color.RED,1),true);
            loc.getWorld().spawnParticle(Particle.REDSTONE,loc.clone().add(0.5,0,0.5).add(-2,2,-5),100,0.5,1,0.5,0,new Particle.DustOptions(Color.LIME,1),true);
            loc.getWorld().spawnParticle(Particle.REDSTONE,loc.clone().add(0.5,0,0.5).add(2,2,-5),100,0.5,1,0.5,0,new Particle.DustOptions(Color.AQUA,1),true);
            },432,5).getTaskId());

        //Laser
        Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(),()->{
            loc.getWorld().playSound(loc,Sound.BLOCK_BEACON_AMBIENT,SoundCategory.AMBIENT,5,0.2f);
            for (int x = -5;x<=5;x=x+10) {
                for (int y = -2;y<=2;y=y+4) {
                    Laser laser = null;
                    Laser laser2 = null;
                    try {
                        laser = new Laser.GuardianLaser(loc.clone().add(0.5,0,0.5).add(x,2,y),loc.clone().add(0.5,-0.5,0.5),200,64);
                        laser2 = new Laser.GuardianLaser(loc.clone().add(0.5,0,0.5).add(y,2,x),loc.clone().add(0.5,-0.5,0.5),200,64);
                    } catch (ReflectiveOperationException e) {
                        e.printStackTrace();
                    }
                    laser.start(EventManager.getPlugin());
                    Laser finalLaser = laser;
                    laser.moveEnd(loc.clone().add(0.5,32,0.5),200, finalLaser::stop);
                    laser2.start(EventManager.getPlugin());
                    Laser finalLaser1 = laser2;
                    laser2.moveEnd(loc.clone().add(0.5,32,0.5),200, finalLaser1::stop);
                }
            }
        },450);

        final float[] rot = {0};
        tasks.add(Bukkit.getScheduler().runTaskTimer(EventManager.getPlugin(),()->{
            Location center = loc.clone().add(0.5,1.1,0.5);
            center.setYaw(rot[0]);
            MagicCircle.inCircle(center,2);
            center.setYaw(-rot[0]);
            MagicCircle.outCircle(center,6);
            rot[0] = rot[0] + 0.8f;
        },450,2).getTaskId());

        //Map change, boss spawn
        Bukkit.getScheduler().runTaskLater(EventManager.getPlugin(),()->{
            loc.getWorld().spawnParticle(Particle.GLOW_SQUID_INK,loc,3000,20,3,20,0,null,true);
            RitualArena.spawnRitualArena(true);
            loc.getWorld().playSound(loc,Sound.ENTITY_WITHER_SPAWN,SoundCategory.HOSTILE,30,0);
            Bukkit.getScheduler().cancelTask(tasks.get(1));
            Bukkit.getScheduler().cancelTask(tasks.get(2));

            for (Player p : Bukkit.getOnlinePlayers()) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,40,0));
                p.spawnParticle(Particle.GLOW,p.getEyeLocation(),200,0.1,0.5,0.1);
            }
        },650);
    }
}
