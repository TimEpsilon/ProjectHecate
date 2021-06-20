package com.github.tim91690.eclipse.mobs.boss;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ScarletRabbit extends Boss {

    public ScarletRabbit(Location loc) {

        super(loc.getWorld().spawnEntity(loc, EntityType.RABBIT),250,ChatColor.translateAlternateColorCodes('&',"&2&lKing Slime"), BarColor.GREEN);

        /*Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',"&eUn &2&lKing Slime &ea spawn en &a<"+(int)loc.getX()+" , "+(int)loc.getY()+" , "+(int)loc.getZ()+">"));

        //au dessus, frapper le mob est difficile
        ((Slime) this.entity).setSize(15);

        //nom
        this.entity.setCustomName(this.name);
        this.entity.setCustomNameVisible(true);

        ((Slime) this.entity).addPotionEffect(new PotionEffect(PotionEffectType.JUMP,2000000,3));
        ((Slime) this.entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING,2000000,2));

        ((Slime) this.entity).getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(60);
        ((Slime) this.entity).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.4);
        ((Slime) this.entity).getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(7);
        ((Slime) this.entity).getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(12);
        ((Slime) this.entity).getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).setBaseValue(2);*/
    }

    @Override
    public void attack(Player p) {
       /* //Slowness aux joueurs alentours, désactive elytra
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,140,3));
        if (p.isGliding()) {
            p.setGliding(false);
            p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Le King Slime souhaite vous voir ramper...");
        }
        //son et particles
        this.getEntity().getWorld().spawnParticle(Particle.BLOCK_CRACK,this.getEntity().getLocation(),500,20,20,20,0, Material.SLIME_BLOCK.createBlockData(),true);
        this.getEntity().getWorld().playSound(this.getEntity().getLocation(), Sound.BLOCK_SCULK_SENSOR_CLICKING,3f,0.2f);*/
    }
}
