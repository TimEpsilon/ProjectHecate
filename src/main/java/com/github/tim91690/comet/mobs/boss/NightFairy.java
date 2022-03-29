package com.github.tim91690.comet.mobs.boss;

import com.github.tim91690.EventManager;
import com.github.tim91690.comet.item.CustomItems;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;


public class NightFairy extends Boss {
    int taskTick;
    int beaconTask;

    public NightFairy(Location loc,int beaconTask) {
        super(loc.getWorld().spawnEntity(loc, EntityType.BAT),60, ChatColor.DARK_PURPLE+""+ChatColor.BOLD+"Night Fairy", BarColor.PINK, CustomItems.SOUL_LUST.getItem(),3,40);
        this.beaconTask = beaconTask;

        this.entity.setCustomName(this.name);
        this.entity.setCustomNameVisible(true);
        this.entity.setSilent(true);
        this.entity.getEquipment().setItemInMainHand(new ItemStack(Material.TORCH));
        this.entity.getEquipment().setItemInMainHandDropChance(0);
        this.entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,100000000,0,false,false));

        this.taskTick = Bukkit.getScheduler().runTaskTimer(EventManager.getPlugin(),() -> {
            this.getEntity().getWorld().spawnParticle(Particle.REDSTONE,this.getEntity().getLocation(),10,0.1,0.1,0.1,0,new Particle.DustOptions(Color.fromRGB(242,204,209),1),true);
            this.getEntity().getWorld().spawnParticle(Particle.REDSTONE,this.getEntity().getLocation(),20,0.5,0.5,0.5,0,new Particle.DustOptions(Color.fromRGB(220,0,140),1),true);
            this.getEntity().getWorld().spawnParticle(Particle.DRAGON_BREATH,this.getEntity().getLocation(),3,0,0,0,0,null,true);
        },0,1).getTaskId();

    }

    @Override
    public void death() {
        this.entity.getWorld().createExplosion(this.entity.getLocation(),3,true,false,this.entity);
        super.death();
        Bukkit.getScheduler().cancelTask(this.taskTick);
        Bukkit.getScheduler().cancelTask(this.beaconTask);
    }

    @Override
    public void attack(List<Player> proxPlayer) {
        //c'est une gentille fée
        //elle attaquerait jamais quelqu'un :(
    }
}
