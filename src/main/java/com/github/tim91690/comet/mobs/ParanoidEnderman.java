package com.github.tim91690.comet.mobs;

import com.github.tim91690.EventManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParanoidEnderman extends CometMobs {

    private final static Random random = new Random();
    private int task;
    private final static ArrayList<Material> blockList = new ArrayList<>(List.of(
            Material.TNT,
            Material.SPONGE,
            Material.RAW_GOLD_BLOCK,
            Material.SMITHING_TABLE,
            Material.COPPER_BLOCK,
            Material.DRIED_KELP_BLOCK,
            Material.CAKE,
            Material.BONE_BLOCK));


    public ParanoidEnderman(LivingEntity ender) {
        super(ender,80);
        this.entity.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(50);
        this.entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.25);

        this.entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,10000000,0,false,false));

        ((Enderman)this.entity).setCarriedBlock(Bukkit.createBlockData(blockList.get(random.nextInt(blockList.size()))));

        task = Bukkit.getScheduler().runTaskTimer(EventManager.getPlugin(),()->{
            if ((!this.entity.isDead())) {
                ((Enderman) this.entity).teleportRandomly();
            } else {
                Bukkit.getScheduler().cancelTask(task);
            }
        },0,30).getTaskId();
    }

    public ParanoidEnderman(Location loc) {
        this((Enderman)loc.getWorld().spawnEntity(loc, EntityType.ENDERMAN));
    }
}