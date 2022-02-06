package com.github.tim91690.eclipse.mobs.semiboss;

import com.github.tim91690.eclipse.mobs.EclipseMobs;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DrownedOverlordRider extends EclipseMobs {

    public DrownedOverlordRider(Location loc) {
        super((LivingEntity)loc.getWorld().spawnEntity(loc,EntityType.DROWNED),120);

        this.entity.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(40);
        this.entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3);
        this.entity.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(-6);

        this.entity.getEquipment().setItemInMainHand(new ItemStack(Material.TRIDENT));
        this.entity.getEquipment().setBoots(new ItemStack(Material.NETHERITE_BOOTS));
        this.entity.getEquipment().setLeggings(new ItemStack(Material.NETHERITE_LEGGINGS));
        this.entity.getEquipment().setChestplate(new ItemStack(Material.NETHERITE_CHESTPLATE));
        this.entity.getEquipment().setHelmet(new ItemStack(Material.NETHERITE_HELMET));

        this.entity.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,2000000,0,true,true));

        this.entity.addScoreboardTag("SemiBoss");
    }
}