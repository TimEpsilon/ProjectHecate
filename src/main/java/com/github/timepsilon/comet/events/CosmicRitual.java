package com.github.timepsilon.comet.events;

import com.github.timepsilon.ProjectHecate;
import com.github.timepsilon.comet.misc.Laser;
import com.github.timepsilon.comet.misc.MagicCircle;
import com.github.timepsilon.comet.misc.TextManager;
import com.github.timepsilon.comet.mobs.boss.Demiurge;
import com.github.timepsilon.comet.structure.RitualArena;
import org.bukkit.*;
import org.bukkit.block.data.Lightable;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CosmicRitual {

    private final List<Integer> tasks;

    public CosmicRitual(Location loc) {
        this.tasks = new ArrayList<>();

        Bukkit.getScheduler().runTask(ProjectHecate.getPlugin(),()->Bukkit.getOnlinePlayers().forEach(Player -> Player.setBedSpawnLocation(loc.clone().add(0,3,0),true)));


        //Cercle allume bougie
        final float[] pos = {0,0,2};
        this.tasks.add(Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(),() -> {
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

        //Pilier
        this.tasks.add(Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(),() -> {
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
        Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(),()->{
            loc.getWorld().playSound(loc,"minecraft:custom/sos",SoundCategory.AMBIENT,10,0.5f);
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
                    laser.start(ProjectHecate.getPlugin());
                    Laser finalLaser = laser;
                    laser.moveEnd(loc.clone().add(0.5,32,0.5),200, finalLaser::stop);
                    laser2.start(ProjectHecate.getPlugin());
                    Laser finalLaser1 = laser2;
                    laser2.moveEnd(loc.clone().add(0.5,32,0.5),200, finalLaser1::stop);
                }
            }
        },450);

        //Magic circle
        final float[] rot = {0};
        tasks.add(Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(),()->{
            Location center = loc.clone().add(0.5,1.1,0.5);
            center.setYaw(rot[0]);
            MagicCircle.inCircle(center,2);
            center.setYaw(-rot[0]);
            MagicCircle.outCircle(center,6);
            rot[0] = rot[0] + 0.8f;
        },450,2).getTaskId());

        //Map change, boss spawn
        Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(),()->{
            loc.getWorld().spawnParticle(Particle.GLOW_SQUID_INK,loc,3000,20,3,20,0,null,true);
            RitualArena.spawnRitualArena(true);
            loc.getWorld().playSound(loc,Sound.ENTITY_WITHER_SPAWN,SoundCategory.HOSTILE,30,0);
            Bukkit.getScheduler().cancelTask(tasks.get(1));
            Bukkit.getScheduler().cancelTask(tasks.get(2));

            for (Player p : Bukkit.getOnlinePlayers()) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,40,0));
                p.spawnParticle(Particle.GLOW,p.getEyeLocation(),200,0.1,0.5,0.1);
            }

            new Demiurge(loc.clone().add(0,16,0));
        },650);

        //Text
        AtomicInteger iter = new AtomicInteger(0);
        tasks.add(Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(),() -> {
            switch (iter.get()) {
                case 0:
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        TextManager.sendSamTextToPlayer(p,ChatColor.RED + "[ALERTE] : Importante quantité d'énergie détectée",true);
                        if (!p.getWorld().equals(loc.getWorld()) || p.getLocation().distance(loc) > 50) TextManager.sendSamTextToPlayer(p,ChatColor.RED + "[ALERTE] : Rapprochez-vous de la zone",true);
                    }
                    break;
                case 1:
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (!p.getWorld().equals(loc.getWorld()) || p.getLocation().distance(loc) < 50) continue;
                        TextManager.sendSamTextToPlayer(p,ChatColor.RED + "[ALERTE] : Votre probabilité de mourir grimpe en flèche. " +
                                "Procédure de téléportation d'urgence enclenchée. S.A.M ne pourra être tenu responsable des potentiels organes perdus en chemin"
                                ,true);
                        p.teleport(loc.clone().add(0,2,0));
                    }
                    break;
                case 2:
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        TextManager.sendSamTextToPlayer(p,ChatColor.RED + "[ALERTE] : Le " + ChatColor.MAGIC + "Demiurge "
                                +ChatColor.RESET + ChatColor.RED + "est à l'approche"
                                ,true);
                    }
                    break;
                case 3:
                    Bukkit.getScheduler().cancelTask(tasks.get(3));
                    Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(),()-> TextManager.sendSamTextToPlayer(ChatColor.GREEN + "La présence du Demiurge vous affaiblit. " +
                                    "Les feux d'artifices sont désactivés." +
                                    "Utilisez les ventilations alentours pour gagner de l'altitude."
                            , true), 200);
            }
            iter.getAndIncrement();
        },0,165).getTaskId());
    }
}
