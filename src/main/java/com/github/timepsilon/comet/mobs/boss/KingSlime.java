package com.github.timepsilon.comet.mobs.boss;

import com.github.timepsilon.comet.item.CustomItems;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.List;

public class KingSlime extends Boss {

    public KingSlime(Location loc, boolean showMessage) {
        super(loc.getWorld().spawnEntity(loc, EntityType.SLIME),250,ChatColor.translateAlternateColorCodes('&',"&2&lKing Slime"), BarColor.YELLOW, CustomItems.SOUL_SLOTH.getItem(),1,5);

        if (showMessage) {
            Bukkit.broadcast(Component.text(ChatColor.translateAlternateColorCodes('&', "&eUn &2&lKing Slime &ea spawn en &a<" + (int) loc.getX() + " , " + (int) loc.getZ() + ">")));
            sendWaypoint("xaero-waypoint:KingSlime:KS:"+(int) loc.getX()+":"+(int) loc.getY()+":"+(int) loc.getZ()+":2:false:0:Internal-overworld-waypoints");
        }

        //au dessus, frapper le mob est difficile
        ((Slime) this.entity).setSize(14);

        //nom
        this.entity.setCustomName(this.name);
        this.entity.setCustomNameVisible(true);

        this.entity.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,2000000,7));

        this.entity.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(60);
        this.entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.4);
        this.entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(7);
        this.entity.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(12);
        this.entity.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).setBaseValue(2);
    }

    public KingSlime(Location loc) {
        this(loc,true);
    }

    @Override
    public void attack(List<Player> proxPlayer) {
        for (Player p : proxPlayer) {
            //Slowness aux joueurs alentours, désactive elytra
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,140,3));
            if (!p.isGliding() && p.isOnGround()) continue;

            p.setGliding(false);
            p.setVelocity(new Vector(0,-2,0));
            p.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Le King Slime souhaite vous voir ramper...");
        }
        //son et particles
        this.getEntity().getWorld().spawnParticle(Particle.BLOCK_CRACK,this.getEntity().getLocation(),500,20,20,20,0, Material.SLIME_BLOCK.createBlockData(),true);
        this.getEntity().getWorld().playSound(this.getEntity().getLocation(), Sound.BLOCK_SCULK_SENSOR_CLICKING,SoundCategory.HOSTILE,3f,0.2f);
    }
}
