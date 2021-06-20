package com.github.tim91690.eclipse.mobs.boss;

import com.github.tim91690.eclipse.mobs.SkeletonSniper;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class PhantomOverlord extends Boss {

    public PhantomOverlord(Location loc) {
        super(loc.getWorld().spawnEntity(loc, EntityType.PHANTOM),280,ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "Phantom Overlord", BarColor.BLUE);

        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',"&eUn &1&lPhantom Overlord &ea spawn en &a<"+(int)loc.getX()+" , "+(int)loc.getY()+" , "+(int)loc.getZ()+">"));

        //au dessus, frapper le mob est difficile
        ((Phantom) this.entity).setSize(64);

        //nom
        this.entity.setCustomName(this.name);
        this.entity.setCustomNameVisible(true);

        ((Phantom) this.entity).getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(60);
        ((Phantom) this.entity).getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(15);
        ((Phantom) this.entity).getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(12);
        ((Phantom) this.entity).getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).setBaseValue(3);
        ((Phantom) this.entity).addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,2000000,0,true,true));
    }

    @Override
    public void attack(Player p) {
        System.out.println("attack");
        int n = (int)(Math.random()*10);
        switch (n) {
            case 0:
                summonSoldiers();
                break;
            case 1:
                succ(p);
                break;
        }
    }

    private void summonSoldiers() {
        for (int i = 0; i < 5; i++) {
            Phantom ph = (Phantom) this.getEntity().getLocation().getWorld().spawnEntity(this.getEntity().getLocation(),EntityType.PHANTOM);
            ph.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(75);
            ph.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(4);

            ph.setSize(10);

            ph.setHealth(75);
            ph.addScoreboardTag("Eclipse");

            ph.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,2000000,0,true,true));

            WitherSkeleton s = (WitherSkeleton) this.getEntity().getLocation().getWorld().spawnEntity(this.getEntity().getLocation(),EntityType.WITHER_SKELETON);

            s.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(60);
            s.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(40);

            s.getEquipment().setItemInMainHand(SkeletonSniper.Bow());
            s.getEquipment().setItemInMainHandDropChance(0f);
            s.getEquipment().setItemInOffHand(SkeletonSniper.Arrow());
            s.getEquipment().setItemInOffHandDropChance(0.1f);

            s.setSilent(true);
            s.setHealth(60);

            s.addScoreboardTag("Eclipse");

            ph.addPassenger(s);

            this.getEntity().getWorld().spawnParticle(Particle.WARPED_SPORE,this.getEntity().getLocation(),500,20,20,20,0);
            this.getEntity().getWorld().playSound(this.getEntity().getLocation(), Sound.ENTITY_PHANTOM_HURT,4f,2f);
        }
    }

    private void succ(Player p) {
        //Vector unitaire du boss au joueur multiplié par -2
        Vector d = p.getLocation().subtract(this.getEntity().getLocation()).toVector().normalize().multiply(-2);
        p.setVelocity(d);
        this.getEntity().getWorld().spawnParticle(Particle.WARPED_SPORE,this.getEntity().getLocation(),500,20,20,20,0);
        this.getEntity().getWorld().playSound(this.getEntity().getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT,4f,0.1f);
    }
}
