package com.github.timepsilon.comet.mobs.semiboss;

import com.github.timepsilon.comet.mobs.CometMobs;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;


public class DrownedOverlordHorse extends CometMobs {

    public DrownedOverlordHorse(Location loc) {
        super((LivingEntity)loc.getWorld().spawnEntity(loc,EntityType.ZOMBIE_HORSE),30);

        this.entity.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(15);
        this.entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.4);

        DrownedOverlordRider rider = new DrownedOverlordRider(loc);
        this.entity.addPassenger(rider.getEntity());
        ((ZombieHorse)this.entity).getInventory().setSaddle(new ItemStack(Material.SADDLE));

        this.entity.addScoreboardTag("SemiBoss");
    }
}