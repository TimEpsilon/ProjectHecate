package com.github.timepsilon.comet.mobs;

import com.github.timepsilon.ProjectHecate;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class WickedWitch extends CometMobs {

    private int task;
    private static final ItemStack itemPotion = getPotion();

    private static ItemStack getPotion() {
        ItemStack item = new ItemStack(Material.SPLASH_POTION);
        PotionMeta potionMeta = (PotionMeta) item.getItemMeta();

        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 20, 0), true);
        potionMeta.setColor(Color.BLACK);

        item.setItemMeta(potionMeta);
        return item;
    }


    public WickedWitch(LivingEntity witch) {
        super(witch,40);
        this.entity.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(50);
        this.entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.1);

        task = Bukkit.getScheduler().runTaskTimer(ProjectHecate.getPlugin(),()->{
            if ((this.entity.isValid())) {
                ThrownPotion potion = (ThrownPotion) entity.getWorld().spawnEntity(entity.getLocation().add(0,2,0), EntityType.SPLASH_POTION);
                potion.setItem(itemPotion);
                potion.setVelocity(Vector.getRandom().subtract(new Vector(0.5,0,0.5)));
            } else {
                Bukkit.getScheduler().cancelTask(task);
            }
        },0,10).getTaskId();
    }

    public WickedWitch(Location loc) {
        this((Witch)loc.getWorld().spawnEntity(loc, EntityType.WITCH));
    }
}