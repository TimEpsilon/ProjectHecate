package com.github.timepsilon.comet.mobs.boss;

import com.github.timepsilon.ProjectHecate;
import com.github.timepsilon.comet.item.CustomItems;
import com.github.timepsilon.comet.mobs.SkeletonSniper;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;

public class PhantomOverlord extends Boss {

    private static final Random random = new Random();
    private int soldiers = 0;
    public static final String NAME = ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "Phantom Overlord";

    public PhantomOverlord(Location loc) {
        this(loc,true);
    }

    public PhantomOverlord(Location loc,boolean showMessage) {
        super(loc.getWorld().spawnEntity(loc, EntityType.PHANTOM),280,NAME, BarColor.BLUE, CustomItems.SOUL_GREED.getItem(),2,10);

        while (isSuffocating(loc,7)) {
            loc.add(0,3,0);
        }
        entity.teleport(loc);

        if (showMessage) {
            Bukkit.broadcast(Component.text(ChatColor.translateAlternateColorCodes('&',"&eUn &1&lPhantom Overlord &ea spawn en &a<"+(int)loc.getX()+" , "+(int)loc.getZ()+">")));
            sendWaypoint("xaero-waypoint:PhantomOverlord:PO:"+(int) loc.getX()+":"+(int) loc.getY()+":"+(int) loc.getZ()+":1:false:0:Internal-overworld-waypoints");
        }

        //au dessus, frapper le mob est difficile
        ((Phantom) this.entity).setSize(64);

        //nom
        this.entity.setCustomName(this.name);
        this.entity.setCustomNameVisible(true);

        this.entity.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(60);
        this.entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(15);
        this.entity.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(12);
        this.entity.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).setBaseValue(3);
        this.entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,2000000,0,true,true));
    }

    @Override
    public void attack(List<Player> proxPlayer) {
        int n = random.nextInt(7);
        switch (n) {
            case 0 -> summonSoldiers();
            case 1,2 -> succ(proxPlayer);
        }
        for (Player p : proxPlayer) {
            if (p.getStatistic(Statistic.TIME_SINCE_REST) > 100000) continue;
            p.setStatistic(Statistic.TIME_SINCE_REST,100000);
        }
    }

    private void summonSoldiers() {
        for (int i = 0; i < 3; i++) {
            summonSoldier();
        }
        this.getEntity().getWorld().spawnParticle(Particle.WARPED_SPORE,this.getEntity().getLocation(),500,20,20,20,0);
        this.getEntity().getWorld().playSound(this.getEntity().getLocation(), Sound.ENTITY_PHANTOM_HURT,SoundCategory.HOSTILE,4f,2f);
    }

    private void summonSoldier() {
        soldiers += 1;
        Phantom ph = (Phantom) this.getEntity().getLocation().getWorld().spawnEntity(this.getEntity().getLocation(),EntityType.PHANTOM);
        ph.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(75);
        ph.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(4);

        ph.setSize(10);

        ph.setHealth(75);
        ph.addScoreboardTag("Comet");

        ph.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,2000000,0,true,true));

        WitherSkeleton s = (WitherSkeleton) this.getEntity().getLocation().getWorld().spawnEntity(this.getEntity().getLocation(),EntityType.WITHER_SKELETON);

        s.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,2000000,0,true,true));
        s.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(60);
        s.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(40);

        s.getEquipment().setItemInMainHand(SkeletonSniper.SkeletonBow);
        s.getEquipment().setItemInMainHandDropChance(0f);
        s.getEquipment().setItemInOffHand(SkeletonSniper.RandomArrow());
        s.getEquipment().setItemInOffHandDropChance(0.1f);

        s.setSilent(true);
        s.setHealth(60);

        s.addScoreboardTag("Comet");

        ph.addPassenger(s);

        int task = Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(),() -> {
            if (s.isDead()) soldiers = soldiers -1;
        },0,400).getTaskId();

        Bukkit.getScheduler().runTaskLater(ProjectHecate.getPlugin(),() -> {
            Bukkit.getScheduler().cancelTask(task);
            s.remove();
            this.soldiers = this.soldiers -1;
        },1200);
    }

    private void succ(List<Player> proxPlayer) {
        for (Player p : proxPlayer) {
            //Vector unitaire du boss au joueur multiplié par -2
            Vector d = p.getLocation().subtract(this.getEntity().getLocation()).toVector().normalize().multiply(-3);
            p.setVelocity(d);
            this.getEntity().getWorld().spawnParticle(Particle.WARPED_SPORE,this.getEntity().getLocation(),500,20,20,20,0);
            this.getEntity().getWorld().playSound(this.getEntity().getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,SoundCategory.HOSTILE,4f,0.1f);
        }
    }
}
